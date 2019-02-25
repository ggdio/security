package br.com.ggdio.lang;

public class StringUtils {

	public static String trimAndAvoidEmpty(String value) {
		if(value == null || value.trim().isEmpty()) return null;
		
		return value.trim();
	}

	public static String wordToKey(String value) {
		if(value == null || value.trim().isEmpty()) return null;
		
		return value.replaceAll("\\s", "-").replaceAll("\\.", "").trim().toLowerCase();
	}
	
}
