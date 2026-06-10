package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

	private final ItemRequestRepository itemRequestRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	public ItemRequestServiceImpl(
			ItemRequestRepository itemRequestRepository,
			ItemRepository itemRepository,
			UserRepository userRepository
	) {
		this.itemRequestRepository = itemRequestRepository;
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public ItemRequestDto create(Long userId, ItemRequestCreateDto requestDto) {
		User requestor = getUser(userId);
		ItemRequest request = new ItemRequest();
		request.setDescription(requestDto.getDescription());
		request.setRequestor(requestor);
		request.setCreated(LocalDateTime.now());
		ItemRequest saved = itemRequestRepository.save(request);
		return ItemRequestMapper.toDto(saved, List.of());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemRequestDto> getAllByRequestor(Long userId) {
		getUser(userId);
		return toDtos(itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemRequestDto> getAllOtherRequests(Long userId) {
		getUser(userId);
		return toDtos(itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(userId));
	}

	@Override
	@Transactional(readOnly = true)
	public ItemRequestDto getById(Long userId, Long requestId) {
		getUser(userId);
		ItemRequest request = itemRequestRepository.findById(requestId)
				.orElseThrow(() -> new NoSuchElementException("Item request not found"));
		return ItemRequestMapper.toDto(request, getItemsByRequestIds(List.of(request.getId())).getOrDefault(request.getId(), List.of()));
	}

	private List<ItemRequestDto> toDtos(List<ItemRequest> requests) {
		if (requests.isEmpty()) {
			return List.of();
		}
		Map<Long, List<ItemShortDto>> itemsByRequestId = getItemsByRequestIds(
				requests.stream().map(ItemRequest::getId).toList());
		return requests.stream()
				.map(request -> ItemRequestMapper.toDto(
						request,
						itemsByRequestId.getOrDefault(request.getId(), List.of())))
				.toList();
	}

	private Map<Long, List<ItemShortDto>> getItemsByRequestIds(List<Long> requestIds) {
		return itemRepository.findAllByRequest_IdIn(requestIds).stream()
				.collect(Collectors.groupingBy(
						item -> item.getRequest().getId(),
						Collectors.mapping(ItemMapper::toItemShortDto, Collectors.toList())));
	}

	private User getUser(Long userId) {
		if (userId == null) {
			throw new IllegalArgumentException("User id is required");
		}
		return userRepository.findById(userId)
				.orElseThrow(() -> new NoSuchElementException("User not found"));
	}
}
