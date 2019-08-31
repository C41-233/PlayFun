package playfun.core.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import javax.validation.ValidationException;

import org.hibernate.engine.jdbc.spi.ResultSetReturn;

import com.sun.org.apache.bcel.internal.generic.NEW;

import play.mvc.ActionInvoker;
import play.mvc.Before;
import play.mvc.Controller;
import play.utils.Java;
import playfun.core.controller.constraints.ControllerParser;
import playfun.core.controller.constraints.ValidationFailException;
import playfun.core.logs.Logs;
import playfun.core.reflect.Reflect;
import playfun.utils.Strings;

public abstract class ControllerBase extends Controller{

    @Before
    private static void before() throws Exception{
        Method method = request.invokedMethod;
        Parameter[] parameters = method.getParameters();
        String[] names = Java.parameterNames(method);
        Object[] args = ActionInvoker.getActionMethodArgs(method, request.controllerInstance);

        for(int i=0, len = parameters.length; i<len; i++) {
    		args[i] = ControllerParser.parameterParse(parameters[i], names[i], args[i]);
        }
    }

}
