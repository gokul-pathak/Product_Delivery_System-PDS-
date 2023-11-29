package PDS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Java Program to to reproduce
 * java.sql.SQLException: No suitable driver found
 * for jdbc:mysql://localhost:3306
 * error which occurs if MySQL JDBC Driver JAR is missing
 * or you not registering the JDBC driver before calling
 * DriverManager.getConnection() method in JDBC.
 */

public class DatabaseConnection {

    public static void main(String[] args) throws ClassNotFoundException {

        Connection con = null;

        try {
            String url = "jdbc:mysql://localhost:3306/productdeliverydb";
            String username = "root";
            String password = "";

             Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);

            if (con != null) {
                System.out
                        .println("Successfully connected to MySQL database test");
            }

        } catch (SQLException ex) {
            System.out
                    .println("An error occurred while connecting MySQL databse");
                            ex.printStackTrace();
        }

    }

}