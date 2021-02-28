package root.api.load_conversation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import root.api.sign_in.CommunicatorDetails;
import root.entites.Message;
import root.entites.Room;
import root.entity_repository_services.CommunicatorService;
import root.utils.rooms.RoomDTO;

@RestController
public class LoadConversationController {
	@Autowired
	private CommunicatorService communicatorService;
	
	@GetMapping("/load-conversations")
	public List<RoomDTO> loadConversations(@AuthenticationPrincipal CommunicatorDetails principal) {
		String signedInEmail = principal.getUsername(); //get the signed in email
		
		//get rooms that the signed in user is participating
		Optional<Set<Room>> participatedRooms = this.communicatorService.findRoomsOfCommunicator(signedInEmail);
		if(participatedRooms.isEmpty()) {
			return new ArrayList<RoomDTO>();
		}
		List<Room> room = Lists.newArrayList(participatedRooms.get());
		Collections.sort(room, Comparator.comparing(Room::getRoomId).reversed());
		
		//convert room objects to room dto objects
		return convertToRoomDTO(room);
	}
	
	private List<RoomDTO> convertToRoomDTO(List<Room> room) {
		List<RoomDTO> roomDTOContainer = new ArrayList<RoomDTO>();
		for(Room element : room) {
			RoomDTO roomDTO = new RoomDTO();
			roomDTO.setRoomId(element.getRoomId());
			roomDTO.setRoomName(element.getRoomName());
			roomDTO.setRoomType(element.getRoomType());
			if(element.getMessage().size() > 0) {
				Message message = Lists.newArrayList(element.getMessage()).get(0);
				roomDTO.setLatestMessage(message.getMessageContent());
				roomDTO.setFrom(message.getCommunicator().getCommunicatorFirstName() + " " + message.getCommunicator().getCommunicatorLastName());
			}
			roomDTOContainer.add(roomDTO);
		}
		return roomDTOContainer;
	}
	
	

}














































