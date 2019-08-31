package controllers;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.apache.ivy.core.event.download.StartArtifactDownloadEvent;

import playfun.core.controller.ViewControllerBase;
import playfun.core.controller.constraints.annotation.ArrayValue;
import playfun.core.controller.constraints.annotation.StringValue;

public class Application extends ViewControllerBase {

	public static class P{
		public String name;
		public String age;
	}
	
    public static void index(@StringValue(required=true, minLength=2) @ArrayValue(minLength=2) P p) {
    	renderText(p.name);
    }

    public static void index2(@ArrayValue(minLength=2) @StringValue(required=true, minLength=2) String[] name) {
    	renderText(Arrays.toString(name));
    }

}