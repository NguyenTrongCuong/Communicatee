package root.hibernate_2nd_level_of_cache_config;

import java.util.Map;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.hibernate.RedissonRegionFactory;

public class CustomRegionFactory extends RedissonRegionFactory {
	private static final long serialVersionUID = 1L;

	@Override
	protected RedissonClient createRedissonClient(Map properties) {
		String address = System.getenv().get("REDIS_URL");
		if(address == null) {
			address = "redis://:p930a4db3e26eee29e3b382a81f7daaa87adf58b71be3779a487f4f0abf2b41eb@ec2-34-198-87-71.compute-1.amazonaws.com:21979";
		}
        String[] passwordComponent1 = address.split(":");
        String[] passwordComponent2 = passwordComponent1[2].split("@");
        String password = passwordComponent2[0];
        Config config = new Config();
        config.useSingleServer()
       		  .setConnectionMinimumIdleSize(5)
       		  .setAddress(address)
       		  .setPassword(password);
        return Redisson.create(config);
	}
	
//	@Override
//	protected RedissonClient createRedissonClient(Map properties) {
//		String address = "redis://:p930a4db3e26eee29e3b382a81f7daaa87adf58b71be3779a487f4f0abf2b41eb@ec2-34-198-87-71.compute-1.amazonaws.com:21979";
//        String[] passwordComponent1 = address.split(":");
//        String[] passwordComponent2 =passwordComponent1[2].split("@");
//        String password = passwordComponent2[0];
//        Config config = new Config();
//        config.useSingleServer()
//       		  .setConnectionMinimumIdleSize(5)
//       		  .setAddress(address)
//       		  .setPassword(password);
//        return Redisson.create(config);
//	}
	
	

}

