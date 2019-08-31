package playfun.core.controller.constraints.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;

import playfun.core.controller.constraints.IArrayConstraint;
import playfun.core.controller.constraints.ValidationFailException;
import playfun.core.controller.constraints.annotation.ArrayValue.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@ValidateBy(Validation.class)
public @interface ArrayValue {

	int minLength() default 0;
	int maxLength() default Integer.MAX_VALUE;
	
	public static class Validation implements IArrayConstraint<ArrayValue>{

		@Override
		public Object validate(ArrayValue annotation, Class type, Object value) throws ValidationFailException {
			int length = Array.getLength(value);
			if(length < annotation.minLength()) {
				throw ValidationFailException.arrayTooShort(annotation.minLength());
			}
			if(length > annotation.maxLength()) {
				throw ValidationFailException.arrayTooLong(annotation.maxLength());
			}
			return value;
		}
		
	}
	
}
