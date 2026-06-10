package ru.practicum.shareit.request;

import java.util.List;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@UtilityClass
public class ItemRequestMapper {

	public static ItemRequestDto toDto(ItemRequest request, List<ItemShortDto> items) {
		if (request == null) {
			return null;
		}
		return new ItemRequestDto(
				request.getId(),
				request.getDescription(),
				request.getRequestor() != null ? request.getRequestor().getId() : null,
				request.getCreated(),
				items != null ? items : List.of()
		);
	}

	public static ItemShortDto toShortDto(ru.practicum.shareit.item.Item item) {
		return ItemMapper.toItemShortDto(item);
	}
}
