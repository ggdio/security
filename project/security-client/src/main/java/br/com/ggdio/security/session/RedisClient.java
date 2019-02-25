package br.com.ggdio.security.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.exceptions.JedisDataException;

class RedisClient {

	private static JedisPool jedisPool;
	private static JedisSentinelPool sentinelPool;

	private static Jedis common;

	public static final int DEFAULT_POOL_SIZE = 8;
	public static final int DEFAULT_TIMEOUT = 1000;

	/**
	 * Redis as is constructor <br>
	 * It won't use sentinel <br>
	 * For sentinel mode use {@link #RedisClient(String, String, String)}
	 * 
	 * @param host     - REDIS hostname
	 * @param port     - REDIS port
	 * @param password - REDIS password
	 */
	RedisClient(String host, int port, String password) {
		if (host == null)
			throw new IllegalArgumentException("Host must not be NULL");

		JedisPoolConfig cfg = new JedisPoolConfig();
		cfg.setMaxTotal(DEFAULT_POOL_SIZE + 1); // Plus common

		if (jedisPool == null) {
			jedisPool = new JedisPool(cfg, host, port, DEFAULT_TIMEOUT, password);
		}

		if (common == null) {
			common = jedisPool.getResource();
		}
	}

	/**
	 * Sentinel constructor <br>
	 * It will use redis over sentinel <br>
	 * For using redis without sentinel use
	 * {@link #RedisClient(String, int, String)}
	 * 
	 * @param clusterName
	 * @param sentinelList
	 * @param password
	 */
	RedisClient(String clusterName, String sentinelList, String password) {
		if (clusterName == null)
			throw new NullPointerException("ClusterName must not be NULL");
		if (sentinelList == null)
			throw new NullPointerException("SentinelList must not be NULL");

		if (sentinelPool == null) {
			String[] cluster = sentinelList.split(",");

			Set<String> sentinels = new HashSet<String>(cluster.length);
			for (String host : cluster) {
				sentinels.add(host);
			}

			GenericObjectPoolConfig cfg = new GenericObjectPoolConfig();
			cfg.setMaxTotal(DEFAULT_POOL_SIZE + 1); // Plus common
			sentinelPool = new JedisSentinelPool(clusterName, sentinels, cfg, DEFAULT_TIMEOUT, password, 0);
		}

		if (common == null) {
			common = sentinelPool.getResource();
		}
	}

	void set(Session session) {
		if (session == null)
			return;

		long expiresIn = session.getExpiresIn();

		int presizeSessionMap = session.getApps().size() + session.getRoles().size() + session.getActions().size() + 5;
		Map<String, String> sessionMap = new HashMap<>(presizeSessionMap);
		sessionMap.put("token", session.getToken());
		sessionMap.put("tokenType", session.getTokenType());
		sessionMap.put("userId", session.getUserId());
		sessionMap.put("expiresIn", String.valueOf(expiresIn));
		sessionMap.put("refTimestamp", String.valueOf(session.getRefTimestamp()));

		int count = 0;
		for (String app : session.getApps()) {
			sessionMap.put("app." + (count++), app);
		}

		count = 0;
		for (String role : session.getRoles()) {
			sessionMap.put("role." + (count++), role);
		}

		count = 0;
		for (String action : session.getActions()) {
			sessionMap.put("action." + (count++), action);
		}

		setMap(getSessionNamespace(session.getToken()), sessionMap, expiresIn);
	}

	Session get(String token) {
		Map<String, String> sessionMap = getMap(getSessionNamespace(token));
		if (sessionMap == null)
			return null;

		Session session = new Session();
		session.setToken(token);
		session.setTokenType(sessionMap.get("tokenType"));
		session.setUserId(sessionMap.get("userId"));
		session.setExpiresIn(calcExpiresIn(sessionMap));

		for (int c = 0;; c++) {
			String key = "app." + c;
			if (!sessionMap.containsKey(key))
				break;

			session.addApp(sessionMap.get(key));
		}

		for (int c = 0;; c++) {
			String key = "role." + c;
			if (!sessionMap.containsKey(key))
				break;

			session.addRole(sessionMap.get(key));
		}

		for (int c = 0;; c++) {
			String key = "action." + c;
			if (!sessionMap.containsKey(key))
				break;

			session.addAction(sessionMap.get(key));
		}

		return session;
	}

	private long calcExpiresIn(Map<String, String> map) {
		long refTimestamp = Long.parseLong(map.get("refTimestamp"));
		long actualExpiresIn = Long.parseLong(map.get("expiresIn"));
		long expiresIn = actualExpiresIn - (System.currentTimeMillis() - refTimestamp);
		return expiresIn;
	}

	boolean exists(String token) {
		Jedis jedis = getResource();
		try {
			return jedis.exists(token);

		} finally {
			jedis.close();

		}
	}

	void remove(String token) {
		Jedis jedis = getResource();
		try {
			jedis.del(token);

		} finally {
			jedis.close();

		}
	}

	private Map<String, String> getMap(String key) {
		Jedis jedis = getResource();
		try {
			Map<String, String> data = jedis.hgetAll(key);

			return data;

		} catch (JedisDataException e) {
			return null;

		} finally {
			jedis.close();

		}
	}

	public void setMap(String key, Map<String, String> map, long timeout) {
		Jedis jedis = getResource();
		try {
			jedis.hmset(key, map);
			expire(timeout, key, jedis);

		} finally {
			jedis.close();

		}
	}

	public List<Map<String, String>> scan(int count, String match) {
		Jedis jedis = getResource();
		try {
			List<Map<String, String>> result = new ArrayList<>();

			ScanParams scanParams = new ScanParams();
			if (count > 0) {
				scanParams = scanParams.count(count);
			}
			scanParams = scanParams.match(match);
			String cur = ScanParams.SCAN_POINTER_START;
			do {
				ScanResult<String> scanResult = jedis.scan(cur, scanParams);

				for (String key : scanResult.getResult()) {
					try {
						Map<String, String> data = getMap(key);
						if (data != null) {
							data.put("_key", key);

							result.add(data);
						}

					} catch (Throwable e) {
					}
				}
				cur = scanResult.getStringCursor();
			} while (!cur.equals(ScanParams.SCAN_POINTER_START));

			return result;

		} finally {
			jedis.close();

		}
	}

	private String getSessionNamespace(String token) {
		return "security:" + token + ":session";
	}

	public long currentTimestamp() {
		return Long.parseLong(common.time().get(0));
	}

	private Jedis getResource() {
		if (jedisPool != null)
			return jedisPool.getResource();

		return sentinelPool.getResource();
	}

	private void expire(long timeout, String key, Jedis jedis) {
		int seconds = (int) (timeout / 1000);
		jedis.expire(key, seconds);
	}

}