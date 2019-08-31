package playfun.core.controller.constraints.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import playfun.core.controller.constraints.IParameterConstraint;
import playfun.core.controller.constraints.IValueConstraint;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ValidateBy {

	Class<? extends IParameterConstraint> value();
	
}
