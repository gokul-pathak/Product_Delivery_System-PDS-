package PDS;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProductDeliverySystem extends Remote {
    // Define remote methods here
    boolean registerAccount(String firstName, String lastName, String email, String phone, String address, String username, String password) throws RemoteException;
    // Add more methods for order processing, etc.
    boolean loginUser(String username, String password) throws RemoteException;
}
