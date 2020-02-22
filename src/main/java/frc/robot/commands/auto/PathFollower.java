/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import frc.robot.subsystems.Drivetrain;
import frc.team2363.commands.HelixFollower;
import frc.team2363.controller.PIDController;
import com.team319.trajectory.Path;
import java.lang.Math;

public class PathFollower extends HelixFollower {

  private final Drivetrain m_Drivetrain;

  private final PIDController headingController = new PIDController(15, 0, 0, 0.001);
  private final PIDController distanceController = new PIDController(10, 0, 0, 0.001);

  /**
   * Creates a new PathFollower.
   */
  public PathFollower(Path path, Drivetrain drivetrain) {
    super(path);
    addRequirements(drivetrain);
    m_Drivetrain = drivetrain;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
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

  @Override
  public void resetDistance() {
    m_Drivetrain.rightFrontTalonFX.getSensorCollection().setIntegratedSensorPosition(0, 0);
    m_Drivetrain.leftFrontTalonFX.getSensorCollection().setIntegratedSensorPosition(0, 0);
  }

  @Override
  public PIDController getHeadingController() {
    return headingController;
  }

  @Override
  public PIDController getDistanceController() {
    return distanceController;
  }

  @Override
  public double getCurrentDistance() {
    return (
              ticksToFeet(m_Drivetrain.rightFrontTalonFX.getSensorCollection().getIntegratedSensorPosition())
              + ticksToFeet(m_Drivetrain.leftFrontTalonFX.getSensorCollection().getIntegratedSensorPosition())
          ) / 2;
  }

  @Override
  public double getCurrentHeading() {
    return Math.toRadians(m_Drivetrain.getHeading());
  }

  @Override
  public void useOutputs(double left, double right) {
    m_Drivetrain.setSetpoint(left, right);
  }

  //Motor encoder ticks(2048) / encoder ticks per motor rotation / low gear ratio * wheel circumference(feet) per wheel rotation 
  private double ticksToFeet(double ticks) {
    return ticks / 2048  / 16 * (9 * Math.PI / 12);
  }

}
