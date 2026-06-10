package ru.practicum.shareit.item;

import java.time.LocalDateTime;
import java.util.List;
import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDetailsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.ItemRequest;

@UtilityClass
public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDto dto = new ItemDto();
        fillCommonFields(item, dto);
        dto.setComments(List.of());
        return dto;
    }

    public static ItemDto toItemDto(
        Item item,
        LocalDateTime lastBooking,
        LocalDateTime nextBooking,
        List<CommentDto> comments
    ) {
        if (item == null) {
            return null;
        }
        ItemDto dto = new ItemDto();
        fillCommonFields(item, dto);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments != null ? comments : List.of());
        return dto;
    }

    public static ItemDetailsDto toItemDetailsDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDetailsDto dto = new ItemDetailsDto();
        fillCommonFields(item, dto);
        dto.setLastBooking(null);
        dto.setNextBooking(null);
        dto.setComments(List.of());
        return dto;
    }

    public static ItemDetailsDto toItemDetailsDto(Item item, List<CommentDto> comments) {
        return toItemDetailsDto(item, null, null, comments);
    }

    public static ItemDetailsDto toItemDetailsDto(
        Item item,
        LocalDateTime lastBooking,
        LocalDateTime nextBooking,
        List<CommentDto> comments
    ) {
        if (item == null) {
            return null;
        }
        ItemDetailsDto dto = new ItemDetailsDto();
        fillCommonFields(item, dto);
        dto.setLastBooking(lastBooking);
        dto.setNextBooking(nextBooking);
        dto.setComments(comments != null ? comments : List.of());
        return dto;
    }

    public static CommentDto toCommentDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return new CommentDto(
            comment.getId(),
            comment.getText(),
            comment.getAuthor() != null ? comment.getAuthor().getName() : null,
            comment.getCreated()
        );
    }

    public static ItemShortDto toItemShortDto(Item item) {
        if (item == null) {
            return null;
        }
        return new ItemShortDto(
                item.getId(),
                item.getName(),
                item.getOwner() != null ? item.getOwner().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getRequestId() != null) {
            ItemRequest request = new ItemRequest();
            request.setId(itemDto.getRequestId());
            item.setRequest(request);
        }
        return item;
    }

    private static void fillCommonFields(Item item, ItemDto dto) {
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
    }

    private static void fillCommonFields(Item item, ItemDetailsDto dto) {
        dto.setId(item.getId());
        dto.setName(item.getName());
        dto.setDescription(item.getDescription());
        dto.setAvailable(item.isAvailable());
        dto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);
    }
}
