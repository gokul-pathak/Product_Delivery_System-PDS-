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


    public static void adminManageProducts(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("\nAdmin Products Management:");
            System.out.println("1. Add Products");
            System.out.println("2. Edit Products");
            System.out.println("3. View Prdocuts");
            System.out.println("4. Delete Products");
            System.out.println("5. Go back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
ProductDTO.addProduct(pds);

                    break;
                case 2:
ProductDTO.updateProduct(pds);

                    break;
                case 3:
ProductDTO.viewProducts(pds);

                    break;

                case 4:
ProductDTO.deleteProduct(pds);
                    break;
                case 5:
                    continueEditing = false;
                    System.out.println("Going back.");
                    Additonal.timer();
                    UserInfoDTO.adminHomePage( pds,userId);
                    break; //
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");

                    // Ask the user if they want to continue
                    System.out.print("Do you want to continue managing categories? (yes/no): ");
                    String continueChoice = scanner.nextLine().toLowerCase();
                    continueEditing = "yes".equals(continueChoice);
                    break;
            }
        }

    }

    public static void addProduct(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean productNameTaken;

            // User is prompted to enter product details
            System.out.println("Add Product");
            System.out.println("Enter the product name:");
            String productName = scanner.nextLine();

            System.out.println("Enter the product description:");
            String productDescription = scanner.nextLine();

            System.out.println("Enter the product price:");
            double productPrice = scanner.nextDouble();

            System.out.println("Enter the quantity available:");
            int quantityAvailable = scanner.nextInt();

            System.out.println("Enter the category ID:");
            int categoryId = scanner.nextInt();
            scanner.nextLine();

            ProductDTO productInfo = new ProductDTO();
            productInfo.setProductName(productName);
            productInfo.setDescription(productDescription);
            productInfo.setPrice(productPrice);
            productInfo.setAvailable(quantityAvailable);
            productInfo.setCategoryId(categoryId);
                boolean addProductStatus = pds.addProducts(productInfo);
                if (addProductStatus) {
                    System.out.println("Product added successfully!");
                } else {
                    System.out.println("Category id does not exist. Add the category first");
                }
   }

    private static void displayProductTable(ProductDeliverySystem pds) throws RemoteException {
        List<ProductDTO> products = pds.getAllProducts();
        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("Product Table:");
            System.out.printf("%-5s %-20s %-40s %-10s %-15s %-10s%n", "ID", "Product Name", "Description", "Price", "Quantity Available", "Category ID");
            for (ProductDTO product : products) {
                System.out.printf("%-5d %-20s %-40s %-10.2f %-15d %-10d%n",
                        product.getProductId(), product.getProductName(), product.getDescription(),
                        product.getPrice(), product.getAvailable(), product.getCategoryId());
            }
        }
    }

    private static void updateProduct(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        displayProductTable(pds);
        System.out.print("Enter the ID of the product you want to edit (or enter 0 to go back): ");
        int productIdToEdit = scanner.nextInt();
        scanner.nextLine();

        if (productIdToEdit == 0) {
            System.out.println("Going back to the main menu.");
            return;
        }
        ProductDTO productToEdit = pds.getProductInfoById(productIdToEdit);
        if (productToEdit == null) {
            System.out.println("Product with ID " + productIdToEdit + " not found.");
            return;
        }
        System.out.println("Current Details:");
        System.out.println("ID: " + productToEdit.getProductId());
        System.out.println("Product Name: " + productToEdit.getProductName());
        System.out.println("Description: " + productToEdit.getDescription());
        System.out.println("Price: " + productToEdit.getPrice());
        System.out.println("Quantity Available: " + productToEdit.getAvailable());
        System.out.println("Category ID: " + productToEdit.getCategoryId());
        System.out.println("Enter new details (press Enter to keep the current value):");
        System.out.print("New Product Name: ");
        String newProductName = scanner.nextLine().trim();

        System.out.print("New Description: ");
        String newDescription = scanner.nextLine().trim();

        System.out.print("New Price: ");
        double newPrice = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("New Quantity Available: ");
        int newQuantityAvailable = scanner.nextInt();
        scanner.nextLine();

        System.out.print("New Category ID: ");
        int newCategoryId = scanner.nextInt();
        scanner.nextLine();
        if (!newProductName.isEmpty() || !newDescription.isEmpty() || newPrice != 0.0 || newQuantityAvailable != 0 || newCategoryId != 0) {
            ProductDTO updatedProduct = new ProductDTO();
            updatedProduct.setProductId(productToEdit.getProductId());
            updatedProduct.setProductName(!newProductName.isEmpty() ? newProductName : productToEdit.getProductName());
            updatedProduct.setDescription(!newDescription.isEmpty() ? newDescription : productToEdit.getDescription());
            updatedProduct.setPrice(newPrice != 0.0 ? newPrice : productToEdit.getPrice());
            updatedProduct.setAvailable(newQuantityAvailable != 0 ? newQuantityAvailable : productToEdit.getAvailable());
            updatedProduct.setCategoryId(newCategoryId != 0 ? newCategoryId : productToEdit.getCategoryId());
            boolean updateResult = pds.updateProduct(updatedProduct);
            if (updateResult) {
                System.out.println("Product details updated successfully.");
            } else {
                System.out.println("Failed to update product details.");
            }
        } else {
            System.out.println("No changes made. Going back to the main menu.");
        }
    }


    public static void viewProducts(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdmin Product Management - View Products:");
            List<ProductDTO> products = pds.getAllProducts();
            System.out.printf("%-5s %-20s %-40s %-10s %-15s %-10s\n", "ID", "Product Name", "Description", "Price", "Quantity Available", "Category ID");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------");
            for (ProductDTO product : products) {
                System.out.printf("%-5d %-20s %-40s %-10.2f %-15d %-10d\n",
                        product.getProductId(), product.getProductName(), product.getDescription(),
                        product.getPrice(), product.getAvailable(), product.getCategoryId());
            }
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("1. Go Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {

                return;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void deleteProduct(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        displayProductTable(pds);
        System.out.println("Enter the ID of the product you want to delete (or enter 0 to go back): ");
        int productIdToDelete = scanner.nextInt();

        if (productIdToDelete == 0) {
            System.out.println("Going back to the main menu.");
            return;
        }
        System.out.println("Are you sure you want to delete this product? (yes/no): ");
        String confirmation = scanner.next().toLowerCase();

        if (confirmation.equals("yes")) {
            boolean deletionResult = pds.deleteProduct(productIdToDelete);

            if (deletionResult) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Failed to delete product.");
            }
        } else {
            System.out.println("Product deletion canceled.");
        }
    }


}
