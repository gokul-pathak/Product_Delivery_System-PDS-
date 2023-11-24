package PDS;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public interface ProductDeliverySystem extends Remote {
    // Define remote methods here
    //boolean registerAccount(String firstName, String lastName, String email, String phone, String address, String username, String password) throws RemoteException;
    // Add more methods for order processing, etc.
    //boolean loginUser(String username, String password) throws RemoteException;

    //boolean isUsernameTaken(Connection connection, String username) throws RemoteException;


    // Define remote methods here
    boolean registerAccount(UserInfoDTO userInfo) throws RemoteException;
    // Add more methods for order processing, etc.
    boolean loginUser(UserInfoDTO userInfo) throws RemoteException;

    boolean isUsernameTaken(UserInfoDTO userInfo) throws RemoteException;

    //List<String> getCategories() throws RemoteException;
    //List<ProductDTO> getProductsByCategory(String category) throws RemoteException;

    List<CategoryDTO> getCategories() throws RemoteException;

    List<ProductDTO> getProductsByCategory(String category) throws RemoteException;



}
