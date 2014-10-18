package com.ssi.util;

public class StringUtil {
	private StringUtil() {
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	public static String trim(String str) {
		if (isEmpty(str)) {
			return "";
		}
		return str.trim();
	}

	public static String trimAndUpper(String str) {
		return trim(str).toUpperCase();
	}

}