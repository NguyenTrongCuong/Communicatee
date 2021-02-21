package root.utils.queues;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("directQueuesDeclaration")
public class DirectQueueDeclaration implements QueueDeclaration {
	@Autowired
	@Qualifier("amqpAdmin")
	private AmqpAdmin amqpAdmin;

	@Override
	public void declareQueues(String queueName) {
		this.amqpAdmin.declareQueue(new Queue(queueName));
	}

	

}
