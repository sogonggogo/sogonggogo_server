package uos.software.sogonggogo.order.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record OrderCreateRequest(
		@Valid @NotNull Customer customer,
		@Valid @NotNull List<OrderItemRequest> orderItems,
		@Valid @NotNull DeliveryInfo deliveryInfo,
		@Valid @NotNull Pricing pricing,
		@Valid @NotNull Metadata metadata
) {

	public record Customer(
			@NotBlank String email,
			@NotBlank String name,
			@NotBlank String phone,
			boolean isRegularCustomer
	) {
	}

	public record OrderItemRequest(
			@NotNull Long menuId,
			@NotBlank String menuName,
			@NotBlank String style,
			@NotNull @Positive Integer quantity,
			@Valid @NotNull List<SelectedItem> selectedItems,
			@NotNull @Positive BigDecimal unitPrice,
			@NotNull @Positive BigDecimal totalPrice
	) {
	}

	public record SelectedItem(
			@NotBlank String name,
			@NotNull @PositiveOrZero Integer quantity,
			@NotNull @Positive BigDecimal unitPrice,
			@NotNull @PositiveOrZero Integer defaultQuantity,
			@NotNull BigDecimal additionalPrice
	) {
	}

	public record DeliveryInfo(
			@NotBlank String address,
			@NotNull LocalDate date,
			@NotBlank String time,
			@NotBlank String cardNumber
	) {
	}

	public record Pricing(
			@NotNull BigDecimal subtotal,
			@NotNull BigDecimal discount,
			@NotNull BigDecimal total
	) {
	}

	public record Metadata(
			@NotNull OffsetDateTime orderDate,
			@NotBlank String clientOrderId
	) {
	}
}
