package root.api.check_email;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import root.entites.Communicator;
import root.entity_repository_services.CommunicatorService;

@RestController
public class CheckEmailController {
	@Autowired
	private CommunicatorService communicatorService;
	
	@PostMapping(value="/check-email", produces="application/json")
	public EmailChecking checkEmail(@RequestBody EmailChecking emailCheckingRequest, HttpSession session) {
		Communicator communicator = null;
		String receiverEmail = emailCheckingRequest.getCommunicatorEmail();
		
		synchronized(session) {
			communicator = (Communicator) session.getAttribute("communicator");
		}
		
		String senderEmail = communicator.getCommunicatorEmail();
		
		//check whether the input email exists or not
		if(this.communicatorService.doesEmailExist(receiverEmail)) {
			//check whether the input email is the sender's email or not
			if(!senderEmail.equals(receiverEmail)) {
				Optional<Set<Communicator>> friendList = this.communicatorService.findFriendsOfCommunicator(senderEmail);
				
				//check whether the sender had friend with that communicator or not
				if(!friendList.isEmpty()) {
					if(this.isFriend(friendList.get(), receiverEmail)) {
						return new EmailChecking("false", receiverEmail, "That communicator was already your friend");
					}
				}
			}
			else return new EmailChecking("false", receiverEmail, "You can not add friend with yourself");
		}
		else return new EmailChecking("false", receiverEmail, "That email does not exist");
		
		return new EmailChecking("true", receiverEmail, "");
	}
	
	private boolean isFriend(Set<Communicator> friendList, String receiverEmail) {
		for(Communicator ele : friendList) {
			if(ele.getCommunicatorEmail().equals(receiverEmail)) {
				return true;
			}
		}
		return false;
	}
	
	

}
























