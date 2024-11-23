package com.cst8277.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@SpringBootApplication
@RequestMapping("/notifications")
@RestController
public class NotificationServiceApplication {

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam("subscriber_id") String subscriberId) {
        return null;
    }

    public static void main(String[] args) {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }


}
