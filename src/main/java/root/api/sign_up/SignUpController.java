package root.api.sign_up;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import root.entites.Communicator;
import root.entity_repository_services.CommunicatorService;

@Controller
public class SignUpController {
	@Autowired
	private CommunicatorService communicatorService;
	
	@GetMapping("/sign-up-page")
	public String signUpPage(@ModelAttribute("model") Communicator model) {
		return "sign-up/sign-up-page";
	}
	
	@PostMapping("/sign-up")
	public String signUp(@Valid @ModelAttribute("model") Communicator communicator, BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "sign-up/sign-up-page";
		}
		this.communicatorService.saveCommunicator(communicator);
		model.addAttribute("message", "Signed up successfully, please sign in to continue");
		return "sign-in/sign-in-page";
	}
	
	

}























