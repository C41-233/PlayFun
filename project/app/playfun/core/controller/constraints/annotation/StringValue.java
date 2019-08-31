package playfun.core.controller.constraints.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.thoughtworks.xstream.mapper.Mapper.Null;

import playfun.core.controller.constraints.IValueConstraint;
import playfun.core.controller.constraints.ValidationFailException;
import playfun.core.controller.constraints.annotation.StringValue.Validation;
import playfun.utils.Strings;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@ValidateBy(Validation.class)
public @interface StringValue {

	boolean required() default false;
	boolean trim() default true;
	boolean emptyAsNull() default true;
	int maxLength() default Integer.MAX_VALUE;
	int minLength() default 0;
	String match() default "";
	
	public static class Validation implements IValueConstraint<StringValue>{

		@Override
		public Object validate(StringValue annotation, Object value) throws ValidationFailException {
			if(value == null || value instanceof String) {
				return validate(annotation, (String)value);
			}
			return ValidationFailException.configError(StringValue.class, value.getClass());
		}

		private String validate(StringValue annotation, String value) throws ValidationFailException {
			if(value != null) {
				if(annotation.trim()) {
					value = value.trim();
				}
				if(value.isEmpty() && annotation.emptyAsNull()) {
					value = null;
				}
			}
			if(value == null && annotation.required()) {
				throw ValidationFailException.parameterRequired();
			}
			if(value != null) {
				int length = value.length();
				if(length > annotation.maxLength()) {
					throw ValidationFailException.stringTooLong(value, annotation.maxLength());
				}
				if(length < annotation.minLength()) {
					throw ValidationFailException.stringTooShort(value, annotation.minLength());
				}
				String match = annotation.match();
				if(!match.isEmpty() && !value.matches(match)) {
					throw ValidationFailException.stringNotMatch(value);
				}
			}
			return value;
		}
		
	}
	
}
