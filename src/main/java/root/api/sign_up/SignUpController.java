package root.api.sign_up;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import root.utils.queues.QueueDeclaration;

@Controller
public class SignUpController {
	@Autowired
	private CommunicatorService communicatorService;
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	@Qualifier("directQueuesDeclaration")
	private QueueDeclaration queueDeclaration;
	
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
		if(this.communicatorService.doesEmailExist(inputEmail)) {
			result.rejectValue("communicatorEmail", "communicatorEmail", "This email has been used, please choose another one");
			return "sign-up/sign-up-page";
		}
		
		communicator.hashPasswordWithCorrespondingPasswordEncoderType(new BCryptPasswordEncoder(), 10); //hash input password
		communicator.convertDOB(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // convert string dob to localdate dob
		
		//get default authorities for a new account
		Optional<Iterable<Authority>> authoritiesOfNewAccount = this.authorityService.findAuthoritiesById(Arrays.asList("ROLE_USER"));
		Set<Authority> authorities = Sets.newHashSet(authoritiesOfNewAccount.get());
		
		//update authorities and set default authorities for a new account
		for(Authority ele : authorities) {
			ele.getCommunicator().add(communicator);
			communicator.getAuthority().add(ele);
		}
		
		this.communicatorService.saveCommunicator(communicator);
		this.authorityService.updateAuthorities(authorities);
		
		//declare necessary queues for the new account
		this.declareNecessaryQueuesForANewAccount(inputEmail);
		
		model.addAttribute("message", "Signed up successfully, please sign in to continue");
		return "sign-in/sign-in-page";
	}
	
	private void declareNecessaryQueuesForANewAccount(String inputEmail) {
		this.queueDeclaration.declareQueues(inputEmail + "-message");
		this.queueDeclaration.declareQueues(inputEmail + "-message-count");
		this.queueDeclaration.declareQueues(inputEmail + "-notification-count");
		this.queueDeclaration.declareQueues(inputEmail + "-notification");
	}
	
	
	
	

}























