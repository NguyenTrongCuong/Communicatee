package root.api.sign_in;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import root.entites.Authority;
import root.entites.Communicator;

public class CommunicatorDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
	private Communicator communicator;
	
	public CommunicatorDetails() {}
	
	public CommunicatorDetails(Communicator communicator) {
		this.username = communicator.getCommunicatorEmail();
		this.password = communicator.getPassword();
		this.communicator = communicator;
		setAuthorities(communicator);
	}
	
	public Communicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	private void setAuthorities(Communicator communicator) {
		List<Authority> temp = new ArrayList<Authority>(communicator.getAuthority());
		for(Authority element : temp) {
			this.authorities.add(new SimpleGrantedAuthority(element.getRole()));
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
