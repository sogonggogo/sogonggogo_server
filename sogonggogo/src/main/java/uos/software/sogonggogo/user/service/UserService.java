package uos.software.sogonggogo.user.service;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.user.domain.User;
import uos.software.sogonggogo.user.dto.LoginRequest;
import uos.software.sogonggogo.user.dto.UserResponse;
import uos.software.sogonggogo.user.dto.UserSignupRequest;
import uos.software.sogonggogo.user.dto.UserUpdateRequest;
import uos.software.sogonggogo.user.repository.UserRepository;
import uos.software.sogonggogo.user.session.SessionKeys;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponse signup(UserSignupRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
		}

		User user = User.builder()
				.name(request.name())
				.address(request.address())
				.phone(request.phone())
				.email(request.email())
				.password(passwordEncoder.encode(request.password()))
				.creditCardNumber(request.creditCardNumber())
				// 신규 가입자는 기본적으로 일반 고객(false)
				.regularCustomer(false)
				.build();

		return UserResponse.from(userRepository.save(user));
	}

	@Transactional(readOnly = true)
	public boolean isEmailAvailable(String email) {
		return !userRepository.existsByEmail(email);
	}

	@Transactional
	public UserResponse login(LoginRequest request, HttpSession session) {
		User user = userRepository.findByEmail(request.email())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "존재하지 않는 계정입니다."));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 올바르지 않습니다.");
		}

		session.setAttribute(SessionKeys.USER_ID, user.getId());
		session.setMaxInactiveInterval((int) Duration.ofMinutes(30).getSeconds());
		return UserResponse.from(user);
	}

	@Transactional
	public void logout(HttpSession session) {
		if (session != null) {
			session.invalidate();
		}
	}

	@Transactional(readOnly = true)
	public UserResponse getCurrentUser(HttpSession session) {
		Long userId = requireLogin(session);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
		return UserResponse.from(user);
	}

	@Transactional
	public UserResponse update(UserUpdateRequest request, HttpSession session) {
		if (request.hasNoChanges()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "변경할 정보가 없습니다.");
		}

		Long userId = requireLogin(session);
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));

		if (request.name() != null) {
			user.updateName(request.name());
		}
		if (request.address() != null) {
			user.updateAddress(request.address());
		}
		if (request.phone() != null) {
			user.updatePhone(request.phone());
		}
		if (request.password() != null) {
			user.updatePassword(passwordEncoder.encode(request.password()));
		}
		if (request.creditCardNumber() != null) {
			user.updateCreditCardNumber(request.creditCardNumber());
		}

		return UserResponse.from(user);
	}

	private Long requireLogin(HttpSession session) {
		if (session == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
		}
		Object userId = session.getAttribute(SessionKeys.USER_ID);
		if (userId instanceof Long) {
			return (Long) userId;
		}
		if (userId instanceof Integer) {
			return ((Integer) userId).longValue();
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
	}
}
