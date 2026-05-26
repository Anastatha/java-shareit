package ru.practicum.shareit.user.service;

import java.util.NoSuchElementException;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDto create(UserCreateDto userDto) {
		if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
			throw new ConflictException("Email already exists");
		}
		User saved = userRepository.save(new User(null, userDto.getName(), userDto.getEmail()));
		return UserMapper.toUserDto(saved);
	}

	@Override
	public UserDto update(Long userId, UserUpdateDto userDto) {
		User existing = userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("User not found"));

		String name = userDto.getName() != null ? userDto.getName() : existing.getName();
		String email = userDto.getEmail() != null ? userDto.getEmail() : existing.getEmail();
		if (!email.equals(existing.getEmail()) && userRepository.findByEmail(email).isPresent()) {
			throw new ConflictException("Email already exists");
		}

		User updated = new User(userId, name, email);
		return UserMapper.toUserDto(userRepository.save(updated));
	}

	@Override
	public UserDto getById(Long userId) {
		return userRepository.findById(userId)
			.map(UserMapper::toUserDto)
			.orElseThrow(() -> new NoSuchElementException("User not found"));
	}

	@Override
	public List<UserDto> getAll() {
		return userRepository.findAll().stream()
			.map(UserMapper::toUserDto)
			.toList();
	}

	@Override
	public void delete(Long userId) {
		if (userRepository.findById(userId).isEmpty()) {
			throw new NoSuchElementException("User not found");
		}
		userRepository.delete(userId);
	}
}
