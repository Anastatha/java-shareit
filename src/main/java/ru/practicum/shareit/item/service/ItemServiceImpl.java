package ru.practicum.shareit.item.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDetailsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final BookingRepository bookingRepository;
	private final CommentRepository commentRepository;
	private final ItemRequestRepository itemRequestRepository;

	public ItemServiceImpl(
		ItemRepository itemRepository,
		UserRepository userRepository,
		BookingRepository bookingRepository,
		CommentRepository commentRepository,
		ItemRequestRepository itemRequestRepository
	) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.bookingRepository = bookingRepository;
		this.commentRepository = commentRepository;
		this.itemRequestRepository = itemRequestRepository;
	}

	@Override
	@Transactional
	public ItemDto create(Long userId, ItemCreateDto itemDto) {
		User owner = getUser(userId);
		validateText(itemDto.getName(), "Item name must not be blank");
		validateText(itemDto.getDescription(), "Item description must not be blank");

		Item item = new Item();
		item.setName(itemDto.getName());
		item.setDescription(itemDto.getDescription());
		item.setAvailable(itemDto.getAvailable());
		item.setOwner(owner);
		if (itemDto.getRequestId() != null) {
			ItemRequest request = itemRequestRepository.findById(itemDto.getRequestId())
				.orElseThrow(() -> new NoSuchElementException("Item request not found"));
			item.setRequest(request);
		}
		Item saved = itemRepository.save(item);
		return ItemMapper.toItemDto(saved);
	}

	@Override
	@Transactional
	public ItemDto update(Long userId, Long itemId, ItemUpdateDto itemDto) {
		Item item = getItem(itemId);
		Long ownerId = item.getOwner() != null ? item.getOwner().getId() : null;
		if (ownerId == null || !ownerId.equals(userId)) {
			throw new ForbiddenException("Only owner can update item");
		}

		if (itemDto.getName() != null) {
			validateText(itemDto.getName(), "Item name must not be blank");
			item.setName(itemDto.getName());
		}
		if (itemDto.getDescription() != null) {
			validateText(itemDto.getDescription(), "Item description must not be blank");
			item.setDescription(itemDto.getDescription());
		}
		if (itemDto.getAvailable() != null) {
			item.setAvailable(itemDto.getAvailable());
		}
		Item saved = itemRepository.save(item);
		return ItemMapper.toItemDto(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public ItemDetailsDto getById(Long itemId) {
		Item item = getItem(itemId);
		ItemDetailsDto dto = ItemMapper.toItemDetailsDto(item);
		dto.setComments(getComments(itemId));
		return dto;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDto> getAll(Long userId) {
		getUser(userId);
		return itemRepository.findAllByOwner_Id(userId).stream()
			.map(this::toItemDtoWithBookingDates)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ItemDto> search(String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		return itemRepository.search(text).stream()
			.map(ItemMapper::toItemDto)
			.toList();
	}

	@Override
	@Transactional
	public CommentDto addComment(Long userId, Long itemId, CommentCreateDto commentDto) {
		User author = getUser(userId);
		Item item = getItem(itemId);
		boolean hasFinishedApprovedBooking = bookingRepository.existsByBooker_IdAndItem_IdAndEndBeforeAndStatus(
			userId,
			itemId,
			LocalDateTime.now(),
			BookingStatus.APPROVED
		);
		if (!hasFinishedApprovedBooking) {
			throw new IllegalArgumentException("User has no finished approved booking for this item");
		}

		Comment comment = new Comment();
		comment.setText(commentDto.getText());
		comment.setItem(item);
		comment.setAuthor(author);
		comment.setCreated(LocalDateTime.now());
		return ItemMapper.toCommentDto(commentRepository.save(comment));
	}

	private ItemDto toItemDtoWithBookingDates(Item item) {
		ItemDto dto = ItemMapper.toItemDto(item);
		List<Booking> bookings = bookingRepository.findByItem_IdOrderByStartDesc(item.getId());
		dto.setLastBooking(bookings.stream()
			.filter(booking -> booking.getStart().isBefore(LocalDateTime.now()) && booking.getStatus() == BookingStatus.APPROVED)
			.max(Comparator.comparing(Booking::getStart))
			.map(Booking::getStart)
			.orElse(null));
		dto.setNextBooking(bookings.stream()
			.filter(booking -> booking.getStart().isAfter(LocalDateTime.now()) && booking.getStatus() == BookingStatus.APPROVED)
			.min(Comparator.comparing(Booking::getStart))
			.map(Booking::getStart)
			.orElse(null));
		dto.setComments(getComments(item.getId()));
		return dto;
	}

	private List<CommentDto> getComments(Long itemId) {
		return commentRepository.findAllByItem_IdOrderByCreatedDesc(itemId).stream()
			.map(ItemMapper::toCommentDto)
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

	private void validateText(String value, String message) {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(message);
		}
	}
}
