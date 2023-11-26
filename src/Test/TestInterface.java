package Test;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestInterface extends Remote {
    void displayMessage(String name) throws RemoteException;
}
