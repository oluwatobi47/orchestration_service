package uk.ac.bcu.invorchestrator.dto.warehouse;

public class AddProductDTO extends UpdateProductDTO {

    private Long quantity;

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
