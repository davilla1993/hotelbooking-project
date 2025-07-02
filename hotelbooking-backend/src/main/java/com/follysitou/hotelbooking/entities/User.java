package com.follysitou.hotelbooking.entities;

import com.follysitou.hotelbooking.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Email is required")
    private String email;

    private String firstName;

    private String lastName;

    @NotBlank(message = "Password is required")
    private String password;

    @Column(name = "phone_number")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private boolean active;

    private final LocalDate createdAt = LocalDate.now();
}
