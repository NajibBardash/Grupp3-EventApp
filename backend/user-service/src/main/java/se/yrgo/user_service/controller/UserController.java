package se.yrgo.user_service.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import se.yrgo.user_service.data.UserDao;
import se.yrgo.user_service.domain.User;
import se.yrgo.user_service.dtos.UserCreateDTO;
import se.yrgo.user_service.dtos.UserResponseDTO;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserDao userData;
    private final PasswordEncoder passwordEncoder;
    private Set<User> users = new HashSet<>();

    public UserController(UserDao userData, PasswordEncoder passwordEncoder) {
        this.userData = userData;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserCreateDTO reg_dto) {
        User user = new User();
        user.setCustomerId(reg_dto.getCustomerId());
        user.setUsername(reg_dto.getUsername());
        user.setName(reg_dto.getName());
        user.setEmail(reg_dto.getEmail());
        user.setPassword(passwordEncoder.encode(reg_dto.getPassword()));
        user.setBirthdate(reg_dto.getBirthdate());
        user.getRoles().add(se.yrgo.user_service.domain.Role.USER);
        userData.save(user);
        users.add(user);
        UserResponseDTO registerDTO = convertToDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerDTO);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> login(@RequestBody UserCreateDTO loginData) {
        return userData.getUserByEmail(loginData.getEmail())
                .filter(user -> passwordEncoder.matches(loginData.getPassword(), user.getPassword()))
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @RequestMapping(value = "/create-user", method = RequestMethod.POST)
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreateDTO create_dto) {
        User user = new User();
        user.setCustomerId(create_dto.getCustomerId());
        user.setUsername(create_dto.getUsername());
        user.setName(create_dto.getName());
        user.setEmail(create_dto.getEmail());
        user.setPassword(passwordEncoder.encode(create_dto.getPassword()));
        user.setBirthdate(create_dto.getBirthdate());
        user.getRoles().add(se.yrgo.user_service.domain.Role.USER);
        userData.save(user);
        UserResponseDTO createDTO = convertToDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUserById(@PathVariable long id) {
        if (userData.existsById(id)) {
            userData.deleteUserById(id);
            users.removeIf(user -> user.getId().equals(id));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> listAllUsers() {
        try {
            List<User> users = userData.listAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UserResponseDTO> responseList = users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDTO> getUserByCustomerId(@PathVariable String customerId) {
        Optional<User> userOpt = users.stream().filter
                (user -> user.getCustomerId().equals(customerId)).findFirst();

        if (!userOpt.isPresent()) {
            userOpt = userData.getUserByCustomerId(customerId);
        }
        return userOpt.map(this::convertToDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDTO> getUserByUsername(@PathVariable String username) {
        Optional<User> userOpt = users.stream().filter
                (user -> user.getName().equals(username)).findFirst();
        if (!userOpt.isPresent()) {
            userOpt = userData.getUserByUsername(username);
        }
        return userOpt.map(this::convertToDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        Optional<User> userOpt = users.stream().filter
                (user -> user.getEmail().equals(email)).findFirst();
        if (!userOpt.isPresent()) {
            userOpt = userData.getUserByEmail(email);
        }
        return userOpt.map(this::convertToDTO).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

    }

    private UserResponseDTO convertToDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setCustomerId(user.getCustomerId());
        dto.setUsername(user.getUsername());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBirthdate(user.getBirthdate());
        // Note: We don't include password in the response for security
        return dto;
    }

    // TODO:  Add and implement
//    public void addUser (Booking booking) {
//        this.bookings.add(booking);
//        booking.setUser(this);
//    }
//    public void removeUser (Booking booking) {
//        this.bookings.remove(booking);
//        booking.setUser(null);
//    }

}



