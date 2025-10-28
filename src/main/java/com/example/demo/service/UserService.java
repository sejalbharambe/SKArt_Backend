package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registration
    public Map<String, Object> registerUser(User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                response.put("message", "Email already exists!");
                return response;
            }

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            String otp = String.format("%06d", new Random().nextInt(999999));
            user.setOtp(otp);
            user.setVerified(false);

            User savedUser = userRepository.save(user);
            emailService.sendOtpEmail(user.getEmail(), otp);

            response.put("message", "OTP sent successfully to your email!");
            response.put("userId", savedUser.getId());
            return response;

        } catch (Exception e) {
            response.put("message", "Error during registration: " + e.getMessage());
            return response;
        }
    }

    // OTP verification
    public Map<String, Object> verifyOtp(Long userId, String otp) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            response.put("message", "User not found!");
            return response;
        }

        User user = optionalUser.get();

        if (user.getOtp().equals(otp)) {
            user.setVerified(true);
            user.setOtp(null);
            userRepository.save(user);

            response.put("message", "Email verified successfully!");
            response.put("userDetails", user);
            return response;
        } else {
            response.put("message", "Invalid OTP!");
            return response;
        }
    }

    //  Login service
    public Map<String, Object> login(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            response.put("message", "User not found!");
            return response;
        }

        User user = optionalUser.get();

        if (!user.isVerified()) {
            response.put("message", "Email not verified. Please verify your account.");
            return response;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("message", "Invalid credentials!");
            return response;
        }

        response.put("message", "Login successful!");
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        response.put("username", user.getUsername());
        response.put("phoneNumber", user.getPhoneNumber());
        return response;
    }

    //  Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
