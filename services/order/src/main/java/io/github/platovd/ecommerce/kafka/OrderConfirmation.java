package io.github.platovd.ecommerce.kafka;

import io.github.platovd.ecommerce.customer.CustomerResponse;
import io.github.platovd.ecommerce.order.PaymentMethod;
import io.github.platovd.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
