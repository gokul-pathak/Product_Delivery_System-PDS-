package PDS;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

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

                    // Call the remote method for user login
                    boolean loginStatus = pds.loginUser(loginUsername, loginPassword);

                    // Process the login status
                    if (loginStatus) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed. Please check your username and password.");
                    }
                    break;
                case 2:
                    // Implement sign up logic here
                    System.out.println("Sign Up");
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
                    System.out.println("Enter your desired username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter your password:");
                    String password = scanner.nextLine();

                    // Call the remote method for account registration
                    boolean registrationStatus = pds.registerAccount(firstName, lastName, email, phone, address, username, password);

                    // Process the registration status
                    if (registrationStatus) {
                        System.out.println("Account registered successfully!");
                    } else {
                        System.out.println("Registration failed. Please try again with a different username.");
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
