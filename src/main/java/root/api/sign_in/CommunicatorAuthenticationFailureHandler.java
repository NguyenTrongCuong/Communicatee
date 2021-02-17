package root.api.sign_in;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class CommunicatorAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
										HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {
		this.redirectStrategy.sendRedirect(request, response, "/sign-in-page?errors=email-or-password-is-incorrect");
	}

}
