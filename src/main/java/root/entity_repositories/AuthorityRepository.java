package root.entity_repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import root.entites.Authority;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority, String> {

}
