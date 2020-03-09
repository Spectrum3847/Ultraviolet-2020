/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ballpath;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class FeedShooter extends CommandBase {

  private double percentage = .95;
  /**
   * Creates a new FeedShooter.
   */
  public FeedShooter() {
    // Use addRequirements() here to declare subsystem dependencies.
    this.addRequirements(RobotContainer.funnel, RobotContainer.tower);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //If shooter wheels are at speed fire
    /*if (Math.abs(RobotContainer.shooter.getAccelRPM()/RobotContainer.shooter.getAccelSetpointRPM()) > percentage && 
        Math.abs(RobotContainer.shooter.getWheelRPM()/RobotContainer.shooter.getWheelSetpointRPM()) > percentage) {
        RobotContainer.tower.feed();

        //If there isn't a bottom ball, run the funnel
        if (!RobotContainer.tower.getBottomBall()){
          RobotContainer.funnel.feed();
        }
    }*/
    RobotContainer.tower.feed();
    RobotContainer.funnel.feed();
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
