package com.banking.modules.identity.entity;

public enum UserStatus {
    PENDING_VERIFICATION, // Waiting verification (after register, wait to enter OTP).
    ACTIVE,
    LOCKED, // Tạm khóa hệ thống tự động (Enter wrong password over 5 times).
    SUSPENDED, // Bị đình chỉ (Admin block if user have any legal issue).
    CLOSED // Close permanently, cannot reactivate
}
