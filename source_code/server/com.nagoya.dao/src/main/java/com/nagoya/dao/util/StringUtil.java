package com.nagoya.dao.util;

public final class StringUtil {

	private StringUtil() {
		// helper class
	}
	
	public static boolean isNullOrBlank(String string) {
		if (string == null) {
			return true;
		}
		
		if (string.trim().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isNotNullOrBlank(String string) {
		return !isNullOrBlank(string);
	}
	
}
