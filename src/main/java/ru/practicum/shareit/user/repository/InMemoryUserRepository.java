package ru.practicum.shareit.user.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

@Repository
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(0);

    @Override
    public User save(User user) {
        User newwUser = new User(user.getId(), user.getName(), user.getEmail());
        if (newwUser.getId() == null) {
            newwUser.setId(idSequence.incrementAndGet());
        }
        users.put(newwUser.getId(), newwUser);
        return newwUser;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .sorted(Comparator.comparing(User::getId))
                .map(user -> new User(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return users.values().stream()
                .filter(user -> email != null && email.equals(user.getEmail()))
                .findFirst()
                .map(user -> new User(user.getId(), user.getName(), user.getEmail()));
    }

    @Override
    public void delete(Long userId) {
        users.remove(userId);
    }
}
