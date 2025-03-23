package src;

import src.Interfaces.src.*;

// Student class - Can Buy, Sell, Update, Delete own products
public class Student extends UserDetails implements UserAction {
    public Student(String username, String enrollmentNumber, String password, String email) {
        super(username, enrollmentNumber, password, email, "Student");
    }

    @Override
    public void displayUserDetails() {
        System.out.println("Student: " + username + " | Email: " + email);
    }

    @Override
    public void register() { System.out.println(username + " registered successfully!"); }

    @Override
    public void login() { System.out.println(username + " logged in!"); }

    @Override
    public void logout() { System.out.println(username + " logged out!"); }

    @Override
    public void editProfile() { System.out.println(username + " updated profile!"); }

    // Product actions
    public void buyProduct(Product product) {
        System.out.println(username + " bought " + product.getProductName());
    }

    public void sellProduct(Product product) {
        System.out.println(username + " sold " + product.getProductName());
    }

    public void updateProduct(Product product) {
        System.out.println(username + " updated " + product.getProductName());
    }

    public void deleteOwnProduct(Product product) {
        System.out.println(username + " deleted their product: " + product.getProductName());
    }

    public String getUsername() {return username;}
}

