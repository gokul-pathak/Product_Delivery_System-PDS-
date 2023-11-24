package PDS;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.List;
import PDS.CartDTO;


public class ProductDeliverySystemClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // Connect to the RMI registry on the server
            Registry registry = LocateRegistry.getRegistry("localhost", 1098);

            // Look up the remote object by name
            ProductDeliverySystem pds = (ProductDeliverySystem) registry.lookup("PDS");

            // Display the menu
            System.out.println("Welcome to Product Delivery System");
            System.out.println("1. Log In");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            // Get user choice
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    // Implement login logic here
                    System.out.println("Log In");
                    System.out.println("Enter your username:");
                    String loginUsername = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String loginPassword = scanner.nextLine();

                    UserInfoDTO loginInfo = new UserInfoDTO();
                    loginInfo.setUsername(loginUsername);
                    loginInfo.setPassword(loginPassword);

                    // Call the remote method for user login
                    boolean loginStatus = pds.loginUser(loginInfo);


                    // Process the login status
                    if (loginStatus) {
                        int userId = pds.getUserIdByUsername(loginUsername);
                        if (userId != -1)
                        {
                            System.out.println(""+userId);
                        }

                        System.out.println("Login successful!");
                        System.out.println("Welcome to Product Delivery System");

                        // Display categories
                        List<CategoryDTO> categories = pds.getCategories();
                        System.out.println("Categories:");
                        for (CategoryDTO category : categories) {

                            System.out.println(category.getCategoryId() + ". " + category.getName());
                        }


                        // Get user input for category selection
                        System.out.print("Enter the number of the category you're interested in: ");
                        int selectedCategory = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character

                        // Display products for the selected category
                        List<ProductDTO> products = pds.getProductsByCategory(String.valueOf(selectedCategory));
                        if (!products.isEmpty()) {
                            System.out.println("Products for the selected category:");

                            // Display header
                            System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", "Product ID", "Product Name", "Description", "Price", "Quantity Available");
                            System.out.println("--------------------------------------------------------------------------------------------");

                            // Display products in a tabular format
                            for (ProductDTO product : products) {
                                System.out.printf("%-15s %-25s %-40s %-10s %-20s%n",
                                        product.getProductId(), product.getProductName(), product.getDescription(),
                                        product.getPrice(), product.getAvailable());
                            }

                            // Prompt user to add a product to the cart
                            System.out.println("\nEnter the number of the product you want to add to the cart (or enter 0 to go back):");
                            int selectedProductId = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character

                            if (selectedProductId != 0) {
                                // Check if the selected product ID is valid
                                ProductDTO selectedProduct = products.stream()
                                        .filter(product -> product.getProductId() == selectedProductId)
                                        .findFirst()
                                        .orElse(null);

                                if (selectedProduct != null) {
                                    // Ask the user if they want to add the product to the cart
                                    System.out.println("Do you want to add \"" + selectedProduct.getProductName() + "\" to the cart? (yes/no)");
                                    String addToCartChoice = scanner.nextLine().toLowerCase();

                                    if (addToCartChoice.equals("yes")) {
                                        // Add the selected product to the cart (implement this logic based on your requirements)
                                        // cart.addProduct(selectedProduct);
                                        // Add the selected product to the cart
                                        CartDTO cart = new CartDTO();
                                        cart.setProduct_id(selectedProduct.getProductId());
                                        cart.setProduct_quantity(1); // Assuming you add one quantity for simplicity
                                        cart.setUser_id(userId); // You need to have the user ID available

                                        // Get the current date and time
                                        LocalDateTime currentDateTime = LocalDateTime.now();
                                        cart.setOrder_date_time(currentDateTime);

                                        // Call a method to insert the cart information into the database
                                        boolean addToCartResult = pds.addToCart(cart);

                                        if (addToCartResult) {
                                            System.out.println("Product added to the cart!");
                                        } else {
                                            System.out.println("Failed to add the product to the cart. Please try again.");
                                        }


                                    } else {
                                        System.out.println("Product not added to the cart.");
                                    }
                                } else {
                                    System.out.println("Invalid product ID. Please try again.");
                                }
                            } else {
                                // User chose to go back
                                System.out.println("Going back to the category selection.");
                            }

                        } else {
                            System.out.println("No products available for the selected category.");
                        }

                    } else {
                        System.out.println("Login failed. Please check your username and password.");
                    }
                    break;


                case 2:
                    // Implement sign-up logic here

                    boolean usernameTaken;
                    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcoms_test", "root", "")) {
                        do {
                            System.out.println("Sign Up");
                            System.out.println("Enter your desired username:");
                            String username = scanner.nextLine();

                            UserInfoDTO signUpInfo = new UserInfoDTO();
                            signUpInfo.setUsername(username);

                            // Check if the username already exists
                            usernameTaken = pds.isUsernameTaken(signUpInfo);

                            if (usernameTaken) {
                                System.out.println("Username already exists. Please choose a different username.");
                            } else {
                                // Continue with other user details
                                System.out.println("Enter your first name:");
                                String firstName = scanner.nextLine();
                                System.out.println("Enter your last name:");
                                String lastName = scanner.nextLine();
                                System.out.println("Enter your email address:");
                                String email = scanner.nextLine();
                                System.out.println("Enter your contact number:");
                                String phone = scanner.nextLine();
                                System.out.println("Enter your address:");
                                String address = scanner.nextLine();
                                System.out.println("Enter your password:");
                                String password = scanner.nextLine();

                                signUpInfo.setFirstName(firstName);
                                signUpInfo.setLastName(lastName);
                                signUpInfo.setEmail(email);
                                signUpInfo.setPhone(phone);
                                signUpInfo.setAddress(address);
                                signUpInfo.setPassword(password);

                                // Call the remote method for account registration
                                boolean registrationStatus = pds.registerAccount(signUpInfo);

                                // Process the registration status
                                if (registrationStatus) {
                                    System.out.println("Account registered successfully!");
                                } else {
                                    System.out.println("Registration failed. Please try again.");
                                }
                            }
                        } while (usernameTaken);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error connecting to the database", e);
                    }


                    break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

}
