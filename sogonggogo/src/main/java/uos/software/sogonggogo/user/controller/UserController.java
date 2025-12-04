package uos.software.sogonggogo.user.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import uos.software.sogonggogo.user.dto.LoginRequest;
import uos.software.sogonggogo.user.dto.UserResponse;
import uos.software.sogonggogo.user.dto.UserSignupRequest;
import uos.software.sogonggogo.user.dto.UserUpdateRequest;
import uos.software.sogonggogo.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@Validated
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<UserResponse> signup(@Valid @RequestBody UserSignupRequest request) {
		UserResponse response = userService.signup(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/check-email")
	public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
		boolean available = userService.isEmailAvailable(email);
		return Map.of("available", available);
	}

	@PostMapping("/login")
	public UserResponse login(@Valid @RequestBody LoginRequest request, HttpSession session) {
		return userService.login(request, session);
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpSession session) {
		userService.logout(session);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/me")
	public UserResponse getProfile(HttpSession session) {
		return userService.getCurrentUser(session);
	}

	@PutMapping("/me")
	public UserResponse updateProfile(@Valid @RequestBody UserUpdateRequest request, HttpSession session) {
		return userService.update(request, session);
	}
}
