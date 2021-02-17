package root.api.sign_in;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import root.entites.Communicator;
import root.entity_repositories.CommunicatorRepository;

@Service
public class CommunicatorDetailsService implements UserDetailsService {
	@Autowired
	private CommunicatorRepository communicatorRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Communicator> communicatorWithCorrespondingUsername = this.communicatorRepository.findById(username);
		if(communicatorWithCorrespondingUsername.isEmpty()) {
			throw new UsernameNotFoundException("Communicator with username " + username + " is not found");
		}
		return new CommunicatorDetails(communicatorWithCorrespondingUsername.get());
	}

}
