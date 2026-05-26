package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	private Long id;
	private LocalDateTime start;
	private LocalDateTime end;
	private Item item;
	private User booker;
	private BookingStatus status;
}
