package com.banking.modules.transaction.entity;

public enum TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED,
    REVERSED // Money was deducted but later refunded due to error
}
