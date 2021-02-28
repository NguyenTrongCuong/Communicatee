package root.entity_repositories;

import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import root.entites.Communicator;
import root.entites.Room;

@Repository
@Transactional
public interface CommunicatorRepository extends CrudRepository<Communicator, String> {
	
	@Query("SELECT c.communicatorEmail FROM communicators c WHERE c.communicatorEmail = :communicatorEmail")
	public Optional<String> findCommunicatorEmail(@Param("communicatorEmail") String communicatorEmail);
	
	@Query("SELECT c.friend FROM communicators c JOIN c.friend f WHERE c.communicatorEmail = :communicatorEmail")
	public Optional<Set<Communicator>> findFriendsOfCommunicator(@Param("communicatorEmail") String communicatorEmail);
	
	@Query("SELECT c.room FROM communicators c JOIN c.room r WHERE c.communicatorEmail = :communicatorEmail")
	public Optional<Set<Room>> findRoomsOfCommunicator(@Param("communicatorEmail") String communicatorEmail);
	

}











































