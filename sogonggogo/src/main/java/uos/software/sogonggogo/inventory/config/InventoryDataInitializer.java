package uos.software.sogonggogo.inventory.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.inventory.domain.InventoryItem;
import uos.software.sogonggogo.inventory.domain.InventoryStatus;
import uos.software.sogonggogo.inventory.repository.InventoryRepository;

@Component
@RequiredArgsConstructor
public class InventoryDataInitializer implements ApplicationRunner {

	private final InventoryRepository inventoryRepository;

	@Override
	public void run(ApplicationArguments args) {
		if (inventoryRepository.count() > 0) {
			return;
		}

		List<InventoryItem> seeds = List.of(
				build("샴페인", 25000, 50),
				build("빵", 5000, 110),
				build("바게트 빵", 4000, 120),
				build("커피", 15000, 80),
				build("와인", 25000, 90),
				build("스테이크", 35000, 70),
				build("에그 스크램블", 7000, 60),
				build("베이컨", 8000, 60),
				build("샐러드", 15000, 55),
				build("하트 장식", 14500, 45),
				build("큐피드 장식", 14500, 40)
		);

		inventoryRepository.saveAll(seeds);
	}

	private InventoryItem build(String name, int price, int stock) {
		return InventoryItem.builder()
				.name(name)
				.price(BigDecimal.valueOf(price))
				.stock(stock)
				.status(InventoryStatus.ON_SALE)
				.build();
	}
}
