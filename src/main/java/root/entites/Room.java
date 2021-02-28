package root.entites;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity(name="rooms")
public class Room implements Persistable<Long>, Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Transient
	private boolean isNew = true;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="room_ids")
	private Long roomId;
	
	@Column(name="room_names")
	private String roomName;
	
	@Column(name="number_of_members")
	private int numberOfMemebers;
	
	@Column(name="room_types")
	private String roomType;
	
	@OneToOne
	@JoinColumn(name="root_ids")
	private Communicator roomRoot;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="rooms_admins",
			   joinColumns=@JoinColumn(name="room_ids"),
			   inverseJoinColumns=@JoinColumn(name="communicator_emails"))
	private Set<Communicator> roomAdmin = new HashSet<Communicator>();
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="communicators_rooms",
			   joinColumns=@JoinColumn(name="room_ids"),
			   inverseJoinColumns=@JoinColumn(name="communicator_emails"))
	private Set<Communicator> communicator = new HashSet<Communicator>();
	
	@OneToMany(mappedBy="room", fetch=FetchType.EAGER)
	@OrderBy("sentAt DESC")
	private Set<Message> message = new HashSet<Message>();

	public Communicator getRoomRoot() {
		return roomRoot;
	}

	public void setRoomRoot(Communicator roomRoot) {
		this.roomRoot = roomRoot;
	}

	public Set<Communicator> getRoomAdmin() {
		return roomAdmin;
	}

	public void setRoomAdmin(Set<Communicator> roomAdmin) {
		this.roomAdmin = roomAdmin;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

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
