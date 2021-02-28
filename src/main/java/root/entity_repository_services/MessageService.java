package root.entity_repository_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.entites.Message;
import root.entity_repositories.MessageRepository;

@Service
public class MessageService {
	@Autowired
	private MessageRepository messageRepository;
	
	public Message saveMessage(Message message) {
		return this.messageRepository.save(message);
	}
	
}
