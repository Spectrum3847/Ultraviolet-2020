// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.RumbleController;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html

public class Turn extends ProfiledPIDCommand {
  /** Creates a new Turn. */
  double currentHeading = RobotContainer._imu.getAngle();
  public Turn(double angle) {
    super(
        // The ProfiledPIDController used by the command
        new ProfiledPIDController(
            // The PID gains
            0.018,
            0,
            0,
            // The motion profile constraints
            new TrapezoidProfile.Constraints(360, 360)),
        // This should return the measurement
        () -> RobotContainer._imu.getAngle(),
        // This should return the goal (can also be a constant)
        () -> angle,
        // This uses the output
        (output, setpoint) -> RobotContainer.drivetrain.arcadeDrive(-output, 0)
          // Use the output (and setpoint, if desired) here
        );

        addRequirements(RobotContainer.drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here
    getController().setTolerance(1);
    //getController().setGoal(currentHeading + angle);
  }
  
  @Override
  public void initialize() {
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    new ParallelCommandGroup(
      new RumbleController(RobotContainer.operatorController, 0.5),
      new RumbleController(RobotContainer.driverController, 0.5)
    ).schedule();
  }
  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atGoal();
  }
}
