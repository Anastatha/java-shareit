package ru.practicum.shareit.gateway.booking;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.gateway.client.BookingClient;

@RestController
@RequestMapping("/bookings")
public class BookingController {

	private final BookingClient bookingClient;

	public BookingController(BookingClient bookingClient) {
		this.bookingClient = bookingClient;
	}

	@PostMapping
	public BookingDto create(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@Valid @RequestBody BookingCreateDto bookingDto
	) {
		return bookingClient.create(userId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	public BookingDto approve(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long bookingId,
			@RequestParam boolean approved
	) {
		return bookingClient.approve(userId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public BookingDto getById(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@PathVariable Long bookingId
	) {
		return bookingClient.getById(userId, bookingId);
	}

	@GetMapping
	public List<BookingDto> getAll(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(defaultValue = "ALL") BookingState state
	) {
		return bookingClient.getAll(userId, state);
	}

	@GetMapping("/owner")
	public List<BookingDto> getAllByOwner(
			@RequestHeader("X-Sharer-User-Id") Long userId,
			@RequestParam(defaultValue = "ALL") BookingState state
	) {
		return bookingClient.getAllByOwner(userId, state);
	}
}
