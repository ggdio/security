package br.com.ggdio.security.client;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class MapUtils {
	
	/**
     * Gets a String from a Map in a null-safe manner.
     * <p>
     * The String is obtained via <code>toString</code>.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a String, <code>null</code> if null map input
     */
    public static Boolean getBoolean(final Map map, final Object key, final Boolean nvl) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return (Boolean) answer;
            }
        }
        return nvl;
    }

	/**
     * Gets a String from a Map in a null-safe manner.
     * <p>
     * The String is obtained via <code>toString</code>.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a String, <code>null</code> if null map input
     */
    public static String getString(final Map map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                return answer.toString();
            }
        }
        return null;
    }
    
    /**
     * Gets a Long from a Map in a null-safe manner.
     * <p>
     * The Long is obtained from the results of {@link #getNumber(Map,Object)}.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Long, <code>null</code> if null map input
     */
    public static Long getLong(final Map map, final Object key) {
        Number answer = getNumber(map, key);
        if (answer == null) {
            return null;
        } else if (answer instanceof Long) {
            return (Long) answer;
        }
        return answer.longValue();
    }
    
    /**
     * Gets a Number from a Map in a null-safe manner.
     * <p>
     * If the value is a <code>Number</code> it is returned directly.
     * If the value is a <code>String</code> it is converted using
     * {@link NumberFormat#parse(String)} on the system default formatter
     * returning <code>null</code> if the conversion fails.
     * Otherwise, <code>null</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Number, <code>null</code> if null map input
     */
	public static Number getNumber(final Map map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer != null) {
                if (answer instanceof Number) {
                    return (Number) answer;
                    
                } else if (answer instanceof String) {
                    try {
                        String text = (String) answer;
                        return NumberFormat.getInstance().parse(text);
                        
                    } catch (ParseException e) {
                        // failure means null is returned
                    }
                }
            }
        }
        return null;
    }
	
	/**
     * Gets a Map from a Map in a null-safe manner.
     * <p>
     * If the value returned from the specified map is not a Map then
     * <code>null</code> is returned.
     *
     * @param map  the map to use
     * @param key  the key to look up
     * @return the value in the Map as a Map, <code>null</code> if null map input
     */
    public static Map getMap(final Map map, final Object key) {
        if (map != null) {
            Object answer = map.get(key);
            if (answer instanceof Map) {
                return (Map) answer;
            }
        }
        return null;
    }
	
}