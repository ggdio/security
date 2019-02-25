package br.com.ggdio;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.ggdio.security.domain.model.Action;
import br.com.ggdio.security.domain.model.Role;
import br.com.ggdio.security.infrastructure.entity.RoleEntity;

public class ReflectionCopyTest {

	@SuppressWarnings("serial")
	public static void main(String[] args) {
		Role domain = new Role(5l, "data.owner", new HashSet<Action>() {{
			add(new Action(1l, "viewx.buttona.click"));
			add(new Action(2l, "viewy.buttonb.click"));
		}});
		domain.setDescription("Data Owner");
		RoleEntity entity = new RoleEntity();
		
		
		copy(domain, entity);
		System.out.println(entity);
		
	}
	
	public static void copy(Object source, Object destiny) {
		try {
			for (Method method : destiny.getClass().getDeclaredMethods()) {
				if(method.getName().startsWith("set")) {
					String attribute = method.getName().substring(3);
					Method getter = findGetter(source, attribute);
					if(getter != null) {
						boolean isSet = getter.getReturnType().isAssignableFrom(Set.class);
						if(isSet || getter.getReturnType().isAssignableFrom(List.class)) {
							Class<?> entityClazz = getActualTypeArguments(method.getGenericParameterTypes()[0]);
							Collection<Object> destinyValues;
							if(isSet) {
								destinyValues = new HashSet<>();
							} else {
								destinyValues = new ArrayList<>();
							}
							Collection<?> originValues = (Collection<?>) getter.invoke(source);
							for (Object value : originValues) {
								Object entity = entityClazz.getConstructor(value.getClass()).newInstance(value);
								destinyValues.add(entity);
							}
							method.invoke(destiny, destinyValues);
							
						} else {
							method.invoke(destiny, getter.invoke(source));
						}
					}
				}
			}
		} catch(Exception e) {
			throw new IllegalStateException("Could not deep copy data from a domain object to an entity object. Please check your fields, getters and setters.", e);
			
		}
	}

	private static Method findGetter(Object obj, String attribute) {
		try {
			return obj.getClass().getDeclaredMethod("get" + attribute);
			
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
			
		}
	}
	
    private static Class<?> getActualTypeArguments(Type type) {
        ParameterizedType pt = (ParameterizedType) type;
        String clazz = pt.getActualTypeArguments()[0].toString().split("\\s")[1];
        
        try {
        	return (Class<?>) Class.forName(clazz);
        } catch(Exception e) {
        	throw new RuntimeException(e);
        }
    }
	
}