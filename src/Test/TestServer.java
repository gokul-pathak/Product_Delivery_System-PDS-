package Test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class TestServer extends UnicastRemoteObject implements TestInterface {

    protected TestServer() throws RemoteException {
        super();
    }

    @Override
    public void displayMessage(String name) throws RemoteException {
        System.out.println("Message received from client: " + name);
    }

    public static void main(String[] args) {
        try {
            TestServer server = new TestServer();
            java.rmi.Naming.rebind("TestServer", server);
            System.out.println("Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
