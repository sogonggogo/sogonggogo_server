package uos.software.sogonggogo.user.dto;

import uos.software.sogonggogo.user.domain.User;

public record UserResponse(
		Long id,
		String name,
		String address,
		String phone,
		String email,
		String creditCardNumber,
		boolean isRegularCustomer
) {

	public static UserResponse from(User user) {
		return new UserResponse(
				user.getId(),
				user.getName(),
				user.getAddress(),
				user.getPhone(),
				user.getEmail(),
				user.getCreditCardNumber(),
				user.isRegularCustomer());
	}
}
