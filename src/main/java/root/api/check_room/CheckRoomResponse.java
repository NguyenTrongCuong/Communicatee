package root.api.check_room;

public class CheckRoomResponse {
	private String isValid;
	private String reasonForInvalidation;
	
	public CheckRoomResponse(String isValid, String reasonForInvalidation) {
		this.isValid = isValid;
		this.reasonForInvalidation = reasonForInvalidation;
	}
	
	public String getIsValid() {
		return isValid;
	}
	
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	public String getReasonForInvalidation() {
		return reasonForInvalidation;
	}
	
	public void setReasonForInvalidation(String reasonForInvalidation) {
		this.reasonForInvalidation = reasonForInvalidation;
	}
	
	

}
