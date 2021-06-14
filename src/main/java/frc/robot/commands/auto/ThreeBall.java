// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.ballpath.shooterVel;
import frc.robot.commands.drive.LLAim;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ThreeBall extends SequentialCommandGroup {
  /** Creates a new ThreeBall. */
  public ThreeBall() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelCommandGroup(
        new shooterVel(4000),  //Shooter spinup
        new SequentialCommandGroup(
          new LLAim().withTimeout(1),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(1.0)).withTimeout(2), //tower feed
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, -0.5)).withTimeout(0.5), //Collect Balls
          new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 1)).withTimeout(1), //Collect Balls
          new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1)
        ))
    );
  }
}
