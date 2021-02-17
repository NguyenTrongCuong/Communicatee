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
//		String address = "redis://:pe8c8d91c1bd757d4debe5906c5a9f6283768954a859ce302d4b9413802dfed25@ec2-50-17-14-172.compute-1.amazonaws.com:31469";
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

