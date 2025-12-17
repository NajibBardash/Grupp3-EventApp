package se.yrgo.user_service.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Objects;

public class UserResponseDTO {
    private Long id;
    @NotBlank
    private String customerId;
    @NotBlank
    @Size(min = 2, max = 14)
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private LocalDate birthdate;

    public UserResponseDTO() {}

    public UserResponseDTO(Long id, String username, String customerId, String name, String email, String password, LocalDate birthdate) {
        this.id = id;
        this.customerId = customerId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserResponseDTO that = (UserResponseDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(customerId, that.customerId) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(birthdate, that.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, name, email, password, birthdate);
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}