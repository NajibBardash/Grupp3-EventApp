package se.yrgo.user_service.service;


import se.yrgo.user_service.domain.User;
import se.yrgo.user_service.dtos.UserCreateDTO;
import se.yrgo.user_service.dtos.UserResponseDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDTO createUser (UserCreateDTO userCreateDTO);
    UserResponseDTO getUserByCustomerId (String customerId);
    UserResponseDTO getUserByUsername (String username);
    UserResponseDTO getUserByEmail (String email);
    void deleteUserById (long id);
    List<User> listAllUsers();
}
