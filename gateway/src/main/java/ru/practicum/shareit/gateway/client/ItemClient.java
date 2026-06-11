package ru.practicum.shareit.gateway.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDetailsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Component
public class ItemClient extends BaseClient {

	public ItemClient(
			RestTemplate restTemplate,
			ObjectMapper objectMapper,
			@Value("${shareit.server-url}") String serverUrl) {
		super(restTemplate, objectMapper, serverUrl);
	}

	public ItemDto create(Long userId, ItemCreateDto itemDto) {
		return post("/items", itemDto, jsonHeaders(userId), ItemDto.class);
	}

	public ItemDto update(Long userId, Long itemId, ItemUpdateDto itemDto) {
		return patch("/items/" + itemId, itemDto, jsonHeaders(userId), ItemDto.class);
	}

	public ItemDetailsDto getById(Long itemId) {
		return get("/items/" + itemId, jsonHeaders(), ItemDetailsDto.class);
	}

	public List<ItemDto> getAll(Long userId) {
		return get("/items", jsonHeaders(userId), new ParameterizedTypeReference<List<ItemDto>>() {
		});
	}

	public List<ItemDto> search(String text) {
		String path = UriComponentsBuilder.fromPath("/items/search")
				.queryParam("text", text)
				.build()
				.toUriString();
		return get(path, jsonHeaders(), new ParameterizedTypeReference<List<ItemDto>>() {
		});
	}

	public CommentDto addComment(Long userId, Long itemId, CommentCreateDto commentDto) {
		return post("/items/" + itemId + "/comment", commentDto, jsonHeaders(userId), CommentDto.class);
	}
}
