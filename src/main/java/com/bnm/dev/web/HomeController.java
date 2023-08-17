package com.bnm.dev.web;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bnm.dev.model.Authorisation;
import com.bnm.dev.model.User;
import com.bnm.dev.service.AuthorisationService;
import com.bnm.dev.service.UserService;
import org.slf4j.Logger;

@Controller
public class HomeController {
	private AuthorisationService authService;
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	public HomeController(AuthorisationService authService) {
		super();
		this.authService = authService;
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}

	@GetMapping("/")
	public String home(Model model) {
        List<Authorisation> auths = authService.getAuthorisationsInProgress("IN_PROGRESS");
		
		model.addAttribute("authorisations", auths);
		
		return "index";
	}
	
	@GetMapping("/update")
	public String updateAuthoriation(@RequestParam Integer id, @RequestParam Integer type) {
		String status = "";
		if(type == 1)
			status = "AUTHORISED";
		else if(type == 2)
			status = "REJECTED";
		
		Authorisation auth = authService.loadAuthorisationById(id);
		
		auth.setStatus(status);
		
		return "index";
	}
}
