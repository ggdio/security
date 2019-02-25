package br.com.ggdio.security.redis;

import java.util.List;
import java.util.Map;

public interface KVRepository {

	boolean has(String key);

	boolean hasMapField(String key, String field);

	String getString(String key);

	Map<String, String> getMap(String key);

	String getMapField(String key, String field);
	
	List<Map<String, String>> scan(int count, String match);

	void setString(String key, String value);

	void setMap(String key, Map<String, String> map);

	void setMapField(String key, String field, String value);
	
	Object remove(String key);
	
	String removeMapField(String key, String field);
	
	long currentTime();
	
	void close();
	
}