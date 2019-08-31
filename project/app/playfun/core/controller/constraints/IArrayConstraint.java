package playfun.core.controller.constraints;

import java.lang.annotation.Annotation;

public interface IArrayConstraint<TAnnotation extends Annotation> {

	public Object validate(TAnnotation annotation, Object array) throws ValidationFailException;
	
}
