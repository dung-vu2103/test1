package com.ringme.cms.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send-message")
    @SendTo("/topic/messages")
    public String sendMessage(@Payload String message) {
        return "Received: " + message;
    }
}
