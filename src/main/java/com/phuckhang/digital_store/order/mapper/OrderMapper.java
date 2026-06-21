package com.phuckhang.digital_store.order.mapper;

import com.phuckhang.digital_store.order.dto.response.OrderDetailResponseDTO;
import com.phuckhang.digital_store.order.dto.response.OrderResponseDTO;
import com.phuckhang.digital_store.order.entity.Order;
import com.phuckhang.digital_store.order.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponseDTO toOrderResponseDTO(Order order);

    @Mapping(target = "productId", source = "product.id")
    OrderDetailResponseDTO toOrderDetailResponseDTO(OrderDetail orderDetail);
}
