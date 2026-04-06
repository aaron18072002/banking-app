package com.banking.modules.transaction.entity;

public enum AccountStatus {

    ACTIVE, // Normal state, can send and receive money
    BLOCKED, // Temporarily locked
    CLOSED // Permanently closed, cannot be reactivated

}
