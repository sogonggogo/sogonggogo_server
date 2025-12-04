package uos.software.sogonggogo.user.dto;

import uos.software.sogonggogo.user.domain.User;

public record UserResponse(
		Long id,
		String name,
		String address,
		String phoneNumber,
		String email,
		String creditCardNumber
) {

	public static UserResponse from(User user) {
		return new UserResponse(
			user.getId(),
			user.getName(),
			user.getAddress(),
			user.getPhoneNumber(),
			user.getEmail(),
			user.getCreditCardNumber());
	}
}
