package root.entites;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity(name="notifications")
public class Notification implements Persistable<Long>, Serializable {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Transient
	private boolean isNew = true;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="notification_ids")
	private Long notificationId;
	
	@Column(name="notification_types")
	private String notificationType;
	
	@Column(name="notification_contents")
	private String notificationContent;
	
	@Column(name="created_at")
	private Instant createdAt;
	
	@Column(name="sender_ids")
	private String from;
	
	@ElementCollection
	@CollectionTable(name="notifications_receivers",
					 joinColumns=@JoinColumn(name="notification_ids"))
	@Column(name="receiver_emails")
	private Set<String> to = new HashSet<String>();
	
	@ManyToMany
	@JoinTable(name="notifications_communicators",
			   joinColumns=@JoinColumn(name="notification_ids"),
			   inverseJoinColumns=@JoinColumn(name="communicator_emails"))	
	private Set<Communicator> communicator = new HashSet<Communicator>();

	public String getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(String notificationType) {
		this.notificationType = notificationType;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public Set<String> getTo() {
		return to;
	}

	public void setTo(Set<String> to) {
		this.to = to;
	}

	public Set<Communicator> getCommunicator() {
		return communicator;
	}

	public void setCommunicator(Set<Communicator> communicator) {
		this.communicator = communicator;
	}

	public Long getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}

	public String getNotificationContent() {
		return notificationContent;
	}

	public void setNotificationContent(String notificationContent) {
		this.notificationContent = notificationContent;
	}

	@Override
	public Long getId() {
		return this.notificationId;
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
