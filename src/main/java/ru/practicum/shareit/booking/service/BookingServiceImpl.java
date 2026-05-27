package ru.practicum.shareit.booking.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	public BookingServiceImpl(
		BookingRepository bookingRepository,
		ItemRepository itemRepository,
		UserRepository userRepository
	) {
		this.bookingRepository = bookingRepository;
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	@Override
	@Transactional
	public BookingDto create(Long userId, BookingCreateDto bookingDto) {
		User booker = getUser(userId);
		Item item = getItem(bookingDto.getItemId());
		validateDates(bookingDto.getStart(), bookingDto.getEnd());
		if (!item.isAvailable()) {
			throw new IllegalArgumentException("Item is not available for booking");
		}
		if (item.getOwner() != null && item.getOwner().getId().equals(userId)) {
			throw new ForbiddenException("Owner cannot book own item");
		}

		Booking booking = new Booking();
		booking.setStart(bookingDto.getStart());
		booking.setEnd(bookingDto.getEnd());
		booking.setItem(item);
		booking.setBooker(booker);
		booking.setStatus(BookingStatus.WAITING);
		return toDto(bookingRepository.save(booking));
	}

	@Override
	@Transactional
	public BookingDto approve(Long userId, Long bookingId, boolean approved) {
		Booking booking = getBooking(bookingId);
		Long ownerId = booking.getItem().getOwner() != null ? booking.getItem().getOwner().getId() : null;
		if (ownerId == null || !ownerId.equals(userId)) {
			throw new ForbiddenException("Only owner can approve booking");
		}
		if (booking.getStatus() != BookingStatus.WAITING) {
			throw new ConflictException("Booking already processed");
		}
		booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
		return toDto(bookingRepository.save(booking));
	}

	@Override
	@Transactional(readOnly = true)
	public BookingDto getById(Long userId, Long bookingId) {
		Booking booking = getBooking(bookingId);
		Long ownerId = booking.getItem().getOwner() != null ? booking.getItem().getOwner().getId() : null;
		Long bookerId = booking.getBooker() != null ? booking.getBooker().getId() : null;
		if (!userId.equals(ownerId) && !userId.equals(bookerId)) {
			throw new ForbiddenException("Access denied");
		}
		return toDto(booking);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookingDto> getAll(Long userId, BookingState state) {
		getUser(userId);
		return filterBookings(bookingRepository.findByBooker_IdOrderByStartDesc(userId), state);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookingDto> getAllByOwner(Long userId, BookingState state) {
		getUser(userId);
		return filterBookings(bookingRepository.findByItem_Owner_IdOrderByStartDesc(userId), state);
	}

	private List<BookingDto> filterBookings(List<Booking> bookings, BookingState state) {
		LocalDateTime now = LocalDateTime.now();
		return bookings.stream()
			.filter(booking -> matchesState(booking, state, now))
			.sorted(Comparator.comparing(Booking::getStart).reversed())
			.map(this::toDto)
			.toList();
	}

	private boolean matchesState(Booking booking, BookingState state, LocalDateTime now) {
		return switch (state) {
			case ALL -> true;
			case CURRENT -> !booking.getStart().isAfter(now) && !booking.getEnd().isBefore(now);
			case PAST -> booking.getEnd().isBefore(now);
			case FUTURE -> booking.getStart().isAfter(now);
			case WAITING -> booking.getStatus() == BookingStatus.WAITING;
			case REJECTED -> booking.getStatus() == BookingStatus.REJECTED;
		};
	}

	private BookingDto toDto(Booking booking) {
		return new BookingDto(
			booking.getId(),
			booking.getStart(),
			booking.getEnd(),
			ItemMapper.toItemDto(booking.getItem()),
			UserMapper.toUserDto(booking.getBooker()),
			booking.getStatus()
		);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new NoSuchElementException("User not found"));
	}

	private Item getItem(Long itemId) {
		return itemRepository.findById(itemId)
			.orElseThrow(() -> new NoSuchElementException("Item not found"));
	}

	private Booking getBooking(Long bookingId) {
		return bookingRepository.findById(bookingId)
			.orElseThrow(() -> new NoSuchElementException("Booking not found"));
	}

	private void validateDates(LocalDateTime start, LocalDateTime end) {
		if (start == null || end == null) {
			throw new IllegalArgumentException("Booking dates are required");
		}
		if (!start.isBefore(end)) {
			throw new IllegalArgumentException("Booking start must be before end");
		}
	}
}
