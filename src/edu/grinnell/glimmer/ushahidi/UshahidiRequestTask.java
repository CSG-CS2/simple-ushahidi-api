package edu.grinnell.glimmer.ushahidi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.os.AsyncTask;

public class UshahidiRequestTask extends AsyncTask<String, String, String>{


	protected String doInBackground(String... input) {
		try {
			return makeRequest(input[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	String makeRequest(String inputURL) throws Exception{
		URL serverURL = new URL(inputURL);
		
		// Connect to the server
		HttpURLConnection connection;
		BufferedReader input; // Textual input from the server
		String line; // One line of data from the server
		String text = ""; // Data read from the server
		String data = ""; //Final result to return
		
		try {
		    connection = (HttpURLConnection) serverURL.openConnection();
		    connection.connect();

		// Read the data from the server
		try {
		    input = new BufferedReader(new InputStreamReader(
			    connection.getInputStream()));
		} catch (Exception e) {
		    throw new Exception("Could not get Ushahidi data from server.");
		}

		while ((line = input.readLine()) != null) {
		    text += line;
		} // while

		// Testing: What does the text look like?
		// System.out.println(text);

		// Grab the JSON text, which starts with an open brace
		text = text.substring(text.indexOf("{"));

		} catch (Exception e) {
		    throw new Exception("Could not connect to server because "
		    		+ e.toString());
		}

		return text;
		
	}
	
}