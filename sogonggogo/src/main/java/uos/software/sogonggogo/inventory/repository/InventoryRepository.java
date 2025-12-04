package uos.software.sogonggogo.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uos.software.sogonggogo.inventory.domain.InventoryItem;
import uos.software.sogonggogo.inventory.domain.InventoryStatus;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

	List<InventoryItem> findByStatus(InventoryStatus status);

	Optional<InventoryItem> findByName(String name);
}
