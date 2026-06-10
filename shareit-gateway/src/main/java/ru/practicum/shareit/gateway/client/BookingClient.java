package ru.practicum.shareit.gateway.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

@Component
public class BookingClient extends BaseClient {

	public BookingClient(
			org.springframework.web.client.RestTemplate restTemplate,
			com.fasterxml.jackson.databind.ObjectMapper objectMapper,
			@org.springframework.beans.factory.annotation.Value("${shareit.server-url}") String serverUrl
	) {
		super(restTemplate, objectMapper, serverUrl);
	}

	public BookingDto create(Long userId, BookingCreateDto bookingDto) {
		return post("/bookings", bookingDto, jsonHeaders(userId), BookingDto.class);
	}

	public BookingDto approve(Long userId, Long bookingId, boolean approved) {
		return patch("/bookings/" + bookingId + "?approved=" + approved, null, jsonHeaders(userId), BookingDto.class);
	}

	public BookingDto getById(Long userId, Long bookingId) {
		return get("/bookings/" + bookingId, jsonHeaders(userId), BookingDto.class);
	}

	public List<BookingDto> getAll(Long userId, BookingState state) {
		return get("/bookings?state=" + state, jsonHeaders(userId), new ParameterizedTypeReference<List<BookingDto>>() {
		});
	}

	public List<BookingDto> getAllByOwner(Long userId, BookingState state) {
		return get("/bookings/owner?state=" + state, jsonHeaders(userId), new ParameterizedTypeReference<List<BookingDto>>() {
		});
	}
}
