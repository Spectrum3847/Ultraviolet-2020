package frc.lib.drivers;

import edu.wpi.first.wpilibj.DigitalInput;

public class SpectrumDigitalInput extends DigitalInput{
	
	private boolean invert = false;

	public SpectrumDigitalInput(int channel){
		super(channel);
	}
	
	public SpectrumDigitalInput(int channel, boolean isInvert) {
		super(channel);
		setInvert(isInvert);
	}
	
	public boolean get(boolean isInvert){
		if (isInvert){
			return !this.get();
		} else{
			return this.get();
		}
	}

	public boolean isInvert() {
		return invert;
	}

	public void setInvert(boolean isInvert) {
		this.invert = isInvert;
	}

}
