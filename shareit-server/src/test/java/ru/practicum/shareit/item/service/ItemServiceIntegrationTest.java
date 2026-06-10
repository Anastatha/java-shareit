package ru.practicum.shareit.item.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void createAndSearchItemWithRequestId() {
        User owner = userRepository.save(new User(null, "Owner", "owner@example.com"));
        User requestor = userRepository.save(new User(null, "Requestor", "requestor@example.com"));
        ItemRequest request = itemRequestRepository
                .save(new ItemRequest(null, "Need drill", requestor, LocalDateTime.now()));

        ItemDto created = itemService.create(owner.getId(),
                new ItemCreateDto("Drill", "Cordless drill", true, request.getId()));

        assertThat(created.getId()).isNotNull();
        assertThat(created.getRequestId()).isEqualTo(request.getId());
        assertThat(itemRepository.findAll()).hasSize(1);
        assertThat(itemService.search("drill")).hasSize(1);
        assertThat(itemService.getAll(owner.getId())).hasSize(1);
    }
}
