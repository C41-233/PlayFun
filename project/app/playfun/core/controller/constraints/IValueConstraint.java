package playfun.core.controller.constraints;

import java.lang.annotation.Annotation;

public interface IValueConstraint<TAnnotation extends Annotation> {

	public Object validate(TAnnotation annotation, Object value) throws ValidationFailException;
	
}
