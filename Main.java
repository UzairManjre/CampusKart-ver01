import code.src.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDetails loggedInUser;

    public static void main(String[] args) {
        Storage.initializeTestData();

        while (true) {
            System.out.println("\n=== CampusKart ===");
            if (loggedInUser == null) {
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) login();
                else if (choice == 2) registerUser();
                else break;
            } else {
                showMenu();
            }
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        loggedInUser = Storage.getUserByUsername(username);

        if (loggedInUser == null || !loggedInUser.getPassword().equals(password)) {
            System.out.println("Invalid username or password!");
            loggedInUser = null;  // Reset login attempt
        } else {
            System.out.println("Welcome, " + loggedInUser.getUsername() + "!");
        }
    }

    private static void registerUser() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (Storage.getUserByUsername(username) != null) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter enrollment number: ");
        String enrollmentNumber = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Are you a Student or Moderator? (S/M): ");
        String userType = scanner.nextLine().trim().toUpperCase();

        UserDetails newUser;
        if (userType.equals("S")) {
            newUser = new Student(username, enrollmentNumber, password, email);
        } else if (userType.equals("M")) {
            newUser = new Moderator(username, enrollmentNumber, password, email); // Assuming Moderator constructor takes only username & password
        } else {
            System.out.println("Invalid user type!");
            return;
        }

        Storage.addUser(newUser);
        System.out.println("User registered successfully! You can now log in.");
    }


    private static void showMenu() {
        if (loggedInUser instanceof Student) {
            studentMenu();
        } else if (loggedInUser instanceof Moderator) {
            moderatorMenu();
        }
    }

    private static void studentMenu() {
        while (true) {
            System.out.println("\n1. View Products");
            System.out.println("2. Buy Product");
            System.out.println("3. View My Orders");
            System.out.println("4. Add Product");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewProducts();
                case 2 -> buyProduct();
                case 3 -> viewMyOrders();
                case 4 -> addProduct();
                case 5 -> { loggedInUser = null; return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void moderatorMenu() {
        while (true) {
            System.out.println("\n1. View All Transactions");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewAllTransactions();
                case 2 -> { loggedInUser = null; return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void viewProducts() {
        List<Product> products = Storage.getAllProducts();
        System.out.println("\nAvailable Products:");
        for (Product p : products) {
            System.out.println(p.getProductId() + ". " + p.getProductName() + " - " + p.getPrice());
        }
    }

    private static void buyProduct() {
        System.out.print("Enter Product ID to buy: ");
        int productId = scanner.nextInt();
        scanner.nextLine();

        Product product = Storage.getProductById(productId);
        if (product == null) {
            System.out.println("Product not found!");
            return;
        }

        Student buyer = (Student) loggedInUser;
        Student seller = product.getSeller();

        Transaction transaction = new Transaction(buyer, seller, product);
        Storage.addTransaction(transaction);
        System.out.println("You bought " + product.getProductName() + "!");
    }

    private static void addProduct() {
        if (!(loggedInUser instanceof Student)) {
            System.out.println("Only students can add products!");
            return;
        }

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter price: ");
        double price = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        Product newProduct = new Product(name, description, price, category, quantity, (Student) loggedInUser);
        Storage.addProduct(newProduct);
        System.out.println("Product added successfully!");
    }

    private static void viewMyOrders() {
        List<Transaction> orders = Storage.getMyOrders(loggedInUser.getUsername());
        System.out.println("\nYour Orders:");
        for (Transaction t : orders) {
            System.out.println(t);
        }
    }

    private static void viewAllTransactions() {
        List<Transaction> transactions = Storage.getTransactions();
        System.out.println("\nAll Transactions:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }
}
