package ru.practicum.shareit.booking.service;

import java.util.List;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {

	BookingDto create(Long userId, BookingCreateDto bookingDto);

	BookingDto approve(Long userId, Long bookingId, boolean approved);

	BookingDto getById(Long userId, Long bookingId);

	List<BookingDto> getAll(Long userId, BookingState state);

	List<BookingDto> getAllByOwner(Long userId, BookingState state);
}
