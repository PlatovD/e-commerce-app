package io.github.platovd.ecommerce.orderline;

import io.github.platovd.ecommerce.order.Order;

public record OrderLineRequest(
        Integer id,
        Order order,
        Integer productId,
        Double quantity
) {
}
