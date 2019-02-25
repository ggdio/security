package br.com.ggdio.security.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisDataException;

/**
 * Redis repository implementation
 * 
 * @author Guilherme Dio
 *
 */
public class SentinelRepository implements KVRepository {
	
	private static JedisSentinelPool pool;
	private static Jedis common;
	
	private final int expirationTimeout;
	
	public static final int NEVER_EXPIRES = -1;
	public static final int DEFAULT_POOL_SIZE = 8;
	public static final int DEFAULT_TIMEOUT = 1000;
	
	
	public SentinelRepository(String clusterName, String sentinelList) {
		this(clusterName, sentinelList, null);
	}
	
	public SentinelRepository(String clusterName, String sentinelList, String password) {
		this(clusterName, sentinelList, password, NEVER_EXPIRES, DEFAULT_POOL_SIZE, DEFAULT_TIMEOUT);
	}
	
	public SentinelRepository(String clusterName, String sentinelList, String password, int expirationTimeout, int poolSize, int timeout) {
		if(clusterName == null) throw new NullPointerException("ClusterName must not be NULL");
		if(sentinelList == null) throw new NullPointerException("SentinelList must not be NULL");
		
		if(pool == null) {
			String[] cluster = sentinelList.split(",");
			
			Set<String> sentinels = new HashSet<String>(cluster.length);
			for(String host : cluster) {
				sentinels.add(host);
			}
			
			GenericObjectPoolConfig cfg = new GenericObjectPoolConfig();
			cfg.setMaxTotal(poolSize + 1); // Plus common
			pool = new JedisSentinelPool(clusterName, sentinels, cfg, timeout, password, 0);
		}
		
		if(common == null) {
			common = pool.getResource();
		}
		
		this.expirationTimeout = expirationTimeout;
	}
	
	@Override
	public boolean has(String key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.exists(key);
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public boolean hasMapField(String key, String field) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.hexists(key, field);
			
		} finally {
			jedis.close();
			
		}
	}

	@Override
	public String getString(String key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.get(key);
			
		} catch(JedisDataException e) {
			return null;
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public Map<String, String> getMap(String key) {
		Jedis jedis = pool.getResource();
		try {
			return jedis.hgetAll(key);
			
		} catch(JedisDataException e) {
			return null;
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public String getMapField(String key, String field) {
		Jedis jedis = pool.getResource();
		try {
			Map<String, String> map = getMap(key);
			
			return map != null ? map.get(field) : null;
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public List<Map<String, String>> scan(int count, String match) {
		Jedis jedis = pool.getResource();
		try {
			List<Map<String, String>> result = new ArrayList<>();
			
			ScanParams scanParams = new ScanParams();
			if(count > 0) {
				scanParams = scanParams.count(count);
			}
			scanParams = scanParams.match(match);
			String cur = ScanParams.SCAN_POINTER_START;
			do {
				ScanResult<String> scanResult = jedis.scan(cur, scanParams);
				
				for (String key : scanResult.getResult()) {
					try {
						Map<String, String> data = getMap(key);
						
						result.add(data);
						
					} catch(Throwable e) {}
				}
				cur = scanResult.getStringCursor();
			} while (!cur.equals(ScanParams.SCAN_POINTER_START));
			
			return result;
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public void setString(String key, String value) {
		Jedis jedis = pool.getResource();
		try {
			jedis.set(key, value);
			expire(key, jedis);
			
		} finally {
			jedis.close();
			
		}
	}

	@Override
	public void setMap(String key, Map<String, String> map) {
		Jedis jedis = pool.getResource();
		try {
			jedis.hmset(key, map);
			expire(key, jedis);
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public void setMapField(String key, String field, String value) {
		Jedis jedis = pool.getResource();
		try {
			jedis.hset(key, field, value);
			expire(key, jedis);
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public Object remove(String key) {
		Jedis jedis = pool.getResource();
		try {
			Object item = getString(key);
			if(item == null) item = getMap(key);
			if(item == null) return null;
			
			jedis.del(key);
			
			return item;
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public String removeMapField(String key, String field) {
		Jedis jedis = pool.getResource();
		try {
			Map<String, String> item = getMap(key);
			jedis.hdel(key, field);
			
			return item != null ? item.get(field) : null;
			
		} finally {
			jedis.close();
			
		}
	}
	
	@Override
	public long currentTime() {
		return Long.parseLong(common.time().get(0));
	}
	
	@Override
	public void close() {
		Jedis jedis = pool.getResource();
		jedis.bgsave();
		
		jedis.close();
		pool.close();
		pool = null;
	}
	
	private void expire(String key, Jedis jedis) {
		if(expirationTimeout != NEVER_EXPIRES) {
			jedis.expire(key, expirationTimeout);
		}
	}

}