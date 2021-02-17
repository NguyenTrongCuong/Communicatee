package root.entites;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity(name="communicators")
public class Communicator implements Persistable<String> {
	@Id
	@Column(name="communicator_emails")
	private String communicatorEmail;
	
	@Column(name="communicator_first_names")
	private String communicatorFirstName;
	
	@Column(name="communicator_last_names")
	private String communicatorLastName;
	
	@Column(name="communicator_dobs")
	private Date communicatorDOB;
	
	@Column(name="password")
	private String password;

	@ManyToMany(mappedBy="communicator")
	private Set<Authority> authority;
	
	@ManyToMany(mappedBy="communicator")
	private Set<Room> room;

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	public Set<Authority> getAuthority() {
		return authority;
	}

	public void setAuthority(Set<Authority> authority) {
		this.authority = authority;
	}

	public Set<Room> getRoom() {
		return room;
	}

	public void setRoom(Set<Room> room) {
		this.room = room;
	}

	@Transient
	private boolean isNew = true;

	public String getCommunicatorFirstName() {
		return communicatorFirstName;
	}

	public void setCommunicatorFirstName(String communicatorFirstName) {
		this.communicatorFirstName = communicatorFirstName;
	}

	public String getCommunicatorLastName() {
		return communicatorLastName;
	}

	public void setCommunicatorLastName(String communicatorLastName) {
		this.communicatorLastName = communicatorLastName;
	}

	public String getCommunicatorEmail() {
		return communicatorEmail;
	}

	public Date getCommunicatorDOB() {
		return communicatorDOB;
	}

	public void setCommunicatorDOB(Date communicatorDOB) {
		this.communicatorDOB = communicatorDOB;
	}

	@Override
	public String getId() {
		return this.communicatorEmail;
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
