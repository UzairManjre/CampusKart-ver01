package src;

import src.exceptions.*;

import java.util.List;
import java.util.Scanner;


public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static UserDetails loggedInUser;

    public static void main(String[] args) {
        try {
            Storage.initializeTestData();

            while (true) {
                System.out.println("\n=== CampusKart ===");
                if (loggedInUser == null) {
                    System.out.println("1. Login");
                    System.out.println("2. Register");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");

                    int choice;
                    try {
                        choice = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        if (choice < 1 || choice > 3) {
                            throw new InvalidInputException("Invalid choice! Please enter a valid option (1-3).");
                        }
                    } catch (InvalidInputException e) {
                        System.out.println(e.getMessage());
                        continue;
                    } catch (Exception e) {
                        System.out.println("Invalid input! Please enter a number.");
                        scanner.nextLine(); // Clear invalid input
                        continue;
                    }

                    try {
                        if (choice == 1) {
                            login();
                        } else if (choice == 2) {
                            registerUser();
                        } else if (choice == 3) {
                            System.out.println("Exiting CampusKart. Goodbye!");
                            break;
                        }
                    } catch (UserAlreadyExistsException e) {
                        System.out.println("Registration failed: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                } else {
                    try {
                        showMenu();
                    } catch (Exception e) {
                        System.out.println("An error occurred while processing your request: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("A critical error occurred: " + e.getMessage());
        } finally {
            scanner.close(); // Ensure scanner is closed properly when exiting
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

    private static void registerUser() throws UserAlreadyExistsException {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (Storage.getUserByUsername(username) != null) {
            throw new UserAlreadyExistsException("Username '" + username + "' is already taken! Try another one.");
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
            newUser = new Moderator(username, enrollmentNumber, password, email);
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
            try {
                System.out.println("\n1. View Products");
                System.out.println("2. Buy Product");
                System.out.println("3. View My Orders");
                System.out.println("4. Add Product");
                System.out.println("5. Logout");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    if (!scanner.hasNextInt()) {
                        scanner.next(); // Clear invalid input
                        throw new InvalidInputException("Invalid input! Please enter a number between 1 and 5.");
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice < 1 || choice > 5) {
                        throw new InvalidInputException("Invalid choice! Please enter a number between 1 and 5.");
                    }
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                switch (choice) {
                    case 1 -> viewProducts();
                    case 2 -> buyProduct();
                    case 3 -> viewMyOrders();
                    case 4 -> addProduct();
                    case 5 -> {
                        loggedInUser = null;
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default -> throw new InvalidInputException("Unexpected error: Invalid menu choice.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void moderatorMenu() {
        while (true) {
            try {
                System.out.println("\n1. View All Transactions");
                System.out.println("2. Logout");
                System.out.print("Choose an option: ");

                int choice;
                try {
                    if (!scanner.hasNextInt()) {
                        scanner.next(); // Clear invalid input
                        throw new InvalidInputException("Invalid input! Please enter either 1 or 2.");
                    }
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (choice < 1 || choice > 2) {
                        throw new InvalidInputException("Invalid choice! Please enter 1 or 2.");
                    }
                } catch (InvalidInputException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                switch (choice) {
                    case 1 -> viewAllTransactions();
                    case 2 -> {
                        loggedInUser = null;
                        System.out.println("Logged out successfully.");
                        return;
                    }
                    default -> throw new InvalidInputException("Unexpected error: Invalid menu choice.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void viewProducts() {
        try {
            List<Product> products = Storage.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("\nNo products available.");
                return;
            }

            System.out.println("\nAvailable Products:");
            for (Product p : products) {
                System.out.println(p.getProductId() + ". " + p.getProductName() + " - " + p.getPrice()+" - " + p.getQuantity());
            }
        } catch (Exception e) {
            System.out.println("An error occurred while retrieving products: " + e.getMessage());
        }
    }

    private static void buyProduct() {
        try {
            System.out.print("Enter Product ID to buy: ");

            if (!scanner.hasNextInt()) {
                scanner.next(); // Clear invalid input
                throw new InvalidInputException("Invalid input! Please enter a valid Product ID.");
            }

            int productId = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            Product product = Storage.getProductById(productId);
            if (product == null) {
                throw new ProductNotFoundException("Product with ID " + productId + " not found!");
            }

            if (!(loggedInUser instanceof Student)) {
                throw new UnauthorizedActionException("Only students can buy products.");
            }

            if (product.getQuantity() <= 0) {
                throw new InsufficientStockException("Product is out of stock!");
            }

            // Proceed with the purchase
            Student buyer = (Student) loggedInUser;
            Student seller = product.getSeller();

            Transaction transaction = new Transaction(buyer, seller, product);
            Storage.addTransaction(transaction);

            // Decrease quantity
            product.setQuantity(product.getQuantity() - 1);

            // Remove product if quantity becomes zero
            if (product.getQuantity() == 0) {
                Storage.removeProduct(productId);
            }

            System.out.println("You bought " + product.getProductName() + "!");

        } catch (ProductNotFoundException | UnauthorizedActionException | InvalidInputException | InsufficientStockException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    private static void addProduct() {
        try {
            if (!(loggedInUser instanceof Student)) {
                throw new UnauthorizedActionException("Only students can add products!");
            }

            System.out.print("Enter product name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) throw new InvalidInputException("Product name cannot be empty.");

            System.out.print("Enter description: ");
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) throw new InvalidInputException("Product description cannot be empty.");

            System.out.print("Enter price: ");
            if (!scanner.hasNextDouble()) {
                scanner.next(); // Clear invalid input
                throw new InvalidInputException("Invalid input! Please enter a valid price.");
            }
            double price = scanner.nextDouble();
            scanner.nextLine();
            if (price <= 0) throw new InvalidInputException("Price must be greater than zero.");

            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();
            if (category.isEmpty()) throw new InvalidInputException("Category cannot be empty.");

            System.out.print("Enter quantity: ");
            if (!scanner.hasNextInt()) {
                scanner.next(); // Clear invalid input
                throw new InvalidInputException("Invalid input! Please enter a valid quantity.");
            }
            int quantity = scanner.nextInt();
            scanner.nextLine();
            if (quantity <= 0) throw new InvalidInputException("Quantity must be greater than zero.");

            Product newProduct = new Product(name, description, price, category, quantity, (Student) loggedInUser);
            Storage.addProduct(newProduct);
            System.out.println("Product added successfully!");

        } catch (InvalidInputException | UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }


    private static void viewMyOrders() {
        try {
            if (loggedInUser == null) {
                throw new UnauthorizedActionException("You must be logged in to view your orders!");
            }

            List<Transaction> orders = Storage.getMyOrders(loggedInUser.getUsername());
            if (orders.isEmpty()) {
                System.out.println("\nYou have no orders.");
                return;
            }

            System.out.println("\nYour Orders:");
            for (Transaction t : orders) {
                System.out.println(t);
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while retrieving orders: " + e.getMessage());
        }
    }

    private static void viewAllTransactions() {
        try {
            if (!(loggedInUser instanceof Moderator)) {
                throw new UnauthorizedActionException("Only moderators can view all transactions!");
            }

            List<Transaction> transactions = Storage.getTransactions();
            if (transactions.isEmpty()) {
                System.out.println("\nNo transactions available.");
                return;
            }

            System.out.println("\nAll Transactions:");
            for (Transaction t : transactions) {
                System.out.println(t);
            }

        } catch (UnauthorizedActionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred while retrieving transactions: " + e.getMessage());
        }
    }

}