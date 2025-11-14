package com.bridgeX.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {
	@Size(min = 3, max = 25)
	@NotEmpty(message = "* User ID cant be empty.")
	private String username;

	@NotEmpty(message = "* Password cant be empty.")
	private String password1;

	@NotEmpty(message = "* Password confirm cant be empty.")
	private String password2;

	@NotEmpty(message = "* E-mail cant be empty.")
	@Email
	private String email;
}
