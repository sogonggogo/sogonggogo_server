package uos.software.sogonggogo.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import uos.software.sogonggogo.order.domain.Order;
import uos.software.sogonggogo.order.domain.OrderStatus;
import uos.software.sogonggogo.user.domain.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUserOrderByCreatedAtDesc(User user);

	List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);
}
