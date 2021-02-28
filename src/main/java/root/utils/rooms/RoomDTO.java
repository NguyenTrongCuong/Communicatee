package root.utils.rooms;

import java.util.Set;

import root.entites.Room;

public class RoomDTO {
	private Long roomId;
	private String roomName;
	private String roomType;
	private int numberOfMembers;
	private Set<String> memberEmails;
	private String latestMessage = "";
	private String from = "";
	
	public RoomDTO(Room room) {
		this.roomId = room.getRoomId();
		this.roomName = room.getRoomName();
		this.roomType = room.getRoomType();
	}
	
	public RoomDTO() {}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getLatestMessage() {
		return latestMessage;
	}

	public void setLatestMessage(String latestMessage) {
		this.latestMessage = latestMessage;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}
	
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	public String getRoomType() {
		return roomType;
	}
	
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}
	
	public int getNumberOfMembers() {
		return numberOfMembers;
	}
	
	public void setNumberOfMembers(int numberOfMembers) {
		this.numberOfMembers = numberOfMembers;
	}

	public Set<String> getMemberEmails() {
		return memberEmails;
	}

	public void setMemberEmails(Set<String> memberEmails) {
		this.memberEmails = memberEmails;
	}
	
	
	

}
