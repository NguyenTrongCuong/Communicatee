package root.api.check_email;

public class EmailChecking {
	private String isValid;
	
	private String communicatorEmail;
	
	private String reasonForInvalidation;
	
	public EmailChecking() {}
	
	public EmailChecking(String isValid, String communicatorEmail, String reasonForInvalidation) {
		this.isValid = isValid;
		this.communicatorEmail = communicatorEmail;
		this.reasonForInvalidation = reasonForInvalidation;
	}

	public String getReasonForInvalidation() {
		return reasonForInvalidation;
	}

	public void setReasonForInvalidation(String reasonForInvalidation) {
		this.reasonForInvalidation = reasonForInvalidation;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getCommunicatorEmail() {
		return communicatorEmail;
	}

	public void setCommunicatorEmail(String communicatorEmail) {
		this.communicatorEmail = communicatorEmail;
	}
	
	

}
