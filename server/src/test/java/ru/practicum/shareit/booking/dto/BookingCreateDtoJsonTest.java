package ru.practicum.shareit.booking.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class BookingCreateDtoJsonTest {

    @Autowired
    private JacksonTester<BookingCreateDto> json;

    @Test
    void shouldSerializeAndDeserializeDates() throws Exception {
        BookingCreateDto dto = new BookingCreateDto(1L, LocalDateTime.of(2026, 6, 20, 10, 0),
                LocalDateTime.of(2026, 6, 21, 10, 0));

        String content = json.write(dto).getJson();

        assertThat(content).contains("\"itemId\":1");
        assertThat(content).contains("2026-06-20T10:00:00");

        BookingCreateDto parsed = json
                .parseObject("{\"itemId\":1,\"start\":\"2026-06-20T10:00:00\",\"end\":\"2026-06-21T10:00:00\"}");

        assertThat(parsed.getItemId()).isEqualTo(1L);
        assertThat(parsed.getStart()).isEqualTo(LocalDateTime.of(2026, 6, 20, 10, 0));
        assertThat(parsed.getEnd()).isEqualTo(LocalDateTime.of(2026, 6, 21, 10, 0));
    }
}
