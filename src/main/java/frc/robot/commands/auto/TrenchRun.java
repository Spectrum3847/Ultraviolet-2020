// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.ballpath.FeedShooter;
import frc.robot.commands.ballpath.FunnelStore;
import frc.robot.commands.ballpath.FunnelToTowerSensors;
import frc.robot.commands.ballpath.IntakeBalls;
import frc.robot.commands.ballpath.IntakeSpeed;
import frc.robot.commands.ballpath.TowerBack;
import frc.robot.commands.ballpath.shooterVel;
import frc.robot.commands.drive.LLAim;
import frc.robot.commands.drive.Turn;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TrenchRun extends SequentialCommandGroup {
  /** Creates a new TrenchRun. */
  public TrenchRun() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new Turn(25).withTimeout(1),
      new shooterVel(4000).withTimeout(1),
      new ParallelCommandGroup(
        new shooterVel(4000), 
        new SequentialCommandGroup(
          new LLAim().withTimeout(1),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.20)).withTimeout(2),
          new Turn(0).withTimeout(1))).withTimeout(4),
              new ParallelCommandGroup(
                new SequentialCommandGroup(
                  new TowerBack(), 
                  new FunnelStore()), 
                new IntakeBalls(),
                new SequentialCommandGroup(
                  new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 0.85)).withTimeout(2.8),
                  new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
                  new ParallelCommandGroup(
                    new Turn(10).withTimeout(1),
                    new RunCommand (() -> RobotContainer.tower.feed()),
                    new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, -0.85))).withTimeout(1))).withTimeout(4),
            new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.stop()).withTimeout(0.1),
          //new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(-0.5)).withTimeout(0.03),
          //new RunCommand(() -> RobotContainer.tower.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.open()).withTimeout(0.1),
          new ParallelCommandGroup(
            new LLAim(),
            new shooterVel(3900)
          ).withTimeout(1),
          new ParallelCommandGroup(new  shooterVel(3800),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.35))).withTimeout(2.5),
          new ParallelCommandGroup(
          new RunCommand(() -> RobotContainer.tower.stop()),
          new RunCommand(() -> RobotContainer.shooter.stop()),
          new RunCommand(() -> RobotContainer.intake.stop()),
          new RunCommand(() -> RobotContainer.intake.up())).withTimeout(0.1)

          );
  }
}
