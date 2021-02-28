package root.entity_repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import root.entites.Room;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
	
	@Query("SELECT r FROM rooms r JOIN r.communicator c WHERE r.roomType = 'Single' and c.communicatorEmail = :communicatorEmail")
	public Optional<Set<Room>> findAllOfSingleRoomsOfCommunicator(@Param("communicatorEmail") String communicatorEmail);

}
