package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCreateDto {

	@NotBlank(message = "Item name is required")
	private String name;

	@NotBlank(message = "Item description is required")
	private String description;

	@NotNull(message = "Item availability is required")
	private Boolean available;

	private Long requestId;
}
