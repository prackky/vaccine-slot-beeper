package tech.knowshipp.vaccinebooking.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {
	
	@GetMapping("/")
	public String subscribe() {
		return "subscribe";
	}
	
	@GetMapping("unsubscribe")
	public String unsubscribe() {
		return "unsubscribe";
	}
	
	@GetMapping("terms")
	public String terms() {
		return "terms";
	}
	
	@GetMapping("verify")
	public String verify() {
		return "verify";
	}
	
	@GetMapping("status")
	public String status() {
		return "status";
	}

}
