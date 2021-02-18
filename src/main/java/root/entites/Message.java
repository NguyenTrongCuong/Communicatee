package root.entites;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity(name="messages")
public class Message implements Persistable<Long>, Serializable {
	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="message_ids")
	private Long messageId;
	
	@Column(name="message_contents")
	private String messageContent;
	
	@Column(name="sent_at")
	private Instant sentAt;
	
	@ManyToOne
	@JoinColumn(name="communicator_emails")
	private Communicator communicator;
	
	@ManyToOne
	@JoinColumn(name="room_ids")
	private Room room;
	
	public Communicator getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Communicator communicator) {
		this.communicator = communicator;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Transient
	private boolean isNew = true;
	
	public Long getMessageId() {
		return messageId;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public void setSentAt(Instant sentAt) {
		this.sentAt = sentAt;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public Long getId() {
		return this.messageId;
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
