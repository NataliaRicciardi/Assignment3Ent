package com.cst8277.usermanagementservice;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserManagementDAO {

	private Connection dbConnection;

	public Connection getConnection() {
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
		return dbConnection;
	}

	public List<User> getAllUsers() {
		List<User> users = new ArrayList<>();
		String query = """
        SELECT u.id, u.name, u.email, u.created, u.last_visit_id, GROUP_CONCAT(r.name) AS roles
        FROM users u
        LEFT JOIN users_has_roles ur ON u.id = ur.users_id
        LEFT JOIN roles r ON ur.roles_id = r.id
        GROUP BY u.id
    """;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					// Convert binary IDs to UUID
					UUID userId = UUID.nameUUIDFromBytes(rs.getBytes("id"));
					UUID lastVisitId = rs.getBytes("last_visit_id") != null
							? UUID.nameUUIDFromBytes(rs.getBytes("last_visit_id"))
							: null;

					// Parse roles string to List<String>
					String rolesStr = rs.getString("roles");
					List<String> roles = rolesStr != null ? List.of(rolesStr.split(",")) : new ArrayList<>();

					// Construct User object
					User user = new User(
							userId,
							rs.getString("name"),
							rs.getString("email"),
							null,
							rs.getInt("created"),
							lastVisitId,
							roles
					);
					users.add(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return users;
	}

	public User getUserById(UUID userId) {
		User user = null;
		String query = """
        SELECT u.id, u.name, u.email, u.created, u.last_visit_id, GROUP_CONCAT(r.name) AS roles
        FROM users u
        LEFT JOIN users_has_roles ur ON u.id = ur.users_id
        LEFT JOIN roles r ON ur.roles_id = r.id
        WHERE u.id = ?
        GROUP BY u.id
    """;

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			stmt.setBytes(1, convertUUIDToBytes(userId));

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					// Convert byte arrays back to UUIDs
					UUID id = UUID.nameUUIDFromBytes(rs.getBytes("id"));
					UUID lastVisitId = rs.getBytes("last_visit_id") != null
							? UUID.nameUUIDFromBytes(rs.getBytes("last_visit_id"))
							: null;

					// Create User object
					user = new User(
							id,
							rs.getString("name"),
							rs.getString("email"),
							rs.getInt("created"),
							lastVisitId,
							rs.getString("roles")
					);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return user;
	}

	public boolean addUser(User user) {
		String insertUserQuery = """
			INSERT INTO users (id, name, email, password, created, last_visit_id)
			VALUES (?, ?, ?, ?, ?, ?) 
			""";

		try (Connection conn = getConnection()) {
			conn.setAutoCommit(false); // Enable transaction

			// Insert into users table
			try (PreparedStatement userStmt = conn.prepareStatement(insertUserQuery)) {
				userStmt.setBytes(1, convertUUIDToBytes(user.getId()));
				userStmt.setString(2, user.getName());
				userStmt.setString(3, user.getEmail());
				userStmt.setString(4, user.getPassword());
				userStmt.setInt(5, user.getCreated());
				userStmt.setBytes(6, convertUUIDToBytes(user.getLastVisitId()));
				userStmt.executeUpdate();
			}

			conn.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteUser(UUID userId) {
		String deleteUserQuery = "DELETE FROM users WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(deleteUserQuery)) {
			stmt.setBytes(1, convertUUIDToBytes(userId));
			return stmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public List<Role> getAllRoles() {
		List<Role> roles = new ArrayList<>();
		String query = "SELECT id, name, description FROM roles";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Role role = new Role(
							convertBytesToUUID(rs.getBytes("id")),
							rs.getString("name"),
							rs.getString("description")
					);
					roles.add(role);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return roles;
	}

	private byte[] convertUUIDToBytes(UUID uuid) {
		ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}

	private UUID convertBytesToUUID(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		long high = buffer.getLong();
		long low = buffer.getLong();
		return new UUID(high, low);
	}

}
