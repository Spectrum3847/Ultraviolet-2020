package frc.lib.util;

import java.util.Vector;

import edu.wpi.first.wpilibj.Preferences;

public class SpectrumPreferences {
	
	private Preferences prefs;
	private static SpectrumPreferences instance;
	private boolean reset = false;

	private SpectrumPreferences() {
		prefs = Preferences.getInstance();
	}
	
	  /**
	   * Returns the preferences instance.
	   *
	   * @return the preferences instance
	   */
	  public static synchronized SpectrumPreferences getInstance() {
	    if (instance == null) {
	      instance = new SpectrumPreferences();
	    }
	    return instance;
	  }

	  private boolean checkForKey(String key){
		  if (prefs.containsKey(key) && reset == false)
			  return true;
		  else{
			  return false;
		  }
	  }
	  /**
	   * adds the given string into the preferences table if it's not there already.
	   *
	   * @param key   the key
	   * @param value the value
	   * @throws NullPointerException if value is null
	   */
	  public String addString(String key, String value) {
		if (!checkForKey(key)){
			if (value == null) {
				throw new NullPointerException("Value is null");
			}
		    prefs.putString(key, value);
		}
		
		return prefs.getString(key, value);
	  }
	  
	  /**
	   * Adds the given boolean into the preferences table if it's not there already.
	   *
	   * @param key   the key
	   * @param value the value
	   */
	  public boolean addBoolean(String key, boolean value) {
		if (!checkForKey(key)){
		    prefs.putBoolean(key, value);
		}
		
		return prefs.getBoolean(key, value);
	  }
	  
	  /**
	   * Adds the given double into the preferences table if it's not there already.
	   *
	   * @param key   the key
	   * @param value the value
	   */
	  public double addNumber(String key, double value) {
		if (!checkForKey(key)){
		    prefs.putDouble(key, value);
		}
		
		return prefs.getDouble(key, value);
	  }
	  
	  /**
	   * Returns the string at the given key. If this table does not have a value for that position,
	   * then the given backup value will be returned.
	   *
	   * @param key    the key
	   * @param backup the value to return if none exists in the table
	   * @return either the value in the table, or the backup
	   */
	  public String getString(String key, String backup) {
		  addString(key, backup);
		  return prefs.getString(key, backup);
	  }
	  
	  /**
	   * Returns the number at the given key. If this table does not have a value for that position,
	   * then the given backup value will be returned.
	   *
	   * @param key    the key
	   * @param backup the value to return if none exists in the table
	   * @return either the value in the table, or the backup
	   */
	  public double getNumber(String key, double backup) {
		  addNumber(key, backup);
	    return prefs.getDouble(key, backup);
	  }
	  
	  /**
	   * Returns the boolean at the given key. If this table does not have a value for that position,
	   * then the given backup value will be returned.
	   *
	   * @param key    the key
	   * @param backup the value to return if none exists in the table
	   * @return either the value in the table, or the backup
	   */
	  public boolean getBoolean(String key, boolean backup) {
		  addBoolean(key, backup);
	    return prefs.getBoolean(key, backup);
	  }
	  
	  /**
	   * Remove a preference.
	   *
	   * @param key the key
	   */
	  public void remove(String key) {
	    prefs.remove(key);
	  }
	  
	  /**
	   * Returns whether or not there is a key with the given name.
	   *
	   * @param key the key
	   * @return if there is a value at the given key
	   */
	  public boolean containsKey(String key) {
	    return prefs.containsKey(key);
	  }
	  
	  /**
	   * Gets the vector of keys.
	   * @return a vector of the keys
	   */
	public Vector<String> getKeys() {
		  Vector<String> keys = new Vector<String>();
		  keys = (Vector<String>) prefs.getKeys();
	    return keys;
	  }

}
