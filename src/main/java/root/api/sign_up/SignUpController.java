package root.api.sign_up;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import root.entites.Communicator;

@Controller
public class SignUpController {
	
	@GetMapping("/sign-up-page")
	public String signUpPage(Model model) {
		model.addAttribute("communicator-model", new Communicator());
		return "sign-up/sign-up-page";
	}
	
	

}
