package playfun.core.logs;

import playfun.utils.Strings;

public final class Logs {
	
	public static void warn(String category, String format, Object... args) {
		System.out.println("[" + category + "] " + Strings.format(format, args));
	}
	
	public static void error(String category, Exception e) {
		System.out.println("[" + category + "] " + e);
	}
	
}
