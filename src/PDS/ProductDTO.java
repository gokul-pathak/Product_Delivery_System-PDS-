package PDS;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 354691329272604946L;
    private int productId;
    private String productName;
    private String description;
    private double price;
    private int available;
    private int categoryId;

    // Constructors

    public ProductDTO() {
        // Default constructor
    }

    public ProductDTO(int productId, String productName, String description, double price, int available, int categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.available = available;
        this.categoryId = categoryId;
    }

    // Setter methods

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    // Getter methods

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }



    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailable() {
        return available;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public static void selectAddCart(ProductDeliverySystem pds, int selectedCategory, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<ProductDTO> products = pds.getProductsByCategory(String.valueOf(selectedCategory));
        if (!products.isEmpty()) {
            System.out.println("Products for the selected category:");
            // Prompt user to add a product to the cart
            boolean isTrue = false;
            while (!isTrue) {

                // Display header
                System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", "Product ID", "Product Name", "Description", "Price", "Quantity Available");
                System.out.println("-----------------------------------------------------------------------------------------------------------------");

                // Display products in a tabular format
                for (ProductDTO product : products) {
                    System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice(), product.getAvailable());
                }
                System.out.println("\nEnter the number of the product you want to add to the cart (or enter 0 to go back):");
                int selectedProductId = scanner.nextInt();
                scanner.nextLine();

                if (selectedProductId != 0) {
                    // Check if the selected product ID is valid
                    ProductDTO selectedProduct = products.stream().filter(product -> product.getProductId() == selectedProductId).findFirst().orElse(null);

                    if (selectedProduct != null) {

                        // Ask the user if they want to add the product to the cart
                        System.out.println("Do you want to add \"" + selectedProduct.getProductName() + "\" to the cart? (yes/no)");
                        String addToCartChoice = scanner.nextLine().toLowerCase();


                        if (addToCartChoice.equals("yes")) {
                            int cartQty;
                            while (true) {
                                try {
                                    System.out.println("Enter the product quantity:");
                                    cartQty = Integer.parseInt(scanner.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                }
                            }
                            CartDTO cart = new CartDTO();
                            cart.setProduct_id(selectedProduct.getProductId());
                            cart.setProduct_quantity(cartQty);
                            cart.setUser_id(userId);
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            cart.setOrder_date_time(currentDateTime);

                            // Call a method to insert the cart information into the database
                            boolean addToCartResult = pds.addProductToCart(cart);

                            if (addToCartResult) {
                                System.out.println("\nProduct added to the cart!\n");
                                System.out.println("Do you want to add more Product? (yes/no)");
                                String addProductChoice = scanner.nextLine().toLowerCase();
                                if (addProductChoice.equals("yes")){
                                    System.out.println("Redirecting to Selected Category Product List in");
                                    Additonal.timer();
                                }else{
                                    System.out.println("Redirecting to the Product Delivery System!");
                                    Additonal.timer();
                                    isTrue = true;
                                    UserInfoDTO.userHomePage(pds, userId);
                                }

                            } else {
                                System.out.println("Failed to add the product to the cart. Please try again.\n\n");
                            }


                        } else {
                            System.out.println("Redirecting to Product List in ");
                            Additonal.timer();
                        }
                    } else {
                        System.out.println("Invalid product ID. Please try again in ");
                        Additonal.timer();
                    }
                } else {
                    // User chose to go back
                    System.out.println("Going back to the category selection in");
                    Additonal.timer();
                    isTrue = true;
                    CategoryDTO.selectCategory(pds, userId);
                }

            }
        } else {
            System.out.println("No products available for the selected category.");
        }
    }

    public static void selectAddCart(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<ProductDTO> products = pds.getAllProducts();
        if (!products.isEmpty()) {
            System.out.println("All Products:");
            // Prompt user to add a product to the cart
            boolean isTrue = false;
            while (!isTrue) {

                // Display header
                System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", "Product ID", "Product Name", "Description", "Price", "Quantity Available");
                System.out.println("-----------------------------------------------------------------------------------------------------------------");

                // Display products in a tabular format
                for (ProductDTO product : products) {
                    System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", product.getProductId(), product.getProductName(), product.getDescription(), product.getPrice(), product.getAvailable());
                }
                System.out.println("\nEnter the number of the product you want to add to the cart (or enter 0 to go back):");
                int selectedProductId = scanner.nextInt();
                scanner.nextLine();


                if (selectedProductId != 0) {
                    ProductDTO selectedProduct = products.stream().filter(product -> product.getProductId() == selectedProductId).findFirst().orElse(null);

                    if (selectedProduct != null) {
                        System.out.println("Do you want to add \"" + selectedProduct.getProductName() + "\" to the cart? (yes/no)");
                        String addToCartChoice = scanner.nextLine().toLowerCase();


                        if (addToCartChoice.equals("yes")) {
                            int cartQty;
                            while (true) {
                                try {
                                    System.out.println("Enter the product quantity:");
                                    cartQty = Integer.parseInt(scanner.nextLine());
                                    break;
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid input. Please enter a valid number.");
                                }
                            }
                            CartDTO cart = new CartDTO();
                            cart.setProduct_id(selectedProduct.getProductId());
                            cart.setProduct_quantity(cartQty);
                            cart.setUser_id(userId);
                            LocalDateTime currentDateTime = LocalDateTime.now();
                            cart.setOrder_date_time(currentDateTime);

                            // Call a method to insert the cart information into the database
                            boolean addToCartResult = pds.addProductToCart(cart);

                            if (addToCartResult) {
                                System.out.println("\nProduct added to the cart!\n");
                                System.out.println("Do you want to add more Product? (yes/no)");
                                String addProductChoice = scanner.nextLine().toLowerCase();
                                if (addProductChoice.equals("yes")){
                                    System.out.println("Redirecting to ALl Product List in");
                                    Additonal.timer();
                                }else{
                                    System.out.println("Redirecting to the Product Delivery System!");
                                    Additonal.timer();
                                    isTrue = true;
                                    UserInfoDTO.userHomePage(pds, userId);
                                    break;
                                }

                            } else {
                                System.out.println("Failed to add the product to the cart. Please try again.\n\n");
                            }


                        } else {
                            System.out.println("Redirecting to Product List in ");
                            Additonal.timer();
                        }
                    } else {
                        System.out.println("Invalid product ID. Please try again in ");
                        Additonal.timer();
                    }
                } else {
                    // User chose to go back
                    System.out.println("Going back to the Home Page in");
                    Additonal.timer();
                    isTrue = true;
                    UserInfoDTO.userHomePage(pds, userId);
                }

            }
        } else {
            System.out.println("No products available for the selected category.");
        }
    }
}
