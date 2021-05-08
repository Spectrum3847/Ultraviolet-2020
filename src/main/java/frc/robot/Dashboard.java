package frc.robot;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.util.Util;

/*
 * @author matthew, JAG
 */
public class Dashboard {

    private static final Notifier dashThread = new Notifier(new dashboardThread());
	
	public static final boolean ENABLE_DASHBOARD = true;
	
	static final double SHORT_DELAY = .02;
    static final double LONG_DELAY = .5;
    
    static double shortOldTime = 0.0;
    static double longOldTime = 0.0;

    //Put values that you want to use as use inputs here and set their default state
    public static void intializeDashboard() {
    	if(ENABLE_DASHBOARD){
            //SmartDashboard.putBoolean("Compressor ENABLE", true);
            SmartDashboard.putBoolean("Limelight-LED Toggle", false);
        }
        dashThread.startPeriodic(0.02);
    }

    //Check each subsystems dashboard values and update them
    private static void updatePutShort() {
        RobotContainer.drivetrain.dashboard();
        RobotContainer.shooter.dashboard();
        RobotContainer.tower.Dashboard();
        //RobotContainer.climber.dashboard();
    }

    //Things that don't need to be sent out each cycle
    private static void updatePutLong(){
    	//SmartDashboard.putBoolean("Compressor On?", RobotContainer.pneumatics.compressor.enabled());
		
		//Can change to show a different message than "Yes" and "No"
        SmartDashboard.putBoolean("Change Battery", Util.changeBattery());
        
        SmartDashboard.putNumber("Brownout Count", Robot.brownOutCtn);  //PutBrownout count on the dashboard
    }

    public static void updateDashboard() {
        double time = Timer.getFPGATimestamp();
    	if (ENABLE_DASHBOARD) {
            if ((time - shortOldTime) > SHORT_DELAY) {
                shortOldTime = time;
                updatePutShort();
            }
            if ((time - longOldTime) > LONG_DELAY) {
                //Thing that should be updated every LONG_DELAY
                longOldTime = time;
                updatePutLong();
            }
        }
    }
    
    static int t = 0;
    static boolean b = true;
    
    public static void dashboardFlash(){
        //Flash a light on the dashboard so that you know that the dashboard is refreshing.
        if (t > 20) {
            t = 0;
            b = !b;
            SmartDashboard.putBoolean("Disabled Toggle", b);
        }
        t++;
    }

    private static class dashboardThread implements Runnable {
        
		@Override
		public void run() {
			updateDashboard();
		}
	}
}
