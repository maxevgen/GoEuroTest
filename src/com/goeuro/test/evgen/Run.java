package com.goeuro.test.evgen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class Run {
	
	public static void main(String[] args) {
		
		// JSON data source url
		String url = "http://api.goeuro.com/api/v2/position/suggest/en/";
		
		// The fields you want to see in your CSV file
		String[] fields = {
				"_id",
				"name",
				"type",
				"geo_position:latitude",
				"geo_position:longitude" };
		
		// csv filename
		String filename = "GoEuroTest.csv";
		
		if (args.length == 0){
		    System.out.println("usage: java -jar GoEuroTest.jar STRING");
		    System.exit(1);
		}
		String userString = args[0];
	    
		JSONdata data = new JSONdata();
		try {
			data.loadFromURL(url + userString);
		} catch (IOException e1) {
			System.err.println("Cannot load data from URL");
			System.exit(1);
		}
		
		String csv = data.getCSV(fields);
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(filename);
			pw.println(csv);
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file " + filename + " for writing!");
		} finally {
			if (pw != null)
				pw.close();
		}
	}
}
