package ru.borisov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.borisov.model.User;
import ru.borisov.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public void registerUser(User user) {

        boolean exists = userRepository.existsById(user.getEmail());
        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User with email= " + user.getEmail() + " is already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
