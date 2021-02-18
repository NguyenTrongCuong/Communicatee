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
    	if(address == null) {
    		address = "redis://:p930a4db3e26eee29e3b382a81f7daaa87adf58b71be3779a487f4f0abf2b41eb@ec2-34-198-87-71.compute-1.amazonaws.com:21979";
    	}
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
//       		  .setAddress("redis://:p930a4db3e26eee29e3b382a81f7daaa87adf58b71be3779a487f4f0abf2b41eb@ec2-34-198-87-71.compute-1.amazonaws.com:21979")
//       		  .setPassword("p930a4db3e26eee29e3b382a81f7daaa87adf58b71be3779a487f4f0abf2b41eb");
//        return Redisson.create(config);
//    }

}
