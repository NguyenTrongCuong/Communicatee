package root.entity_repository_services;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

import root.entites.Communicator;
import root.entity_repositories.CommunicatorRepository;

@Service
public class CommunicatorService {
	@Autowired
	private CommunicatorRepository communicatorRepository;
	
	public void saveCommunicator(Communicator communicator) {
		this.communicatorRepository.save(communicator);
	}
	
	public boolean doesEmailExist(String communicatorEmail) {
		Optional<String> result = this.communicatorRepository.findCommunicatorEmail(communicatorEmail);
		return result.isEmpty() ? false : true;
	}
	
	public Set<Communicator> findCommunicatorsById(Set<String> communicatorEmail) {
		return Sets.newHashSet(this.communicatorRepository.findAllById(communicatorEmail));
	}
	
	public void updateCommunicators(Set<Communicator> communicator) {
		this.communicatorRepository.saveAll(communicator);
	}
	
	public Optional<Set<Communicator>> findFriendsOfCommunicator(String communicatorEmail) {
		return this.communicatorRepository.findFriendsOfCommunicator(communicatorEmail);
	}

}




