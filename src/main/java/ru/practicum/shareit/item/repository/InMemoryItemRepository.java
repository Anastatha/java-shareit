package ru.practicum.shareit.item.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.Item;

@Repository
public class InMemoryItemRepository implements ItemRepository {

	private final Map<Long, Item> items = new ConcurrentHashMap<>();
	private final AtomicLong idSequence = new AtomicLong(0);

	@Override
	public Item save(Item item) {
		Item copy = new Item(
			item.getId(),
			item.getName(),
			item.getDescription(),
			item.isAvailable(),
			item.getOwner(),
			item.getRequest()
		);
		if (copy.getId() == null) {
			copy.setId(idSequence.incrementAndGet());
		}
		items.put(copy.getId(), copy);
		return copy;
	}

	@Override
	public Optional<Item> findById(Long itemId) {
		return Optional.ofNullable(items.get(itemId));
	}

	@Override
	public List<Item> findAll() {
		return items.values().stream()
			.sorted(Comparator.comparing(Item::getId))
			.map(this::copyOf)
			.toList();
	}

	@Override
	public List<Item> findAllByOwnerId(Long ownerId) {
		return items.values().stream()
			.filter(item -> item.getOwner() != null && ownerId != null && ownerId.equals(item.getOwner().getId()))
			.sorted(Comparator.comparing(Item::getId))
			.map(this::copyOf)
			.toList();
	}

	@Override
	public List<Item> search(String text) {
		if (text == null || text.isBlank()) {
			return List.of();
		}
		String lowerText = text.toLowerCase(Locale.ROOT);
		return items.values().stream()
			.filter(Item::isAvailable)
			.filter(item -> matches(item.getName(), lowerText) || matches(item.getDescription(), lowerText))
			.sorted(Comparator.comparing(Item::getId))
			.map(this::copyOf)
			.toList();
	}

	private boolean matches(String source, String text) {
		return source != null && source.toLowerCase(Locale.ROOT).contains(text);
	}

	private Item copyOf(Item item) {
		return new Item(item.getId(), item.getName(), item.getDescription(), item.isAvailable(), item.getOwner(), item.getRequest());
	}
}
