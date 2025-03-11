package com.bank.user_service.Permission.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Permissions")
@Getter
@Setter
public class Permission {

    // Default constructor required by JPA
    public Permission() {
    }

    // constructor
    public Permission(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "VIEW_DOGS", "EDIT_DOGS", "DELETE_CUSTOMERS"
}

