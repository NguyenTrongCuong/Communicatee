package root.api.check_room;

import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import root.entites.Communicator;
import root.entites.Room;
import root.entity_repository_services.CommunicatorService;
import root.entity_repository_services.RoomService;
import root.utils.rooms.RoomDTO;

@RestController
public class CheckRoomController {
	@Autowired
	private RoomService roomService;
	@Autowired
	private CommunicatorService communicatorService;
	
	@PostMapping(value="/check-room", produces="application/json")
	public CheckRoomResponse checkRoom(@RequestBody RoomDTO roomDTO, HttpSession session) {
		// check whether the room's type is Single or Group
		if(roomDTO.getRoomType().equals("Single")) {
			//if Single then perform check
			Communicator signedInPrincipal = null;
			
			synchronized(session) {
				//get the signed in user's
				signedInPrincipal = (Communicator) session.getAttribute("communicator");
			}
			
			//find all of single rooms of the signed in user
			Optional<Set<Room>> singleRoomsOfCommunicator = this.roomService.findAllOfSingleRoomsOfCommunicator(signedInPrincipal.getCommunicatorEmail());
			
			//if the signed in user is not in any single room then return true
			if(singleRoomsOfCommunicator.isEmpty()) {
				return new CheckRoomResponse("true", "");
			}
			
			//else
			//get the other communicator of the room
			Communicator friend = this.communicatorService.findCommunicatorById(Lists.newArrayList(roomDTO.getMemberEmails()).get(0)).get();
			
			//check whether the signed in user and the other communicator have ever created a single room before 
			return this.performCheckRoom(singleRoomsOfCommunicator.get(), friend);
		}
		return new CheckRoomResponse("true", "");
	}
	
	private CheckRoomResponse performCheckRoom(Set<Room> singleRoomsOfCommunicator, Communicator friend) {
		for(Room element : singleRoomsOfCommunicator) {
			if(element.getCommunicator().contains(friend)) {
				return new CheckRoomResponse("false", "You already created room with this communicator");
			}
		}
		return new CheckRoomResponse("true", "");
	}

}



















