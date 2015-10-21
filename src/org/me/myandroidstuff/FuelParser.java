package org.me.myandroidstuff;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public class FuelParser {
	
	public FuelParser()
	{
		
	}
	
	public ArrayList<FuelType> getData(String RSSResult) throws IOException
    {
    	ArrayList<FuelType> fuelData = new ArrayList<FuelType>();
		try {
			//Setup the strings I need to store the data from the XML temporarily before building the Carpark Object.
    		String fuelName = "", highest = "", lowest = "", average = "";
    		//Setup the Pull Parser
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		factory.setNamespaceAware(true);
    		XmlPullParser xpp = factory.newPullParser();
    		xpp.setInput(new StringReader(RSSResult));
    		int eventType = xpp.getEventType();
    		//Loop through the XML
    		while (eventType != XmlPullParser.END_DOCUMENT) {
    			//I want to check for an End tag first, I am looking for the 'fuel'
    			//this is because each car park in the stream has a start and end tag of 'fuel'.
    			if (eventType == XmlPullParser.END_TAG) {
    				if (xpp.getName().equalsIgnoreCase("fuel")) {
    					//Build the object and add it to the ArrayList
    					fuelData.add(new FuelType(fuelName, highest, lowest, average));
    					//Since I have already built the object there is no need to continue through this
    					//while loop. I can set the eventType to the next tag and then go back to the top
    					//of the while.
    					eventType = xpp.next();
    					continue;
    				}
    			}
    			if (eventType == XmlPullParser.START_TAG) {
    				if (xpp.getName().equalsIgnoreCase("error")) {
    					Log.e("XML", "Rate Limit was reached!");
    					throw new IOException("Rate Limit reached");
    				}
    				else {
    					if (xpp.getName().equalsIgnoreCase("fuel")) {
    						fuelName = xpp.getAttributeValue(null, "type");
    						Log.i("XML", "Found the Fuel Type: " + fuelName);
    					}
    					else {
    						if (xpp.getName().equalsIgnoreCase("highest")) {
    							highest = xpp.nextText();
    							Log.i("XML", "Found the Highest: " + highest);
    						}
    						else {
    							if (xpp.getName().equalsIgnoreCase("average")) {
    								average = xpp.nextText();
    								Log.i("XML", "Found the Average: " + average);
    							}
    							else {
    								if (xpp.getName().equalsIgnoreCase("lowest")) {
    									lowest = xpp.nextText();
    									Log.i("XML", "Found the lowest: " + lowest);
    								}
    							}
    						}
    					}
    				}
    			}
    			eventType = xpp.next(); //Important! It moves to the next tag in the XML.
    		}
    		return fuelData;
    	}
    	catch (Exception ex) {
    		throw new IOException("Error parsing");
    	}
    }
}
