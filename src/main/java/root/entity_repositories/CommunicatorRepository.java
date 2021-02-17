package root.entity_repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.entites.Communicator;

@Repository
public interface CommunicatorRepository extends CrudRepository<Communicator, String> {

}
