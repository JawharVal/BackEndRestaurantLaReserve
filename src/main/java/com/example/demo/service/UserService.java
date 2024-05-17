package com.example.demo.service;


import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO registerUser(UserDTO userDTO);
    // Add this method
    void updateUserRole(Long userId, String newRole);
    UserDTO loginUser(String email, String password);

    UserDTO getUserByEmail(String email);

    User getUserEntityById(Long id);

    UserDTO getUserByName(String name);

}