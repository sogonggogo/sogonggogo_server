package uos.software.sogonggogo.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
		@Size(min = 1, message = "name cannot be empty")
		String name,

		@Size(min = 1, message = "address cannot be empty")
		String address,

		@Pattern(regexp = "^[0-9\\-]{7,20}$", message = "phone must be digits or '-' only")
		String phone,

		@Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
		String password,

		@Pattern(regexp = "\\d{16}", message = "creditCardNumber must be 16 digits")
		String creditCardNumber
) {

	public boolean hasNoChanges() {
		return name == null && address == null && phone == null && password == null && creditCardNumber == null;
	}
}
