package root.api.sign_up;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.common.collect.Sets;

import root.entites.Authority;
import root.entites.Communicator;
import root.entity_repository_services.AuthorityService;
import root.entity_repository_services.CommunicatorService;
import root.password_encoder.BCryptPasswordEncoder;

@Controller
public class SignUpController {
	@Autowired
	private CommunicatorService communicatorService;
	@Autowired
	private AuthorityService authorityService;
	
	@GetMapping("/sign-up-page")
	public String signUpPage(@ModelAttribute("model") Communicator model) {
		return "sign-up/sign-up-page";
	}
	
	@PostMapping("/sign-up")
	public String signUp(@Valid @ModelAttribute("model") Communicator communicator, BindingResult result, Model model) {
		//check input errors. if true ask the client to reenter invalid properties
		if(result.hasErrors()) { 
			return "sign-up/sign-up-page";
		}
		
		//check for whether the input email has been registered or not, if true ask the client to enter a new one
		String inputEmail = communicator.getCommunicatorEmail();
		if(this.communicatorService.isEmailDuplicated(inputEmail)) {
			result.rejectValue("communicatorEmail", "communicatorEmail", "This email has been used, please choose another one");
			return "sign-up/sign-up-page";
		}
		
		communicator.hashPasswordWithCorrespondingPasswordEncoderType(new BCryptPasswordEncoder(), 10); //hash input password
		
		//get default authorities for a new account
		Optional<Iterable<Authority>> authoritiesOfNewAccount = this.authorityService.findAuthoritiesById(Arrays.asList("ROLE_USER"));
		Set<Authority> authorities = Sets.newHashSet(authoritiesOfNewAccount.get());
		
		//set default authorities for a new account
		communicator.setAuthority(authorities);
		
		//update authorities
		for(Authority ele : authorities) {
			ele.setCommunicator(Sets.newHashSet(communicator));
		}
		
		this.communicatorService.saveCommunicator(communicator);
		this.authorityService.updateAuthorities(authorities);
		model.addAttribute("message", "Signed up successfully, please sign in to continue");
		return "sign-in/sign-in-page";
	}
	
	
	
	

}























