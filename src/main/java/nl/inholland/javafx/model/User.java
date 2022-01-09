package nl.inholland.javafx.model;

public class User {

    public enum Position {
        Admin,
        Cashier
    }

    private String username;
    private String password;
    private String firstName;
    private Position position;

    public User(String username, String password, String firstName, Position position) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.position = position;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Position getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return position + " (" + firstName +")";
    }
}
