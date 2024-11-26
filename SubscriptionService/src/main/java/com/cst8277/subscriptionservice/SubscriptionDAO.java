package com.cst8277.subscriptionservice;

import java.nio.ByteBuffer;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionDAO {
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

    public List<Subscriber> getSubscribersForProducer(UUID producerId) {
        String query = """
                    SELECT subscribers.id, subscribers.comment
                    FROM subscribers
                    JOIN subscriptions ON subscribers.id = subscriptions.subscribers_id
                    WHERE subscriptions.producers_id = ?
                """;
        List<Subscriber> subscribers = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(producerId));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Subscriber subscriber = new Subscriber(
                            rs.getBytes("id"),
                            rs.getString("comment")
                    );
                    subscribers.add(subscriber);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return subscribers;
    }

    private byte[] convertUUIDToBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public List<Producer> getProducersForSubscriber(UUID subscriberId) {
        String query = """
        SELECT producers.id, producers.comment
        FROM producers
        JOIN subscriptions ON producers.id = subscriptions.producers_id
        WHERE subscriptions.subscribers_id = ?
    """;
        List<Producer> producers = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(subscriberId));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Producer producer = new Producer(
                            rs.getBytes("id"),
                            rs.getString("comment")
                    );
                    producers.add(producer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producers;
    }

    public boolean addSubscription(UUID producerId, UUID subscriberId) {
        String query = "INSERT INTO subscriptions (producers_id, subscribers_id) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(producerId));
            stmt.setBytes(2, convertUUIDToBytes(subscriberId));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeSubscription(UUID producerId, UUID subscriberId) {
        String query = "DELETE FROM subscriptions WHERE producers_id = ? AND subscribers_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(producerId));
            stmt.setBytes(2, convertUUIDToBytes(subscriberId));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isSubscribed(UUID producerId, UUID subscriberId) {
        String query = "SELECT 1 FROM subscriptions WHERE producers_id = ? AND subscribers_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBytes(1, convertUUIDToBytes(producerId));
            stmt.setBytes(2, convertUUIDToBytes(subscriberId));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
