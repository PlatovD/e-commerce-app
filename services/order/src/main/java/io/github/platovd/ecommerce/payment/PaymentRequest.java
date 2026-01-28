package io.github.platovd.ecommerce.payment;

import io.github.platovd.ecommerce.customer.CustomerResponse;
import io.github.platovd.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
