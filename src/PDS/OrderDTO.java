package PDS;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OrderDTO implements Serializable {
    private List<OrderDTO> orderDetailsList = new ArrayList<>();
    private int orderId;

    private int productId;
    private int productQuantity;
    private int userId;
    private LocalDateTime orderDateTime;
    private int product_id;
    private int product_quantity;
    private int user_id;
    private Object order_date_time;
    private int orderDetailId;
    private String datetime;
    private String orderStatus;
    private String dateTime;
    private Object productName;
    private Object price;
    private String username;

    public static void viewAllOrderpds(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean validChoice = false;
        while (!validChoice) {
            System.out.println("Choose Option:");
            System.out.println("1. Pending Order");
            System.out.println("2. Processing Order");
            System.out.println("3. Delivered Order");
            System.out.println("4. Cancelled Order");
            System.out.println("5. Back");
            System.out.print("Enter your choice: ");
            int adminChoice = scanner.nextInt();
            scanner.nextLine();
            switch (adminChoice) {
                case 1:
                    String status = "Pending";
                    validChoice = true;
                    viewOrderbyStatus(pds, userId, status);
                    break;
                case 2:
                    String status1 = "Processing";
                    validChoice = true;
                    viewOrderbyStatus(pds, userId, status1);
                    break;
                case 3:
                    String status2 = "Delivered";
                    validChoice = true;
                    viewOrderbyStatus(pds, userId, status2);
                    break;
                case 4:
                    String status3 = "Cancelled";
                    validChoice = true;
                    viewOrderbyStatus(pds, userId, status3);
                    break;
                case 5:
                    validChoice = true;
                    UserInfoDTO.adminHomePage(pds, userId);
                    break;
                default:
                    System.out.println("Invalid Option!! Please Try Again");
            }
        }


    }

    public static void viewOrderbyStatus(ProductDeliverySystem pds, int userId, String status) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<OrderDTO> orders = pds.getAllOrdersbyStatus(status);
        if (!orders.isEmpty()) {
            System.out.println("All Orders:");
            System.out.printf("%-15s %-25s %-20s %-10s%n", "Bill No.", "Orders Date and Time", "Orders", "UserName");
            System.out.println("-------------------------------------------------------------------------");
            int num = 1;
            for (OrderDTO order : orders) {
                System.out.printf("%-15s %-25s %-20s %-10s%n", order.getId(), order.getDateTime(), "Order"+num, order.getUsername() );
                num++;
            }
            System.out.println("\nEnter the Bill number of the Order if you want to view order details (or enter 0 to go back):");
            int selectedOrderId = scanner.nextInt();
            scanner.nextLine();
            if (selectedOrderId != 0){
                OrderDTO selectedOrder = orders.stream().filter(order -> order.getOrderId() == selectedOrderId).findFirst().orElse(null);
                if (selectedOrder != null){
                    boolean isTrue = false;
                    while(!isTrue) {
                        System.out.println("Order Details:");
                        System.out.printf("%-15s %-25s %-20s %-10s %-15s%n", "S.N", "Product Name", "Qty", "Price", "Total Price");
                        System.out.println("--------------------------------------------------------------------------------------");
                        int SN = 1;
                        double count = 0;
                        for (OrderDTO orderDetail : selectedOrder.getOrderDetailsList()) {
                            System.out.printf("%-15s %-25s %-20s %-10s %-15s%n", SN, orderDetail.getProductName(), orderDetail.getProductQuantity(), orderDetail.getPrice(), (orderDetail.getProductQuantity() * orderDetail.getPrice()));
                            SN++;
                            count = count + (orderDetail.getProductQuantity() * orderDetail.getPrice());
                        }
                        System.out.println("Total Amount :" + count);
                        if ("Pending".equalsIgnoreCase(selectedOrder.getOrderStatus())) {
                            System.out.println("\nChoose Option");
                            System.out.println("1. Process Order");
                            System.out.println("2. Cancel Order");
                            System.out.println("3. Back");
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            if (choice == 1){
                                String orderStatus = "Processing";
                                System.out.println("\nDo you want to process this order?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.print("Enter your choice: ");
                                int cancelOption = scanner.nextInt();
                                scanner.nextLine();

                                if (cancelOption == 1) {
                                    boolean isOrderCanceled = pds.updateOrderStatus(selectedOrderId, orderStatus);
                                    if (isOrderCanceled) {
                                        isTrue = true;
                                        System.out.println("Order Process successfully!");
                                        Additonal.timer();
                                        UserInfoDTO.adminHomePage(pds, userId);
                                    } else {
                                        System.out.println("Failed to Process the order. Please try again later.");
                                        Additonal.timer();
                                    }
                                } else {
                                    System.out.println("Order not Processed.");
                                    Additonal.timer();
                                }
                            }
                            else if (choice == 2){
                                System.out.println("\nDo you want to cancel this order?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.print("Enter your choice: ");
                                int cancelOption = scanner.nextInt();
                                scanner.nextLine();

                                if (cancelOption == 1) {
                                    boolean isOrderCanceled = pds.cancelOrder(selectedOrderId);
                                    if (isOrderCanceled) {
                                        isTrue = true;
                                        System.out.println("Order canceled successfully!");
                                        Additonal.timer();
                                        UserInfoDTO.adminHomePage(pds, userId);
                                    } else {
                                        System.out.println("Failed to cancel the order. Please try again later.");
                                        Additonal.timer();
                                    }
                                } else {
                                    System.out.println("Order not canceled.");
                                    Additonal.timer();
                                }
                            }
                            else if(choice == 3){
                                isTrue = true;
                                viewOrderbyStatus(pds, userId, status);
                            }
                            else {
                                System.out.println("Invalid Choice!! Try Again");
                            }

                        }
                        else if ("Processing".equalsIgnoreCase(selectedOrder.getOrderStatus())) {
                            System.out.println("\nChoose Option");
                            System.out.println("1. Deliver Order");
                            System.out.println("2. Cancel Order");
                            System.out.println("3. Back");
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            if (choice == 1){
                                String orderStatus = "Delivered";
                                System.out.println("\nDo you want to deliver this order?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.print("Enter your choice: ");
                                int cancelOption = scanner.nextInt();
                                scanner.nextLine();

                                if (cancelOption == 1) {
                                    boolean isOrderCanceled = pds.updateOrderStatus(selectedOrderId, orderStatus);
                                    if (isOrderCanceled) {
                                        isTrue = true;
                                        System.out.println("Order deliver successfully!");
                                        Additonal.timer();
                                        UserInfoDTO.adminHomePage(pds, userId);
                                    } else {
                                        System.out.println("Failed to deliver the order. Please try again later.");
                                        Additonal.timer();
                                    }
                                } else {
                                    System.out.println("Order not Delivered.");
                                    Additonal.timer();
                                }
                            }
                            else if (choice == 2){
                                System.out.println("\nDo you want to cancel this order?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.print("Enter your choice: ");
                                int cancelOption = scanner.nextInt();
                                scanner.nextLine();

                                if (cancelOption == 1) {
                                    boolean isOrderCanceled = pds.cancelOrder(selectedOrderId);
                                    if (isOrderCanceled) {
                                        isTrue = true;
                                        System.out.println("Order canceled successfully!");
                                        Additonal.timer();
                                        UserInfoDTO.adminHomePage(pds, userId);
                                    } else {
                                        System.out.println("Failed to cancel the order. Please try again later.");
                                        Additonal.timer();
                                    }
                                } else {
                                    System.out.println("Order not canceled.");
                                    Additonal.timer();
                                }
                            }
                            else if(choice == 3){
                                isTrue = true;
                                viewOrderbyStatus(pds, userId, status);
                            }
                            else {
                                System.out.println("Invalid Choice!! Try Again");
                            }

                        }
                        else {
                            System.out.println("Since the order status is " + selectedOrder.getOrderStatus() + ", it can't be canceled.");
                            System.out.println("1. Back");
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            if(choice == 1){
                                isTrue = true;
                                viewOrderbyStatus(pds, userId, status);
                            }
                            else {
                                System.out.println("Invalid Choice!! Try Again");
                            }
                        }
                    }
                }
            }
            else {
                viewAllOrderpds(pds, userId);
            }
        }else {
            boolean isTrue = false;
            while (!isTrue) {
                System.out.println("No order yet\n");
                System.out.println("1. Back");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1) {
                    isTrue = true;
                    UserInfoDTO.adminHomePage(pds, userId);
                } else {
                    System.out.println("Invalid Option!! Please Choose Valid Option\n");
                }
            }
        }
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int productId) {
        this.product_id = productId;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(int productQuantity) {
        this.product_quantity = productQuantity;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int userId) {
        this.user_id = userId;
    }

    public Object getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(Object orderDateTime) {
        this.order_date_time = orderDateTime;
    }

    public static void viewOrderByUserId(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        List<OrderDTO> orders = pds.getAllOrderbyUserId(userId);
        if (!orders.isEmpty()) {
            System.out.println("All Orders:");
            System.out.printf("%-15s %-25s %-20s %-10s%n", "Bill No.", "Orders Date and Time", "Orders", "Status");
            System.out.println("-------------------------------------------------------------------------");
            int num = 1;
            for (OrderDTO order : orders) {
                System.out.printf("%-15s %-25s %-20s %-10s%n", order.getId(), order.getDateTime(), "Order"+num, order.getOrderStatus() );
                num++;
            }
            System.out.println("\nEnter the Bill number of the Order if you want to view order details (or enter 0 to go back):");
            int selectedOrderId = scanner.nextInt();
            scanner.nextLine();
            if (selectedOrderId != 0){
                OrderDTO selectedOrder = orders.stream().filter(order -> order.getOrderId() == selectedOrderId).findFirst().orElse(null);
                if (selectedOrder != null){
                    boolean isTrue = false;
                    while(!isTrue) {
                        System.out.println("Order Details:");
                        System.out.printf("%-15s %-25s %-20s %-10s %-15s%n", "S.N", "Product Name", "Qty", "Price", "Total Price");
                        System.out.println("--------------------------------------------------------------------------------------");
                        int SN = 1;
                        double count = 0;
                        for (OrderDTO orderDetail : selectedOrder.getOrderDetailsList()) {
                            System.out.printf("%-15s %-25s %-20s %-10s %-15s%n", SN, orderDetail.getProductName(), orderDetail.getProductQuantity(), orderDetail.getPrice(), (orderDetail.getProductQuantity() * orderDetail.getPrice()));
                            SN++;
                            count = count + (orderDetail.getProductQuantity() * orderDetail.getPrice());
                        }
                        System.out.println("Total Amount :" + count);
                        if ("Pending".equalsIgnoreCase(selectedOrder.getOrderStatus())) {
                            System.out.println("\nChoose Option");
                            System.out.println("1. Cancel Order");
                            System.out.println("2. Back");
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            if (choice == 1){
                                System.out.println("\nDo you want to cancel this order?");
                                System.out.println("1. Yes");
                                System.out.println("2. No");
                                System.out.print("Enter your choice: ");
                                int cancelOption = scanner.nextInt();
                                scanner.nextLine();

                                if (cancelOption == 1) {
                                    boolean isOrderCanceled = pds.cancelOrder(selectedOrderId);
                                    if (isOrderCanceled) {
                                        isTrue = true;
                                        System.out.println("Order canceled successfully!");
                                        Additonal.timer();
                                        UserInfoDTO.userHomePage(pds, userId);
                                    } else {
                                        System.out.println("Failed to cancel the order. Please try again later.");
                                        Additonal.timer();
                                    }
                                } else {
                                    System.out.println("Order not canceled.");
                                    Additonal.timer();
                                }
                            }
                            else if(choice == 2){
                                isTrue = true;
                                viewOrderByUserId(pds, userId);
                            }
                            else {
                                System.out.println("Invalid Choice!! Try Again");
                            }

                        } else {
                            System.out.println("Since the order status is " + selectedOrder.getOrderStatus() + ", it can't be canceled.");
                            System.out.println("1. Back");
                            System.out.print("Enter your choice: ");
                            int choice = scanner.nextInt();
                            scanner.nextLine();
                            if(choice == 1){
                                isTrue = true;
                                viewOrderByUserId(pds, userId);
                            }
                            else {
                                System.out.println("Invalid Choice!! Try Again");
                            }
                        }
                    }
                }
            }
            else {
                UserInfoDTO.userHomePage(pds, userId);
            }
        }else {
            boolean isTrue = false;
            while (!isTrue) {
                System.out.println("You haven't order anything yet\n");
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

    public List<OrderDTO> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderDTO> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public void setId(int id) {
        this.orderId = id;
    }

    public int getId() {
        return orderId;
    }


    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime() {
        return dateTime;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Object getProductName() {
        return productName;
    }

    public void setProductName(Object productName) {
        this.productName = productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public double getPrice() {
        return (double) price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
