package com.phuckhang.digital_store.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    PRODUCT_NAME_BLANK(1110, "Tên sản phẩm không được để trống", HttpStatus.BAD_REQUEST),
    PRODUCT_SKU_BLANK(1111, "Mã SKU không được để trống", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_NULL(1112, "Giá sản phẩm không được để trống", HttpStatus.BAD_REQUEST),
    PRODUCT_PRICE_NEGATIVE(1113, "Giá sản phẩm không được là số âm", HttpStatus.BAD_REQUEST),
    PRODUCT_STOCK_NEGATIVE(1114, "Số lượng tồn kho không được là số âm", HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORY_ID_NULL(1115, "Thiếu ID Danh mục", HttpStatus.BAD_REQUEST),
    PRODUCT_BRAND_ID_NULL(1116, "Thiếu ID Thương hiệu", HttpStatus.BAD_REQUEST),
    PRODUCT_STATUS_NULL(1117, "Trạng thái sản phẩm không được để trống", HttpStatus.BAD_REQUEST),


    CATEGORY_NAME_BLANK(1210, "Tên danh mục không được để trống", HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_TOO_LONG(1211, "Tên danh mục không được vượt quá 100 ký tự", HttpStatus.BAD_REQUEST),


    BRAND_NAME_BLANK(1310, "Tên thương hiệu không được để trống", HttpStatus.BAD_REQUEST),
    BRAND_NAME_TOO_LONG(1311, "Tên thương hiệu không được vượt quá 100 ký tự", HttpStatus.BAD_REQUEST),
    BRAND_STATUS_NULL(1312, "Trạng thái thương hiệu không được để trống", HttpStatus.BAD_REQUEST),

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi hệ thống không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid message key", HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_FOUND(1101, "Sản phẩm không tồn tại trong hệ thống", HttpStatus.NOT_FOUND),
    PRODUCT_SKU_EXISTED(1102, "Mã SKU này đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),

    CATEGORY_NOT_FOUND(1201, "Danh mục không tồn tại", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_EXISTED(1202, "Tên danh mục này đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    CATEGORY_PARENT_DELETED(1203, "Không thể thêm vào danh mục cha đã bị xóa", HttpStatus.BAD_REQUEST),
    CATEGORY_PARENT_CANNOT_BE_SELF(1204, "Lỗi Logic: Một danh mục không thể tự nhận chính nó làm cha", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_ACTIVE_CHILDREN(1205, "CHẶN XÓA: Không thể xóa vì vẫn còn danh mục con đang hoạt động", HttpStatus.BAD_REQUEST),

    BRAND_NOT_FOUND(1301, "Thương hiệu không tồn tại", HttpStatus.NOT_FOUND),
    BRAND_NAME_EXISTED(1302, "Tên thương hiệu này đã tồn tại", HttpStatus.BAD_REQUEST),

    USER_EXISTED(2001, "Tên đăng nhập này đã tồn tại trong hệ thống", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(2002, "Email này đã được đăng ký", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(2003, "Số điện thoại này đã được đăng ký", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2004, "Người dùng không tồn tại", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(2005, "Tên đăng nhập hoặc mật khẩu không chính xác", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(2006, "Bạn không có quyền thực hiện hành động này", HttpStatus.FORBIDDEN),

    INVALID_USERNAME(3001, "Username không được để trống", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(3002, "Mật khẩu phải có ít nhất 6 ký tự", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(3003, "Định dạng Email không hợp lệ", HttpStatus.BAD_REQUEST),
    INVALID_FULLNAME(3001, "FullName không được để trống", HttpStatus.BAD_REQUEST),


    ;


    int code;
    String message;
    HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
