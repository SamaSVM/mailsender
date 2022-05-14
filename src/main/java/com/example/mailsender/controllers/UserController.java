package com.example.mailsender.controllers;

import com.example.mailsender.domain.User;
import com.example.mailsender.service.EmailSenderService;
import com.example.mailsender.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User, UserService> {
    public UserController(UserService service, EmailSenderService senderService) {
        super(service);
        this.senderService = senderService;
    }

    private final EmailSenderService senderService;

    @PostMapping("/mails/{id}")
    public ResponseEntity<String> sendMail(@PathVariable Integer id) {
        User user;
        try {
            user = service.getById(id);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("User from id - " + id + " does not exist!", HttpStatus.BAD_REQUEST);
        }
        String body = "Ім'я користувача: " + user.getUsername() + "\nДата та час створення: " + user.getCreatedOn();
        senderService.sendMail(user.getEmail(), "Вітання!", body);
        return new ResponseEntity<>("The mail has been sent!", HttpStatus.OK);
    }
}
