package root.session_config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;

@Configuration
@EnableRedisHttpSession
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {
	
	@Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
    	String address = System.getenv().get("REDIS_URL");
        String[] passwordComponent1 = address.split(":");
        String[] passwordComponent2 = passwordComponent1[2].split("@");
        String password = passwordComponent2[0];
        Config config = new Config();
        config.useSingleServer()
       		  .setConnectionMinimumIdleSize(10)
       		  .setAddress(address)
       		  .setPassword(password);
        return Redisson.create(config);
    }
    
//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redisson() {
//        Config config = new Config();
//        config.useSingleServer()
//       		  .setConnectionMinimumIdleSize(10)
//       		  .setAddress("redis://:pe8c8d91c1bd757d4debe5906c5a9f6283768954a859ce302d4b9413802dfed25@ec2-50-17-14-172.compute-1.amazonaws.com:31469")
//       		  .setPassword("pe8c8d91c1bd757d4debe5906c5a9f6283768954a859ce302d4b9413802dfed25");
//        return Redisson.create(config);
//    }

}
