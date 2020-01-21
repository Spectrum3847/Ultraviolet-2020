package frc.lib.util;

import java.util.List;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import frc.robot.Constants.RobotConstants;

/**
 * Contains basic functions that are used often.
 *
 * @author richard@team254.com (Richard Lin)
 * @author brandon.gonzalez.451@gmail.com (Brandon Gonzalez)
 * @author tom.bottiglieri@gmail.com (Tom Bottiglieri)
 */
public class Util {
    // Prevent this class from being instantiated.
    private Util() {
    }

    /**
     * Limits the given input to the given magnitude.
     */
    public static double limit(double v, double limit) {
        return limit(v, limit, -limit);
    	//return (Math.abs(v) < limit) ? v : limit * (v < 0 ? -1 : 1);
    }
    
    /**
     * limits input to the max and min
     * @param v - input
     * @param max - max value
     * @param min - min value
     * @return
     */
    public static double limit(double v, double max, double min){
    	return (v > max) ? max : ((v < min) ? min : v);
    }

    public static String joinStrings(String delim, List<?> strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); ++i) {
            sb.append(strings.get(i).toString());
            if (i < strings.size() - 1) {
                sb.append(delim);
            }
        }
        return sb.toString();
    }
    
    //In seconds
    public static double getTime(){
    	return Timer.getFPGATimestamp();
    }
    
    /*******************************************************************************
	 * KEEP MOVING AVERAGES OF CURRENT THROUGH EACH MOTOR
	 * Probably not the best way, especially if we are recording values a lot more frequently than we are actually updating our moving average
	 ******************************************************************************/
	private static double[] currentValues/* = new double[number]*/; //Let number be the number of terms you want to average
	private static int count = 0;
	private static int total = 0;
	public static double movingAvgCurrent(TalonSRX motor) {
		double a = motor.getStatorCurrent();
		if (currentValues[count] != 0) {
			total -= currentValues[count];
		}
		total += a;
		currentValues[count] = a;
		return total / currentValues.length;
	}
    
    /*******************************************************************************
	 * VALUE FOR CHANGE BATTERY ON DASHBOARD
	 * Should return true if robot is disabled and voltage is less than 12
	 ******************************************************************************/
	public static boolean changeBattery () {
		return (Robot.s_robot_state == Robot.RobotState.DISABLED && RobotController.getInputVoltage() < RobotConstants.minBatteryVoltage);
	}

    public static boolean closeTo(double a, double b, double epsilon) {
        return epsilonEquals(a,b,epsilon);
    }
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
    }

    public static boolean allCloseTo(List<Double> list, double value, double epsilon) {
        boolean result = true;
        for (Double value_in : list) {
            result &= epsilonEquals(value_in, value, epsilon);
        }
        return result;
    }
    
    public static double standardDeviation(double[] arr) {
    	double mean = 0.0;    	
    	double[] temp = new double[arr.length];
    	
    	mean = mean(arr);
    	
    	for (int i = 0; i < temp.length; i++) {
			temp[i] = Math.pow((arr[i] - mean),2);
		}
    	
    	return Math.sqrt(mean(temp));
    }
    
    public static double mean(double[] arr) {
    	double sum = 0.0;
    	
    	for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
		}
    	
    	return sum/arr.length;
    }
    
    public static double max(double[] arr) {
    	double max = 0;
    	for (int i = 0; i < arr.length; i++) {
			if (arr[i] > max) {
				max = arr[i];
			}
		}
    	
    	return max;
    }
    
    public static double powKeepSign(double v, double p) {
    	return Math.signum(v)*Math.abs(Math.pow(v, p));
    }

}
