package br.com.ggdio.reflection;

import java.lang.reflect.Field;

/**
 * Reflection utils methods
 * 
 * @author Guilherme Dio
 *
 */
public class ReflectionUtils {

	public static void forceSetField(Object target, String fieldName, Object value) {
		try {
			Field field = target.getClass().getDeclaredField(fieldName);
			
			boolean accessible = field.isAccessible();
			if(!accessible) field.setAccessible(true);
			
			field.set(target, value);
			
			if(!accessible) field.setAccessible(false);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}