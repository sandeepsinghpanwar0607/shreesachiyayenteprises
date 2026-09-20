package com.SSE.Website.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.SSE.Website.entity.OtpVerification;
import com.SSE.Website.entity.User;
import com.SSE.Website.repository.OtpVerificationRepository;
import com.SSE.Website.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserRepository userRepository;

    private final OtpVerificationRepository otpRepository;


    // =========================
    // GENERATE OTP
    // =========================
    public String generateOtp(User user) {

        System.out.println(
                "========== OTP METHOD CALLED =========="
        );

        // 6 digit OTP
        String otp = String.format(
                "%06d",
                new Random().nextInt(1000000)
        );

        OtpVerification otpVerification =
                OtpVerification.builder()
                        .mobileOtp(otp)
                        .expiresAt(
                                LocalDateTime.now().plusMinutes(5)
                        )
                        .verified(false)
                        .user(user)
                        .build();

        otpRepository.save(otpVerification);

        System.out.println(
                "========== OTP SAVED =========="
        );

        // Temporary testing ke liye
        // OTP Eclipse console mein show hoga
        System.out.println(
                "OTP for "
                + user.getMobileNumber()
                + " = "
                + otp
        );

        return otp;
    }


    // =========================
    // VERIFY OTP
    // =========================
    public boolean verifyOtp(
            Long userId,
            String otp) {

        OtpVerification verification =
                otpRepository
                        .findTopByUserIdOrderByIdDesc(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "OTP not found"
                                )
                        );


        // OTP already verified
        if (verification.getVerified()) {

            throw new RuntimeException(
                    "OTP already verified"
            );
        }


        // OTP expired
        if (verification.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException(
                    "OTP expired"
            );
        }


        // Wrong OTP
        if (!verification.getMobileOtp()
                .equals(otp)) {

            throw new RuntimeException(
                    "Invalid OTP"
            );
        }


        // OTP verified
        verification.setVerified(true);

        otpRepository.save(verification);


        // Activate user
        User user = verification.getUser();

        user.setMobileVerified(true);

        user.setActive(true);

        userRepository.save(user);


        System.out.println(
                "========== OTP VERIFIED =========="
        );

        System.out.println(
                "User ID " + userId
                + " account activated successfully"
        );

        return true;
    }
}