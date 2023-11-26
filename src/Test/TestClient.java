package Test;

import java.rmi.Naming;

public class TestClient {
    public static void main(String[] args) {
        try {
            TestInterface server = (TestInterface) Naming.lookup("rmi://localhost/TestServer");

            // Replace "John" with the user's entered name
            String name = "John";
            server.displayMessage(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
