package root.api.process_friend_request;

import java.time.Instant;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Sets;

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
	
	@MessageMapping("/send-friend-request-to-communicator/{receiverEmail}")
	public void sendFriendRequestToCommunicator(@DestinationVariable("receiverEmail") String receiverEmail, @Payload Notification friendRequestNotification) {
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
		
		// send friend request to the receiver
		this.template.convertAndSend("/queue/" + receiverEmail + "-notification-count", new NotificationDTO());
		this.template.convertAndSend("/queue/" + receiverEmail + "-notification", new NotificationDTO(friendRequestNotification));
	}
	
	@MessageMapping("/process-friend-request/{notification-id}/{is-accepted}")
	public void processFriendRequest(@DestinationVariable("notification-id") Long notificationId, @DestinationVariable("is-accepted") boolean isAccepted) {
		Notification notification = this.notificationService.findNotificationById(notificationId).get(); //get the corresponding friend request
		
		String senderEmail = notification.getFrom(); 
		Set<String> receiverEmail = notification.getTo(); 
		
		Communicator sender = this.communicatorService.findCommunicatorById(senderEmail).get(); //get the friend request's sender
		Set<Communicator> receiver = this.communicatorService.findCommunicatorsById(receiverEmail); //get the friend request's receiver
		
		NotificationDTO replyNotificationDTO = null;
		
		if(isAccepted) {
			// if the receiver accepts the friend request
			replyNotificationDTO = this.acceptFriendRequest(notification, sender, receiver);
		}
		else {
			// if the receiver rejects the friend request
			replyNotificationDTO = this.rejectFriendRequest(notification, sender, receiver);
		}
		
		// send reply notification to the friend request's sender
		this.template.convertAndSend("/queue/" + senderEmail +"-notification-count", new NotificationDTO());
		this.template.convertAndSend("/queue/" + senderEmail + "-notification", replyNotificationDTO);
	}
	
	private NotificationDTO acceptFriendRequest(Notification notification, Communicator sender, Set<Communicator> receiver) {
		// update the content of the friend request to accepted
		String oldNotificationContent = notification.getNotificationContent();
		String newNotificationContent = oldNotificationContent + "<p>" + "Friend request accepted" + "</p>";
		
		notification.setNotificationContent(newNotificationContent);
		
		this.notificationService.updateNotification(notification);
		
		// update friend list of the sender and receiver
		for(Communicator ele : receiver) {
			sender.getFriend().add(ele);
			sender.getFriendOf().add(ele);
			ele.getFriend().add(sender);
			ele.getFriendOf().add(sender);
		}
		
		sender = this.communicatorService.updateCommunicatorWithReturn(sender);
		receiver = this.communicatorService.updateCommunicatorsWithReturn(receiver);
		
		//create accepted reply notification
		return createReplyNotification("accepted", sender, receiver);
		
	}
	
	private NotificationDTO rejectFriendRequest(Notification notification, Communicator sender, Set<Communicator> receiver) {
		// update the content of the friend request to rejected
		String oldNotificationContent = notification.getNotificationContent();
		String newNotificationContent = oldNotificationContent + "<p>" + "Friend request rejected" + "</p>";
		
		notification.setNotificationContent(newNotificationContent);
		
		this.notificationService.updateNotification(notification);
		
		//create rejected reply notification
		return createReplyNotification("rejected", sender, receiver);
	}
	
	private NotificationDTO createReplyNotification(String acceptOrNot, Communicator sender, Set<Communicator> receiver) {
		String senderName = "";
		
		String receiverEmail = sender.getCommunicatorEmail();
		String senderEmail = "";
		
		for(Communicator ele : receiver) {
			senderName += ele.getCommunicatorFirstName() + " " + ele.getCommunicatorLastName();
			senderEmail += ele.getCommunicatorEmail();
		}
		
		String replyNotificationContent = senderName + " " + acceptOrNot + " your friend request";
		Notification replyNotification = new Notification();
		replyNotification.setCreatedAt(Instant.now());
		replyNotification.setNotificationType("Friend Request Reply");
		replyNotification.setNotificationContent(replyNotificationContent);
		replyNotification.setFrom(senderEmail);
		replyNotification.setTo(Sets.newHashSet(receiverEmail));
		
		replyNotification.getCommunicator().add(sender);
		
		sender.getNotification().add(replyNotification);
		
		replyNotification = this.notificationService.saveNotification(replyNotification);
		this.communicatorService.updateCommunicator(sender);
		
		return new NotificationDTO(replyNotification);
	}

}













