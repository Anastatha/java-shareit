package ru.practicum.shareit.gateway.item;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDetailsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@RestController
@RequestMapping("/items")
public class ItemController {

	private final ItemClient itemClient;

	public ItemController(ItemClient itemClient) {
		this.itemClient = itemClient;
	}

	@PostMapping
	public ItemDto create(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@Valid @RequestBody ItemCreateDto itemDto
	) {
		return itemClient.create(userId, itemDto);
	}

	@PatchMapping("/{itemId}")
	public ItemDto update(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long itemId,
			@Valid @RequestBody ItemUpdateDto itemDto
	) {
		return itemClient.update(userId, itemId, itemDto);
	}

	@GetMapping("/{itemId}")
	public ItemDetailsDto getById(@PathVariable Long itemId) {
		return itemClient.getById(itemId);
	}

	@GetMapping
	public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
		return itemClient.getAll(userId);
	}

	@GetMapping("/search")
	public List<ItemDto> search(@RequestParam String text) {
		return itemClient.search(text);
	}

	@PostMapping("/{itemId}/comment")
	public CommentDto addComment(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long itemId,
			@Valid @RequestBody CommentCreateDto commentDto
	) {
		return itemClient.addComment(userId, itemId, commentDto);
	}
}
