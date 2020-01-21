package frc.lib.controllers;

import edu.wpi.first.wpilibj.Joystick;
import frc.lib.controllers.SpectrumXboxController.XboxAxis;

//Based on Code form Bob319 2017
public class SpectrumThumbStick {
	Joystick controller;
	XboxAxis xAxis;
	XboxAxis yAxis;
	double yDeadband = 0.0;
	double xDeadband = 0.0;
	
	public SpectrumThumbStick(Joystick controller, XboxAxis xAxis, XboxAxis yAxis) {
		this.controller = controller;
		this.xAxis = xAxis;
		this.yAxis = yAxis;
	}
	public SpectrumThumbStick(Joystick controller, XboxAxis xAxis, XboxAxis yAxis, double yDeadband, double xDeadband) {
		this(controller, xAxis, yAxis);
		this.yDeadband = Math.abs(yDeadband);
		this.xDeadband = Math.abs(xDeadband);
	}

	public double getX() {
		double value = this.controller.getRawAxis(xAxis.value);
		return handleDeadband(value, xDeadband);
	}
	
	public double getY() {
		double value = this.controller.getRawAxis(yAxis.value) * -1;
		return handleDeadband(value, yDeadband);
	}
	
	public void setXDeadband(double deadband) {
		this.xDeadband = deadband;
	}
	
	public void setYDeadband(double deadband) {
		this.yDeadband = deadband;
	}
	
	public void setDeadband(double xDeadband, double yDeadband) {
		setXDeadband(xDeadband);
		setYDeadband(yDeadband);
	}
	
	public double handleDeadband(double input, double deadband) {
		if (input > -deadband && input < deadband) {
			return 0;
		}else {
			return input;
		}
	}
	
	public double getDirectionRadians() {
		return Math.atan2(getX(), -getY());
	}
	
	public double getDirectionDegrees() {
		    return Math.toDegrees(getDirectionRadians());
	}

}
