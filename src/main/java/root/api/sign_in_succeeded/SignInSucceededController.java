package root.api.sign_in_succeeded;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import root.api.sign_in.CommunicatorDetails;

@Controller
public class SignInSucceededController {
	
	@GetMapping("/sign-in-succeeded-page")
	public String signInSucceededPage(HttpSession session) {
		savePrincipalOfSignedInCommunicatorIntoSession(session);
		return "sign-in-succeeded/sign-in-succeeded-page";
	}
	
	private void savePrincipalOfSignedInCommunicatorIntoSession(HttpSession session) {
		synchronized(session) {
			CommunicatorDetails communicatorDetails = (CommunicatorDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			session.setAttribute("communicator", communicatorDetails.getCommunicator());
		}
	}

}






