package frc.lib.drivers;

import edu.wpi.first.wpilibj.DigitalInput;

public class SpectrumDigitalInput extends DigitalInput{
	
	private boolean isPNP = false;

	public SpectrumDigitalInput(int channel){
		super(channel);
	}
	
	public SpectrumDigitalInput(int channel, boolean isPNP) {
		super(channel);
		setPNP(isPNP);
	}
	
	public boolean get(boolean isPNP){
		return (isPNP)? this.get():!this.get();
	}

	public boolean isPNP() {
		return isPNP;
	}

	public void setPNP(boolean isPNP) {
		this.isPNP = isPNP;
	}

}
