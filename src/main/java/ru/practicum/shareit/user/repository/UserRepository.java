package ru.practicum.shareit.user.repository;

import java.util.List;
import java.util.Optional;

import ru.practicum.shareit.user.User;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long userId);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    void delete(Long userId);
}
