package PDS;
import PDS.ProductDeliverySystem;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


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


    @Override
    public List<CategoryDTO> getCategories() throws RemoteException {
        List<CategoryDTO> categories = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM categories";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        CategoryDTO category = new CategoryDTO();
                        category.setCategoryId(resultSet.getInt("category_id"));
                        category.setName(resultSet.getString("category_name"));
                        categories.add(category);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }


    @Override
    public List<ProductDTO> getProductsByCategory(String category) throws RemoteException {
        List<ProductDTO> products = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM products WHERE category_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, category);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ProductDTO product = new ProductDTO();
                        product.setProductId(resultSet.getInt("product_id"));
                        product.setProductName(resultSet.getString("product_name"));
                        product.setDescription(resultSet.getString("description"));
                        product.setPrice(resultSet.getDouble("price"));
                        product.setAvailable(resultSet.getInt("quantity_available"));
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }



}

