package uos.software.sogonggogo.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "password_hash", nullable = false)
	private String password;

	@Column(name = "credit_card_number", nullable = false, length = 16)
	private String creditCardNumber;

	@Builder.Default
	@Column(name = "is_regular_customer", nullable = false)
	private boolean regularCustomer = false;

	public void updateName(String name) {
		this.name = name;
	}

	public void updateAddress(String address) {
		this.address = address;
	}

	public void updatePhone(String phone) {
		this.phone = phone;
	}

	public void updatePassword(String encodedPassword) {
		this.password = encodedPassword;
	}

	public void updateCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public void markAsRegularCustomer() {
		this.regularCustomer = true;
	}
}
