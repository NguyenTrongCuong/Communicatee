package root.utils.notifications;

import java.util.ArrayList;
import java.util.List;

import root.entites.Notification;

public class NotificationDTO {
	private Long notificationId;
	private String notificationContent;
	private String notificationType;
	private String from;
	private List<String> to = new ArrayList<String>();
	
	public NotificationDTO() {}
	
	public NotificationDTO(Notification friendRequestNotification) {
		this.notificationId = friendRequestNotification.getNotificationId();
		this.notificationContent = friendRequestNotification.getNotificationContent();
		this.from = friendRequestNotification.getFrom();
		this.to = new ArrayList<String>(friendRequestNotification.getTo());
		this.notificationType = friendRequestNotification.getNotificationType();
	}
	
	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Long getNotificationId() {
		return notificationId;
	}
	
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	
	public String getNotificationContent() {
		return notificationContent;
	}
	
	public void setNotificationContent(String notificationContent) {
		this.notificationContent = notificationContent;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public List<String> getTo() {
		return to;
	}
	
	public void setTo(List<String> to) {
		this.to = to;
	}
	
	

}
