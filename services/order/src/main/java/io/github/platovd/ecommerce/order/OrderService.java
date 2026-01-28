package io.github.platovd.ecommerce.order;

import io.github.platovd.ecommerce.customer.CustomerClient;
import io.github.platovd.ecommerce.exception.BusinessException;
import io.github.platovd.ecommerce.kafka.OrderConfirmation;
import io.github.platovd.ecommerce.kafka.OrderProducer;
import io.github.platovd.ecommerce.orderline.OrderLineRequest;
import io.github.platovd.ecommerce.orderline.OrderLineService;
import io.github.platovd.ecommerce.payment.PaymentClient;
import io.github.platovd.ecommerce.payment.PaymentRequest;
import io.github.platovd.ecommerce.product.ProductClient;
import io.github.platovd.ecommerce.product.PurchaseRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final PaymentClient paymentClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));
        var purchasedProducts = productClient.purchaseProducts(request.products());
        var order = repository.save(mapper.toOrder(request));
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        paymentClient.requestOrderPayment(new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        ));

        orderProducer.sendOrderConformation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );
        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public OrderResponse findById(Integer orderId) {
        return repository
                .findById(orderId).map(mapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("No order found with the provided ID:: " + orderId));
    }
}
