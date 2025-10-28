package com.example.demo.controller;

import com.example.demo.dto.OtpVerifyRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody OtpVerifyRequest request) {
        return userService.verifyOtp(request.getUserId(), request.getOtp());
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        String password = loginData.get("password");
        return userService.login(email, password);
    }

    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
