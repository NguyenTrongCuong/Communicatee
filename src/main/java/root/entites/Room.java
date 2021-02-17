package root.entites;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity(name="rooms")
public class Room implements Persistable<Long> {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="room_ids")
	private Long roomId;
	
	@Column(name="room_names")
	private String roomName;
	
	@Column(name="number_of_members")
	private int numberOfMemebers;
	
	@ManyToMany
	@JoinTable(name="communicators_rooms",
			   joinColumns=@JoinColumn(name="room_ids"),
			   inverseJoinColumns=@JoinColumn(name="communicator_emails"))
	private Set<Communicator> communicator;
	
	@OneToMany(mappedBy="room")
	private Set<Message> message;
	
	public Set<Communicator> getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Set<Communicator> communicator) {
		this.communicator = communicator;
	}

	public Set<Message> getMessage() {
		return message;
	}

	public void setMessage(Set<Message> message) {
		this.message = message;
	}

	@Transient
	private boolean isNew = true;

	public Long getRoomId() {
		return roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getNumberOfMemebers() {
		return numberOfMemebers;
	}

	public void setNumberOfMemebers(int numberOfMemebers) {
		this.numberOfMemebers = numberOfMemebers;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public Long getId() {
		return this.roomId;
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
