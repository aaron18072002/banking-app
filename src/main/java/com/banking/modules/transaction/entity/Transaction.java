package com.banking.modules.transaction.entity;

import com.banking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // A unique string generated for this specific transaction
    // Prevents double-spending. If the mobile app lags and the user clicks "Transfer" twice,
    //      both requests will have the same reference number. The database will reject the
    //      second one because of unique=true.
    @Column(name = "reference_number", unique = true, nullable = false, length = 50)
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_id", nullable = false)
    private BankAccount sourceAccount;

    @Column(name = "destination_account_number", nullable = false, length = 30)
    private String destinationAccountNumber;

    // The exact amount of money being moved
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 30)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 30)
    private TransactionStatus transactionStatus;

    // The transfer message input by the user
    @Column(length = 255)
    private String description;

}
