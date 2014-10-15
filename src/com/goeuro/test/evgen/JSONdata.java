package com.goeuro.test.evgen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * The JSONdata class provides the required functionality to JSONArray.
 */
public class JSONdata {

	/**
	 * JSONArray contains parsed JSON data.
	 */
	private JSONArray jsonData = null;
	
	/**
	 * Loads data from the specified url to the JSONArray.
	 * @param userURL JSON data source url.
	 * @throws IOException
	 */
	public void loadFromURL(String userURL) throws IOException {
		
		URL url = new URL(userURL);
		URLConnection connect = url.openConnection();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
		String rawJSON = "";
		String readline;
		while ((readline = in.readLine()) != null)
			rawJSON += readline;
		in.close();
		
		this.jsonData = new JSONArray(rawJSON);
	}

	/**
	 * Returns CSV data from JSONArray for specified fields.
	 * @param fields The fields you want to see in your CSV file.
	 * @return CSV data as a String.
	 */
	public String getCSV(String[] fields) {
		
		JSONArray simplearray = this.simplify(fields);
		
		// Use, if you don't care about column order
		// return CDL.toString(simplearray);
		
		// Use, if you need an order, specified by String[] fields
		JSONArray fieldsArray = new JSONArray();
		for (String s : fields) {
			if (s.contains(":")) {
				String[] nested = s.split(":");
				s = nested[1];
			}
			fieldsArray.put(s);
		}
		return CDL.toString(fieldsArray, simplearray);
	}
	
	/**
	 * Creates a simple JSONArray with fields which are specified.
	 * @param fields The fields you want to see in your new JSONArray.
	 * @return simple JSONArray with fields which were specified.
	 */
	private JSONArray simplify (String[] fields) {
		
		JSONArray simplearray = new JSONArray();
		
		for (int i = 0; i < jsonData.length(); i++) {
			
			JSONObject currentJSONobject = jsonData.getJSONObject(i);
			JSONObject newJSONobject = new JSONObject();
			
			for (String field : fields) {
				
				// get data from nested JSON object
				// works only with one nested level (according to the task)
				if (field.contains(":")) {
					String[] part = field.split(":");
					if (currentJSONobject.has(part[0])) {
						JSONObject nestedJSON = currentJSONobject.getJSONObject(part[0]);
						if (nestedJSON.has(part[1])) {
							newJSONobject.put(part[1], nestedJSON.get(part[1]));
						}
					}
				}
				
				// get JSON value for key
				else if (currentJSONobject.has(field))
					newJSONobject.put(field, currentJSONobject.get(field));
			}
			
			simplearray.put(newJSONobject);
		}
		return simplearray;
	}
}
