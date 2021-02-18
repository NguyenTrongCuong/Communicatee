package root.password_encoder;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordEncoder implements PasswordEncoder {

	@Override
	public String hashPassword(String password, int levelOfSalt) {
		return BCrypt.hashpw(password, BCrypt.gensalt(levelOfSalt));
	}

}
