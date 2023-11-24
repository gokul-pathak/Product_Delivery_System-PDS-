package PDS;
import java.io.Serializable;

public class CategoryDTO implements Serializable {
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
}