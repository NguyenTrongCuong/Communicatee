package root.entity_repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.entites.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {

}
