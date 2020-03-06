/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.VisionLL;

public class DefaultLL extends CommandBase {

  VisionLL m_visionLL;
  /**
   * Creates a new DefaultLL.
   */
  public DefaultLL(VisionLL visionLL) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_visionLL = visionLL;
    addRequirements(m_visionLL);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(RobotContainer.shooter.getWheelSetpoint() > 10 || RobotContainer.driverController.bButton.get()) {
      RobotContainer.visionLL.limeLightLEDOn();
    } else {
      RobotContainer.visionLL.limeLightLEDOff();
    }
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
