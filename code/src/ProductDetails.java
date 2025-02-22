package code.src;

import java.util.Date;

public abstract class ProductDetails {
    public static int productId = 2400;
    protected String productName;
    protected String description;
    protected double price;
    protected String category;
    protected int quantity;
    protected Date timestamp;
    protected Student seller;

    public ProductDetails(String productName, String description, double price, String category, int quantity,Student seller) {
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.timestamp = new Date(); // Sets the current date & time
        this.seller = seller;
    }

    // Abstract methods (to be implemented by subclasses)
    public abstract void updateProduct(String newDescription, double newPrice, int newQuantity);
    public abstract void deleteProduct();

    // Getters
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public Date getTimestamp() { return timestamp; }
}
