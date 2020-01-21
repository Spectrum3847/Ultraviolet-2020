package frc.lib.controllers;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.lib.controllers.SpectrumXboxController.XboxButton;

public class SpectrumButton extends JoystickButton {

	public SpectrumButton(GenericHID joystick, int buttonNumber) {
		super(joystick, buttonNumber);
	}

	public SpectrumButton(XboxController joystick, XboxButton button) {
		super(joystick, button.value);
	}

	public SpectrumButton(SpectrumXboxController joystick, XboxButton button) {
		super(joystick, button.value);
	}

}
