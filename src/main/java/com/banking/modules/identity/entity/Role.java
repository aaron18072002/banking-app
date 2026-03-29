package com.banking.modules.identity.entity;

import com.banking.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Start with "ROLE_" (Ex: "ROLE_CUSTOMER")
    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(length = 100)
    private String description;

    // If you use List with @ManyToMany, Hibernate will delete all rows in the join table
    // and re-inserts them when you remove just one item. Set prevents this terrible performance issue.
    @ManyToMany
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions = new HashSet<Permission>();

}
