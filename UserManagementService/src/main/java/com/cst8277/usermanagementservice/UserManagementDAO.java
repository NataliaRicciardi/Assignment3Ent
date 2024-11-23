package com.cst8277.usermanagementservice;

import com.cst8277.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserManagementDAO {

	private final DatabaseConnection dbConnection;

	public UserManagementDAO() {
		this.dbConnection = new DatabaseConnection("user_management", "admin");
	}

	public List<User> getAllUsers() {
		String query = "SELECT * FROM user_roles";
		List<User> users = new ArrayList<>();

		try (Connection connection = dbConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query);
			 ResultSet resultSet = preparedStatement.executeQuery()) {

			while (resultSet.next()) {
				User user = new User();
				user.setUserId(resultSet.getInt("user_id"));
				user.setUsername(resultSet.getString("username"));
				user.setRole(resultSet.getString("role"));
				users.add(user);
			}

		} catch (SQLException e) {
			System.err.println("Error fetching all users: " + e.getMessage());
		}

		return users;
	}

	public User getUserById(int userId) {
		String query = "SELECT * FROM user_credentials WHERE user_id = ?";
		User user = null;

		try (Connection connection = dbConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setInt(1, userId);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					user = new User();
					user.setUserId(resultSet.getInt("user_id"));
					user.setUsername(resultSet.getString("username"));
					user.setPassword(resultSet.getString("password"));
				}
			}

		} catch (SQLException e) {
			System.err.println("Error fetching user by ID: " + e.getMessage());
		}

		return user;
	}

	public boolean addUser(User user) {
		String query = "INSERT INTO user_credentials (username, password) VALUES (?, ?)";

		try (Connection connection = dbConnection.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getPassword());
			return preparedStatement.executeUpdate() > 0;

		} catch (SQLException e) {
			System.err.println("Error adding user: " + e.getMessage());
		}

		return false;
	}

}
