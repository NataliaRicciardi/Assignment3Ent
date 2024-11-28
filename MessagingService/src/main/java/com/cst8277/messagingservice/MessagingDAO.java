package com.cst8277.messagingservice;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class MessagingDAO {
    private Connection dbConnection;

    public Connection getConnection() {
        String url = "jdbc:mysql://127.0.0.1:3306/messages";
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

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String query = """
        SELECT id, content, created, producer_id
        FROM messages
    """;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID messageId = convertBytesToUUID(rs.getBytes("id"));
                    String content = rs.getString("content");
                    int created = rs.getInt("created");
                    UUID producerId = convertBytesToUUID(rs.getBytes("producer_id"));

                    // Convert UNIX timestamp to ZonedDateTime
                    ZonedDateTime createdDateTime = Instant.ofEpochSecond(created)
                            .atZone(ZoneId.systemDefault());

                    // Add message to the list
                    messages.add(new Message(messageId, content, createdDateTime, producerId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public List<Message> getMessagesForProducer(UUID producerId) {
        List<Message> messages = new ArrayList<>();
        String query = """
            SELECT id, content, created
            FROM messages
            WHERE producer_id = ?
        """;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(producerId));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID messageId = convertBytesToUUID(rs.getBytes("id"));
                    String content = rs.getString("content");
                    int created = rs.getInt("created");

                    //Convert UNIX timestamp
                    ZonedDateTime createdDateTime = Instant.ofEpochSecond(created)
                            .atZone(ZoneId.systemDefault());

                    messages.add(new Message(messageId, content, createdDateTime, null));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public List<Message> getMessagesForSubscriber(UUID subscriberId) {
        List<Message> messages = new ArrayList<>();
        String query = """
        SELECT m.id, m.content, m.created, m.producer_id
        FROM messages m
        JOIN subscriptions s ON m.producer_id = s.producer_id
        WHERE s.subscriber_id = ?
    """;

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(subscriberId));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UUID messageId = convertBytesToUUID(rs.getBytes("id"));
                    String content = rs.getString("content");
                    int created = rs.getInt("created");
                    UUID producerId = convertBytesToUUID(rs.getBytes("producer_id"));

                    //Convert UNIX timestamp
                    ZonedDateTime createdDateTime = Instant.ofEpochSecond(created)
                            .atZone(ZoneId.systemDefault());

                    // message to list
                    messages.add(new Message(messageId, content, createdDateTime, producerId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    public boolean addMessage(UUID producerId, String content, ZonedDateTime createdDateTime) {
        String query = """
            INSERT INTO messages (id, producer_id, content, created)
            VALUES (?, ?, ?, ?)
        """;
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, UUID.randomUUID().toString().getBytes());
            stmt.setBytes(2, convertUUIDToBytes(producerId));
            stmt.setString(3, content);
            stmt.setInt(4, (int) createdDateTime.toEpochSecond());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMessage(UUID messageId) {
        String query = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(messageId));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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
