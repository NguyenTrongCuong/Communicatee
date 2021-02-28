package root.api.create_room;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import root.entites.Communicator;
import root.entites.Room;
import root.entity_repository_services.CommunicatorService;
import root.entity_repository_services.RoomService;
import root.utils.notifications.NotificationDTO;
import root.utils.rooms.RoomDTO;

@Controller
public class CreateRoomController {
	@Autowired
	private CommunicatorService communicatorService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private SimpMessagingTemplate template;
	
	@MessageMapping("/create-room")
	public void createRoom(@Payload RoomDTO roomDTO, SimpMessageHeaderAccessor header) {
		Communicator signedInPrincipal = (Communicator) header.getSessionAttributes().get("communicator");
		String signedInEmail = signedInPrincipal.getCommunicatorEmail(); //get the signed in user's email
		
		//create the room
		Room room = this.performCreateRoom(roomDTO, signedInEmail);
		RoomDTO response = new RoomDTO(room);
		
//		//create necessary queues for members of the room
//		this.createNecessaryQueuesForMembersOfRoom(roomDTO, response);
		
		//send notification to members of the room
		this.sendNotificationToMembersOfRoom(roomDTO, response);
	}
	
//	private void createNecessaryQueuesForMembersOfRoom(RoomDTO roomDTO, RoomDTO response) {
//		Set<String> memberEmails = roomDTO.getMemberEmails();
//		for(String element : memberEmails) {
//			this.amqpAdmin.declareQueue(new Queue(element + "-" + response.getRoomId() + "-message-cover"));
//		}
//	}
	
	private void sendNotificationToMembersOfRoom(RoomDTO roomDTO, RoomDTO response) {
		Set<String> memberEmails = roomDTO.getMemberEmails();
		NotificationDTO notification = new NotificationDTO();
		notification.setFrom(response.getRoomId().toString());
		notification.setNotificationType("Member Adding");
		for(String element : memberEmails) {
			this.template.convertAndSend("/queue/" + element + "-message-notification", response);
			this.template.convertAndSend("/queue/" + element + "-notification-count", notification);
		}
	}
	
	private Room performCreateRoom(RoomDTO roomDTO, String signedInEmail) {
		Set<Communicator> members = this.communicatorService.findCommunicatorsById(roomDTO.getMemberEmails());
		Room room = new Room();
		room.setRoomName(roomDTO.getRoomName());
		room.setRoomType(roomDTO.getRoomType());
		room.setNumberOfMemebers(roomDTO.getNumberOfMembers());
		for(Communicator ele : members) {
			ele.getRoom().add(room);
			room.getCommunicator().add(ele);
			if(ele.getCommunicatorEmail().equals(signedInEmail)) {
				ele.setRootOf(room);
				ele.getAdminOf().add(room);
				room.setRoomRoot(ele);
				room.getRoomAdmin().add(ele);
			}
		}
		room = this.roomService.saveRoom(room);
		this.communicatorService.updateCommunicators(members);
		return room;
	}
	
	

}






















































