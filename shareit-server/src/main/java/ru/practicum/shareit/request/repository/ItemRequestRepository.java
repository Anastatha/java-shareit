package ru.practicum.shareit.request.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

	@Query("select r from ItemRequest r where r.requestor.id = ?1 order by r.created desc")
	List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Long requestorId);

	@Query("select r from ItemRequest r where r.requestor.id <> ?1 order by r.created desc")
	List<ItemRequest> findAllByRequestorIdNotOrderByCreatedDesc(Long requestorId);
}
