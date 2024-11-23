package com.cst8277.subscriptionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RequestMapping("/subscriptions")
@RestController
public class SubscriptionServiceApplication {

    @PostMapping("/{producer_id}")
    public ResponseEntity<?> subscribe(@PathVariable String producerId, @RequestHeader("Authorization") String token) {
        return null;
    }

    @DeleteMapping("/{producer_id}")
    public ResponseEntity<?> unsubscribe(@PathVariable String producerId, @RequestHeader("Authorization") String token) {
        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionServiceApplication.class, args);
    }

}
