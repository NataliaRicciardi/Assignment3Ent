package com.cst8277.usermanagementservice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserManagementDAO {

	private Connection dbConnection;

	public UserManagementDAO() {
		String url = "jdbc:mysql://127.0.0.1:3306/ums";
		String username = "root";
		String password = "passw";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			dbConnection = DriverManager.getConnection(url, username, password);
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
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();


		return users;
	}

	public User getUserById(int userId) {
		User user = null;


		return user;
	}

	public boolean addUser(User user) {

		return false;
	}

}
