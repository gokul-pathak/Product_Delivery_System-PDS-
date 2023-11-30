package PDS;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class UserInfoDTO implements Serializable {

    private static final long serialVersionUID = 5974191834878891739L;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String username;
    private String password;
    private int id;
    private int role;
    private String icNumber;

    public String getICNumber() {
        return icNumber;
    }

    public void setICNumber(String icNumber) {
        this.icNumber = icNumber;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }



    // Constructors

    public UserInfoDTO() {
        // Default constructor
    }

    public UserInfoDTO(String firstName, String lastName,String ICNumber, String email, String phone, String address, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.icNumber =ICNumber;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    // Setter methods

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter methods

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static void userRegister(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        boolean usernameTaken;
        do {
            System.out.println("Sign Up");
            System.out.println("Enter your desired username:");
            String username = scanner.nextLine();

            UserInfoDTO signUpInfo = new UserInfoDTO();
            signUpInfo.setUsername(username);

            // Check if the username already exists
            usernameTaken = pds.isUsernameTaken(signUpInfo);

            if (usernameTaken) {
                System.out.println("Username already exists. Please choose a different username.\n");
            } else {
                // Continue with other user details
                System.out.println("Enter your first name:");
                String firstName = scanner.nextLine();
                System.out.println("Enter your last name:");
                String lastName = scanner.nextLine();
                System.out.println("Enter your IC_Number (Passport):");
                String ICNumber = scanner.nextLine();
                System.out.println("Enter your email address:");
                String email = scanner.nextLine();
                System.out.println("Enter your contact number:");
                String phone = scanner.nextLine();
                System.out.println("Enter your address:");
                String address = scanner.nextLine();
                System.out.println("Enter your password:");
                String password = scanner.nextLine();

                signUpInfo.setFirstName(firstName);
                signUpInfo.setLastName(lastName);
                signUpInfo.setICNumber(ICNumber);
                signUpInfo.setEmail(email);
                signUpInfo.setPhone(phone);
                signUpInfo.setAddress(address);
                signUpInfo.setPassword(password);

                // Call the remote method for account registration
                boolean registrationStatus = pds.registerAccount(signUpInfo);

                // Process the registration status
                if (registrationStatus) {
                    System.out.println("Account registered successfully!");
                    System.out.println("Redirecting to Login Page!");
                    Additonal.timer();
                    userLogin(pds);
                } else {
                    System.out.println("Registration failed. Please try again.");
                }
            }
        } while (usernameTaken);
    }

    public static void userLogin(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        int maxLoginAttempts = 3;
        int loginAttempts = 0;

        while (loginAttempts < maxLoginAttempts) {
            // Login logic
            Additonal.clearConsole();
            System.out.println("Log In");
            System.out.println("Enter your username:");
            String loginUsername = scanner.nextLine();
            System.out.println("Enter your password:");
            String loginPassword = scanner.nextLine();

            UserInfoDTO loginInfo = new UserInfoDTO();
            loginInfo.setUsername(loginUsername);
            loginInfo.setPassword(loginPassword);

            boolean loginStatus = pds.loginUser(loginInfo);
            System.out.println("Login Status: " + loginStatus);



            // Process the login
            if (loginStatus) {

                int userId = pds.getUserIdByUsername(loginUsername);
                if (userId != -1) {
                    System.out.println("User ID:" + userId);
                    int role = pds.getUserRole(userId);
                    if (role == 0) {
                        System.out.println("Admin login successful!");
                        // Add admin-specific functionality here
                        System.out.println("Redirecting user page in ");
                        //Additonal.timer();
                        adminHomePage(pds,userId);

                    } else if (role == 1) {
                        System.out.println("Normal user login successful!");
                        // Redirect to normal user home page
                        System.out.println("Redirecting user page in ");
                        Additonal.timer();
                        userHomePage(pds, userId);
                    } else {
                        System.out.println("Unknown user role. Exiting program.");
                        break;
                    }

                }


            } else {
                System.out.println("Login failed. Please check your username and password.");
                loginAttempts++;

                if (loginAttempts < maxLoginAttempts) {
                    System.out.println("Remaining attempts: " + (maxLoginAttempts - loginAttempts));
                    System.out.println("Do you want to try again? (yes/no)");
                    String tryAgainChoice = scanner.nextLine().toLowerCase();

                    if (!"yes".equals(tryAgainChoice)) {
                        System.out.println("Exiting program.");
                        break;
                    }
                } else {
                    System.out.println("Maximum login attempts reached. Exiting program.");
                    break;
                }
            }
        }
    }

    public static void userHomePage(ProductDeliverySystem pds, int userId) throws RemoteException{
        Scanner scanner = new Scanner(System.in);
        boolean choiceValid = false;
        while (!choiceValid) {
            System.out.println("User HomePage\n\nWelcome to Product Delivery System");
            System.out.println("1. View Category");
            System.out.println("2. View All Product");
            System.out.println("3. View Cart");
            System.out.println("4. View/Track Order");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    Additonal.randomSpace();
                    CategoryDTO.selectCategory(pds, userId);
                    choiceValid = true;
                    break;
                case 2:
                    Additonal.randomSpace();
                    ProductDTO.selectAddCart(pds, userId);
                    choiceValid = true;
                    break;
                case 3:
                    Additonal.randomSpace();
                    CartDTO.viewCartItems(pds, userId);
                    choiceValid = true;
                    break;
                case 4:
                    Additonal.randomSpace();
                    OrderDTO.viewOrderByUserId(pds, userId);
                    choiceValid = true;
                    break;
                case 5:
                    System.out.println("Exiting user panel.");
                    choiceValid = true;
                    ProductDeliverySystemClient.startSystem();

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");

            }
        }
    }
    public static void adminHomePage(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        int role = pds.getUserRole(userId);
        System.out.println("Admin HomePage\n\nWelcome Admin!");

        boolean isChoice = false;
        while (!isChoice) {
            System.out.println("1. Manage User");
            System.out.println("2. Manage Category");
            System.out.println("3. Manage Product");
            System.out.println("4. Manage Order");
            System.out.println("5. Logout");

            // Get user input
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminManageUsers(pds, userId);
                    isChoice= true;
                    break;
                case 2:
                    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcoms_test", "root", "")) {
                        CategoryDTO.adminManageCategory(pds, userId);

                    } catch (SQLException e) {
                        throw new RuntimeException("Error connecting to the database", e);
                    }

                    break;
                case 3:
                    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dcoms_test", "root", "")) {
                       ProductDTO.adminManageProducts(pds,userId);

                    } catch (SQLException e) {
                        throw new RuntimeException("Error connecting to the database", e);
                    }

                    break;
                case 4:
                    Additonal.randomSpace();
                    OrderDTO.viewAllOrderpds(pds, userId);
                    isChoice = true;
                    break;
                case 5:
                    // Exit
                    System.out.println("Exiting admin panel.");
                    isChoice = true;
                    Additonal.timer();
                    ProductDeliverySystemClient.startSystem();

                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
                    break;
            }
        }
    }

    public static void adminManageUsers(ProductDeliverySystem pds, int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("\nAdmin User Management:");
            System.out.println("1. View Users");
            System.out.println("2. Delete User");
            System.out.println("3. Update User");
            System.out.println("4. Go Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    // View Users
                    viewUsers(pds, userId);
                    continueEditing = false;
                    break;
                case 2:
                    // Delete User
                    deleteUser(pds);
                    continueEditing = false;
                    break;
                case 3:
                    // Update User
                    updateUser(pds);
                    continueEditing = false;
                    break;
                case 4:
                    continueEditing = false;
                    System.out.println("Going back.");
                    //Additonal.timer();
                    adminHomePage(pds, userId);
                    break; //
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");

                    // Ask the user if they want to continue
                    System.out.print("Do you want to continue editing users? (yes/no): ");
                    String continueChoice = scanner.nextLine().toLowerCase();
                    continueEditing = "yes".equals(continueChoice);
                    break;
            }
        }

    }

    public static void viewUsers(ProductDeliverySystem pds , int userId) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nAdmin User Management - View Users:");

            // Retrieve users from the database
            //List<UserInfoDTO> users = pds.getAllUsers();

            // Display users in tabular format
            displayUserTable(pds);

            System.out.println("-----------------------------------------------------------------------------------------------------------------");

            // Menu options
            System.out.println("1. Go Back");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            if (choice == 1) {
                // Go Back
                adminManageUsers(pds, userId);
                  // Exit the method and go back
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void deleteUser(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        // Display the user table to help the user choose a user to delete
        displayUserTable(pds);

        // Ask the user to enter the user ID to delete
        System.out.println("Enter the ID of the user you want to delete (or enter 0 to go back): ");
        int userIdToDelete = scanner.nextInt();

        if (userIdToDelete == 0) {
            System.out.println("Going back to the main menu.");
            return; // Exit the method and go back to the main menu
        }

        // Confirm the deletion with the user
        System.out.println("Are you sure you want to delete this user? (yes/no): ");
        String confirmation = scanner.next().toLowerCase();

        if (confirmation.equals("yes")) {
            // Call the delete user method in the server
            boolean deletionResult = pds.deleteUser(userIdToDelete);

            if (deletionResult) {
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("Failed to delete user.");
            }
        } else {
            System.out.println("User deletion canceled.");
        }
    }


    private static void displayUserTable(ProductDeliverySystem pds) throws RemoteException {
        List<UserInfoDTO> users = pds.getAllUsers();

        if (users.isEmpty()) {
            System.out.println("No users found.");
        } else {
            System.out.println("User Table:");
            System.out.printf("%-5s %-15s %-15s %-15s %-30s %-15s %-15s %-15s\n",
                    "ID", "First Name", "Last Name", "IC_Number", "Email", "Phone", "Address", "Username");
            System.out.println("-----------------------------------------------------------------------------------------------------------------");
            for (UserInfoDTO user : users) {
                System.out.printf("%-5d %-15s %-15s %-15s %-30s %-15s %-15s %-15s\n",
                        user.getId(), user.getFirstName(), user.getLastName(), user.getICNumber(),
                        user.getEmail(), user.getPhone(), user.getAddress(), user.getUsername());
            }

        }
    }



    private static void updateUser(ProductDeliverySystem pds) throws RemoteException {
        Scanner scanner = new Scanner(System.in);

        // Display the user table
        displayUserTable(pds);

        // Prompt the user to enter the ID of the user they want to edit
        System.out.print("Enter the ID of the user you want to edit (or enter 0 to go back): ");
        int userIdToEdit = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (userIdToEdit == 0) {
            System.out.println("Going back to the main menu.");
            return;
        }

        // Check if the user with the entered ID exists
        UserInfoDTO userToEdit = pds.getUserInfoById(userIdToEdit);
        if (userToEdit == null) {
            System.out.println("User with ID " + userIdToEdit + " not found.");
            return;
        }

        // Display the current details of the selected user
        System.out.println("Current Details:");
        System.out.println("ID: " + userToEdit.getId());
        System.out.println("First Name: " + userToEdit.getFirstName());
        System.out.println("Last Name: " + userToEdit.getLastName());
        System.out.println("IC_Number: " + userToEdit.getICNumber());
        System.out.println("Email: " + userToEdit.getEmail());
        System.out.println("Phone: " + userToEdit.getPhone());
        System.out.println("Username: " + userToEdit.getUsername());
        System.out.println("Role: " + userToEdit.getRole());

        // Prompt the user for the details to edit
        System.out.println("Enter new details (press Enter to keep the current value):");
        System.out.print("New First Name: ");
        String newFirstName = scanner.nextLine().trim();

        System.out.print("New Last Name: ");
        String newLastName = scanner.nextLine().trim();

        System.out.print("New IC_Number: ");
        String newICNumber = scanner.nextLine().trim();

        System.out.print("New Email: ");
        String newEmail = scanner.nextLine().trim();

        System.out.print("New Phone: ");
        String newPhone = scanner.nextLine().trim();

        System.out.print("New Username: ");
        String newUsername = scanner.nextLine().trim();

        // Update the user details if the user entered new values
        if (!newFirstName.isEmpty() || !newLastName.isEmpty() || !newICNumber.isEmpty() || !newEmail.isEmpty() ||
                !newPhone.isEmpty() || !newUsername.isEmpty()) {
            UserInfoDTO updatedUser = new UserInfoDTO();
            updatedUser.setId(userToEdit.getId());
            updatedUser.setFirstName(newFirstName.isEmpty() ? userToEdit.getFirstName() : newFirstName);
            updatedUser.setLastName(newLastName.isEmpty() ? userToEdit.getLastName() : newLastName);
            updatedUser.setICNumber(newICNumber.isEmpty() ? userToEdit.getICNumber(): newICNumber);
            updatedUser.setEmail(newEmail.isEmpty() ? userToEdit.getEmail() : newEmail);
            updatedUser.setPhone(newPhone.isEmpty() ? userToEdit.getPhone() : newPhone);
            updatedUser.setUsername(newUsername.isEmpty() ? userToEdit.getUsername() : newUsername);
            updatedUser.setRole(userToEdit.getRole()); // Keep the role unchanged

            // Call the server method to update the user details
            boolean updateResult = pds.updateUser(updatedUser);

            if (updateResult) {
                System.out.println("User details updated successfully.");
            } else {
                System.out.println("Failed to update user details.");
            }
        } else {
            System.out.println("No changes made. Going back to the main menu.");
        }
    }




}
