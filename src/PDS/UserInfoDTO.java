package PDS;
import java.io.Serializable;
import java.rmi.RemoteException;
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

    // Constructors

    public UserInfoDTO() {
        // Default constructor
    }

    public UserInfoDTO(String firstName, String lastName, String email, String phone, String address, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
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


            // Process the login
            if (loginStatus) {
                int userId = pds.getUserIdByUsername(loginUsername);
                if (userId != -1) {
                    System.out.println("User ID:" + userId);
                }

                System.out.println("Login successful!");
                System.out.println("Redirecting user page in ");
                Additonal.timer();
                userHomePage(pds, userId);

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
            System.out.println("\n\nWelcome to Product Delivery System");
            System.out.println("1. View Category");
            System.out.println("2. View All Product");
            System.out.println("3. View Cart");
            System.out.println("4. View/Track Order");
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
                    choiceValid = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }
}
