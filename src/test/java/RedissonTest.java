import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author zrz
 * @date 2021/1/29 17:13
 */
public class RedissonTest {

  public void test() throws IOException {
    // 1. Create config object
    Config config = new Config();
    config
        .useClusterServers()
        // use "rediss://" for SSL connection
        .addNodeAddress("redis://127.0.0.1:6379");

    // or read config from file
    config = Config.fromYAML(new File("config-file.yaml"));
    // 2. Create Redisson instance

    // Sync and Async API
    RedissonClient redisson = Redisson.create(config);

    // Reactive API
    RedissonReactiveClient redissonReactive = Redisson.createReactive(config);

    // RxJava2 API
    RedissonRxClient redissonRx = Redisson.createRx(config);
    // 3. Get Redis based implementation of java.util.concurrent.ConcurrentMap
    RMap<Object, Object> map = redisson.getMap("myMap");

    RMapReactive<Object, Object> mapReactive = redissonReactive.getMap("myMap");

    RMapRx<Object, Object> mapRx = redissonRx.getMap("myMap");
    // 4. Get Redis based implementation of java.util.concurrent.locks.Lock
    RLock lock = redisson.getLock("myLock");

    RLockReactive lockReactive = redissonReactive.getLock("myLock");

    RLockRx lockRx = redissonRx.getLock("myLock");
    // 4. Get Redis based implementation of java.util.concurrent.ExecutorService
    RExecutorService executor = redisson.getExecutorService("myExecutorService");
  }
}
