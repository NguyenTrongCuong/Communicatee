package root.entity_repository_services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.entites.Authority;
import root.entity_repositories.AuthorityRepository;

@Service
public class AuthorityService {
	@Autowired
	private AuthorityRepository authorityRepository;
	
	public Optional<Iterable<Authority>> findAuthoritiesById(List<String> role) {
		Iterable<Authority> result = this.authorityRepository.findAllById(role);
		return Optional.ofNullable(result);
	}
	
	public void updateAuthorities(Set<Authority> authorities) {
		this.authorityRepository.saveAll(authorities);
	}

}
