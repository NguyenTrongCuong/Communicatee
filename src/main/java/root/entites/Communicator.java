package root.entites;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

import root.password_encoder.PasswordEncoder;

@Entity(name="communicators")
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class Communicator implements Persistable<String>, Serializable {
	@Transient
	private static final long serialVersionUID = 1L;

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
	private LocalDate communicatorDateOfBirth;
	
	@Transient
	@NotBlank(message="Invalid date of birth")
	private String communicatorDOB;
	
	@Column(name="password")
	@NotBlank(message="Invalid password")
	private String password;
	
	@Transient
	private boolean isNew = true;

	@ManyToMany(mappedBy="communicator", fetch=FetchType.EAGER)
	private Set<Authority> authority = new HashSet<Authority>();
	
	@ManyToMany(mappedBy="communicator", fetch=FetchType.EAGER)
	private Set<Room> room = new HashSet<Room>();
	
	@ManyToMany(mappedBy="communicator", fetch=FetchType.EAGER)
	private Set<Notification> notification = new HashSet<Notification>();
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="friends_communicators",
	   		   joinColumns=@JoinColumn(name="communicator_ids"),
	   		   inverseJoinColumns=@JoinColumn(name="communicator_friends_ids"))
	private Set<Communicator> friend = new HashSet<Communicator>();
	
	@ManyToMany(mappedBy="friend", fetch=FetchType.EAGER)
	private Set<Communicator> friendOf = new HashSet<Communicator>();
	
	@OneToOne(mappedBy="roomRoot")
	private Room rootOf;
	
	@ManyToMany(mappedBy="roomAdmin", fetch=FetchType.EAGER)
	private Set<Room> adminOf = new HashSet<Room>();

	public Room getRootOf() {
		return rootOf;
	}

	public void setRootOf(Room rootOf) {
		this.rootOf = rootOf;
	}

	public Set<Room> getAdminOf() {
		return adminOf;
	}

	public void setAdminOf(Set<Room> adminOf) {
		this.adminOf = adminOf;
	}

	public Set<Communicator> getFriend() {
		return friend;
	}

	public void setFriend(Set<Communicator> friend) {
		this.friend = friend;
	}

	public LocalDate getCommunicatorDateOfBirth() {
		return communicatorDateOfBirth;
	}

	public void setCommunicatorDateOfBirth(LocalDate communicatorDateOfBirth) {
		this.communicatorDateOfBirth = communicatorDateOfBirth;
	}

	public String getCommunicatorDOB() {
		return communicatorDOB;
	}

	public void setCommunicatorDOB(String communicatorDOB) {
		this.communicatorDOB = communicatorDOB;
	}

	public Set<Communicator> getFriendOf() {
		return friendOf;
	}

	public void setFriendOf(Set<Communicator> friendOf) {
		this.friendOf = friendOf;
	}

	public Set<Notification> getNotification() {
		return notification;
	}

	public void setNotification(Set<Notification> notification) {
		this.notification = notification;
	}

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
	
	public void hashPasswordWithCorrespondingPasswordEncoderType(PasswordEncoder passwordEncoderType, int levelOfSalt) {
		String hashedPassword = passwordEncoderType.hashPassword(getPassword(), levelOfSalt);
		setPassword(hashedPassword);
	}
	
	public void convertDOB(DateTimeFormatter pattern) {
		this.setCommunicatorDateOfBirth(LocalDate.parse(this.getCommunicatorDOB(), pattern));
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((communicatorEmail == null) ? 0 : communicatorEmail.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Communicator other = (Communicator) obj;
		if (communicatorEmail == null) {
			if (other.communicatorEmail != null)
				return false;
		} else if (!communicatorEmail.equals(other.communicatorEmail))
			return false;
		return true;
	}

}
