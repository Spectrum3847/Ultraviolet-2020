package frc.lib.util;

import edu.wpi.first.wpilibj.DriverStation;
import frc.team2363.logger.HelixEvents;

import java.io.File;
import java.util.function.Supplier;

public class SpectrumLogger {
	private static SpectrumLogger instance;
    
	private static String loggingLocation = "/home/lvuser/logs/";

    private SpectrumLogger() {
        //Start Logging HelixEvents
        HelixEvents.getInstance().startLogging();  

        //Start Logging BadLog Events

        //Check if there is a flash drive plugged in
        File usb1 = new File("/media/sda1/");
		if (usb1.exists()) {
			loggingLocation = "/media/sda1/logs/";
        }
        //Check if we are attached to FMS
        if (DriverStation.getInstance().isFMSAttached()) {
				loggingLocation = loggingLocation + 
						DriverStation.getInstance().getEventName() + "_"+ 
						DriverStation.getInstance().getMatchType() + 
						DriverStation.getInstance().getMatchNumber() + "_" + "Log.txt";
			} else {
				loggingLocation = loggingLocation + "Log.txt";
			}
    }

    /**
     * Returns the preferences instance.
     *
     * @return the preferences instance
     */
	  public static synchronized SpectrumLogger getInstance() {
	    if (instance == null) {
	      instance = new SpectrumLogger();
	    }
	    return instance;
      }
      
    //Doesn't need to do anything just intalize the instance
    public void intialize(){
    }

    public void finalize(){
    }

    public void updateTopics(){
    }

    public void log(){
    }

    //Call this whenever you want to just see how events occur, not for use with graphs, etc.
    public void addEvent(String subsystem, String event){
        HelixEvents.getInstance().addEvent(subsystem, event);
    }

    //Only Call this during subsystem constructors
    /** Example = createTopic("Match_Time","s",() -> DriverStation.getInstance().getMatchTime());
    */
    public void createTopic(String name, String unit, Supplier<Double> supplier, String... attrs){
    }

}