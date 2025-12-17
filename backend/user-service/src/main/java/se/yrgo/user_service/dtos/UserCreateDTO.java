package se.yrgo.user_service.dtos;

import jakarta.validation.constraints.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Objects;

@Component
public class UserCreateDTO {
    @NotBlank
    private String customerId;
    @NotBlank
    @Size(min = 1, max = 14)
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
    private LocalDate birthdate;

    public UserCreateDTO() {}

    public UserCreateDTO(String customerId, String username, String name, String email, String password, LocalDate birthdate) {
        this.customerId = customerId;
        this.username = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthdate = birthdate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserCreateDTO that = (UserCreateDTO) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(birthdate, that.birthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, name, email, password, birthdate);
    }

    @Override
    public String toString() {
        return "UserCreateDTO{" +
                "customerId='" + customerId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
