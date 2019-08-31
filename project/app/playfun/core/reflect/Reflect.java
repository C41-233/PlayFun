package playfun.core.reflect;

import java.util.HashMap;

public final class Reflect {

	private static final HashMap<Class, Class> primitiveToBox = new HashMap<>();
	
	static {
		register_box(boolean.class, Boolean.class);
		register_box(byte.class, Byte.class);
		register_box(short.class, Short.class);
		register_box(int.class, Integer.class);
		register_box(long.class, Long.class);
		register_box(char.class, Character.class);
		register_box(float.class, Float.class);
		register_box(double.class, Double.class);
		register_box(void.class, Void.class);
	}

	private static void register_box(Class primitive, Class box) {
		primitiveToBox.put(primitive, box);
	}
	
	public static Class<?> toBoxClass(Class<?> type) {
		Class cl = primitiveToBox.get(type);
		return cl != null ? cl : type;
	}
	
	public static <T extends Exception> RuntimeException rethrow(T e){
		throw (RuntimeException)e;
	}
	
}
