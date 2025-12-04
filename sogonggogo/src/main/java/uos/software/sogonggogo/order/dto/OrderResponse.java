package uos.software.sogonggogo.order.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import uos.software.sogonggogo.order.domain.Order;
import uos.software.sogonggogo.order.domain.OrderItem;
import uos.software.sogonggogo.order.domain.OrderItemOption;
import uos.software.sogonggogo.order.domain.OrderStatus;

public record OrderResponse(
		Long id,
		String status,
		Customer customer,
		DeliveryInfo deliveryInfo,
		Pricing pricing,
		Metadata metadata,
		List<OrderItemResponse> orderItems
) {

	public static OrderResponse from(Order order) {
		return new OrderResponse(
				order.getId(),
				order.getStatus().name(),
				new Customer(
						order.getCustomerEmail(),
						order.getCustomerName(),
						order.getCustomerPhone(),
						order.isRegularCustomer()),
				new DeliveryInfo(
						order.getDeliveryAddress(),
						order.getDeliveryDate(),
						order.getDeliveryTime(),
						order.getCardNumber()),
				new Pricing(order.getSubtotal(), order.getDiscount(), order.getTotal()),
				new Metadata(order.getOrderDate(), order.getClientOrderId()),
				order.getItems().stream().map(OrderResponse::fromItem).toList()
		);
	}

	private static OrderItemResponse fromItem(OrderItem item) {
		return new OrderItemResponse(
				item.getId(),
				item.getMenuId(),
				item.getMenuName(),
				item.getStyle(),
				item.getQuantity(),
				item.getUnitPrice(),
				item.getTotalPrice(),
				item.getOptions().stream().map(OrderResponse::fromOption).toList()
		);
	}

	private static SelectedItemResponse fromOption(OrderItemOption option) {
		return new SelectedItemResponse(
				option.getName(),
				option.getQuantity(),
				option.getUnitPrice(),
				option.getDefaultQuantity(),
				option.getAdditionalPrice()
		);
	}

	public record Customer(
			String email,
			String name,
			String phone,
			boolean isRegularCustomer
	) {
	}

	public record DeliveryInfo(
			String address,
			LocalDate date,
			String time,
			String cardNumber
	) {
	}

	public record Pricing(
			BigDecimal subtotal,
			BigDecimal discount,
			BigDecimal total
	) {
	}

	public record Metadata(
			LocalDateTime orderDate,
			String clientOrderId
	) {
	}

	public record OrderItemResponse(
			Long id,
			Long menuId,
			String menuName,
			String style,
			Integer quantity,
			BigDecimal unitPrice,
			BigDecimal totalPrice,
			List<SelectedItemResponse> selectedItems
	) {
	}

	public record SelectedItemResponse(
			String name,
			Integer quantity,
			BigDecimal unitPrice,
			Integer defaultQuantity,
			BigDecimal additionalPrice
	) {
	}
}
