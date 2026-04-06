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
@Table(name = "bank_account")
public class BankAccount extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // The actual number given to customer
    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    // Instead of @ManyToOne(User.class)
    // Breaks the tight coupling between the Identity module and Transaction module.
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 30)
    private AccountStatus accountStatus;

    // Act like a shield against Race Conditions
    // If two requests try to withdraw money at the exact same millisecond, Hibernate checks this version
    // The first request succeeds and increments the version. The second request sees the version changed,
    //      realizes the data is stale, and throws an OptimisticLockException
    //      instead of causing a negative balance.
    @Version
    private Long version;

}
