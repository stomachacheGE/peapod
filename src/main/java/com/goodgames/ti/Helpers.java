package com.goodgames.ti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Helpers {

	public static String getJSON(InputStream s) throws IOException {
	
        BufferedReader br = new BufferedReader(new InputStreamReader(s));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();
	}
    
    public static List<Artifact> parseArtifacts(String json) throws JSONException {
    	JSONObject theJson = new JSONObject(json);
    	JSONArray theJsonArray = theJson.getJSONArray("artifacts");
    	//System.out.println(theJsonArray.getString(0));
    	int numberOfItems = theJsonArray.length();
    	List<Artifact> artifacts = new ArrayList<Artifact>();
    	for (int i=0; i<numberOfItems; i++) {
    		Gson gson = new Gson();
    		Artifact artifact =  gson.fromJson(theJsonArray.getString(i), Artifact.class);
    		artifacts.add(artifact);    		
    	}
    	
    	return artifacts;
    }
    
    public static <T> List<T> parseItems(String json, String name, TypeToken<T> theTypeToken) throws JSONException {
    	JSONObject theJson = new JSONObject(json);
    	JSONArray theJsonArray = theJson.getJSONArray(name);
    	Type theType = theTypeToken.getType();
    	//System.out.println(theJsonArray.getString(0));
    	int numberOfItems = theJsonArray.length();
    	List<T> items = new ArrayList<T>();
    	for (int i=0; i<numberOfItems; i++) {
    		Gson gson = new Gson();
    		//System.out.println(theJsonArray.getString(i));
    		//System.out.println(theJsonArray.getString(i));
    		T item =  gson.fromJson(theJsonArray.getString(i), theType);
    		items.add(item);    		
    	}
    	//Gson gson = new Gson();
    	//Type theType = new TypeToken<List<T>>() {}.getType();
    	//List<T> items = gson.fromJson(theJsonArray.toString(), theType);
    	
    	return items;
    }
}
