package root.api.sign_up;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	public String signUp(@Valid @ModelAttribute("model") Communicator model, BindingResult result) {
		if(result.hasErrors()) {
			return "sign-up/sign-up-page";
		}
		this.communicatorService.saveCommunicator(model);
		return "sign-in/sign-in-page?message=Sign%20up%successfully,%20please%20sign%20in%20to%20continue";
	}
	
	

}























