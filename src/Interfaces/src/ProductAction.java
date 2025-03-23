package src.Interfaces.src;

public interface ProductAction {
    void addProduct();
    void updateProduct(String newDescription, double newPrice, int newQuantity);
    void deleteProduct();
    void buyProduct();
    void sellProduct();

    int getProductId();
}