package uk.ac.bcu.invorchestrator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.ac.bcu.invorchestrator.dto.warehouse.AddProductDTO;
import uk.ac.bcu.invorchestrator.dto.warehouse.UpdateProductDTO;
import uk.ac.bcu.invorchestrator.service.impl.ExceptionHandler;
import uk.ac.bcu.invorchestrator.service.WarehouseService;

@Component
public class WarehouseRoute extends RouteBuilder {
    private final WarehouseService warehouseService;

    private final ExceptionHandler exceptionHandler;


    public WarehouseRoute(WarehouseService warehouseService, ExceptionHandler exceptionHandler) {
        this.warehouseService = warehouseService;
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Handle Associated business process relevant to
     * @throws Exception Exception from process
     */
    private void initBusinessProcessHandlers() throws Exception {
        this.warehouseService.handleAddProduct(from("direct:add-product"));
        this.warehouseService.handleUpdateProduct(from("direct:update-product"));
        this.warehouseService.handleGetAllClientProducts(from("direct:get-client-products"));
        this.warehouseService.handleGetProduct(from("direct:get-product"));
        this.warehouseService.handleDeleteProduct(from("direct:delete-product"));
        this.warehouseService.registerAdditionalHandlers(this::from);
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .enableCORS(true)
                .component("servlet")
                .bindingMode(RestBindingMode.json)

                //Enable swagger endpoint.
                .apiContextPath("/swagger") //swagger endpoint path
                .apiContextRouteId("swagger");

        // Define endpoints and reference to request handlers
        rest()
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/inv/{clientId}/get-product/{productId}").to("direct:get-product")
                .get("/inv/{clientId}/get-products").to("direct:get-client-products")
                .post("/inv/{clientId}/add-product").type(AddProductDTO.class).to("direct:add-product")
                .put("/inv/{clientId}/update-product/{productId}").type(UpdateProductDTO.class).to("direct:update-product")
                .delete("/inv/{clientId}/delete-product/{productId}").to("direct:delete-product");

        this.initBusinessProcessHandlers();
    }
}
