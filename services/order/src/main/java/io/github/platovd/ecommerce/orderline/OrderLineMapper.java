package io.github.platovd.ecommerce.orderline;

import io.github.platovd.ecommerce.order.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderLineMapper {

    public OrderLine toOrderLine(OrderLineRequest orderLineRequest) {
        return OrderLine.builder()
                .id(orderLineRequest.id())
                .order(
                        Order.builder().id(orderLineRequest.id()).build()
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
