package org.me.myandroidstuff;

import android.app.Activity;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultsDisplayLayout extends Activity {
	
	public ResultsDisplayLayout()
	{
		
	}
	
	public LinearLayout getLinearLayout(String value)
	{
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setMargins(20, 7, 20, 7);
		
		LinearLayout newLayout = new LinearLayout(this);
		newLayout.setOrientation(LinearLayout.VERTICAL);
		newLayout.setLayoutParams(layout);
		newLayout.setPadding(20, 20, 20, 20);

		if (value.equalsIgnoreCase("diesel"))
		{
			newLayout.setBackground(getResources().getDrawable(R.drawable.border_black));
		}
		else
		{
			if (value.equalsIgnoreCase("unleaded"))
			{
				newLayout.setBackground(getResources().getDrawable(R.drawable.border_green));
			}
			else
			{
				if (value.equalsIgnoreCase("super unleaded"))
				{
					newLayout.setBackground(getResources().getDrawable(R.drawable.border_red));
				}
				else
				{
					newLayout.setBackground(getResources().getDrawable(R.drawable.border_blue));
				}
			}
			
		}
		
		return newLayout;
	}
	
	public TextView getHeadingText(String value)
	{
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		
		TextView fuelType = new TextView(this);
		fuelType.setText(value);
		fuelType.setTextColor(Color.BLACK);
		fuelType.setTextSize(16);
		fuelType.setLayoutParams(layout);
		fuelType.setPadding(0, 0, 0, 5);
		
		return fuelType;
	}
	
	public TextView getDataText(String value, String type)
	{
		LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layout.setMargins(20, 0, 0, 0);
		
		TextView fuelValue = new TextView(this);
		fuelValue.setText(value);
		fuelValue.setTextColor(Color.parseColor(getColor(type)));
		fuelValue.setLayoutParams(layout);
		fuelValue.setPadding(0, 0, 0, 5);
		
		return fuelValue;
	}
	
	private String getColor(String type)
	{
		if (type.equalsIgnoreCase("highest"))
		{
			return "#e50000";
		}
		else
		{
			if (type.equalsIgnoreCase("average"))
			{
				return "#116493";
			}
			else
			{
				if (type.equalsIgnoreCase("lowest"))
				{
					return "#74d600";
				}
				else
				{
					return "#000000";
				}
			}
			
		}
	}
}
