package PDS;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class CartDTO implements Serializable {
    private int cart_id;
    private int product_id;
    private int product_quantity;
    private int user_id;
    private LocalDateTime order_date_time;
    private int productId;
    private String productName;
    private double price;
    private int quantity;

    // Constructors

    public CartDTO() {
        // Default constructor
    }

    public CartDTO(int cart_id, int product_id, int product_quantity, int user_id, LocalDateTime order_date_time) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.product_quantity = product_quantity;
        this.user_id = user_id;
        this.order_date_time = order_date_time;
    }

    // Getter and Setter methods

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(LocalDateTime order_date_time) {
        this.order_date_time = order_date_time;
    }

    public static void viewCartItems(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<CartDTO> carts = pds.getAllCartProductsbyUserId(userId);
        if (!carts.isEmpty()) {
            System.out.println("All Cart Products:");
            System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", "Product ID", "Product Name", "Quantity", "Price", "Total Price");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            // Display products in a tabular format
            int SN = 1;
            for (CartDTO cart : carts) {
                System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", SN, cart.getProductName(), cart.getQuantity(), cart.getPrice(), (cart.getQuantity() * cart.getPrice()));
                SN++;
            }
        }
        else{
            System.out.println("No Data in Cart!!!");
        }
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
