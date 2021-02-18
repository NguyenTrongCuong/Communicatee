package root.entites;


import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.Persistable;

import root.password_encoder.PasswordEncoder;

@Entity(name="communicators")
public class Communicator implements Persistable<String> {
	@Id
	@Column(name="communicator_emails")
	@Pattern(regexp="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",
			 message="Invalid email")
	private String communicatorEmail;
	
	@Column(name="communicator_first_names")
	@Pattern(regexp="^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$",
			 message="Invalid first name")
	@NotBlank(message="Invalid first name")
	private String communicatorFirstName;
	
	@Column(name="communicator_last_names")
	@Pattern(regexp="^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$",
			 message="Invalid last name")
	@NotBlank(message="Invalid last name")
	private String communicatorLastName;
	
	@Column(name="communicator_dobs")
	@NotNull(message="Invalid date of birth")
	private Date communicatorDOB;
	
	@Column(name="password")
	@NotBlank(message="Invalid password")
	private String password;
	
	@Transient
	private boolean isNew = true;

	@ManyToMany(mappedBy="communicator")
	private Set<Authority> authority = new HashSet<Authority>();
	
	@ManyToMany(mappedBy="communicator")
	private Set<Room> room = new HashSet<Room>();

	public void setCommunicatorEmail(String communicatorEmail) {
		this.communicatorEmail = communicatorEmail;
	}

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
	
	public void hashPasswordWithCorrespondingPasswordEncoderType(PasswordEncoder passwordEncoderType, int levelOfSalt) {
		String hashedPassword = passwordEncoderType.hashPassword(getPassword(), levelOfSalt);
		setPassword(hashedPassword);
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
