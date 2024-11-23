package com.cst8277.messagingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RequestMapping("/messages")
@RestController
public class MessagingServiceApplication {

    @PostMapping
    public ResponseEntity<?> postMessage(@RequestBody Message message, @RequestHeader("Authorization") String token) {
        return null;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getMessages(@RequestParam(value="user_id", required = false)String userId) {
        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(MessagingServiceApplication.class, args);
    }



}
