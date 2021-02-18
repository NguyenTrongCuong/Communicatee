package root.api.sign_in;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignInController {
	
	@GetMapping("/sign-in-page")
	public String signInPage(@RequestParam(name="message", required=false) String message,
							 @RequestParam(name="errors", required=false) String errors,
						 	 Model model) {
		model.addAttribute("message", message);
		model.addAttribute("errors", errors);
		return "/sign-in/sign-in-page";
	}

}
