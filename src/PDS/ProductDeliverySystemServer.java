package PDS;
import PDS.ProductDeliverySystem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class ProductDeliverySystemServer extends UnicastRemoteObject implements ProductDeliverySystem {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/dcoms_test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    // Implement remote methods from the interface
    public ProductDeliverySystemServer() throws RemoteException {
        super();
    }

    @Override
    public boolean registerAccount(String firstName, String lastName, String email, String phone, String address, String username, String password) throws RemoteException {
        // Server-side logic for user registration and database integration
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (first_name, last_name, email, phone, address, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, phone);
                preparedStatement.setString(5, address);
                preparedStatement.setString(6, username);
                preparedStatement.setString(7, password);

                int rowsAffected = preparedStatement.executeUpdate();

                // Return true if registration is successful
                return rowsAffected > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Return false if registration fails
            return false;
        }
    }


    // Implement more methods for order processing, etc.

    public static void main(String[] args) {
        try {
            // Register the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create an instance of the server and bind it to RMI registry
            ProductDeliverySystemServer server = new ProductDeliverySystemServer();
            java.rmi.registry.LocateRegistry.createRegistry(1098).rebind("PDS", server);
            System.out.println("Server is running...");
        } catch (ClassNotFoundException | RemoteException e) {
            e.printStackTrace();
        } finally {
            // Close any resources if needed
        }
    }
    @Override
    public boolean loginUser(String username, String password) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next(); // If a record is found, login is successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurs
        }
    }
}

