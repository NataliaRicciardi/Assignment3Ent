package com.cst8277.messagingservice;

import com.cst8277.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessagingDAO {
    private final DatabaseConnection dbConnection;

    public MessagingDAO() {
        this.dbConnection = new DatabaseConnection("messaging_service", "admin");
    }

    public List<Message> getMessagesForProducer(int producerId) {
        String query = "SELECT * FROM producer_messages WHERE producer_id = ?";
        List<Message> messages = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, producerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = new Message();
                    message.setMessageId(resultSet.getInt("message_id"));
                    message.setContent(resultSet.getString("content"));
                    message.setCreatedAt(resultSet.getTimestamp("created_at").toInstant());
                    messages.add(message);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching messages: " + e.getMessage());
        }

        return messages;
    }

}
