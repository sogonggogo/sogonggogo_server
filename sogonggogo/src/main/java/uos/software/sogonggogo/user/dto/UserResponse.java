package uos.software.sogonggogo.user.dto;

import uos.software.sogonggogo.user.domain.User;

public record UserResponse(
		Long id,
		String name,
		String address,
		String phoneNumber,
		String email,
		String creditCardMasked
) {

	public static UserResponse from(User user) {
		return new UserResponse(
				user.getId(),
				user.getName(),
				user.getAddress(),
				user.getPhoneNumber(),
				user.getEmail(),
				maskCreditCard(user.getCreditCardNumber()));
	}

	private static String maskCreditCard(String creditCardNumber) {
		if (creditCardNumber == null || creditCardNumber.length() < 4) {
			return null;
		}
		String last4 = creditCardNumber.substring(creditCardNumber.length() - 4);
		return "************" + last4;
	}
}
