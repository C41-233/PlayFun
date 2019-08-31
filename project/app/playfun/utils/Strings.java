package playfun.utils;

public final class Strings {

	public static String format(String format, Object... args) {
		if(args == null || args.length == 0) {
			return format;
		}
		return String.format(format, args);
	}
	
}
