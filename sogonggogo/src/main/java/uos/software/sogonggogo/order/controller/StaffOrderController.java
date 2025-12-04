package uos.software.sogonggogo.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.order.domain.OrderStatus;
import uos.software.sogonggogo.order.dto.OrderResponse;
import uos.software.sogonggogo.order.service.StaffOrderService;

@RestController
@RequestMapping("/api/staff/orders")
@Validated
@RequiredArgsConstructor
public class StaffOrderController {

	private final StaffOrderService staffOrderService;

	@GetMapping
	public List<OrderResponse> list(@RequestParam(name = "status", required = false) OrderStatus status) {
		return staffOrderService.listByStatus(status);
	}

	@PostMapping("/{id}/approve")
	public ResponseEntity<OrderResponse> approve(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(staffOrderService.approve(id));
	}

	@PostMapping("/{id}/reject")
	public ResponseEntity<OrderResponse> reject(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(staffOrderService.reject(id));
	}

	@PostMapping("/{id}/cook")
	public ResponseEntity<OrderResponse> cook(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(staffOrderService.startCooking(id));
	}

	@PostMapping("/{id}/ready")
	public ResponseEntity<OrderResponse> ready(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(staffOrderService.readyForDelivery(id));
	}

	@PostMapping("/{id}/start-delivery")
	public ResponseEntity<OrderResponse> startDelivery(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(staffOrderService.startDelivery(id));
	}

	@PostMapping("/{id}/complete")
	public ResponseEntity<OrderResponse> complete(@PathVariable Long id) {
		return ResponseEntity.status(HttpStatus.OK).body(staffOrderService.complete(id));
	}
}
