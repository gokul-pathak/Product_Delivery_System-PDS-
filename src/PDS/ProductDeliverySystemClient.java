package PDS;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
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
            Additonal.randomSpace();
            // Display the menu
            while (true) {
                System.out.println("Welcome to Product Delivery System");
                System.out.println("1. Admin");
                System.out.println("2. User");
                System.out.print("Enter your choice: ");
                int userChoice = scanner.nextInt();
                if (userChoice == 1) {
                    UserInfoDTO.userLogin(pds);
                    break;
                } else if (userChoice == 2) {
                    boolean choiceValid = false;
                    while (!choiceValid) {
                        System.out.println("\n\nProduct Delivery System");
                        System.out.println("1. Log In");
                        System.out.println("2. Sign Up");
                        System.out.println("3. Exit");
                        System.out.print("Enter your choice: ");
                        int choice = scanner.nextInt();
                        scanner.nextLine();
                        switch (choice) {
                            case 1:
                                UserInfoDTO.userLogin(pds);
                                choiceValid =  true;
                                break;
                            case 2:
                                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcoms_test", "root", "")) {
                                    UserInfoDTO.userRegister(pds);
                                    choiceValid =  true;
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
                    }
                } else {
                    System.out.println("Incorrect Choice. Please try again!!\n ");
                }
            }


        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }


}
