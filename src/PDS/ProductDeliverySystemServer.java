package PDS;
import PDS.ProductDeliverySystem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;


public class ProductDeliverySystemServer extends UnicastRemoteObject implements ProductDeliverySystem {
    // Database connection details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/dcoms_test";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";
    // Implement remote methods from the interface
    public ProductDeliverySystemServer() throws RemoteException {
        super();
    }

    @Override
    public boolean registerAccount(UserInfoDTO userInfo) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the username already exists in the database
            if (isUsernameTaken(userInfo)) {
                // If the username is taken, prompt the user to enter a new one
                System.out.println("Username already exists. Please choose a different username.");
                return false;
            }

            // If the username is not taken, proceed with the registration
            String sql = "INSERT INTO users (first_name, last_name, email, phone, address, username, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userInfo.getFirstName());
                preparedStatement.setString(2, userInfo.getLastName());
                preparedStatement.setString(3, userInfo.getEmail());
                preparedStatement.setString(4, userInfo.getPhone());
                preparedStatement.setString(5, userInfo.getAddress());
                preparedStatement.setString(6, userInfo.getUsername());
                preparedStatement.setString(7, userInfo.getPassword());

                int rowsAffected = preparedStatement.executeUpdate();

                // Return true if registration is successful
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException or log it
            return false; // Return false if registration fails
        }
    }





    // Helper method to check if a username is already taken

    @Override
    public boolean isUsernameTaken(UserInfoDTO userInfo) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userInfo.getUsername());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    // If a record is found, the username is taken
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException or log it
            return false; // Return false if an exception occurs
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
    public boolean loginUser(UserInfoDTO userInfo) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userInfo.getUsername());
                preparedStatement.setString(2, userInfo.getPassword());

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

