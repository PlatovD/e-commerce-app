package io.github.platovd.ecommerce.notification;

import io.github.platovd.ecommerce.kafka.payment.PaymentConformation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

}
