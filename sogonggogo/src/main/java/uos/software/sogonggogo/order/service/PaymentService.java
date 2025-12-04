package uos.software.sogonggogo.order.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import lombok.Value;

@Service
public class PaymentService {

	/**
	 * 결제 시스템 연동 대신 항상 성공을 반환하는 더미 구현.
	 */
	public PaymentResult requestPayment(String cardNumber, BigDecimal amount) {
		// TODO: 실제 결제 연동 시 카드번호/금액 검증 및 PG 호출 추가
		return PaymentResult.success(cardNumber, amount);
	}

	@Value
	public static class PaymentResult {
		boolean success;
		String message;
		String maskedCard;
		BigDecimal amount;

		public static PaymentResult success(String cardNumber, BigDecimal amount) {
			String masked = cardNumber == null ? null : mask(cardNumber);
			return new PaymentResult(true, "OK", masked, amount);
		}

		private static String mask(String cardNumber) {
			if (cardNumber.length() <= 4) {
				return cardNumber;
			}
			String last4 = cardNumber.substring(cardNumber.length() - 4);
			return "****-****-****-" + last4;
		}
	}
}
