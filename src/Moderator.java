package src;

import src.Interfaces.src.UserAction;
import src.exceptions.UnauthorizedActionException;

public class Moderator extends UserDetails implements UserAction {
    public Moderator(String username, String enrollmentNumber, String password, String email) {
        super(username, enrollmentNumber, password, email, "Moderator");
    }

    @Override
    public void displayUserDetails() {
        System.out.println("Moderator: " + username + " | Email: " + email);
    }

    @Override
    public void register() { System.out.println(username + " registered successfully!"); }

    @Override
    public void login() { System.out.println(username + " logged in!"); }

    @Override
    public void logout() { System.out.println(username + " logged out!"); }

    @Override
    public void editProfile() { System.out.println(username + " updated profile!"); }

    // Moderator actions
    public void deleteOtherUserProduct(Product product, Student student) {
        System.out.println("Moderator " + username + " deleted " + student.getUsername() + "'s product: " + product.getProductName());
    }

    public void viewAllTransactions() {
        System.out.println(username + " is viewing all transactions...");
        try {
            for (Transaction t : Transaction.getAllTransactions(this)) {
                System.out.println(t);
            }
        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        }
    }
}