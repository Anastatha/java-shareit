package ru.practicum.shareit.booking.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateDto {

	@NotNull(message = "Item id is required")
	private Long itemId;

	@NotNull(message = "Booking start is required")
	private LocalDateTime start;

	@NotNull(message = "Booking end is required")
	private LocalDateTime end;
}
