/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.controller.ProfiledPIDController;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ProfiledPIDCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.RumbleController;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class LLAim extends ProfiledPIDCommand {
  /**
   * Creates a new LLAim.
   */

  boolean hasTarget = false;
  
  public LLAim() {
    super(
        // The ProfiledPIDController used by the command
        new ProfiledPIDController(
            // The PID gains
            0.01625, 0, 0,
            // The motion profile constraints
            new TrapezoidProfile.Constraints(360, 360)),
        // This should return the measurement
        RobotContainer.visionLL::getLLDegToTarget,
        // This should return the goal (can also be a constant)
        0,
        // This uses the output
        (output, setpoint) -> RobotContainer.drivetrain.arcadeDrive(-output, 0),
        // Requirements
        RobotContainer.drivetrain);
    // Use addRequirements() here to declare subsystem dependencies.
    // Configure additional PID options by calling `getController` here.
    getController().setTolerance(0.5);
  }

  @Override
  public void initialize() {
    if(RobotContainer.visionLL.getLimelightHasValidTarget()) {
      hasTarget = true;
    } else {
      hasTarget = false;
    }
    super.initialize();
  }

  @Override
  public void end(boolean interrupted) {
    super.end(interrupted);
    if (hasTarget) {
      new ParallelCommandGroup(
        new RumbleController(RobotContainer.operatorController, 0.5),
        new RumbleController(RobotContainer.driverController, 0.5)
      ).schedule();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getController().atGoal() || !hasTarget;
  }
}
