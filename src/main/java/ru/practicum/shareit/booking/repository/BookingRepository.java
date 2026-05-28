package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByBooker_IdOrderByStartDesc(Long bookerId);

	List<Booking> findByItem_Owner_IdOrderByStartDesc(Long ownerId);

	List<Booking> findByItem_IdOrderByStartDesc(Long itemId);

	List<Booking> findAllByItem_IdInAndStatusOrderByStartDesc(List<Long> itemIds, BookingStatus status);

	boolean existsByBooker_IdAndItem_IdAndEndBeforeAndStatus(
		Long bookerId,
		Long itemId,
		LocalDateTime end,
		BookingStatus status
	);
}
