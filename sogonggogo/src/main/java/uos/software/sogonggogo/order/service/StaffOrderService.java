package uos.software.sogonggogo.order.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.inventory.domain.InventoryItem;
import uos.software.sogonggogo.inventory.domain.InventoryStatus;
import uos.software.sogonggogo.inventory.service.InventoryService;
import uos.software.sogonggogo.order.domain.Order;
import uos.software.sogonggogo.order.domain.OrderItemOption;
import uos.software.sogonggogo.order.domain.OrderStatus;
import uos.software.sogonggogo.order.dto.OrderResponse;
import uos.software.sogonggogo.order.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class StaffOrderService {

	private final OrderRepository orderRepository;
	private final InventoryService inventoryService;

	@Transactional(readOnly = true)
	public List<OrderResponse> listByStatus(OrderStatus status) {
		List<Order> orders = (status == null) ? orderRepository.findAll() : orderRepository.findByStatusOrderByCreatedAtAsc(status);
		return orders.stream().map(OrderResponse::from).toList();
	}

	@Transactional
	public OrderResponse approve(Long orderId) {
		Order order = findOrder(orderId);
		requireStatus(order, OrderStatus.PENDING);
		order.setStatus(OrderStatus.APPROVED);
		return OrderResponse.from(order);
	}

	@Transactional
	public OrderResponse reject(Long orderId) {
		Order order = findOrder(orderId);
		requireStatus(order, OrderStatus.PENDING);
		order.setStatus(OrderStatus.REJECTED);
		return OrderResponse.from(order);
	}

	@Transactional
	public OrderResponse startCooking(Long orderId) {
		Order order = findOrder(orderId);
		requireStatus(order, OrderStatus.APPROVED);
		consumeInventory(order);
		order.setStatus(OrderStatus.COOKING);
		return OrderResponse.from(order);
	}

	@Transactional
	public OrderResponse readyForDelivery(Long orderId) {
		Order order = findOrder(orderId);
		requireStatus(order, OrderStatus.COOKING);
		order.setStatus(OrderStatus.READY_FOR_DELIVERY);
		return OrderResponse.from(order);
	}

	@Transactional
	public OrderResponse startDelivery(Long orderId) {
		Order order = findOrder(orderId);
		requireStatus(order, OrderStatus.READY_FOR_DELIVERY);
		order.setStatus(OrderStatus.IN_DELIVERY);
		return OrderResponse.from(order);
	}

	@Transactional
	public OrderResponse complete(Long orderId) {
		Order order = findOrder(orderId);
		requireStatus(order, OrderStatus.IN_DELIVERY);
		order.setStatus(OrderStatus.COMPLETED);
		return OrderResponse.from(order);
	}

	private Order findOrder(Long orderId) {
		return orderRepository.findById(orderId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."));
	}

	private void requireStatus(Order order, OrderStatus expected) {
		if (order.getStatus() != expected) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상태 전이가 올바르지 않습니다. 현재 상태: " + order.getStatus());
		}
	}

	private void consumeInventory(Order order) {
		order.getItems().forEach(item -> item.getOptions().forEach(this::consumeOption));
	}

	private void consumeOption(OrderItemOption option) {
		if (option.getQuantity() == null || option.getQuantity() <= 0) {
			return; // 수량 0 이하는 차감 없음
		}
		InventoryItem inventory = inventoryService.getByNameOrThrow(option.getName());
		if (inventory.getStatus() != InventoryStatus.ON_SALE) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "판매중이 아닌 재고: " + inventory.getName());
		}
		if (inventory.getStock() < option.getQuantity()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "재고 부족: " + inventory.getName());
		}
		inventory.decreaseStock(option.getQuantity());
	}
}
