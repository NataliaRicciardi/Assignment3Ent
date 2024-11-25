package com.cst8277.messagingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@RequestMapping("/messages")
@RestController
public class MessagingServiceApplication {

    private final MessagingDAO messagingDAO;

    public MessagingServiceApplication() {
        this.messagingDAO = new MessagingDAO();
    }

    @PostMapping
    public ResponseEntity<?> postMessage(@RequestBody Message message, @RequestHeader("Authorization") String token) {
        if (message.getProducerId() == null || message.getContent() == null) {
            return ResponseEntity.badRequest().body("Message and ProducerId must be provided.");
        }

        // Add the message
        boolean success = messagingDAO.addMessage(message.getProducerId(), message.getContent(), message.getCreated());

        return success
                ? ResponseEntity.status(201).body("Message posted successfully.")
                : ResponseEntity.status(500).body("Failed to post message.");
    }

    @GetMapping("/producer/{producerId}")
    public ResponseEntity<List<Message>> getMessagesForProducer(@PathVariable("producerId") String producerId) {
        UUID producerUuid = UUID.fromString(producerId);
        List<Message> messages = messagingDAO.getMessagesForProducer(producerUuid);

        return messages.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(messages);
    }

    @GetMapping("/subscriber/{subscriberId}")
    public ResponseEntity<List<Message>> getMessagesForSubscriber(@PathVariable("subscriberId") String subscriberId) {
        UUID subscriberUuid = UUID.fromString(subscriberId);
        List<Message> messages = messagingDAO.getMessagesForSubscriber(subscriberUuid);

        return messages.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(messages);
    }

    public static void main(String[] args) {
        SpringApplication.run(MessagingServiceApplication.class, args);
    }



}
