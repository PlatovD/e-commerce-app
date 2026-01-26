package io.github.platovd.ecommerce.order;

import io.github.platovd.ecommerce.customer.CustomerClient;
import io.github.platovd.ecommerce.exception.BusinessException;
import io.github.platovd.ecommerce.orderline.OrderLineRequest;
import io.github.platovd.ecommerce.orderline.OrderLineService;
import io.github.platovd.ecommerce.product.ProductClient;
import io.github.platovd.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;

    public Integer createOrder(OrderRequest request) {
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer exists with the provided ID"));
        productClient.purchaseProducts(request.products());
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

//        todo: start payment

        return null;
    }
}
