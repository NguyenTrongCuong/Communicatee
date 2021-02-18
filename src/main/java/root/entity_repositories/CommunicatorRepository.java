package root.entity_repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import root.entites.Communicator;

@Repository
@Transactional
public interface CommunicatorRepository extends CrudRepository<Communicator, String> {
	
	@Query("SELECT c.communicatorEmail FROM communicators c WHERE c.communicatorEmail = :communicatorEmail")
	public Optional<String> findCommunicatorEmail(@Param("communicatorEmail") String communicatorEmail);

}
