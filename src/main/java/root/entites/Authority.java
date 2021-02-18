package root.entites;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity(name="authorities")
public class Authority implements Persistable<String>, Serializable {
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="roles")
	private String role;
	
	@Transient
	private boolean isNew = true;

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

	@Override
	public String getId() {
		return this.role;
	}

	@Override
	public boolean isNew() {
		return this.isNew;
	}
	
	@PrePersist
	@PostLoad
	public void markNotNew() {
		this.isNew = false;
	}

	

}
