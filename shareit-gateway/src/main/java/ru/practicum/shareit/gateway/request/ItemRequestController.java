package ru.practicum.shareit.gateway.request;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.client.RequestClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping("/requests")
public class ItemRequestController {

	private final RequestClient requestClient;

	public ItemRequestController(RequestClient requestClient) {
		this.requestClient = requestClient;
	}

	@PostMapping
	public ItemRequestDto create(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@Valid @RequestBody ItemRequestCreateDto requestDto
	) {
		return requestClient.create(userId, requestDto);
	}

	@GetMapping
	public List<ItemRequestDto> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return requestClient.getAllByRequestor(userId);
	}

	@GetMapping("/all")
	public List<ItemRequestDto> getAllOtherRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return requestClient.getAllOtherRequests(userId);
	}

	@GetMapping("/{requestId}")
	public ItemRequestDto getById(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long requestId
	) {
		return requestClient.getById(userId, requestId);
	}
}
