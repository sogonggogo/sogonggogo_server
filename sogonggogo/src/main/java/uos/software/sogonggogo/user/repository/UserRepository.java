package uos.software.sogonggogo.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import uos.software.sogonggogo.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmail(String email);
}
