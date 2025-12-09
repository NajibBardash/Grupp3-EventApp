package se.yrgo.user_service.dto;

import java.time.LocalDate;

public class UserDTO {
    private Long id;
    private String customerId;
    private String name;
    private String email;
    private String password;
    private LocalDate birthdate;

    public UserDTO(Long id, String customerId, String name, String email, String password, LocalDate birthdate) {
        this.id = id;
        this.customerId = customerId;
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
}