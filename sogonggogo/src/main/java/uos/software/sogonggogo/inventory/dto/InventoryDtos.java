package uos.software.sogonggogo.inventory.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import uos.software.sogonggogo.inventory.domain.InventoryItem;
import uos.software.sogonggogo.inventory.domain.InventoryStatus;

public class InventoryDtos {

	private InventoryDtos() {
	}

	public record InventoryResponse(
			Long id,
			String name,
			Integer stock,
			BigDecimal price,
			String status
	) {
		public static InventoryResponse from(InventoryItem item) {
			return new InventoryResponse(item.getId(), item.getName(), item.getStock(), item.getPrice(), item.getStatus().name());
		}
	}

	public record CreateRequest(
			@NotBlank String name,
			@NotNull @Min(0) Integer stock,
			@NotNull @Min(0) BigDecimal price,
			@NotNull InventoryStatus status
	) {
	}

	public record UpdateStatusRequest(
			@NotNull InventoryStatus status
	) {
	}

	public record UpdateStockRequest(
			@NotNull @Min(0) Integer stock
	) {
	}
}
