package org.me.myandroidstuff;


import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class PetrolPriceActivity extends Activity implements OnClickListener
{	
    private Button searchButton;
    private Button backButton;
    private EditText townValue;
    private LinearLayout displayLayout;
    private Spinner categorySpinner;
    private ViewSwitcher switcher;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        searchButton = (Button)findViewById(R.id.searchButton);
        backButton = (Button)findViewById(R.id.backButton);
        townValue = (EditText)findViewById(R.id.townValue);
        displayLayout = (LinearLayout)findViewById(R.id.resultDisplay);
        categorySpinner = (Spinner)findViewById(R.id.category);
        switcher = (ViewSwitcher)findViewById(R.id.vwSwitch);
        
        searchButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }
    
	public void onClick(View arg0) 
	{
		if (arg0 == searchButton)
		{
			try
	        {	
				LoadFuelDataAsyncTask tasker = new LoadFuelDataAsyncTask();
				tasker.execute();
				
				switcher.showNext();
	        }
	        catch(Exception ex)
	        {
	        	DisplayErrorText("Oops! Seems there was a problem with getting the data. Please try again.");
	        } 
		}
		if (arg0 == backButton)
		{
			switcher.showPrevious();
			
			displayLayout.removeAllViews();
			DisplayProgressBar();
		}
	}
	
	private void DisplayResults(ArrayList<FuelType> data)
	{
		displayLayout.removeAllViews();
		LinearLayout.LayoutParams headingParam = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);

		LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
		textParam.setMargins(20, 0, 0, 0);
		
		for (int i = 0; i < data.size(); i++)
		{
			FuelType tempType = data.get(i);
			
			LinearLayout newLayout = new LinearLayout(this);
			newLayout.setOrientation(LinearLayout.VERTICAL);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			layout.setMargins(20, 7, 20, 7);
			newLayout.setLayoutParams(layout);
			newLayout.setPadding(20, 20, 20, 20);

			if (tempType.Name().equalsIgnoreCase("diesel"))
			{
				newLayout.setBackground(getResources().getDrawable(R.drawable.border_black));
			}
			else
			{
				if (tempType.Name().equalsIgnoreCase("unleaded"))
				{
					newLayout.setBackground(getResources().getDrawable(R.drawable.border_green));
				}
				else
				{
					if (tempType.Name().equalsIgnoreCase("super unleaded"))
					{
						newLayout.setBackground(getResources().getDrawable(R.drawable.border_red));
					}
					else
					{
						newLayout.setBackground(getResources().getDrawable(R.drawable.border_blue));
					}
				}
				
			}
			
			
			//Get and setup the stored values in TextView objects to append to my LinearLayout
			TextView fuelType = new TextView(this);
			fuelType.setText(tempType.Name());
			fuelType.setTextColor(Color.BLACK);
			fuelType.setTextSize(16);
			fuelType.setLayoutParams(headingParam);
			fuelType.setPadding(0, 0, 0, 5);
			newLayout.addView(fuelType);
			
			TextView high = new TextView(this);
			high.setText(tempType.HighestString());
			high.setTextColor(Color.parseColor("#e50000"));
			high.setLayoutParams(textParam);
			high.setPadding(0, 0, 0, 5);
			newLayout.addView(high);
			
			TextView avg = new TextView(this);
			avg.setText(tempType.AverageString());
			avg.setTextColor(Color.parseColor("#116493"));
			avg.setLayoutParams(textParam);
			avg.setPadding(0, 0, 0, 5);
			newLayout.addView(avg);
			
			TextView low = new TextView(this);
			low.setText(tempType.LowestString());
			low.setTextColor(Color.parseColor("#74d600"));
			low.setLayoutParams(textParam);
			low.setPadding(0, 0, 0, 5);
			newLayout.addView(low);
			
			displayLayout.addView(newLayout);
		}
	}
	
	private void DisplayErrorText(String errorMsg)
	{
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		//Set the TextView
		TextView errText = new TextView(this);
		errText.setText(errorMsg);
		errText.setTextColor(Color.RED);
		errText.setBackgroundResource(R.drawable.border_black);
		errText.setLayoutParams(param);
	}
	
	private void DisplayProgressBar()
	{
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		//Set the TextView
		TextView loadText = new TextView(this);
		loadText.setText("Loading Results..");
		loadText.setTextColor(Color.BLACK);
		loadText.setLayoutParams(param);
		
		//Set the Progressbar
		ProgressBar progress = new ProgressBar(this);
		progress.setLayoutParams(param);
		
		//Add to DisplayLayout view
		displayLayout.addView(loadText);
		displayLayout.addView(progress);
	}

	private class LoadFuelDataAsyncTask extends AsyncTask<Void, ArrayList<FuelType>, Void> {
    	public String errText;
		
		@Override
        protected void onPreExecute() {
    		super.onPreExecute();
    		errText = "";
        }
         
        @SuppressWarnings("unchecked")
		@Override
        protected Void doInBackground(Void... params) {
        	try
        	{
        		String location = townValue.getText().toString();
        		String type = categorySpinner.getSelectedItem().toString();
        		Log.i("XML", "SELECTED: " + type);
        		FuelDataConnector rssDownload = new FuelDataConnector(type, location);
        		String rssResult = rssDownload.DownloadStreamToString();
			
        		FuelParser parser = new FuelParser();
        		ArrayList<FuelType> data = parser.getData(rssResult);
        		
        		publishProgress(data);
        	}
        	catch (Exception ex)
        	{
        		errText = "Oops! Seems there was a problem connecting to the server. Check you have a stable internet connection before trying again.";
        		publishProgress(new ArrayList<FuelType>());
        	}
       	 	return null;
        }
     
        @Override
        protected void onProgressUpdate(ArrayList<FuelType>... values) {
        	if (errText.isEmpty())
        	{
        		DisplayResults(values[0]);
        	}
        	else
        	{
        		DisplayErrorText(errText);
        	}
        }
      
        @Override
        protected void onPostExecute(Void result) {
        	super.onPostExecute(result);
        }
    }
} 