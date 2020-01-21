/**
 * Spectrum3847
 * SpectrumDigitalRelay.java
 * Author: JAG
 */
package frc.lib.drivers;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * @author JAG
 * This is used for controlling a small digital relay on a digital output.
 */
public class SpectrumDigitalRelay extends DigitalOutput {
	
	private boolean state = false;
	/**
	 * Create an instance of a digital relay given a channel
     *
	 * @param channel
	 *            the DIO channel to use for the digital output. 0-9 are on-board, 10-19 are on the MXP
	 */
	public SpectrumDigitalRelay(int value){
		super(value);
	}
	
	public SpectrumDigitalRelay turnOn(){
		state = true;
		set(true);
		return this;
	}
	
	public SpectrumDigitalRelay turnOff(){
		state = false;
		set(false);
		return this;
	}
	
	public void set(boolean value){
		state = value;
		super.set(!value); //The Digital output state is false=on and true=off
	}
	
	public boolean currentValue(){
		return state;
	}

}
