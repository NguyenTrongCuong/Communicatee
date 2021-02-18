package root;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class CommunicateApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommunicateApplication.class, args);
	}

}
