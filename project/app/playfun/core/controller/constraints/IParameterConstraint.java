package playfun.core.controller.constraints;

import java.lang.annotation.Annotation;

public interface IParameterConstraint<TAnnotation extends Annotation> {

	public Object validate(TAnnotation annotation, Class<?> type, Object value) throws ValidationFailException;
	
}
