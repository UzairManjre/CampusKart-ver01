package code.src;

import code.exceptions.UnauthorizedActionException;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private Student buyer;
    private Student seller;
    private Product product;

    // List to store all transactions
    private static List<Transaction> transactionList = new ArrayList<>();

    public Transaction(Student buyer, Student seller, Product product) {
        this.buyer = buyer;
        this.seller = seller;
        this.product = product;
        transactionList.add(this);
    }

    public Student getBuyer() {
        return buyer;
    }

    public Student getSeller() {
        return seller;
    }

    public Product getProduct() {
        return product;
    }

    public static List<Transaction> getUserTransactions(Student user) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction t : transactionList) {
            if (t.getBuyer().equals(user) || t.getSeller().equals(user)) {
                userTransactions.add(t);
            }
        }
        return userTransactions;
    }

    public static List<Transaction> getAllTransactions(UserDetails user) throws UnauthorizedActionException {
        if (user instanceof Moderator) {  // Now valid check
            return new ArrayList<>(transactionList);
        }
        throw new UnauthorizedActionException("Only moderators can view all transactions.");
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "Buyer=" + buyer.getUsername() +
                ", Seller=" + seller.getUsername() +
                ", Product=" + product.getProductName() +
                '}';
    }
}
