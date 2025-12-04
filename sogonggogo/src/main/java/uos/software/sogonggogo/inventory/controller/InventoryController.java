package uos.software.sogonggogo.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.inventory.domain.InventoryStatus;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.CreateRequest;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.InventoryResponse;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.UpdateStatusRequest;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.UpdateStockRequest;
import uos.software.sogonggogo.inventory.service.InventoryService;

@RestController
@RequestMapping("/api/staff/inventory")
@Validated
@RequiredArgsConstructor
public class InventoryController {

	private final InventoryService inventoryService;

	@GetMapping
	public List<InventoryResponse> list(@RequestParam(name = "status", required = false) InventoryStatus status) {
		return inventoryService.list(status);
	}

	@PostMapping
	public ResponseEntity<InventoryResponse> create(@Valid @RequestBody CreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.create(request));
	}

	@PatchMapping("/{id}/status")
	public InventoryResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
		return inventoryService.updateStatus(id, request);
	}

	@PatchMapping("/{id}/stock")
	public InventoryResponse updateStock(@PathVariable Long id, @Valid @RequestBody UpdateStockRequest request) {
		return inventoryService.updateStock(id, request);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		inventoryService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
