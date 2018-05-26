package shougang.guigang.safety.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * login and register page controller
 * @author admin
 *
 */
@Controller
@RequestMapping("/page")
public class PageController {

	@RequestMapping("/login")
	private String showLogin(Boolean flag, String redirectURL, Model model) {
		model.addAttribute("flag" , flag);
		model.addAttribute("redirect" , redirectURL);
		return "login";
	}
	
	@RequestMapping("register")
	private String showRegister() {
		return "register";
	}
}
