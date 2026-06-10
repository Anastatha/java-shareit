package ru.practicum.shareit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@WebMvcTest(controllers = {
        UserController.class,
        ItemController.class,
        BookingController.class,
        ItemRequestController.class
})
@AutoConfigureMockMvc(addFilters = false)
class ServerControllersMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemRequestService itemRequestService;

    @Test
    void usersEndpointsReturnExpectedJson() throws Exception {
        when(userService.create(any())).thenReturn(new UserDto(1L, "Alice", "alice@example.com"));
        when(userService.getAll()).thenReturn(List.of(new UserDto(1L, "Alice", "alice@example.com")));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Alice\",\"email\":\"alice@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("alice@example.com"));
    }

    @Test
    void itemsEndpointsReturnExpectedJson() throws Exception {
        when(itemService.create(eq(1L), any()))
                .thenReturn(new ItemDto(10L, "Drill", "Cordless", true, null, null, null, List.of()));
        when(itemService.getAll(1L))
                .thenReturn(List.of(new ItemDto(10L, "Drill", "Cordless", true, null, null, null, List.of())));

        mockMvc.perform(post("/items")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Drill\",\"description\":\"Cordless\",\"available\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Drill"));

        mockMvc.perform(get("/items")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Cordless"));
    }

    @Test
    void bookingsEndpointsReturnExpectedJson() throws Exception {
        when(bookingService.create(eq(1L), any())).thenReturn(new BookingDto(
                7L,
                LocalDateTime.of(2026, 6, 20, 10, 0),
                LocalDateTime.of(2026, 6, 21, 10, 0),
                null,
                null,
                BookingStatus.APPROVED));

        mockMvc.perform(post("/bookings")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"itemId\":1,\"start\":\"2026-06-20T10:00:00\",\"end\":\"2026-06-21T10:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void requestsEndpointsReturnExpectedJson() throws Exception {
        when(itemRequestService.create(eq(1L), any()))
                .thenReturn(new ItemRequestDto(5L, "Need drill", 1L, LocalDateTime.now(), List.of()));
        when(itemRequestService.getAllByRequestor(1L))
                .thenReturn(List.of(new ItemRequestDto(5L, "Need drill", 1L, LocalDateTime.now(), List.of())));

        mockMvc.perform(post("/requests")
                .header("X-Sharer-User-Id", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"description\":\"Need drill\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Need drill"));

        mockMvc.perform(get("/requests")
                .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Need drill"));
    }
}
