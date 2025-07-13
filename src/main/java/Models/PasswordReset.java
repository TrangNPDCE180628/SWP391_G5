package Models;

import java.time.LocalDateTime;

public class PasswordReset {
    private int id;
    private String email;
    private String otp;
    private LocalDateTime expiry;
    private LocalDateTime createdAt;

    public PasswordReset() {}

    public PasswordReset(String email, String otp, LocalDateTime expiry) {
        this.email = email;
        this.otp = otp;
        this.expiry = expiry;
    }

    // Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
