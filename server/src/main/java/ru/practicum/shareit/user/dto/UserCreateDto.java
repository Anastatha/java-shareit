package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

	@NotBlank(message = "User name is required")
	private String name;

	@NotBlank(message = "User email is required")
	@Email(message = "User email is invalid")
	private String email;
}
