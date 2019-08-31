package controllers;

import java.lang.reflect.Array;
import java.util.Arrays;

import org.apache.ivy.core.event.download.StartArtifactDownloadEvent;

import playfun.core.controller.ViewControllerBase;
import playfun.core.controller.constraints.annotation.ArrayValue;
import playfun.core.controller.constraints.annotation.BlobValue;
import playfun.core.controller.constraints.annotation.StringValue;

public class Application extends ViewControllerBase {

	public static class P{
		
		@StringValue(required=true) public String name;
		@StringValue(required=true) public String age;
		
		@Override
		public String toString() {
			return "name=" + name + " age=" + age;
		}
	}
	
    public static void index() {
    	P p = new P();
    	index2(p);
    }

    public static void index2(@BlobValue(defaultValue=true) P p) {
    	renderText(p);
    }

}