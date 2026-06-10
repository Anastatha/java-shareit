package ru.practicum.shareit.gateway.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Component
public class RequestClient extends BaseClient {

	public RequestClient(
			org.springframework.web.client.RestTemplate restTemplate,
			com.fasterxml.jackson.databind.ObjectMapper objectMapper,
			@org.springframework.beans.factory.annotation.Value("${shareit.server-url}") String serverUrl
	) {
		super(restTemplate, objectMapper, serverUrl);
	}

	public ItemRequestDto create(Long userId, ItemRequestCreateDto requestDto) {
		return post("/requests", requestDto, jsonHeaders(userId), ItemRequestDto.class);
	}

	public List<ItemRequestDto> getAllByRequestor(Long userId) {
		return get("/requests", jsonHeaders(userId), new ParameterizedTypeReference<List<ItemRequestDto>>() {
		});
	}

	public List<ItemRequestDto> getAllOtherRequests(Long userId) {
		return get("/requests/all", jsonHeaders(userId), new ParameterizedTypeReference<List<ItemRequestDto>>() {
		});
	}

	public ItemRequestDto getById(Long userId, Long requestId) {
		return get("/requests/" + requestId, jsonHeaders(userId), ItemRequestDto.class);
	}
}
