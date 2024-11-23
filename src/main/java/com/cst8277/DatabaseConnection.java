package com.cst8277;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    String url = "jdbc:mysql://127.0.0.1:3306/Assign3Enterprise";
    private final String username;
    private final String password;

    public DatabaseConnection(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
        }
        catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found.");
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.err.println("Failed to connect to the database.");
            e.printStackTrace();
        }

        return connection;
    }

}
