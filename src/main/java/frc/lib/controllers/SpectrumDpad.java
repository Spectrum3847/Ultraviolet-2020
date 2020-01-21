package frc.lib.controllers;

import edu.wpi.first.wpilibj.Joystick;
import frc.lib.controllers.SpectrumAxisButton.ThresholdType;
import frc.lib.controllers.SpectrumXboxController.XboxAxis;
import frc.lib.controllers.SpectrumXboxController.XboxDpad;

public class SpectrumDpad {
	public final Joystick joy;
	public SpectrumAxisButton Up;
	public SpectrumAxisButton Down;
	public SpectrumAxisButton Left;
	public SpectrumAxisButton Right;
	public SpectrumAxisButton UpLeft;
	public SpectrumAxisButton UpRight;
	public SpectrumAxisButton DownLeft;
	public SpectrumAxisButton DownRight;
	
	public SpectrumDpad(Joystick joystick) {
		this.joy = joystick;
		this.Up = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.UP.value, ThresholdType.POV);
		this.Down = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.DOWN.value, ThresholdType.POV);		
		this.Left = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.LEFT.value, ThresholdType.POV);
		this.Right = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.RIGHT.value, ThresholdType.POV);
		this.UpLeft = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.UP_LEFT.value, ThresholdType.POV);
		this.UpRight = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.UP_RIGHT.value, ThresholdType.POV);
		this.DownLeft = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.DOWN_LEFT.value, ThresholdType.POV);
		this.DownRight = new SpectrumAxisButton(joy, XboxAxis.DPAD, XboxDpad.DOWN_RIGHT.value, ThresholdType.POV);
	}
	
	public double getValue() {
		return joy.getRawAxis(XboxAxis.DPAD.value);
	}

}
