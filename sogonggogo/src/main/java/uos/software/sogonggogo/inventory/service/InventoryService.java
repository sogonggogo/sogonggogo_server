package uos.software.sogonggogo.inventory.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.inventory.domain.InventoryItem;
import uos.software.sogonggogo.inventory.domain.InventoryStatus;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.CreateRequest;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.InventoryResponse;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.UpdateStatusRequest;
import uos.software.sogonggogo.inventory.dto.InventoryDtos.UpdateStockRequest;
import uos.software.sogonggogo.inventory.repository.InventoryRepository;

@Service
@RequiredArgsConstructor
public class InventoryService {

	private final InventoryRepository inventoryRepository;

	@Transactional(readOnly = true)
	public List<InventoryResponse> list(InventoryStatus status) {
		List<InventoryItem> items = (status == null)
				? inventoryRepository.findAll()
				: inventoryRepository.findByStatus(status);
		return items.stream().map(InventoryResponse::from).toList();
	}

	@Transactional
	public InventoryResponse create(CreateRequest request) {
		InventoryItem item = InventoryItem.builder()
				.name(request.name())
				.stock(request.stock())
				.price(request.price())
				.status(request.status())
				.build();
		return InventoryResponse.from(inventoryRepository.save(item));
	}

	@Transactional
	public InventoryResponse updateStatus(Long id, UpdateStatusRequest request) {
		InventoryItem item = getItem(id);
		item.updateStatus(request.status());
		return InventoryResponse.from(item);
	}

	@Transactional
	public InventoryResponse updateStock(Long id, UpdateStockRequest request) {
		InventoryItem item = getItem(id);
		item.updateStock(request.stock());
		return InventoryResponse.from(item);
	}

	@Transactional
	public void delete(Long id) {
		if (!inventoryRepository.existsById(id)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "재고를 찾을 수 없습니다.");
		}
		inventoryRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public InventoryItem getByNameOrThrow(String name) {
		return inventoryRepository.findByName(name)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "재고 품목을 찾을 수 없습니다: " + name));
	}

	private InventoryItem getItem(Long id) {
		return inventoryRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "재고를 찾을 수 없습니다."));
	}
}
