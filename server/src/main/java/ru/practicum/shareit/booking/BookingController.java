package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constants.Headers;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

	private final BookingService bookingService;

	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	public BookingDto create(
		@RequestHeader(Headers.USER_ID) Long userId,
		@Valid @RequestBody BookingCreateDto bookingDto
	) {
		return bookingService.create(userId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	public BookingDto approve(
		@RequestHeader(Headers.USER_ID) Long userId,
		@PathVariable Long bookingId,
		@RequestParam boolean approved
	) {
		return bookingService.approve(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public BookingDto getById(
		@RequestHeader(Headers.USER_ID) Long userId,
		@PathVariable Long bookingId
	) {
		return bookingService.getById(userId, bookingId);
	}

	@GetMapping
	public List<BookingDto> getAll(
		@RequestHeader(Headers.USER_ID) Long userId,
		@RequestParam(defaultValue = "ALL") BookingState state
	) {
		return bookingService.getAll(userId, state);
	}

	@GetMapping("/owner")
	public List<BookingDto> getAllByOwner(
		@RequestHeader(Headers.USER_ID) Long userId,
		@RequestParam(defaultValue = "ALL") BookingState state
	) {
		return bookingService.getAllByOwner(userId, state);
	}
}
