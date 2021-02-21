package root.entity_repository_services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.entites.Notification;
import root.entity_repositories.NotificationRepository;

@Service
public class NotificationService {
	@Autowired
	private NotificationRepository notificationRepository;
	
	public void saveNotification(Notification notification) {
		this.notificationRepository.save(notification);
	}

}




































