package root.api.process_invalid_session;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProcessInvalidSessionController {
	
	@GetMapping("/process-invalid-session")
	public String processInvalidSession(Model model) {
		model.addAttribute("message", "Please sign in to continue");
		return "sign-in/sign-in-page";
	}

}
