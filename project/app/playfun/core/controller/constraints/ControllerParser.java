package playfun.core.controller.constraints;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;

import playfun.core.controller.constraints.annotation.ValidateBy;
import playfun.utils.Strings;

public final class ControllerParser {

	private static final HashMap<Class, Object> validators = new HashMap<>();
	
	private static Object getValidator(Annotation annotation) {
		Class annotationType = getAnnotationClass(annotation);
		if(validators.containsKey(annotationType)) {
			return validators.get(annotationType);
		}
		ValidateBy validateBy = (ValidateBy) annotationType.getAnnotation(ValidateBy.class);
		if(validateBy == null) {
			validators.put(annotationType, null);
			return null;
		}
		try {
			Object constraint = validateBy.value().newInstance();
			validators.put(annotationType, constraint);
			return constraint;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static Class getAnnotationClass(Annotation annotation) {
		return annotation.getClass().getInterfaces()[0];
	}
	
    public static Object parameterParse(Parameter parameter, String name, Object value) throws ValidationFailException {
    	return parameterParse(parameter.getAnnotations(), name, parameter.getType(), value);
    }

    private static Object parameterParse(Annotation[] annotations, String name, Class parameterType, Object value) throws ValidationFailException {
    	if(parameterType.isArray()) {
    		return parameterParseArray(annotations, name, parameterType, value);
    	}
    	if(parameterType.isPrimitive() || parameterType == String.class) {
        	return parameterParseValue(annotations, name, parameterType, value);
    	}
    	return value;
    }
    
    private static Object parameterParseArray(Annotation[] annotations, String name, Class parameterType, Object value) throws ValidationFailException {
    	Class componentType = parameterType.getComponentType();
    	if(value == null) {
    		value = Array.newInstance(componentType, 0);
    	}
    	for(Annotation annotation : annotations) {
			try {
				Object constraint = getValidator(annotation);
				if(constraint == null) {
					continue;
				}
				if(constraint instanceof IArrayConstraint) {
					IArrayConstraint arrayConstraint = (IArrayConstraint) constraint;
					value = arrayConstraint.validate(annotation, value);
				}
				else if(constraint instanceof IValueConstraint) {
					IValueConstraint valueConstraint = (IValueConstraint) constraint;
					Object[] array = (Object[])value;
			    	int len = array.length;
					for(int i=0; i<len; i++) {
						array[i] = valueConstraint.validate(annotation, array[i]);
					}
				}
			}
			catch (ValidationFailException e) {
				Object[] array = (Object[])value;
				String msg = Strings.format("%s=%s %s", name, Arrays.toString(array), e.getMessage());
				throw new ValidationFailException(msg);
			}
    	}
    	return value;
    }
    
    private static Object parameterParseValue(Annotation[] annotations, String name, Class parameterType, Object value) throws ValidationFailException {
    	for(Annotation annotation : annotations) {
			try {
	    		Object constraint = getValidator(annotation);
	    		if(constraint == null) {
	    			continue;
	    		}
				if(constraint instanceof IArrayConstraint) {
					throw ValidationFailException.configError(getAnnotationClass(annotation), parameterType);
				}
				else if(constraint instanceof IValueConstraint) {
					IValueConstraint valueConstraint = (IValueConstraint) constraint;
					value = valueConstraint.validate(annotation, value);
				}
			}
			catch (ValidationFailException e) {
				String msg = Strings.format("%s=%s %s", name, value, e.getMessage());
				throw new ValidationFailException(msg);
			}
    	}
    	return value;
    }
    
}
