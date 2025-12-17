package se.yrgo.user_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.*;
import java.time.LocalDate;

@Entity
@Table(name="users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NotBlank
    private String customerId;

    @NotNull
    @Size(min = 2, max = 14)
    private String username;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    private LocalDate birthdate;
    @NotBlank
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    //TODO: Solve this later, with the other applications.
    //private List<Booking> bookings = new ArrayList<>();

    public User() {

    }

    public User(String customerId, String username, String name, String email, LocalDate birthdate) {
        this.customerId = customerId;
        this.username = username;
        this.name = name;
        this.email = email;
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

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public String getPassword() {return password;}

    public Set<Role> getRoles() {
        return roles;
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

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(customerId, user.customerId) && Objects.equals(username, user.username) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(birthdate, user.birthdate) && Objects.equals(password, user.password) && Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customerId, username, name, email, birthdate, password, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", customerId='" + customerId + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birthdate=" + birthdate +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }

    // TODO: See List <Booking> Above
//    public List<Booking> getBookings() {
//        return bookings;
//    }

}
