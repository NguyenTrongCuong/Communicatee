package root.api.home;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import root.api.sign_in.CommunicatorDetails;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String homePage(HttpSession session) {
		synchronized(session) {
			CommunicatorDetails communicatorDetails = (CommunicatorDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			session.setAttribute("communicator", communicatorDetails.getCommunicator());
		}
		return "home/home-page";
	}

}
