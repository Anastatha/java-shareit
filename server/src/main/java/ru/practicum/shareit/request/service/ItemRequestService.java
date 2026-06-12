package ru.practicum.shareit.request.service;

import java.util.List;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface ItemRequestService {

	ItemRequestDto create(Long userId, ItemRequestCreateDto requestDto);

	List<ItemRequestDto> getAllByRequestor(Long userId);

	List<ItemRequestDto> getAllOtherRequests(Long userId);

	ItemRequestDto getById(Long userId, Long requestId);
}
