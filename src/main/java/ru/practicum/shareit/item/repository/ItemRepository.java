package ru.practicum.shareit.item.repository;

import java.util.List;
import java.util.Optional;
import ru.practicum.shareit.item.Item;

public interface ItemRepository {

	Item save(Item item);

	Optional<Item> findById(Long itemId);

	List<Item> findAll();

	List<Item> findAllByOwnerId(Long ownerId);

	List<Item> search(String text);
}
