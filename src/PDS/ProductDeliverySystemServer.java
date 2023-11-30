package PDS;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;


public class ProductDeliverySystemServer extends UnicastRemoteObject implements ProductDeliverySystem {
    // Database connection details
    public static final String DB_URL = "jdbc:mysql://localhost:3306/dcoms_test";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";

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
            String sql = "INSERT INTO users (first_name, last_name, IC_Number, email, phone, address, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, userInfo.getFirstName());
                preparedStatement.setString(2, userInfo.getLastName());
                preparedStatement.setString(3, userInfo.getICNumber());
                preparedStatement.setString(4, userInfo.getEmail());
                preparedStatement.setString(5, userInfo.getPhone());
                preparedStatement.setString(6, userInfo.getAddress());
                preparedStatement.setString(7, userInfo.getUsername());
                preparedStatement.setString(8, userInfo.getPassword());

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
            // Start the RMI registry on port 1099
            //LocateRegistry.createRegistry(1099);

            // Create an instance of your RMI server
            ProductDeliverySystemServer server = new ProductDeliverySystemServer();

            // Bind the server to the registry with the name "PDS"
            Registry registry = LocateRegistry.getRegistry(1099);
            registry.rebind("PDS", server);

            System.out.println("Server ready.");

            // You might want to add some code here to keep the server running,
            // for example, waiting for user input or using a loop.

        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
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
                        product.setCategoryId(resultSet.getInt("category_id"));
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
                        product.setProductId(resultSet.getString("product_id"));
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
    public boolean addUserOrder(OrderDTO order) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            connection.setAutoCommit(false);

