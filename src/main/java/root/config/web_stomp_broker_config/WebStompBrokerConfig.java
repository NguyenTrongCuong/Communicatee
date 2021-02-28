package root.config.web_stomp_broker_config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.Session;
import org.springframework.session.web.socket.config.annotation.AbstractSessionWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebStompBrokerConfig extends AbstractSessionWebSocketMessageBrokerConfigurer<Session> {
	@Autowired
	private Environment env;

	@Override
	protected void configureStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/process-web-socket-handshake")
				.addInterceptors(new HttpSessionHandshakeInterceptor())
				.withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableStompBrokerRelay("/queue", "/topic")
				.setRelayHost(this.env.getProperty("spring.rabbitmq.host"))
				.setRelayPort(Integer.parseInt(this.env.getProperty("spring.rabbitmq.port")))
				.setClientLogin(this.env.getProperty("spring.rabbitmq.username"))
				.setClientPasscode(this.env.getProperty("spring.rabbitmq.password"));
	}
	
	

}
