package put.poznan.textmanager.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import put.poznan.textmanager.exception.UserNotFoundException;
import put.poznan.textmanager.model.User;
import put.poznan.textmanager.service.EmailService;
import put.poznan.textmanager.service.UserService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    public UserController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User newUser) {
        User user = userService.addUser(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User newUser) {
        try {
            userService.findUserByEmail(newUser.getEmail());
        } catch (UserNotFoundException e) {
            try {
                userService.findUserByUsername(newUser.getUsername());
            } catch (UserNotFoundException f) {
                newUser.setHashedPassword(newUser.getPassword());
                User user = userService.addUser(newUser);
                return new ResponseEntity<>(user.getUserId().toString(), HttpStatus.CREATED);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User loggedUser) {
        User user;
        try {
            user = userService.findUserByUsername(loggedUser.getUsername());
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (user.checkPassword(loggedUser.getPassword())) {
            return new ResponseEntity<>(user.getUserId().toString(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/req-reset/{mail}")
    public ResponseEntity<?> sendMail(@PathVariable("mail") String mail) {
        User user;
        try {
            user = userService.findUserByEmail(mail);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setResetUrl(UUID.randomUUID().toString());
        userService.updateUser(user);
        //błąd jakarta.mail.AuthenticationFailedException: 535 5.7.8 Error: authentication failed
        //oznacza, że konto zostało najprawdopodobniej zablkowane z powodu podejrzeń wirusa
        emailService.sendEmail(mail, "New password for TextManager", "Hi, " + user.getUsername() + "!\n\n"
                + "Use the link below to reset your password:\n\n"
                + "{url}/reset/" + user.getResetUrl());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reset")
    @Transactional
    public ResponseEntity<?> resetPassword(@RequestBody PasswordChange passwordChange) {
        try {
            User user = userService.findUserByCode(passwordChange.code);
            user.setHashedPassword(passwordChange.password);
            user.setResetUrl("");
            userService.updateUser(user);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static class PasswordChange {
        public String code;
        public String password;
    }
}
