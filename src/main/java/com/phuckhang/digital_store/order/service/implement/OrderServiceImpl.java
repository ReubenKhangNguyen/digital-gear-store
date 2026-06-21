package com.phuckhang.digital_store.order.service.implement;

import com.phuckhang.digital_store.catalog.entity.Product;
import com.phuckhang.digital_store.catalog.repository.ProductRepository;
import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.entity.UserAddress;
import com.phuckhang.digital_store.iam.repository.UserAddressRepository;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import com.phuckhang.digital_store.order.dto.request.OrderRequestDTO;
import com.phuckhang.digital_store.order.dto.response.OrderResponseDTO;
import com.phuckhang.digital_store.order.entity.Cart;
import com.phuckhang.digital_store.order.entity.Order;
import com.phuckhang.digital_store.order.entity.OrderDetail;
import com.phuckhang.digital_store.order.enums.OrderStatus;
import com.phuckhang.digital_store.order.enums.PaymentStatus;
import com.phuckhang.digital_store.order.mapper.OrderMapper;
import com.phuckhang.digital_store.order.repository.CartRepository;
import com.phuckhang.digital_store.order.repository.OrderRepository;
import com.phuckhang.digital_store.order.service.CartService;
import com.phuckhang.digital_store.order.service.OrderHistoryService;
import com.phuckhang.digital_store.order.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    UserRepository userRepository;
    CartRepository cartRepository;
    CartService cartService;
    UserAddressRepository userAddressRepository;
    ProductRepository productRepository;
    OrderRepository orderRepository;
    OrderHistoryService orderHistoryService;
    OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponseDTO checkout(OrderRequestDTO requestDTO) {
        // 1. Lấy thông tin User
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        // 2. Kiểm tra Giỏ hàng
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_EMPTY)); // Hãy thêm CART_EMPTY vào ErrorCode
        if (cart.getCartItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }
        // 3. Lấy và kiểm tra Địa chỉ
        UserAddress address = userAddressRepository.findById(requestDTO.getAddressId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Chuỗi địa chỉ in ra giấy
        String fullShippingAddress = address.getSpecificAddress() + ", " +
                address.getWardName() + ", " +
                address.getDistrictName() + ", " +
                address.getProvinceName();
        // 4. Tạo Entity Order (Snapshot 1)
        Order order = Order.builder()
                .orderCode("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .receiverName(address.getReceiverName())
                .receiverPhone(address.getReceiverPhone())
                .shippingAddress(fullShippingAddress)
                .toDistrictId(address.getDistrictId())
                .toWardCode(address.getWardCode())
                .shippingFee(BigDecimal.ZERO) // Tạm thời Free Ship
                .orderStatus(OrderStatus.PENDING)
                .paymentStatus(PaymentStatus.UNPAID)
                .paymentMethod(requestDTO.getPaymentMethod())
                .customerNote(requestDTO.getCustomerNote())
                .build();
        // 5. Duyệt Giỏ hàng, tạo OrderDetail (Snapshot 2) và Giữ Kho
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (var cartItem : cart.getCartItems()) {
            // MySQL khóa dòng dữ liệu của Product này lại cho đến khi Transaction kết thúc
            Product product = productRepository.findByIdForUpdate(cartItem.getProduct().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));


            BigDecimal itemTotal = product.getPrice().multiply(new BigDecimal(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            int availableQuantity = product.getStockQuantity() - product.getHoldQuantity();
            if (cartItem.getQuantity() > availableQuantity) {
                throw new AppException(ErrorCode.QUANTITY_EXCEEDS_STOCK);
            }

            product.setHoldQuantity(product.getHoldQuantity() + cartItem.getQuantity());
            productRepository.save(product);
            // Tạo Chi tiết Đơn hàng
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .productName(product.getName())
                    .build();
            order.getOrderDetails().add(orderDetail);
        }
        order.setTotalAmount(totalAmount);
        // 6. Lưu toàn bộ xuống Database
        Order savedOrder = orderRepository.save(order);
        // 7. Ghi Lịch sử
        orderHistoryService.saveHistory(savedOrder, null, OrderStatus.PENDING, "Khởi tạo đơn hàng từ Giỏ hàng");
        // 8. Dọn dẹp Giỏ hàng
        cartService.clearCart();
        // 9. Trả kết quả
        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getMyOrders() {
        User user = getCurrentUser();
        // Lấy danh sách đơn, sắp xếp mới nhất lên đầu
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return orders.stream().map(orderMapper::toOrderResponseDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND)); // Vui lòng tạo ErrorCode này
        User currentUser = getCurrentUser();

        // IDOR Check: Chống xem trộm đơn hàng của người khác
        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        return orderMapper.toOrderResponseDTO(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        User currentUser = getCurrentUser();
        if (!order.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Chỉ cho phép hủy khi chưa được duyệt
        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.ORDER_CANNOT_BE_CANCELLED); // Tạo thêm ErrorCode này
        }
        // Xả kho ảo (Nhả hàng lại cho người khác mua)
        for (OrderDetail detail : order.getOrderDetails()) {
            Product product = productRepository.findByIdForUpdate(detail.getProduct().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            product.setHoldQuantity(product.getHoldQuantity() - detail.getQuantity());
            productRepository.save(product);
        }
        // Đổi trạng thái & Lưu DB
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        // Lưu vết lịch sử
        orderHistoryService.saveHistory(order, OrderStatus.PENDING, OrderStatus.CANCELLED, "Khách hàng tự hủy đơn");
    }

    @Override
    @Transactional (readOnly = true)
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);
        return orderPage.map(orderMapper::toOrderResponseDTO);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        OrderStatus oldStatus = order.getOrderStatus();

        // 1. Chặn các đơn đã chốt sổ
        if (oldStatus == OrderStatus.CANCELLED || oldStatus == OrderStatus.COMPLETED || oldStatus == OrderStatus.RETURNED) {
            throw new AppException(ErrorCode.ORDER_LOCKED);
        }

        // 2. CHẶN LỖI NHẢY CÓC (STATE MACHINE VALIDATION)
        if (oldStatus == OrderStatus.PENDING && (newStatus != OrderStatus.CONFIRMED && newStatus != OrderStatus.CANCELLED)) {
            throw new AppException(ErrorCode.INVALID_STATE_TRANSITION); // Đang chờ duyệt thì chỉ được Duyệt hoặc Hủy
        }
        if (oldStatus == OrderStatus.CONFIRMED && (newStatus != OrderStatus.SHIPPING && newStatus != OrderStatus.CANCELLED)) {
            throw new AppException(ErrorCode.INVALID_STATE_TRANSITION); // Đã duyệt thì chỉ được Giao hàng hoặc Hủy
        }
        if (oldStatus == OrderStatus.SHIPPING && (newStatus != OrderStatus.COMPLETED && newStatus != OrderStatus.DELIVERY_FAILED)) {
            throw new AppException(ErrorCode.INVALID_STATE_TRANSITION); // Đang giao thì chỉ được Thành công hoặc Thất bại
        }

        // 3. XỬ LÝ KHO: Duyệt Đơn (Từ PENDING -> CONFIRMED)
        if (oldStatus == OrderStatus.PENDING && newStatus == OrderStatus.CONFIRMED) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Product product = productRepository.findByIdForUpdate(detail.getProduct().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                // Trừ kho thật & Xả kho ảo
                product.setStockQuantity(product.getStockQuantity() - detail.getQuantity());
                product.setHoldQuantity(product.getHoldQuantity() - detail.getQuantity());
            }
        }

        // 4. XỬ LÝ KHO: Admin Hủy/Hoàn Đơn
        if (newStatus == OrderStatus.CANCELLED || newStatus == OrderStatus.RETURNED || newStatus == OrderStatus.DELIVERY_FAILED) {
            for (OrderDetail detail : order.getOrderDetails()) {
                Product product = productRepository.findByIdForUpdate(detail.getProduct().getId())
                        .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

                if (oldStatus == OrderStatus.PENDING) {
                    // Hủy lúc chưa duyệt -> Chỉ xả kho ảo
                    product.setHoldQuantity(product.getHoldQuantity() - detail.getQuantity());
                } else {
                    // Hủy/Giao thất bại sau khi đã duyệt -> Cộng trả lại kho thật
                    product.setStockQuantity(product.getStockQuantity() + detail.getQuantity());
                }
            }
        }

        // Cập nhật trạng thái
        order.setOrderStatus(newStatus);

        if (newStatus == OrderStatus.COMPLETED && order.getPaymentMethod().equals("COD")) {
            order.setPaymentStatus(PaymentStatus.PAID);
        }

        // Ghi lại lịch sử
        orderHistoryService.saveHistory(order, oldStatus, newStatus, "Admin cập nhật trạng thái hệ thống");

        // Gọi lệnh save cho order để mapping trả về kết quả ngay lập tức
        return orderMapper.toOrderResponseDTO(orderRepository.save(order));
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
