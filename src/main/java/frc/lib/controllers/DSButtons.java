package frc.lib.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * Used to connect TI Launchpad Buttons on the Spectrum 2016 Driver Station
 * @author JAG
 *
 */
public class DSButtons extends Joystick {
	
	public DSButtons(){
		super(5);
	}
	
	public DSButtons(int port){
		super(port);
	}
	
	/**
	 * @param button - which button number 0-4
	 * @return the button state
	 */	
	public boolean getDriverButtonVal(int button){
		button = (button > 4) ? 4 : button;
		button = (button < 0) ? 0 : button;
		
		return this.getRawButton(1 + button);
	}
	
    public Button getDriverButton(int button){
		button = (button > 4) ? 4 : button;
		button = (button < 0) ? 0 : button;
		
    	return new JoystickButton(this, button);
    }
	
	/**
	 * @param button - which button number 0-4
	 * @return the button state
	 */
	public boolean getOperatorButtonVal(int button){
		button = (button > 4) ? 4 : button;
		button = (button < 0) ? 0 : button;
		
		return this.getRawButton(6 + button);
	}	
	
    public Button getOperatorButton(int button){
		button = (button > 4) ? 4 : button;
		button = (button < 0) ? 0 : button;
		
    	return new JoystickButton(this, 6 + button);
    }
    
	/**
	 * @param LED - 0 to 4 for the driver LEDS
	 * @param value
	 */
	public void setDriverLED(int LED, boolean value){
		LED = (LED > 4) ? 4 : LED;
		LED = (LED < 0) ? 0 : LED;
		
		this.setOutput(LED, value);
	}
	
	/**
	 * @param LED - 0 to 4 for the operator LEDS
	 * @param value
	 */
	public void setOperatorLED(int LED, boolean value){
		LED = (LED > 4) ? 4 : LED;
		LED = (LED < 0) ? 0 : LED;
		
		this.setOutput(LED + 5, value);
	}
}
