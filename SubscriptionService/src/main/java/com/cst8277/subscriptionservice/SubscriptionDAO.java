package com.cst8277.subscriptionservice;

import com.cst8277.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDAO {
    private final DatabaseConnection dbConnection;

    public SubscriptionDAO() {
        this.dbConnection = new DatabaseConnection("subscription_service", "admin");
    }

    public List<Subscription> getActiveSubscriptions() {
        String query = "SELECT * FROM active_subscriptions";
        List<Subscription> subscriptions = new ArrayList<>();

        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Subscription subscription = new Subscription();
                subscription.setSubscriberId(resultSet.getInt("subscriber_id"));
                subscription.setSubscriberName(resultSet.getString("subscriber_name"));
                subscription.setProducerId(resultSet.getInt("producer_id"));
                subscription.setProducerName(resultSet.getString("producer_name"));
                subscriptions.add(subscription);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching subscriptions: " + e.getMessage());
        }

        return subscriptions;
    }

}
