package com.bnm.dev.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bnm.dev.dto.UserRegistrationDto;
import com.bnm.dev.model.User;
import com.bnm.dev.service.UserService;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

	private UserService userService;

	public RegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}

	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}

	@PostMapping
	public String registerUserAccount(Model model, @ModelAttribute("user") UserRegistrationDto registrationDto) throws Exception {
		User user = userService.loadUserByEmail(registrationDto.getEmail());
		if(user != null) {
			model.addAttribute("errorMessage", "Email already exists");
			return "redirect:/registration?failure";
		}
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}
}
