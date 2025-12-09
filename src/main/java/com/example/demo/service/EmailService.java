package com.example.demo.service;

import com.resend.Resend;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService() {
        // ✅ Read API key directly from environment variable
        String apiKey = System.getenv("RESEND_API_KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("RESEND_API_KEY environment variable is missing!");
        }

        this.resend = new Resend(apiKey);
    }

    public void sendOtpEmail(String to, String otp) throws Exception {
        String htmlContent = "<h2>Your OTP for Email Verification</h2>" +
                             "<p><strong>" + otp + "</strong></p>" +
                             "<p>This OTP will expire in 5 minutes.</p>";

        SendEmailRequest request = SendEmailRequest.builder()
                .from("SKArt <onboarding@resend.dev>")
                .to(to)
                .subject("Email Verification OTP")
                .html(htmlContent)
                .build();

        SendEmailResponse response = resend.emails().send(request);
        // Optional: process response.getId() if needed
    }
}
