package root.api.load_message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import root.entites.Message;
import root.entites.Room;
import root.entity_repository_services.RoomService;
import root.utils.messages.MessageDTO;

@RestController
public class LoadMessageController {
	@Autowired
	private RoomService roomService;
	
	@GetMapping("/load-messages/{roomId}")
	public List<MessageDTO> loadMessages(@PathVariable("roomId") long roomId) {
		Room room = this.roomService.findRoomById(roomId).get();
		List<Message> message = Lists.newArrayList(room.getMessage());
		Collections.sort(message, Comparator.comparing(Message::getMessageId));
		return this.convertMessageToMessageDTO(message);
	}
	
	private List<MessageDTO> convertMessageToMessageDTO(List<Message> message) {
		List<MessageDTO> messageDTO = new ArrayList<MessageDTO>();
		for(Message element : message) {
			messageDTO.add(new MessageDTO(element));
		}
		return messageDTO;
	}

}
