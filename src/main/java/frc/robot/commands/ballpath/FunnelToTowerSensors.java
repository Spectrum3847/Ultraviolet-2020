/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ballpath;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class FunnelToTowerSensors extends CommandBase {
  /**
   * Creates a new intakeToTower.
   */
  public FunnelToTowerSensors() {
    // Use addRequirements() here to declare subsystem dependencies.
    this.addRequirements(RobotContainer.funnel, RobotContainer.tower);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    RobotContainer.tower.open();
    RobotContainer.funnel.intakeTower();
    RobotContainer.tower.indexUp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    RobotContainer.tower.close();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    //If we have a ball at both spots stop the motion
    return (RobotContainer.tower.getBot() && RobotContainer.tower.getTop());
  }
}
