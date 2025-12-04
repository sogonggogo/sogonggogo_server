package uos.software.sogonggogo.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignupRequest(
		@NotBlank(message = "name is required")
		String name,

		@NotBlank(message = "address is required")
		String address,

		@NotBlank(message = "phoneNumber is required")
		@Pattern(regexp = "^[0-9\\-]{7,20}$", message = "phoneNumber must be digits or '-' only")
		String phoneNumber,

		@NotBlank(message = "email is required")
		@Email(message = "email must be valid")
		String email,

		@NotBlank(message = "password is required")
		@Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
		String password,

		@NotBlank(message = "creditCardNumber is required")
		@Pattern(regexp = "\\d{16}", message = "creditCardNumber must be 16 digits")
		String creditCardNumber
) {
}
