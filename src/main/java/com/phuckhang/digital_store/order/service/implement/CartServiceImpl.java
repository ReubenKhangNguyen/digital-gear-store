package com.phuckhang.digital_store.order.service.implement;

import com.phuckhang.digital_store.catalog.entity.Product;
import com.phuckhang.digital_store.catalog.enums.ProductStatus;
import com.phuckhang.digital_store.catalog.repository.ProductRepository;
import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import com.phuckhang.digital_store.order.dto.request.CartItemRequestDTO;
import com.phuckhang.digital_store.order.dto.response.CartItemResponseDTO;
import com.phuckhang.digital_store.order.dto.response.CartResponseDTO;
import com.phuckhang.digital_store.order.entity.Cart;
import com.phuckhang.digital_store.order.entity.CartItem;
import com.phuckhang.digital_store.order.mapper.CartMapper;
import com.phuckhang.digital_store.order.repository.CartItemRepository;
import com.phuckhang.digital_store.order.repository.CartRepository;
import com.phuckhang.digital_store.order.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {

    UserRepository userRepository;
    ProductRepository productRepository;
    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getMyCart() {
        Cart cart = getCurrentUserCart();
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO addToCart(CartItemRequestDTO requestDTO) {
        // Bước 1: Lấy Giỏ hàng hiện tại
        Cart cart = getCurrentUserCart();
        // Bước 2: Tìm Sản phẩm & Check trạng thái
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE);
        }
        // Bước 3: Tính tồn kho khả dụng
        int availableQuantity = product.getStockQuantity() - product.getHoldQuantity();
        // Bước 4: Kiểm tra xem Sản phẩm đã có trong Giỏ chưa
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);
        if (cartItem != null) {
            int newQuantity = cartItem.getQuantity() + requestDTO.getQuantity();
            if (newQuantity > availableQuantity) {
                throw new AppException(ErrorCode.QUANTITY_EXCEEDS_STOCK);
            }
            cartItem.setQuantity(newQuantity);
        } else {
            if (requestDTO.getQuantity() > availableQuantity) {
                throw new AppException(ErrorCode.QUANTITY_EXCEEDS_STOCK);
            }
            cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(requestDTO.getQuantity())
                    .build();
            cart.getCartItems().add(cartItem);
        }

        cartItemRepository.save(cartItem);
        // Bước 5: Tính tổng và Map sang DTO trả về
        return buildCartResponse(cart);
    }

    @Override
    @Transactional
    public CartResponseDTO updateCartItem(Long cartItemId, Integer quantity) {
        // Lấy Cart của user hiện tại để đối chiếu
        Cart currentCart = getCurrentUserCart();
        // Tìm CartItem
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        // Kiểm tra bảo mật: Món hàng này có nằm trong giỏ của user đang đăng nhập không?
        if (!cartItem.getCart().getId().equals(currentCart.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED); // Không được phép sửa đồ của người khác
        }
        // Tính tồn kho khả dụng
        Product product = cartItem.getProduct();
        int availableQuantity = product.getStockQuantity() - product.getHoldQuantity();
        // Xử lý số lượng
        if (quantity <= 0) {
            // Nếu khách cố tình truyền số lượng <= 0, ta coi như họ muốn xóa món đó luôn
            removeCartItem(cartItemId);
            // Sau khi xóa thì phải fetch lại currentCart để trả về cho đúng
            return buildCartResponse(getCurrentUserCart());
        }
        if (quantity > availableQuantity) {
            throw new AppException(ErrorCode.QUANTITY_EXCEEDS_STOCK);
        }
        // Cập nhật và Lưu
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        return buildCartResponse(currentCart);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        Cart currentCart = getCurrentUserCart();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        if (!cartItem.getCart().getId().equals(currentCart.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Gỡ bỏ khỏi List của Cart và xóa dưới DB
        currentCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart() {
        Cart currentCart = getCurrentUserCart();

        // Xóa sạch tất cả các item thuộc về Cart này
        cartItemRepository.deleteAll(currentCart.getCartItems());

        // Xóa trong bộ nhớ Java để đồng bộ
        currentCart.getCartItems().clear();
    }

    // Lấy giỏ hàng hiện tại của user
    private Cart getCurrentUserCart() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }


    private CartResponseDTO buildCartResponse(Cart cart) {
        List<CartItemResponseDTO> itemDTOs = cart.getCartItems().stream()
                .map(item -> {
                    CartItemResponseDTO dto = cartMapper.toCartItemResponseDTO(item);
                    int available = item.getProduct().getStockQuantity() - item.getProduct().getHoldQuantity();
                    dto.setAvailableQuantity(available);
                    return dto;
                })
                .toList();

        int totalQuantity = itemDTOs.stream().mapToInt(CartItemResponseDTO::getQuantity).sum();

        BigDecimal totalPrice = itemDTOs.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponseDTO.builder()
                .cartItems(itemDTOs)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .build();
    }
}
