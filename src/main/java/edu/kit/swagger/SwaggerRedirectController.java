package edu.kit.swagger;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // Spring Controller, not a REST Controller
public class SwaggerRedirectController {
	@RequestMapping("/swagger")
	public String swaggerRedirect() {
		return "redirect:/swagger-ui.html";
	}
}
