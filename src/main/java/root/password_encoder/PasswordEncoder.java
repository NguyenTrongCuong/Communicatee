package root.password_encoder;

public interface PasswordEncoder {
	
	public String hashPassword(String password, int levelOfSalt);

}
