package io.github.platovd.ecommerce.product;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Integer id,
        String name,
        String description,
        Double quantity,
        BigDecimal price
) {
}
