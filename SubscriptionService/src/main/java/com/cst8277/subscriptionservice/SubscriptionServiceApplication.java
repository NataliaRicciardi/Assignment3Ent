package com.cst8277.subscriptionservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.UUID;

@SpringBootApplication
@RequestMapping("/subscriptions")
@RestController
public class SubscriptionServiceApplication {

    private final SubscriptionDAO subscriptionDAO;

    @Autowired
    public SubscriptionServiceApplication(SubscriptionDAO subscriptionDAO) {
        this.subscriptionDAO = subscriptionDAO;
    }

    @PostMapping("/{producer_id}")
    public ResponseEntity<?> subscribe(@PathVariable("producer_id") String producerIdStr,
                                       @RequestHeader("Authorization") String token) {
        UUID producerId = UUID.fromString(producerIdStr);
        UUID subscriberId = extractSubscriberIdFromToken(token);

        if (subscriptionDAO.isSubscribed(producerId, subscriberId)) {
            return ResponseEntity.badRequest().body("Already subscribed");
        }

        boolean success = subscriptionDAO.addSubscription(producerId, subscriberId);
        if (success) {
            return ResponseEntity.ok("Subscribed successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to subscribe");
        }
    }

    @DeleteMapping("/{producer_id}")
    public ResponseEntity<?> unsubscribe(@PathVariable("producer_id") String producerIdStr,
                                         @RequestHeader("Authorization") String token) {
        UUID producerId = UUID.fromString(producerIdStr);
        UUID subscriberId = extractSubscriberIdFromToken(token);

        if (!subscriptionDAO.isSubscribed(producerId, subscriberId)) {
            return ResponseEntity.badRequest().body("Not subscribed");
        }

        boolean success = subscriptionDAO.removeSubscription(producerId, subscriberId);
        if (success) {
            return ResponseEntity.ok("Unsubscribed successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to unsubscribe");
        }
    }

    @GetMapping
    public ResponseEntity<?> getSubscribedProducers(@RequestHeader("Authorization") String token) {
        UUID subscriberId = extractSubscriberIdFromToken(token);

        List<Producer> producers = subscriptionDAO.getProducersForSubscriber(subscriberId);
        return ResponseEntity.ok(producers);
    }

    @GetMapping("/{producer_id}/subscribers")
    public ResponseEntity<?> getSubscribersForProducer(@PathVariable("producer_id") String producerIdStr) {
        UUID producerId = UUID.fromString(producerIdStr);

        List<Subscriber> subscribers = subscriptionDAO.getSubscribersForProducer(producerId);
        return ResponseEntity.ok(subscribers);
    }

    private UUID extractSubscriberIdFromToken(String token) {
        return UUID.fromString(token);
    }

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionServiceApplication.class, args);
    }

}
