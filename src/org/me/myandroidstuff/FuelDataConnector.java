package org.me.myandroidstuff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class FuelDataConnector {
	private static String townURL = "http://www.petrolprices.com/feeds/averages.xml?search_type=town&search_value=";
	private static String countyURL = "http://www.petrolprices.com/feeds/averages.xml?search_type=county&search_value=";
	private static String postURL = "http://www.petrolprices.com/feeds/averages.xml?search_type=postcode&search_value=";
	
	private String downloadResult;
	private String URLString;
	
	public String Result() { return downloadResult; }
	
	public FuelDataConnector(String searchType, String searchLocation)
	{
		downloadResult = "";
		if (searchType.equalsIgnoreCase("town"))
		{
			URLString = townURL + searchLocation;
		}
		else
		{
			if (searchType.equalsIgnoreCase("county"))
			{
				URLString = countyURL + searchLocation;
			}
			else
			{
				if (searchType.equalsIgnoreCase("postcode"))
				{
					URLString = postURL + searchLocation;
				}
				else
				{
					URLString = townURL + searchLocation;
				}
			}
		}
		Log.i("DATA", "UrlString Got! : " + URLString);
	}
	
	public String DownloadStreamToString() throws IOException
	{
		String result = "";
    	InputStream anInStream = null;
    	int response = -1;
    	URL url = new URL(URLString);
    	URLConnection conn = url.openConnection();
    	
    	// Check that the connection can be opened
    	if (!(conn instanceof HttpURLConnection))
    		throw new IOException("Not an HTTP connection");
    	try
    	{
    		// Open connection
    		HttpURLConnection httpConn = (HttpURLConnection) conn;
    		httpConn.setAllowUserInteraction(false);
    		httpConn.setInstanceFollowRedirects(true);
    		httpConn.setRequestMethod("GET");
    		httpConn.connect();
    		response = httpConn.getResponseCode();
    		// Check that connection is Ok
    		if (response == HttpURLConnection.HTTP_OK)
    		{
    			// Connection is Ok so open a reader 
    			anInStream = httpConn.getInputStream();
    			InputStreamReader in= new InputStreamReader(anInStream);
    			BufferedReader bin= new BufferedReader(in);
    			
    			// Throw away the header
    			bin.readLine();
    			// Read in the data from the RSS stream
    			String line = new String();
    			while (( (line = bin.readLine())) != null)
    			{
    				result = result + "\n" + line;
    			}
    		}
    	}
    	catch (Exception ex)
    	{
    		throw new IOException("Error connecting");
    	}
    	
    	// Return result as a string for further processing
    	this.downloadResult = result;
    	return result;
	}
}
