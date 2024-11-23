package com.cst8277.notificationservice;

import com.cst8277.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    private final DatabaseConnection dbConnection;

    public NotificationDAO() {
        this.dbConnection = new DatabaseConnection("notification_service", "admin");
    }

    public List<Notification> getRecentNotifications(int subscriberId) {
        String query = "SELECT * FROM subscriber_notifications WHERE subscriber_id = ?";
        List<Notification> notifications = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, subscriberId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Notification notification = new Notification();
                    notification.setSubscriberId(resultSet.getInt("subscriber_id"));
                    notification.setProducerName(resultSet.getString("producer_name"));
                    notification.setMessageContent(resultSet.getString("content"));
                    notification.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
                    notifications.add(notification);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching notifications: " + e.getMessage());
        }

        return notifications;
    }
}
