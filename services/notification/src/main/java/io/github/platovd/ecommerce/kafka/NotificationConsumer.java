package io.github.platovd.ecommerce.kafka;

import io.github.platovd.ecommerce.email.EmailService;
import io.github.platovd.ecommerce.kafka.order.OrderConformation;
import io.github.platovd.ecommerce.kafka.payment.PaymentConformation;
import io.github.platovd.ecommerce.notification.Notification;
import io.github.platovd.ecommerce.notification.NotificationRepository;
import io.github.platovd.ecommerce.notification.NotificationType;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConformation conformation) throws MessagingException {
        log.info(String.format("Consuming the message from payment topic Topic:: %s", conformation));
        repository.save(
                Notification.builder()
                        .notificationType(NotificationType.PAYMENT_CONFORMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConformation(conformation)
                        .build()
        );

        var customerName = conformation.customerFirstname() + " " + conformation.customerLastname();
        emailService.sendPaymentSuccessEmail(
                conformation.customerEmail(),
                customerName,
                conformation.amount(),
                conformation.orderReference()
        );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderSuccessNotification(OrderConformation conformation) throws MessagingException {
        log.info(String.format("Consuming the message from order topic Topic:: %s", conformation));
        repository.save(
                Notification.builder()
                        .notificationType(NotificationType.ORDER_CONFORMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConformation(conformation)
                        .build()
        );

        var customerName = conformation.customer().firstname() + " " + conformation.customer().lastname();
        emailService.sendOrderConformationEmail(
                conformation.customer().email(),
                customerName,
                conformation.totalAmount(),
                conformation.orderReference(),
                conformation.products()
        );
    }
}
