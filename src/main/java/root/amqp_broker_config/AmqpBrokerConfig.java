package root.amqp_broker_config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class AmqpBrokerConfig {
	@Autowired
	private Environment env;
	
	@Bean(name={"amqpConnectionFactory"})
	public ConnectionFactory connectionFactoryConfig() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(this.env.getProperty("spring.rabbitmq.host"));
		connectionFactory.setPort(5672);
		connectionFactory.setUsername(this.env.getProperty("spring.rabbitmq.username"));
		connectionFactory.setPassword(this.env.getProperty("spring.rabbitmq.password"));
		connectionFactory.setChannelCacheSize(25);
		return connectionFactory;
	}
	
	@Bean(name={"amqpAdmin"})
	public AmqpAdmin amqpAdminConfig() {
		return new RabbitAdmin(this.connectionFactoryConfig());
	}
	
	

}






