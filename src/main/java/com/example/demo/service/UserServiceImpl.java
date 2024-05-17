package com.example.demo.service;

import com.example.demo.config.JWTGenerator;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.Subscriber;
import com.example.demo.model.User;
import com.example.demo.repositories.SubscriberRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired

    private final UserRepository userRepository;

    @Autowired
    private final SubscriberRepository subscriberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    private AuthenticationManager authenticationManager;

    private JWTGenerator jwtGenerator;

    public UserServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,SubscriberRepository subscriberRepository, PasswordEncoder passwordEncoder, JWTGenerator jwtGenerator) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.subscriberRepository = subscriberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCity(userDTO.getCity());
        user.setRegion(userDTO.getRegion());
        user.setRole(userDTO.getRole());
        user = userRepository.save(user);
        return new UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), userDTO.getPassword(),
                user.getCity(),user.getRegion(),user.getRole()
        );
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setRole(userDTO.getRole());
        user = userRepository.save(user);
        return new  UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), userDTO.getPassword(),
                user.getCity(),user.getRegion(),user.getRole());
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), user.getPassword(),
                user.getCity(),user.getRegion(),user.getRole()
        );
    }

    // Inside UserServiceImpl.java
    @Override
    @Transactional
    public void updateUserRole(Long userId, String newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(newRole);
        userRepository.save(user);
    }
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), user.getPassword(),
                user.getCity(),user.getRegion(),user.getRole()); // Password is not included for security
    }
    public User getUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }
    @Override
    public UserDTO getUserByName(String name) {
        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), user.getPassword(),
                user.getCity(),user.getRegion(),user.getRole());

    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> new UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), user.getPassword(),
                user.getCity(),user.getRegion(),user.getRole())).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCity(userDTO.getCity());
        user.setRegion(userDTO.getRegion());
        user.setRole(userDTO.getRole());


        user = userRepository.save(user);

        return new UserDTO(user.getId(), user.getEmail(), user.getUsername(),
                userDTO.getPassword(), user.getCity(), user.getRegion(), userDTO.getRole());
    }

    public UserDTO loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }



        return new UserDTO(user.getId(), user.getEmail(),
                user.getUsername(), user.getPassword(),
                user.getCity(),user.getRegion(),user.getRole()); // Avoid returning the password
    }
}
