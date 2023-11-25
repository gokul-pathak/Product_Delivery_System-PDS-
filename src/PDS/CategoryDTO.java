package PDS;
import java.io.Serializable;
import java.rmi.RemoteException;
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
        return name;
    }

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
        // Implement your validation logic here
        return selectedCategory >= 1 && selectedCategory <= categories.size();
    }
}
