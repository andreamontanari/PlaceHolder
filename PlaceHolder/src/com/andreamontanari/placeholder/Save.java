package com.andreamontanari.placeholder;

import android.content.Context;
import android.content.SharedPreferences;

public class Save {
	
	public static final String PREFS_NAME = "MyPrefsFile";
	static SharedPreferences prefs;
	
	
	 public static void saveCounter(int count, String name, Context mContext) {
		 prefs = mContext.getSharedPreferences(PREFS_NAME, 0);  
	  	 SharedPreferences.Editor editor = prefs.edit(); 
	  	 editor.putInt(name, count);
	  	 editor.commit();
	 }
	 
	 public static int loadCounter(String name, Context mContext) {
		 prefs = mContext.getSharedPreferences(PREFS_NAME, 0);  
		 int counter = prefs.getInt(name, 0);
		 return counter;
	 }
	  public static void saveArray(String[] array, String arrayName, Context mContext) {   
  	    prefs = mContext.getSharedPreferences(PREFS_NAME, 0);  
  	    SharedPreferences.Editor editor = prefs.edit();  
  	    editor.putInt(arrayName +"_size", array.length);  
  	    for(int i=0;i<array.length;i++)  
  	        editor.putString(arrayName + "_" + i, array[i]);  
  	    editor.commit();  
  	} 
     
     public static String[] loadArray(String arrayName, Context mContext) {  
  	    prefs = mContext.getSharedPreferences(PREFS_NAME, 0);  
  	    int size = 5; 
  	    String array[] = new String[size];  
  	    for(int i=0;i<size;i++)  
  	        array[i] = prefs.getString(arrayName + "_" + i, null);  
  	    return array;  
  	}
     
}