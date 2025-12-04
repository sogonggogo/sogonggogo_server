package uos.software.sogonggogo.order.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.order.domain.Order;
import uos.software.sogonggogo.order.domain.OrderItem;
import uos.software.sogonggogo.order.domain.OrderItemOption;
import uos.software.sogonggogo.order.domain.OrderStatus;
import uos.software.sogonggogo.order.dto.OrderCreateRequest;
import uos.software.sogonggogo.order.dto.OrderResponse;
import uos.software.sogonggogo.order.repository.OrderRepository;
import uos.software.sogonggogo.user.domain.User;
import uos.software.sogonggogo.user.repository.UserRepository;
import uos.software.sogonggogo.user.session.SessionKeys;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	@Transactional
	public OrderResponse createOrder(OrderCreateRequest request, HttpSession session) {
		User user = findLoggedInUser(session);

		Order order = Order.builder()
				.user(user)
				.customerName(request.customer().name())
				.customerEmail(request.customer().email())
				.customerPhone(request.customer().phone())
				.regularCustomer(request.customer().isRegularCustomer())
				.deliveryAddress(request.deliveryInfo().address())
				.deliveryDate(request.deliveryInfo().date())
				.deliveryTime(request.deliveryInfo().time())
				.cardNumber(request.deliveryInfo().cardNumber())
				.subtotal(request.pricing().subtotal())
				.discount(request.pricing().discount())
				.total(request.pricing().total())
				.orderDate(request.metadata().orderDate().toLocalDateTime())
				.clientOrderId(request.metadata().clientOrderId())
				.status(OrderStatus.PENDING)
				.build();

		request.orderItems().forEach(item -> {
			OrderItem orderItem = OrderItem.builder()
					.menuId(item.menuId())
					.menuName(item.menuName())
					.style(item.style())
					.quantity(item.quantity())
					.unitPrice(item.unitPrice())
					.totalPrice(item.totalPrice())
					.build();

			item.selectedItems().forEach(opt -> {
				OrderItemOption option = OrderItemOption.builder()
						.name(opt.name())
						.quantity(opt.quantity())
						.unitPrice(opt.unitPrice())
						.defaultQuantity(opt.defaultQuantity())
						.additionalPrice(opt.additionalPrice())
						.build();
				orderItem.addOption(option);
			});

			order.addItem(orderItem);
		});

		return OrderResponse.from(orderRepository.save(order));
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> getMyOrders(HttpSession session) {
		User user = findLoggedInUser(session);
		return orderRepository.findByUserOrderByCreatedAtDesc(user).stream()
				.map(OrderResponse::from)
				.toList();
	}

	private User findLoggedInUser(HttpSession session) {
		if (session == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		}
		Object userIdObj = session.getAttribute(SessionKeys.USER_ID);
		if (userIdObj == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		}
		Long userId = (userIdObj instanceof Integer) ? ((Integer) userIdObj).longValue() : (Long) userIdObj;
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
	}
}
