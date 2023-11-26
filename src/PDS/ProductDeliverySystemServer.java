package PDS;
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


    @Override
    public int getUserIdByUsername(String username) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT id FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if user not found or an error occurs
    }

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
    public List<ProductDTO> getAllProducts() throws RemoteException {
        List<ProductDTO> products = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM products";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
            throw new RuntimeException(e);
        }
        return products;
    }


    @Override
    public List<CategoryDTO> getCategories() throws RemoteException {
        List<CategoryDTO> categories = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM categories";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
            String query = "SELECT * FROM products WHERE category_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
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
    @Override
    public boolean addProductToCart(CartDTO cart) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String querySql = "SELECT * FROM carts WHERE user_id = ? AND product_id = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(querySql)) {
                checkStatement.setInt(1, cart.getUser_id());
                checkStatement.setInt(2, cart.getProduct_id());

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int existingQuantity = resultSet.getInt("product_quantity");
                        int newQuantity = existingQuantity + cart.getProduct_quantity();
                        String updateSql = "UPDATE carts SET product_quantity = ? WHERE user_id = ? AND product_id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                            updateStatement.setInt(1, newQuantity);
                            updateStatement.setInt(2, cart.getUser_id());
                            updateStatement.setInt(3, cart.getProduct_id());
                            int rowsAffected = updateStatement.executeUpdate();
                            return rowsAffected > 0;
                        }
                    }
                }
            }
            String query = "INSERT INTO carts (product_id, product_quantity, user_id, order_date_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, cart.getProduct_id());
                preparedStatement.setInt(2, cart.getProduct_quantity());
                preparedStatement.setInt(3, cart.getUser_id());
                preparedStatement.setObject(4, cart.getOrder_date_time());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getUserRole(int userId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT role FROM users WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int role = resultSet.getInt("role");
                        System.out.println("User role retrieved: " + role); // Add this line
                        return role;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("User role not retrieved. Returning -1."); // Add this line
        return -1; // Return -1 if an error occurs or user not found
    }




    @Override
    public List<CartDTO> getAllCartProductsbyUserId(int userId) throws RemoteException {
        List<CartDTO> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT p.product_id, p.product_name, p.description, p.price, c.product_quantity " +
                    "FROM carts c " +
                    "JOIN products p ON c.product_id = p.product_id " +
                    "WHERE c.user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        CartDTO product = new CartDTO();
                        product.setProductName(resultSet.getString("product_name"));
                        product.setPrice(resultSet.getDouble("price"));
                        product.setQuantity(resultSet.getInt("product_quantity"));
                        products.add(product);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    @Override
    public List<UserInfoDTO> getAllUsers() throws RemoteException {

        List<UserInfoDTO> users = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        UserInfoDTO user = new UserInfoDTO();
                        user.setId(resultSet.getInt("id"));
                        user.setFirstName(resultSet.getString("first_name"));
                        user.setLastName(resultSet.getString("last_name"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPhone(resultSet.getString("phone"));
                        user.setUsername(resultSet.getString("username"));
                        user.setRole(resultSet.getInt("role"));

                        users.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it
        }

        return users;
    }


    @Override
    public boolean deleteUser(int userId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Retrieve user details before deleting
            UserInfoDTO deletedUser = getUserInfoById(userId);

            // Debugging: Print deletedUser information
            System.out.println("Deleted User Information: " + deletedUser);

            // Delete the user from the main users table
            String deleteSql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, userId);

                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    // If deletion from the main table is successful, insert into the deleted_users table
                    String insertSql = "INSERT INTO deleted_users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        if (deletedUser != null) {
                            insertStatement.setInt(1, deletedUser.getId());
                            insertStatement.setString(2, deletedUser.getUsername());
                            insertStatement.setString(3, deletedUser.getFirstName());
                            insertStatement.setString(4, deletedUser.getLastName());
                            insertStatement.setString(5, deletedUser.getEmail());
                            insertStatement.setString(6, deletedUser.getPhone());
                            insertStatement.setString(7, deletedUser.getAddress());
                            insertStatement.setString(8, deletedUser.getPassword());
                            insertStatement.setInt(9, deletedUser.getRole());

                            int insertedRows = insertStatement.executeUpdate();

                            // Debugging: Print the number of rows inserted
                            System.out.println("Rows Inserted: " + insertedRows);

                            // Return true if deletion and insertion are both successful
                            return insertedRows > 0;
                        } else {
                            // Debugging: Print a message if deletedUser is null
                            System.out.println("Deleted User is null");

                            // Handle the case where deletedUser is null
                            // You might log a message or take appropriate action
                            return false; // or throw an exception, depending on your requirements
                        }
                    }
                } else {
                    // Return false if deletion from the main table fails
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException or log it
            return false; // Return false if deletion fails
        }
    }


    @Override
    public UserInfoDTO getUserInfoById(int userId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        UserInfoDTO userInfo = new UserInfoDTO();
                        userInfo.setId(resultSet.getInt("id"));
                        userInfo.setUsername(resultSet.getString("username"));
                        userInfo.setFirstName(resultSet.getString("first_name"));
                        userInfo.setLastName(resultSet.getString("last_name"));
                        userInfo.setEmail(resultSet.getString("email"));
                        userInfo.setPhone(resultSet.getString("phone"));
                        userInfo.setAddress(resultSet.getString("address"));
                        userInfo.setPassword(resultSet.getString("password"));
                        userInfo.setRole(resultSet.getInt("role"));

                        // Debugging: Print user information
                        System.out.println("Retrieved User Information: " + userInfo);

                        return userInfo;
                    } else {
                        // Debugging: Print a message if the user is not found
                        System.out.println("User not found with ID: " + userId);
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQLException or log it
            return null; // Return null if an exception occurs
        }
    }

}