            // Insert into orders table
            String queryOrder = "INSERT INTO orders (user_id, order_date_time) VALUES (?, ?)";
            try (PreparedStatement orderStatement = connection.prepareStatement(queryOrder, Statement.RETURN_GENERATED_KEYS)) {
                orderStatement.setInt(1, order.getUser_id());
                orderStatement.setObject(2, order.getOrder_date_time());

                int rowsAffectedOrder = orderStatement.executeUpdate();

                if (rowsAffectedOrder > 0) {
                    // Get the generated order ID
                    ResultSet generatedKeys = orderStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int orderId = generatedKeys.getInt(1);

                        // Insert into orderdetails table for each order detail
                        String queryDetails = "INSERT INTO orderdetails (product_id, product_quantity, order_id) VALUES (?, ?, ?)";
                        try (PreparedStatement detailsStatement = connection.prepareStatement(queryDetails)) {
                            for (OrderDTO orderDetail : order.getOrderDetailsList()) {
                                detailsStatement.setInt(1, orderDetail.getProduct_id());
                                detailsStatement.setInt(2, orderDetail.getProduct_quantity());
                                detailsStatement.setInt(3, orderId);

                                int rowsAffectedDetails = detailsStatement.executeUpdate();

                                if (rowsAffectedDetails <= 0) {
                                    connection.rollback();
                                    return false;
                                }
                            }

                            // Delete from carts table
                            String cartDeleteQuery = "DELETE FROM carts WHERE product_id = ? AND user_id = ?";
                            try (PreparedStatement cartDeleteStatement = connection.prepareStatement(cartDeleteQuery)) {
                                for (OrderDTO orderDetail : order.getOrderDetailsList()) {
                                    cartDeleteStatement.setInt(1, orderDetail.getProduct_id());
                                    cartDeleteStatement.setInt(2, order.getUser_id());

                                    int rowsAffectedCart = cartDeleteStatement.executeUpdate();

                                    if (rowsAffectedCart <= 0) {
                                        connection.rollback();
                                        return false;
                                    }
                                }
                                connection.commit();
                                return true;
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    @Override
    public List<OrderDTO> getAllOrderbyUserId(int userId) throws RemoteException {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT o.id, o.order_date_time, o.order_status, od.product_id, od.product_quantity, p.product_name, p.price " +
                    "FROM orders o " +
                    "JOIN orderdetails od ON o.id = od.order_id " +
                    "JOIN products p ON od.product_id = p.product_id " +
                    "WHERE o.user_id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, userId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Map<Integer, OrderDTO> orderMap = new HashMap<>();

                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("id");

                        // Check if the order is already in the map
                        OrderDTO order = orderMap.get(orderId);

                        if (order == null) {
                            order = new OrderDTO();
                            order.setId(orderId);
                            order.setDateTime(resultSet.getString("order_date_time"));
                            order.setOrderStatus(resultSet.getString("order_status"));
                            orderMap.put(orderId, order);
                        }

                        // Order details
                        OrderDTO orderDetail = new OrderDTO();
                        orderDetail.setProductName(resultSet.getString("product_name"));
                        orderDetail.setProductQuantity(resultSet.getInt("product_quantity"));
                        orderDetail.setPrice(resultSet.getDouble("price"));

                        // Add order detail to the order
                        order.getOrderDetailsList().add(orderDetail);
                    }

                    // Add all orders to the list
                    orders.addAll(orderMap.values());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }

    @Override
    public List<OrderDTO> getAllOrdersbyStatus(String status) throws RemoteException {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT o.id, o.order_date_time, o.order_status, od.product_id, od.product_quantity, p.product_name, p.price, u.username " +
                    "FROM orders o " +
                    "JOIN orderdetails od ON o.id = od.order_id " +
                    "JOIN products p ON od.product_id = p.product_id " +
                    "JOIN users u ON o.user_id = u.id " +
                    "WHERE o.order_status = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, status);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    Map<Integer, OrderDTO> orderMap = new HashMap<>();

                    while (resultSet.next()) {
                        int orderId = resultSet.getInt("id");

                        // Check if the order is already in the map
                        OrderDTO order = orderMap.get(orderId);

                        if (order == null) {
                            order = new OrderDTO();
                            order.setId(orderId);
                            order.setDateTime(resultSet.getString("order_date_time"));
                            order.setUsername(resultSet.getString("username"));
                            order.setOrderStatus(resultSet.getString("order_status"));
                            orderMap.put(orderId, order);
                        }

                        // Order details
                        OrderDTO orderDetail = new OrderDTO();
                        orderDetail.setProductName(resultSet.getString("product_name"));
                        orderDetail.setProductQuantity(resultSet.getInt("product_quantity"));
                        orderDetail.setPrice(resultSet.getDouble("price"));

                        // Add order detail to the order
                        order.getOrderDetailsList().add(orderDetail);
                    }

                    // Add all orders to the list
                    orders.addAll(orderMap.values());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return orders;
    }


    @Override
    public boolean cancelOrder(int selectedOrderId) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE orders SET order_status = 'Cancelled' WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, selectedOrderId);
                int rowsAffected = updateStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateOrderStatus(int selectedOrderId, String orderStatus) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateQuery = "UPDATE orders SET order_status = ? WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, orderStatus);
                updateStatement.setInt(2, selectedOrderId);
                int rowsAffected = updateStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                        //user.setLastName(resultSet.getString("last_name"));
                        user.setLastName(resultSet.getString("IC_Number"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPhone(resultSet.getString("phone"));
                        user.setAddress(resultSet.getString("address"));
                        user.setUsername(resultSet.getString("username"));

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
                    String insertSql = "INSERT INTO deleted_users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        if (deletedUser != null) {
                            insertStatement.setInt(1, deletedUser.getId());
                            insertStatement.setString(2, deletedUser.getUsername());
                            insertStatement.setString(3, deletedUser.getFirstName());
                            insertStatement.setString(4, deletedUser.getLastName());
                            insertStatement.setString(5, deletedUser.getICNumber());
                            insertStatement.setString(6, deletedUser.getEmail());
                            insertStatement.setString(7, deletedUser.getPhone());
                            insertStatement.setString(8, deletedUser.getAddress());
                            insertStatement.setString(9, deletedUser.getPassword());
                            insertStatement.setInt(10, deletedUser.getRole());

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
                        userInfo.setLastName(resultSet.getString("IC_Number"));
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

    @Override
    public boolean updateUser(UserInfoDTO updatedUser) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Construct SQL query to update user details
            String updateSql = "UPDATE users SET first_name=?, last_name=?, IC_Number=?, email=?, phone=?, username=? WHERE id=?";

            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                // Set parameters for the update query
                updateStatement.setString(1, updatedUser.getFirstName());
                updateStatement.setString(2, updatedUser.getLastName());
                updateStatement.setString(3, updatedUser.getICNumber());
                updateStatement.setString(4, updatedUser.getEmail());
                updateStatement.setString(5, updatedUser.getPhone());
                updateStatement.setString(6, updatedUser.getUsername());
                updateStatement.setInt(7, updatedUser.getId());

                // Execute the update query
                int rowsAffected = updateStatement.executeUpdate();

                // Return true if the update is successful (rowsAffected > 0), false otherwise
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception or log it
            return false; // Return false if the update fails
        }
    }

    @Override
    public boolean addCategory(CategoryDTO categoryInfo) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (isCategoryNameTaken(categoryInfo)) {
                System.out.println("Category name already exists. Please choose a different category name.");
                return false;
            }
            String sql = "INSERT INTO categories (category_name) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, categoryInfo.getName());
                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean updateCategory(CategoryDTO updatedCategory) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateSql = "UPDATE categories SET category_name=? WHERE category_id=?";

            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setString(1, updatedCategory.getName());
                updateStatement.setInt(2, updatedCategory.getCategoryId());
                int rowsAffected = updateStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public boolean isCategoryNameTaken(CategoryDTO categoryInfo) throws RemoteException {
        String sql = "SELECT COUNT(*) FROM categories WHERE category_name = ?";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryInfo.getName());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
    @Override
    public CategoryDTO getCategoryInfoById(int categoryId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM categories WHERE category_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, categoryId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        CategoryDTO categoryInfo = new CategoryDTO();
                        categoryInfo.setCategoryId(resultSet.getInt("category_id"));
                        categoryInfo.setName(resultSet.getString("category_name"));
                        System.out.println("Retrieved Category Information: " + categoryInfo);

                        return categoryInfo;
                    } else {
                        System.out.println("Category not found with ID: " + categoryId);
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

  

    private boolean areProductsInCategory(Connection connection, int categoryId) throws SQLException {
        String queryProductsSql = "SELECT COUNT(*) FROM products WHERE category_id = ?";
        try (PreparedStatement queryProductsStatement = connection.prepareStatement(queryProductsSql)) {
            queryProductsStatement.setInt(1, categoryId);
            try (ResultSet resultSet = queryProductsStatement.executeQuery()) {
                if (resultSet.next()) {
                    int productCount = resultSet.getInt(1);
                    return productCount > 0;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteCategoryServer(int categoryId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            if (areProductsInCategory(connection, categoryId)) {
                System.out.print("There are products associated with this category. Do you want to delete all associated products and the category? (yes/no): ");
                Scanner scanner = new Scanner(System.in);
                String userConfirmation = scanner.nextLine().trim().toLowerCase();
                if (!userConfirmation.equals("yes")) {
                    System.out.println("Category deletion canceled by user.");
                    return false;
                }
            }
            String deleteCategorySql = "DELETE FROM categories WHERE category_id = ?";
            try (PreparedStatement deleteCategoryStatement = connection.prepareStatement(deleteCategorySql)) {
                deleteCategoryStatement.setInt(1, categoryId);
                int rowsAffected = deleteCategoryStatement.executeUpdate();
                if (rowsAffected > 0) {
                    String insertSql = "INSERT INTO deleted_categories (category_id, category_name) VALUES (?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        CategoryDTO deletedCategory = getCategoryInfoById(categoryId);

                        if (deletedCategory != null) {
                            insertStatement.setInt(1, deletedCategory.getCategoryId());
                            insertStatement.setString(2, deletedCategory.getName());
                            int insertedRows = insertStatement.executeUpdate();
                            System.out.println("Rows Inserted: " + insertedRows);
                            return insertedRows > 0;
                        } else {
                            System.out.println("Deleted Category is null");
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addProducts(ProductDTO productInfo) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if the category exists
            int categoryId = productInfo.getCategoryId();
            if (!categoryExists(connection, categoryId)) {
                System.out.println("Category with ID " + categoryId + " does not exist. Cannot add product.");
                return false;
            }
            String sql = "INSERT INTO products (product_name, description, price, quantity_available, category_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, productInfo.getProductName());
                preparedStatement.setString(2, productInfo.getDescription());
                preparedStatement.setDouble(3, productInfo.getPrice());
                preparedStatement.setInt(4, productInfo.getAvailable());
                preparedStatement.setInt(5, productInfo.getCategoryId());

                int rowsAffected = preparedStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean categoryExists(Connection connection, int categoryId) throws SQLException {
        String checkCategorySql = "SELECT * FROM categories WHERE category_id = ?";
        try (PreparedStatement checkCategoryStatement = connection.prepareStatement(checkCategorySql)) {
            checkCategoryStatement.setInt(1, categoryId);
            try (ResultSet resultSet = checkCategoryStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    @Override
    public boolean updateProduct(ProductDTO updatedProduct) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String updateSql = "UPDATE products SET product_name=?, description=?, price=?, quantity_available=?, category_id=? WHERE product_id=?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                updateStatement.setString(1, updatedProduct.getProductName());
                updateStatement.setString(2, updatedProduct.getDescription());
                updateStatement.setDouble(3, updatedProduct.getPrice());
                updateStatement.setInt(4, updatedProduct.getAvailable());
                updateStatement.setInt(5, updatedProduct.getCategoryId());
                updateStatement.setInt(6, updatedProduct.getProductId());
                int rowsAffected = updateStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public ProductDTO getProductInfoById(int productId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM products WHERE product_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, productId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        ProductDTO productInfo = new ProductDTO();
                        productInfo.setProductId(resultSet.getInt("product_id"));
                        productInfo.setProductName(resultSet.getString("product_name"));
                        productInfo.setDescription(resultSet.getString("description"));
                        productInfo.setPrice(resultSet.getDouble("price"));
                        productInfo.setAvailable(resultSet.getInt("quantity_available"));
                        productInfo.setCategoryId(resultSet.getInt("category_id"));


                        System.out.println("Retrieved Product Information: " + productInfo);

                        return productInfo;
                    } else {

                        System.out.println("Product not found with ID: " + productId);
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public boolean deleteProduct(int productId) throws RemoteException {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            ProductDTO deletedProduct = getProductInfoById(productId);

            System.out.println("Deleted Product Information: " + deletedProduct);
            String deleteSql = "DELETE FROM products WHERE product_id = ?";
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, productId);

                int rowsAffected = deleteStatement.executeUpdate();

                if (rowsAffected > 0) {
                    String insertSql = "INSERT INTO deleted_products (product_id, product_name, description, price, quantity_available, category_id) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                        if (deletedProduct != null) {
                            insertStatement.setInt(1, deletedProduct.getProductId());
                            insertStatement.setString(2, deletedProduct.getProductName());
                            insertStatement.setString(3, deletedProduct.getDescription());
                            insertStatement.setDouble(4, deletedProduct.getPrice());
                            insertStatement.setInt(5, deletedProduct.getAvailable());
                            insertStatement.setInt(6, deletedProduct.getCategoryId());

                            int insertedRows = insertStatement.executeUpdate();
                            System.out.println("Rows Inserted: " + insertedRows);
                            return insertedRows > 0;
                        } else {
                            System.out.println("Deleted Product is null");
                            return false;
                        }
                    }
                } else {

                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

}

