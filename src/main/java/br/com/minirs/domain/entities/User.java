package br.com.minirs.domain.entities;

import br.com.minirs.domain.enums.PrivacyStatusEnum;
import br.com.minirs.domain.exceptions.ResourceAlreadyActiveException;
import br.com.minirs.domain.exceptions.ResourceDisabledException;
import br.com.minirs.domain.valueobjects.Email;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String userName;

    @Embedded
    private Email email;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "biography", length = 500)
    private String biography;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "privacy_status", nullable = false, length = 20)
    private PrivacyStatusEnum profilePrivacyStatus = PrivacyStatusEnum.PUBLIC;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected User() {
    }

    public User(String fullName, String userName, Email email, LocalDate birthDate, String password) {
        changeFullName(fullName);
        changeUserName(userName);
        changeEmail(email);
        changeBirthDate(birthDate);
        changePassword(password);
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email.getValue();
    }

    public boolean isActive() {
        return active;
    }

    public PrivacyStatusEnum getProfilePrivacyStatus() {
        return profilePrivacyStatus;
    }

    public boolean isPublicProfile() {
        return profilePrivacyStatus == PrivacyStatusEnum.PUBLIC;
    }

    public boolean isPrivateProfile() {
        return profilePrivacyStatus == PrivacyStatusEnum.PRIVATE;
    }

    public void changeFullName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name must not be null or blank");
        }
        if (fullName.length() > 100) {
            throw new IllegalArgumentException("Full name must be at most 100 characters");
        }
        this.fullName = fullName.trim();
    }

    public void changeUserName(String userName) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username must not be null or blank");
        }
        if (userName.length() > 50) {
            throw new IllegalArgumentException("Username must be at most 50 characters");
        }
        this.userName = userName.trim();
    }

    public void changeEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email must not be null");
        }
        this.email = email;
    }

    public void changeBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException("Birth date must not be null");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date must not be in the future");
        }
        this.birthDate = birthDate;
    }

    public void changeBiography(String biography) {
        if (biography != null && biography.length() > 500) {
            throw new IllegalArgumentException("Biography must be at most 500 characters");
        }
        this.biography = biography != null ? biography.trim() : null;
    }

    public void changePassword(String password) {
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (password.length() > 255) {
            throw new IllegalArgumentException("Password must be at most 255 characters");
        }
        this.password = password;
    }

    public void changeProfilePrivacyStatus(PrivacyStatusEnum profilePrivacyStatus) {
        if (profilePrivacyStatus == null) {
            throw new IllegalArgumentException("Privacy status must not be null");
        }
        this.profilePrivacyStatus = profilePrivacyStatus;
    }

    public void disable() {
        if (!this.active) {
            throw new ResourceDisabledException("User is already disabled");
        }
        this.active = false;
    }

    public void enable() {
        if (this.active) {
            throw new ResourceAlreadyActiveException("User is already active");
        }
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
