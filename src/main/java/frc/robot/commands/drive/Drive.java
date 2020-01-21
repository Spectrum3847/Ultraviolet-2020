/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drivetrain;
import frc.lib.controllers.SpectrumXboxController;


public class Drive extends CommandBase {
  /**
   * Creates a new Drive.
   */

  private final Drivetrain m_drive;

  private final SpectrumXboxController driverController;
  
  public Drive(Drivetrain subsystem, SpectrumXboxController controller) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_drive = subsystem;
    driverController = controller;
    addRequirements(m_drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_drive.arcadeDrive(driverController.leftStick.getX(), driverController.triggers.getTwist());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
