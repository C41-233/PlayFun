package playfun.core.controller.constraints.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import playfun.core.controller.constraints.IValueConstraint;
import playfun.core.controller.constraints.ValidationFailException;
import playfun.core.controller.constraints.annotation.Id.Validation;

@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
@ValidateBy(Validation.class)
public @interface Id {

	public static class Validation implements IValueConstraint<Id>{

		@Override
		public Object validate(Id annotation, Class<?> type, Object value) throws ValidationFailException {
			if(type != long.class) {
				throw ValidationFailException.configError(Id.class, type);
			}
			if(value == null) {
				throw ValidationFailException.parameterRequired();
			}
			long id = (long)value;
			if(id <= 0) {
				throw ValidationFailException.idValueError();
			}
			return null;
		}

	}
	
}
