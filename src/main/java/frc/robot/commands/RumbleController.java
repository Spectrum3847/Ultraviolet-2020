/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.controllers.SpectrumXboxController;

/**
 * An example command.  You can replace me with your own command.
 */
public class RumbleController extends CommandBase {
  private SpectrumXboxController controller;
  private double value;

  public RumbleController(SpectrumXboxController controller, double value) {
    // Use requires() here to declare subsystem dependencies
    //requires(Robot.m_subsystem);\
    this.controller = controller;
    this.value = value;
  }

  // Called just before this Command runs the first time
  @Override
  public void initialize() {
    controller.setRumble(value, value);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  public void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  public boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  public void end(boolean interrupted) {
    controller.setRumble(0, 0);
  }

}