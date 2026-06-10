package ru.practicum.shareit.gateway.user;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.client.UserClient;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserClient userClient;

	public UserController(UserClient userClient) {
		this.userClient = userClient;
	}

	@PostMapping
	public UserDto create(@Valid @RequestBody UserCreateDto userDto) {
		return userClient.create(userDto);
	}

	@PatchMapping("/{userId}")
	public UserDto update(@PathVariable Long userId, @Valid @RequestBody UserUpdateDto userDto) {
		return userClient.update(userId, userDto);
	}

	@GetMapping("/{userId}")
	public UserDto getById(@PathVariable Long userId) {
		return userClient.getById(userId);
	}

	@GetMapping
	public List<UserDto> getAll() {
		return userClient.getAll();
	}

	@DeleteMapping("/{userId}")
	public void delete(@PathVariable Long userId) {
		userClient.delete(userId);
	}
}
