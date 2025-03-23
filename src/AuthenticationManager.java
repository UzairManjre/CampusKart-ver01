package src;

public class AuthenticationManager {
    public static UserDetails login(String username, String password) {
        UserDetails user = Storage.getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Login successful: " + user.getUsername());
            return user;
        } else {
            System.out.println("Invalid credentials.");
            return null;
        }
    }
}
