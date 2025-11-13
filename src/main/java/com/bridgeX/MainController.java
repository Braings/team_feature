package com.bridgeX;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
	// Back-end testing only. With front-end, don't need.
	
	@ResponseBody
	@GetMapping("/")
	public String root() {
		return "This is Main(/): Test String";
	}
}
