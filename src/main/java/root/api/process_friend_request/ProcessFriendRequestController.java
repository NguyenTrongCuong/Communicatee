package root.api.process_friend_request;

import java.time.Instant;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import root.entites.Communicator;
import root.entites.Notification;
import root.entity_repository_services.CommunicatorService;
import root.entity_repository_services.NotificationService;
import root.utils.notifications.NotificationDTO;

@Controller
public class ProcessFriendRequestController {
	@Autowired
	private SimpMessagingTemplate template;
	@Autowired
	private CommunicatorService communicatorService;
	@Autowired
	private NotificationService notificationService;
	
	@MessageMapping("/send-friend-request-to-communicator/{communicatorEmail}")
	public void sendFriendRequestToCommunicator(@DestinationVariable("communicatorEmail") String communicatorEmail, @Payload Notification friendRequestNotification) {
		Set<String> toEmail = friendRequestNotification.getTo(); //get receivers' email
		Set<Communicator> toCommunicator = this.communicatorService.findCommunicatorsById(toEmail); //get receivers
		
		//add the new notification to communicators and add receivers to the new notification
		for(Communicator ele : toCommunicator) {
			ele.getNotification().add(friendRequestNotification);
			friendRequestNotification.getCommunicator().add(ele);
		}
		
		friendRequestNotification.setCreatedAt(Instant.now());
		
		this.notificationService.saveNotification(friendRequestNotification);
		this.communicatorService.updateCommunicators(toCommunicator);
		
		this.template.convertAndSend("/queue/" + communicatorEmail + "-notification-count", new NotificationDTO());
		this.template.convertAndSend("/queue/" + communicatorEmail + "-notification", new NotificationDTO(friendRequestNotification));
	}

}













