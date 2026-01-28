package io.github.platovd.ecommerce.email;

import lombok.Getter;

public enum EmailTemplates {
    PAYMENT_CONFORMATION("payment-conformation.html", "Payment successfully processed"),
    ORDER_CONFORMATION("order-conformation.html", "Order conformation");

    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
