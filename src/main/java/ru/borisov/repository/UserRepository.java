package ru.borisov.repository;

import org.springframework.data.repository.CrudRepository;
import ru.borisov.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

    Optional<User> findByEmail(String email);
}
