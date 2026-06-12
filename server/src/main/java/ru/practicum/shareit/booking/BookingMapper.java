package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@UtilityClass
public class BookingMapper {

	public static BookingDto toDto(Booking booking) {
		if (booking == null) {
			return null;
		}
		return new BookingDto(
			booking.getId(),
			booking.getStart(),
			booking.getEnd(),
			ItemMapper.toItemDto(booking.getItem()),
			UserMapper.toUserDto(booking.getBooker()),
			booking.getStatus()
		);
	}
}
