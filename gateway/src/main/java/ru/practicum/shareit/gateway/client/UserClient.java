package ru.practicum.shareit.gateway.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Component
public class UserClient extends BaseClient {

	public UserClient(
			org.springframework.web.client.RestTemplate restTemplate,
			com.fasterxml.jackson.databind.ObjectMapper objectMapper,
			@org.springframework.beans.factory.annotation.Value("${shareit.server-url}") String serverUrl
	) {
		super(restTemplate, objectMapper, serverUrl);
	}

	public UserDto create(UserCreateDto userDto) {
		return post("/users", userDto, jsonHeaders(), UserDto.class);
	}

	public UserDto update(Long userId, UserUpdateDto userDto) {
		return patch("/users/" + userId, userDto, jsonHeaders(), UserDto.class);
	}

	public UserDto getById(Long userId) {
		return get("/users/" + userId, new HttpHeaders(), UserDto.class);
	}

	public List<UserDto> getAll() {
		return get("/users", new HttpHeaders(), new ParameterizedTypeReference<List<UserDto>>() {
		});
	}

	public void delete(Long userId) {
		delete("/users/" + userId, new HttpHeaders(), Void.class);
	}
}
