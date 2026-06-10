package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

	private final ItemRequestService itemRequestService;

	public ItemRequestController(ItemRequestService itemRequestService) {
		this.itemRequestService = itemRequestService;
	}

	@PostMapping
	public ItemRequestDto create(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@Valid @RequestBody ItemRequestCreateDto requestDto
	) {
		return itemRequestService.create(userId, requestDto);
	}

	@GetMapping
	public List<ItemRequestDto> getAllByRequestor(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return itemRequestService.getAllByRequestor(userId);
	}

	@GetMapping("/all")
	public List<ItemRequestDto> getAllOtherRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return itemRequestService.getAllOtherRequests(userId);
	}

	@GetMapping("/{requestId}")
	public ItemRequestDto getById(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long requestId
	) {
		return itemRequestService.getById(userId, requestId);
	}
}
