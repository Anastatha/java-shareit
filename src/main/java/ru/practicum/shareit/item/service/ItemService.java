package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

public interface ItemService {

	ItemDto create(Long userId, ItemCreateDto itemDto);

	ItemDto update(Long userId, Long itemId, ItemUpdateDto itemDto);

	ItemDto getById(Long itemId);

	List<ItemDto> getAll(Long userId);

	List<ItemDto> search(String text);
}
