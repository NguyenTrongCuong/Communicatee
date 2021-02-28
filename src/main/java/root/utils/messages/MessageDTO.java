package root.utils.messages;

import root.entites.Message;

public class MessageDTO {
	private long messageId;
	private String messageContent;
	private String from;
	private Long roomId;
	private String senderName;
	
	public MessageDTO(Message message) {
		this.messageId = message.getMessageId();
		this.messageContent = message.getMessageContent();
		this.from = message.getCommunicator().getCommunicatorEmail();
		this.senderName = message.getCommunicator().getCommunicatorFirstName() + " " + message.getCommunicator().getCommunicatorLastName();
	}
	
	public MessageDTO() {}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public long getMessageId() {
		return messageId;
	}
	
	public void setMessageId(long messageId) {
		this.messageId = messageId;
	}
	
	public String getMessageContent() {
		return messageContent;
	}
	
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	
	public String getFrom() {
		return from;
	}
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	

}
