package com.codewithmosh.store.controllers;

import com.codewithmosh.store.models.MessageModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @RequestMapping("/")
    public MessageModel getMessage() {
        MessageModel message = new MessageModel();
        message.setMessage("hello");
        return message;
    }
}
