package root.utils.communicators;

import root.entites.Communicator;

public class CommunicatorDTO {
	private String communicatorFirstName;
	private String communicatorLastName;
	private String communicatorEmail;
	
	public CommunicatorDTO() {}
	
	public CommunicatorDTO(Communicator communicator) {
		this.communicatorEmail = communicator.getCommunicatorEmail();
		this.communicatorFirstName = communicator.getCommunicatorFirstName();
		this.communicatorLastName = communicator.getCommunicatorLastName();
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
	
	public void setCommunicatorEmail(String communicatorEmail) {
		this.communicatorEmail = communicatorEmail;
	}
	
	

}
