package root.entity_repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.entites.Notification;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

}
