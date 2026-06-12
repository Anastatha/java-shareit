package ru.practicum.shareit.gateway.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserClient extends BaseClient {

	public UserClient(
			RestTemplate restTemplate,
			ObjectMapper objectMapper,
			@Value("${shareit.server-url}") String serverUrl) {
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
