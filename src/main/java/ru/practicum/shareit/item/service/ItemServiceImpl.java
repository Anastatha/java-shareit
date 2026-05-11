package ru.practicum.shareit.item.service;

import java.util.NoSuchElementException;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	@Override
	public ItemDto create(Long userId, ItemCreateDto itemDto) {
		User owner = getUser(userId);
		Item item = new Item();
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setAvailable(itemDto.getAvailable());
		if (itemDto.getRequestId() != null) {
			item.setRequest(new ru.practicum.shareit.request.ItemRequest(itemDto.getRequestId(), null, null, null));
		}
		item.setOwner(owner);
		Item saved = itemRepository.save(item);
		return ItemMapper.toItemDto(saved);
	}

	@Override
	public ItemDto update(Long userId, Long itemId, ItemUpdateDto itemDto) {
		Item item = getItem(itemId);
		Long ownerId = item.getOwner() != null ? item.getOwner().getId() : null;
		if (ownerId == null || !ownerId.equals(userId)) {
			throw new ForbiddenException("Only owner can update item");
		}

		if (itemDto.getName() != null) {
			item.setName(itemDto.getName());
		}
		if (itemDto.getDescription() != null) {
			item.setDescription(itemDto.getDescription());
		}
		if (itemDto.getAvailable() != null) {
			item.setAvailable(itemDto.getAvailable());
		}
		Item saved = itemRepository.save(item);
		return ItemMapper.toItemDto(saved);
	}

	@Override
	public ItemDto getById(Long itemId) {
		return ItemMapper.toItemDto(getItem(itemId));
	}

	@Override
	public List<ItemDto> getAll(Long userId) {
		getUser(userId);
		return itemRepository.findAllByOwnerId(userId).stream()
			.map(ItemMapper::toItemDto)
			.toList();
	}

	@Override
	public List<ItemDto> search(String text) {
		return itemRepository.search(text).stream()
			.map(ItemMapper::toItemDto)
			.toList();
	}

	private User getUser(Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User id is required");
		}
		return userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("User not found"));
	}

	private Item getItem(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new NoSuchElementException("Item not found"));
	}
}
