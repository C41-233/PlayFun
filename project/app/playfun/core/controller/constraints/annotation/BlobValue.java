package playfun.core.controller.constraints.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import playfun.core.controller.constraints.IBlobConstraint;
import playfun.core.controller.constraints.ValidationFailException;
import playfun.core.controller.constraints.annotation.BlobValue.Validation;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@ValidateBy(Validation.class)
public @interface BlobValue {

	boolean required() default false;
	boolean defaultValue() default false;
	
	public static class Validation implements IBlobConstraint<BlobValue>{

		@Override
		public Object validate(BlobValue annotation, Class type, Object value) throws ValidationFailException {
			if(value == null) {
				if(annotation.defaultValue()) {
					try {
						value = type.newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				if(annotation.required()) {
					throw ValidationFailException.parameterRequired();
				}
			}
			return value;
		}
		
	}
	
}
