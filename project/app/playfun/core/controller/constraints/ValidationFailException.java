package playfun.core.controller.constraints;

import playfun.utils.Strings;

public class ValidationFailException extends Exception{

	public ValidationFailException(String format, Object...args) {
		super(Strings.format(format, args));
	}

	public static ValidationFailException configError(Class annotation, Class real) throws ValidationFailException {
		throw new ValidationFailException("注解%s不支持类型%s", annotation.getName(), real.getName());
	}

	public static ValidationFailException parameterRequired() throws ValidationFailException {
		throw new ValidationFailException("参数缺失");
	}

	public static ValidationFailException stringTooLong(String value, int length) throws ValidationFailException {
		throw new ValidationFailException("字符串%s过长，限定为%d", value, length);
	}

	public static ValidationFailException stringTooShort(String value, int length) throws ValidationFailException {
		throw new ValidationFailException("字符串%s过短，限定为%d", value, length);
	}

	public static ValidationFailException stringNotMatch(String value) throws ValidationFailException{
		throw new ValidationFailException("字符串%s不匹配", value);
	}

	public static ValidationFailException arrayTooShort(int length) throws ValidationFailException {
		throw new ValidationFailException("数组元素过少，限定%d", length);
	}

	public static ValidationFailException arrayTooLong(int length) throws ValidationFailException {
		throw new ValidationFailException("数组元素过多，限定%d", length);
	}

	public static ValidationFailException idValueError() throws ValidationFailException {
		throw new ValidationFailException("非法的id数值");
	}
	
}
