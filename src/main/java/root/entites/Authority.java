package root.entites;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity(name="authorities")
public class Authority {
	@Id
	@Column(name="roles")
	private String role;

	@ManyToMany
	@JoinTable(name="communicators_authorities",
			   joinColumns=@JoinColumn(name="roles"),
			   inverseJoinColumns=@JoinColumn(name="communicator_emails"))
	private Set<Communicator> communicator = new HashSet<Communicator>();
	
	public Set<Communicator> getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Set<Communicator> communicator) {
		this.communicator = communicator;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	

}
