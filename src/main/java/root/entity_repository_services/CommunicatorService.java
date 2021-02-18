package root.entity_repository_services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.entites.Communicator;
import root.entity_repositories.CommunicatorRepository;

@Service
public class CommunicatorService {
	@Autowired
	private CommunicatorRepository communicatorRepository;
	
	public void saveCommunicator(Communicator communicator) {
		this.communicatorRepository.save(communicator);
	}
	
	public boolean isEmailDuplicated(String communicatorEmail) {
		Optional<String> result = this.communicatorRepository.findCommunicatorEmail(communicatorEmail);
		return result.isEmpty() ? true : false;
	}

}
