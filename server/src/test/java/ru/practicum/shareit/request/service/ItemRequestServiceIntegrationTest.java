package ru.practicum.shareit.request.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.repository.UserRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ItemRequestServiceIntegrationTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createAndListRequestsForRequestor() {
        User requestor = userRepository.save(new User(null, "Requestor", "requestor@example.com"));
        User other = userRepository.save(new User(null, "Other", "other@example.com"));

        ItemRequestDto created = itemRequestService.create(requestor.getId(), new ItemRequestCreateDto("Need drill"));

        assertThat(created.getId()).isNotNull();
        assertThat(created.getDescription()).isEqualTo("Need drill");
        assertThat(created.getCreated()).isNotNull();
        assertThat(itemRequestService.getAllByRequestor(requestor.getId())).hasSize(1);
        assertThat(itemRequestService.getAllOtherRequests(requestor.getId())).isEmpty();
        assertThat(itemRequestService.getAllOtherRequests(other.getId())).hasSize(1);
    }
}
