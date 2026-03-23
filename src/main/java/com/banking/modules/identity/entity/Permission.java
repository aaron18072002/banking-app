package com.banking.modules.identity.entity;

import com.banking.common.identity.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Exact string used in @PreAuthorize (Ex: "TRANSACTION_APPROVE")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 100)
    private String description;

}
