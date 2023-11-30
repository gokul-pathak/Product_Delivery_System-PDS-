package PDS;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 7064398314960866093L;

    private int categoryId;
    private String name;

    // Constructors

    public CategoryDTO() {
        // Default constructor
    }

    public CategoryDTO(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    // Setter methods

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter methods

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name; }

    public static void selectCategory(ProductDeliverySystem pds, int UserId) throws RemoteException {
        List<CategoryDTO> categories = pds.getCategories();
        System.out.println("Categories:");
        for (CategoryDTO category : categories) {
            System.out.println(category.getCategoryId() + ". " + category.getName());
        }

        int selectedCategory;
        while (true) {
            System.out.print("Enter the number of the category you're interested in: ");
            Scanner scanner = new Scanner(System.in);
            selectedCategory = scanner.nextInt();
            scanner.nextLine();
            if (isValidCategory(selectedCategory, categories)) {
                break;
            } else {
                System.out.println("Invalid category. Please try again.");
            }
        }
        ProductDTO.selectAddCart(pds, selectedCategory, UserId);
    }

    private static boolean isValidCategory(int selectedCategory, List<CategoryDTO> categories) {
        // validation logic
        return selectedCategory >= 1 && selectedCategory <= categories.size();
    }
    public static void adminManageCategory(ProductDeliverySystem pds,int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("\nAdmin Ctegory Management:");
            System.out.println("1. Add Categories");
            System.out.println("2. Edit Categories");
            System.out.println("3. View Categories");
            System.out.println("4. Delete Categories");
            System.out.println("5. Go back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    CategoryDTO.addCategory(pds);


                    break;
                case 2:
                    CategoryDTO.updateCategory(pds);

                    break;
                case 3:
                   CategoryDTO.viewCategories(pds);

                    break;

                case 4:
                    deleteCategoryClient(pds);
                    break;
                case 5:
                    continueEditing = false;
                    System.out.println("Going back.");
                    Additonal.timer();
                    UserInfoDTO.adminHomePage( pds,userId);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    System.out.print("Do you want to continue managing categories? (yes/no): ");
                    String continueChoice = scanner.nextLine().toLowerCase();
                    continueEditing = "yes".equals(continueChoice);
                    break;
            }
        }

    }
    public static void addCategory(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean categoryNameTaken;

        do {
            System.out.println("Add Category");
            System.out.println("Enter the category name:");
            String categoryName = scanner.nextLine();
            CategoryDTO categoryInfo = new CategoryDTO();
            categoryInfo.setName(categoryName);
            categoryNameTaken = pds.isCategoryNameTaken(categoryInfo);

            if (categoryNameTaken) {
                System.out.println("Category name already exists. Please choose a different category name.\n");
            } else { boolean addCategoryStatus = pds.addCategory(categoryInfo);
                if (addCategoryStatus) {
                    System.out.println("Category added successfully!");
                } else {
                    System.out.println("Adding category failed. Please try again.");
                }
            }
        } while (categoryNameTaken);
    }


    private static void displayCategoryTable(ProductDeliverySystem pds) throws RemoteException {
        List<CategoryDTO> categories = pds.getCategories();

        if (categories.isEmpty()) {
            System.out.println("No categories found.");
        } else {
            System.out.println("Category Table:");
            System.out.printf("%-5s %-20s%n", "ID", "Category Name");
            for (CategoryDTO category : categories) {
                System.out.printf("%-5d %-20s%n", category.getCategoryId(), category.getName());
            }
        }
    }

    private static void updateCategory(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
    displayCategoryTable(pds);
    System.out.print("Enter the ID of the category you want to edit (or enter 0 to go back): ");
        int categoryIdToEdit = scanner.nextInt();
        scanner.nextLine();

        if (categoryIdToEdit == 0) {
            System.out.println("Going back to the main menu.");
            return;
        }
        CategoryDTO categoryToEdit = pds.getCategoryInfoById(categoryIdToEdit);
        if (categoryToEdit == null) {
            System.out.println("Category with ID " + categoryIdToEdit + " not found.");
            return;
        }
        System.out.println("Current Details:");
        System.out.println("ID: " + categoryToEdit.getCategoryId());
        System.out.println("Category Name: " + categoryToEdit.getName());
        System.out.println("Enter new details (press Enter to keep the current value):");
        System.out.print("New Category Name: ");
        String newCategoryName = scanner.nextLine().trim();
        if (!newCategoryName.isEmpty()) {
            CategoryDTO updatedCategory = new CategoryDTO();
            updatedCategory.setCategoryId(categoryToEdit.getCategoryId());
            updatedCategory.setName(newCategoryName);
            boolean updateResult = pds.updateCategory(updatedCategory);

            if (updateResult) {
                System.out.println("Category details updated successfully.");
            } else {
                System.out.println("Failed to update category details.");
            }
        } else {
            System.out.println("No changes made. Going back to the main menu.");
        }
    }

    public static void viewCategories(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nAdmin Category Management - View Categories:");
            List<CategoryDTO> categories = pds.getCategories();
            System.out.printf("%-5s %-20s\n", "ID", "Category Name");
            System.out.println("-------------------------------");
            for (CategoryDTO category : categories) {
                System.out.printf("%-5s %-20s\n", category.getCategoryId(), category.getName());
            }
            System.out.println("-------------------------------");
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

    private static void deleteCategoryClient(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        displayCategoryTable(pds);
        System.out.println("Enter the ID of the user you want to delete (or enter 0 to go back): ");
        int categoryId = scanner.nextInt();
        if (categoryId == 0) {
            System.out.println("Going back to the main menu.");
            return;
        }
        System.out.println("Are you sure you want to delete this category? (yes/no): ");
        String confirmation = scanner.next().toLowerCase();
        if (confirmation.equals("yes")) {
            boolean deletionResult = pds.deleteCategoryServer(categoryId);
            if (deletionResult) {
                System.out.println("Category deleted successfully.");
            } else {
                System.out.println("Failed to delete category.");
                System.out.println("There are products in that category.");
                System.out.println("Make sure you delete them first.");
            }
        } else {
            System.out.println("Category deletion canceled.");
        }
    }




}
