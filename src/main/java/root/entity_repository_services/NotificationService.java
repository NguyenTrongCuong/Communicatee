package root.entity_repository_services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.entites.Notification;
import root.entity_repositories.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	
	public Notification saveNotification(Notification notification) {
		return this.notificationRepository.save(notification);
	}
	
	public Optional<Notification> findNotificationById(Long notificationId) {
		return this.notificationRepository.findById(notificationId);
	}
	
	public void updateNotification(Notification notification) {
		this.notificationRepository.save(notification);
	}
	

}




































