package PDS;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CartDTO implements Serializable {
    private int cart_id;
    private int product_id;
    private int product_quantity;
    private int user_id;
    private LocalDateTime order_date_time;
    private String productId;
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
            boolean choiceValid = false;
            while (!choiceValid) {
                System.out.println("All Cart Products:");
                System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", "S.N", "Product Name", "Quantity", "Price", "Total Price");
                System.out.println("----------------------------------------------------------------------------------------------------------");

                // Display products in a tabular format
                int SN = 1;
                double count = 0;
                for (CartDTO cart : carts) {
                    System.out.printf("%-15s %-25s %-40s %-10s %-20s%n", SN, cart.getProductName(), cart.getQuantity(), cart.getPrice(), (cart.getQuantity() * cart.getPrice()));
                    SN++;
                    count = count + (cart.getQuantity() * cart.getPrice());
                }
                System.out.println("Total Amount :"+ count);
                System.out.println("\n\nChoose Option");
                System.out.println("1. Continue Order");
                System.out.println("2. Back");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                switch (choice) {
                    case 1:
                        while(true) {
                            System.out.println("\n\nChoose Option");
                            System.out.println("1. Cash On Delivery");
                            System.out.println("2. Back");
                            System.out.print("Enter your choice: ");
                            int Orderchoice = scanner.nextInt();
                            scanner.nextLine();
                            if (Orderchoice == 1) {
                                System.out.println("Processing your Order in Cash on Delivery!!");
                                System.out.println("\nDo you want to complete the order process? (yes/no)!!");
                                String CompleteOrderChoice = scanner.nextLine().toLowerCase();
                                if ("yes".equals(CompleteOrderChoice)) {
                                    OrderDTO order = new OrderDTO();
                                    order.setUser_id(userId);
                                    LocalDateTime currentDateTime = LocalDateTime.now();
                                    order.setOrder_date_time(currentDateTime);

                                    boolean addUserOrder = false;
                                    List<OrderDTO> orderDetailsList = new ArrayList<>();
                                    for (CartDTO cart : carts) {
                                        OrderDTO orderDetail = new OrderDTO();
                                        orderDetail.setProduct_id(Integer.parseInt(cart.getProductId()));
                                        orderDetail.setProduct_quantity(cart.getQuantity());
                                        orderDetailsList.add(orderDetail);
                                    }
                                    order.setOrderDetailsList(orderDetailsList);
                                    addUserOrder = pds.addUserOrder(order);
                                    if (addUserOrder) {
                                        System.out.println("Your Order has been Sent.");
                                        System.out.println("Redirectly to Home Page!");
                                        Additonal.timer();
                                        choiceValid = true;
                                        UserInfoDTO.userHomePage(pds, userId);
                                    }
                                    else {
                                        System.out.println("Error while Sendint the Order!! Please Try Again");
                                        Additonal.timer();
                                        break;
                                    }

                                    break;
                                }else {
                                    break;
                                }
                            } else if (Orderchoice == 2) {
                                break;
                            } else {
                                System.out.println("Invalid Choice! Try Again");
                            }
                        }
                        break;
                    case 2:
                        choiceValid = true;
                        UserInfoDTO.userHomePage(pds, userId);
                        break;
                    default:
                        System.out.println("Invalid Option! Try again");

                }
            }

        } else {
            boolean isTrue = false;
            while (!isTrue) {
                System.out.println("No Data in Cart!!!\n");
                System.out.println("1. Back");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    isTrue = true;
                    UserInfoDTO.userHomePage(pds, userId);
                } else {
                    System.out.println("Invalid Option!! Please Choose Valid Option\n");
                }
            }

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

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }
}
