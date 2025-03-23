package src;

import src.Interfaces.src.ProductAction;

public class Product extends ProductDetails implements ProductAction {
    private static int productCounter = 0;  // Static counter for unique product IDs
    private int productId;  // Instance variable for each product's ID

    public Product(String productName, String description, double price, String category, int quantity, Student seller) {
        super(productName, description, price, category, quantity, seller);
        this.productId = ++productCounter;  // Assign unique ID and increment counter
    }

    @Override
    public void addProduct() {
        System.out.println("Product added: " + productName);

    }



    @Override
    public void updateProduct(String newDescription, double newPrice, int newQuantity) {
        this.description = newDescription;
        this.price = newPrice;
        this.quantity = newQuantity;
        System.out.println("Product updated: " + productName);
    }

    @Override
    public void deleteProduct() {
        System.out.println("Product deleted: " + productName);
    }

    @Override
    public void buyProduct() {
        if (quantity > 0) {
            quantity--;
            System.out.println("Product bought: " + productName);
        } else {
            System.out.println("Out of stock: " + productName);
        }
    }

    @Override
    public void sellProduct() {
        quantity++;
        System.out.println("Product restocked: " + productName);
    }

    public int getProductId() {
        return productId;
    }

    public Student getSeller() {
        return seller;
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
