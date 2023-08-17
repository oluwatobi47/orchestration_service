package uk.ac.bcu.invorchestrator.dto.order;

import lombok.Data;
import uk.ac.bcu.orderservice.proxy.OrderItems;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderRequest implements Serializable {
    protected String clientId;
    protected List<OrderItems> orderItems;
}
