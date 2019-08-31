package playfun.core.controller.constraints;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
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

    private static final Class[] BaseTypes = new Class[] {
		String.class, 
		byte.class, Byte.class,
		short.class, Short.class,
		int.class, Integer.class,
		long.class, Long.class,
    };
    
    private static boolean isBaseType(Class cl) {
    	for(Class type : BaseTypes) {
    		if(type == cl) {
    			return true;
    		}
    	}
    	return false;
    }
    
    private static Object parameterParse(Annotation[] annotations, String name, Class parameterType, Object value) throws ValidationFailException {
    	if(parameterType.isArray()) {
    		return parameterParseArray(annotations, name, parameterType, value);
    	}
    	if(isBaseType(parameterType)) {
        	return parameterParseValue(annotations, name, parameterType, value);
    	}
    	return parameterParseObject(annotations, name, parameterType, value);
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
					value = arrayConstraint.validate(annotation, parameterType, value);
				}
				else if(constraint instanceof IValueConstraint) {
					IValueConstraint valueConstraint = (IValueConstraint) constraint;
					Object[] array = (Object[])value;
			    	int len = array.length;
					for(int i=0; i<len; i++) {
						array[i] = valueConstraint.validate(annotation, componentType, array[i]);
					}
				}
				else if(constraint instanceof IBlobConstraint) {
					throw ValidationFailException.configError(getAnnotationClass(annotation), parameterType);
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
				if(constraint instanceof IValueConstraint) {
					IValueConstraint valueConstraint = (IValueConstraint) constraint;
					value = valueConstraint.validate(annotation, parameterType, value);
				}
				else if(constraint instanceof IArrayConstraint || constraint instanceof IBlobConstraint) {
					throw ValidationFailException.configError(getAnnotationClass(annotation), parameterType);
				}
			}
			catch (ValidationFailException e) {
				String msg = Strings.format("%s=%s %s", name, value, e.getMessage());
				throw new ValidationFailException(msg);
			}
    	}
    	return value;
    }
    
    private static Object parameterParseObject(Annotation[] annotations, String name, Class parameterType, Object value) throws ValidationFailException {
    	for(Annotation annotation : annotations) {
    		try {
        		Object constraint = getValidator(annotation);
    			if(constraint instanceof IBlobConstraint) {
    				IBlobConstraint blobConstraint = (IBlobConstraint) constraint;
    				value = blobConstraint.validate(annotation, parameterType, value);
    			}
    			else if(constraint instanceof IArrayConstraint || constraint instanceof IValueConstraint) {
    				throw ValidationFailException.configError(getAnnotationClass(annotation), parameterType);
    			}
    		}
    		catch (ValidationFailException e) {
				String msg = Strings.format("%s %s", name, e.getMessage());
				throw new ValidationFailException(msg);
			}
    	}if(value == null) {
    		return value;
    	}
    	for(Field field : parameterType.getFields()) {
    		if(!Modifier.isPublic(field.getModifiers())) {
    			continue;
    		}
    		try {
				Object fieldValue = field.get(value);
				Object newValue = parameterParse(field.getAnnotations(), name + "." + field.getName(), field.getType(), fieldValue);
				if(fieldValue != newValue) {
					field.set(value, newValue);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
    	}
    	return value;
    }
    
}
