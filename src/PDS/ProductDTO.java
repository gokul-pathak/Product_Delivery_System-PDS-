package PDS;

import java.io.Serializable;

public class ProductDTO implements Serializable {
    private int productId;
    private String productName;
    private String description;
    private double price;
    private int available;
    private int categoryId;

    // Constructors

    public ProductDTO() {
        // Default constructor
    }

    public ProductDTO(int productId, String productName, String description, double price, int available, int categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.available = available;
        this.categoryId = categoryId;
    }

    // Setter methods

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    // Getter methods

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }



    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailable() {
        return available;
    }

    public int getCategoryId() {
        return categoryId;
    }
}
