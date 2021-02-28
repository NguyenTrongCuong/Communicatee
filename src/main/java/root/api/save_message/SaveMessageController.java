package root.api.save_message;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import root.entites.Communicator;
import root.entites.Message;
import root.entites.Room;
import root.entity_repository_services.CommunicatorService;
import root.entity_repository_services.MessageService;
import root.entity_repository_services.RoomService;
import root.utils.messages.MessageDTO;

@RestController
public class SaveMessageController {
	@Autowired
	private RoomService roomService;
	@Autowired
	private CommunicatorService communicatorService;
	@Autowired
	private MessageService messageService;
	
	@PostMapping(value="/save-message",
				 produces="application/json")
	public MessageDTO saveMessage(@RequestBody MessageDTO messageDTO) {
//		System.out.println(messageDTO.getRoomId());
		Room room = this.roomService.findRoomById(messageDTO.getRoomId()).get();
		Communicator sender = this.communicatorService.findCommunicatorById(messageDTO.getFrom()).get();
		Message message = new Message();
		message.setMessageContent(messageDTO.getMessageContent());
		message.setSentAt(Instant.now());
		message.setCommunicator(sender);
		message.setRoom(room);
		room.getMessage().add(message);
		message = this.messageService.saveMessage(message);
		this.roomService.updateRoom(room);
		messageDTO.setMessageId(message.getMessageId());
		messageDTO.setSenderName(sender.getCommunicatorFirstName() + " " + sender.getCommunicatorLastName());
		return messageDTO;
	}

}














































