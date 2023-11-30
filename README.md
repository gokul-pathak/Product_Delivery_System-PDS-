# Product_Delivery_System-PDS-

# Product Delivery System (PDS)

## Overview

Product Delivery System is a simple RMI-based application that facilitates product delivery and user management.

## How to Start the Project

1. **Compile the Java Files:**
   - Open a terminal in the project directory.
   - Run the following command to compile all Java files:
     ```bash
     javac *.java
     ```

2. **Start RMI Registry:**
   - To start the RMI registry, use the following command (replace `<port_number>` with the desired port, e.g., 1099):
     ```bash
     start rmiregistry <port_number>
     ```
     If you encounter a connection issue, start the RMI registry separately before starting the server.

3. **Start the Server:**
   - Run the following command to start the server:
     ```bash
     java PDS.ProductDeliverySystemServer
     ```
   - If the server starts successfully, you should see the message "Server ready."

4. **Start the Client:**
   - Open a new terminal.
   - Run the following command to start the client:
     ```bash
     java PDS.PDS_client
     ```

5. **Access Denied Custom Error:**
   - If you encounter an "Access Denied" custom error, ensure that the RMI registry is running before starting the server. Use the command mentioned in step 2.

## Notes
- Make sure to adjust the port numbers as needed.
- You may customize the code and configurations based on your requirements.
- If there are any issues, check the console output for error messages.

Feel free to enhance or modify this README file according to the specific details of your project.
