package io.github.platovd.ecommerce.orderline;

import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return OrderLine.builder()
                .id(orderLineRequest.id())
                .order(
                        orderLineRequest.order()
                )
                .quantity(orderLineRequest.quantity())
                .productId(orderLineRequest.productId())
                .build();
    }

    public OrderLineResponse toResponse(OrderLine orderLine) {
        return new OrderLineResponse(
                orderLine.getId(),
                orderLine.getQuantity()
        );
    }
}
