package frc.lib.drivers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Robot;
import frc.lib.util.Debugger;

/**
 *
1 - LeftX
2 - LeftY
3 - Triggers (Each trigger = 0 to 1, axis value = right - left)
4 - RightX
5 - RightY
6 - DPad Left/Right
 * @author David, Matthew, JAG
 */
public class Gamepad extends Joystick {

    public static final int LeftX = 0;
    public static final int LeftY = 1;
    public static final int LeftTrigger = 2;
    public static final int RightTrigger = 3;
    public static final int RightX = 4;
    public static final int RightY = 5;
    public static final int Dpad = 6;

    public static final int A_BUTTON = 1;
    public static final int B_BUTTON = 2;
    public static final int X_BUTTON = 3;
    public static final int Y_BUTTON = 4;
    public static final int LEFT_BUMPER = 5;
    public static final int RIGHT_BUMPER = 6;
    public static final int BACK_BUTTON = 7;
    public static final int START_BUTTON = 8;
    public static final int LEFT_CLICK = 9;
    public static final int RIGHT_CLICK = 10;

    private static final int DEFAULT_USB_PORT = 1;

    private String name = "";
    
    public Gamepad() {
        this("Gamepad Port 1", DEFAULT_USB_PORT);
    }

    public Gamepad(String n, int port) {
    	super(port);
    	name = n;
    }
    
    public String getName(){
    	return name;
    }

    public double getLeftX() {
    	double value = this.getRawAxis(LeftX);
    	Debugger.println(name +"_Left_X: " + value, Robot._controls, Debugger.debug2);
        return value;
    }

    public double getLeftY() {
    	double value = -this.getRawAxis(LeftY);
    	Debugger.println(name +"_Left_Y: " + value, Robot._controls, Debugger.debug2);
        return value;
    }
    
    public double getLeftTrigger() {
    	double value = -this.getRawAxis(LeftTrigger);
    	Debugger.println(name +"_Left_Trigger: " + value, Robot._controls, Debugger.debug2);
        return value;
    }
    
    public double getRightTrigger() {
    	double value = this.getRawAxis(RightTrigger);
    	Debugger.println(name +"_Right_Trigger: " + value, Robot._controls, Debugger.debug2);
        return value;
    	
    }
    
    public double getOldTriggers() {
    	return -getRightTrigger()+ getLeftTrigger();
    }

    public double getRightX() {
    	double value = this.getRawAxis(RightX);
    	Debugger.println(name +"_Right_X: " + value, Robot._controls, Debugger.debug2);
        return value;
    }

    public double getRightY() {
    	double value = this.getRawAxis(RightY);
    	Debugger.println(name +"_Right_Y: " + value, Robot._controls, Debugger.debug2);
        return value;
    }

    public boolean getButtonVal(int button) {
    	boolean value = this.getRawButton(button);
        return value;
    }
    
    //Returns a button object
    public Button getButton(int button){
    	return new JoystickButton(this, button);
    }

    public double getDPad() {
        return this.getPOV();
    }

    public Joystick getGamepad() {
        return this;
    }
    
    /**
     * Set the rumble output for the joystick. The DS currently supports 2 rumble values,
     * left rumble and right rumble
     * @param type Which rumble value to set
     * @param value The normalized value (0 to 1) to set the rumble to
     */
    public void setRumble(RumbleType type, float value) {
    	super.setRumble(type, value);
    }
    
    /**
     * Make the controller rumble without having to declare a type
     * @param value - 0-1 for the intensity of the rumble
     */
    public void rumble(float value){
    	setRumble(RumbleType.kLeftRumble, value);
    	setRumble(RumbleType.kRightRumble, value);
    }

}
