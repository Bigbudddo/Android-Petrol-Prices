package org.me.myandroidstuff;

public class FuelType {
	private String typeName;
	private float highest;
	private float average;
	private float lowest;
	
	public float Highest() { return highest; }
	public float Average() { return average; }
	public float Lowest() { return lowest; }
	
	public String Name() { return typeName; }
	public String HighestString() { return "High: " + Float.toString(highest) + "p"; }
	public String AverageString() { return "Average: " + Float.toString(average) + "p"; }
	public String LowestString()  { return "Low: " + Float.toString(lowest) + "p"; }
	
	public FuelType(String name, String high, String low, String avg) 
	{
		this.typeName = name;
		this.highest = Float.parseFloat(high);
		this.average = Float.parseFloat(avg);
		this.lowest = Float.parseFloat(low);
	}
}
