package PDS;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public interface ProductDeliverySystem extends Remote {

    boolean registerAccount(UserInfoDTO userInfo) throws RemoteException;
    boolean loginUser(UserInfoDTO userInfo) throws RemoteException;

    boolean isUsernameTaken(UserInfoDTO userInfo) throws RemoteException;

    //List<String> getCategories() throws RemoteException;
    //List<ProductDTO> getProductsByCategory(String category) throws RemoteException;

    List<CategoryDTO> getCategories() throws RemoteException;

    List<ProductDTO> getAllProducts() throws RemoteException;
    List<ProductDTO> getProductsByCategory(String category) throws RemoteException;

    List<CartDTO> getAllCartProductsbyUserId(int userId) throws RemoteException;

    boolean addProductToCart(CartDTO cart) throws RemoteException;
    int getUserIdByUsername(String username) throws RemoteException;

    int getUserRole(int userId) throws RemoteException;

    List<UserInfoDTO> getAllUsers() throws RemoteException;
    boolean deleteUser(int userId) throws RemoteException;

    UserInfoDTO getUserInfoById(int userId) throws RemoteException;

    boolean updateUser(UserInfoDTO updatedUser) throws RemoteException;

    boolean addUserOrder(OrderDTO order) throws RemoteException;

    boolean cancelOrder(int selectedOrderId) throws RemoteException;

    List<OrderDTO> getAllOrderbyUserId(int userId) throws RemoteException;

    List<OrderDTO> getAllOrdersbyStatus(String status) throws RemoteException;

    boolean updateOrderStatus(int selectedOrderId, String orderStatus) throws RemoteException;
}
