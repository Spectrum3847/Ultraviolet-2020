// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.commands.ballpath.FunnelStore;
import frc.robot.commands.ballpath.FunnelToTowerSensors;
import frc.robot.commands.ballpath.IntakeBalls;
import frc.robot.commands.drive.LLAim;
import frc.robot.commands.drive.Turn;
import frc.robot.commands.ballpath.IntakeDown;
import frc.robot.commands.ballpath.TowerBack;
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
      new RunCommand(()-> RobotContainer.shooter.setShooterVelocity(4000)).withTimeout(2),
      new ParallelCommandGroup(
        new RunCommand(()-> RobotContainer.shooter.setShooterVelocity(4000)), 
        new SequentialCommandGroup(
          new LLAim().withTimeout(2),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.20)).withTimeout(3),
          new Turn(0).withTimeout(2))).withTimeout(5),
          //new IntakeDown().withTimeout(0.3),
          new ParallelCommandGroup(
            new RunCommand(() -> RobotContainer.intake.down()),
            new RunCommand(() -> RobotContainer.intake.collect()), 
            new FunnelToTowerSensors(),
            new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 0.75))).withTimeout(4.5),
            new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
          new Turn(10).withTimeout(2),
          new ParallelCommandGroup(
          new RunCommand(() -> RobotContainer.intake.down()),
          new RunCommand(() -> RobotContainer.intake.collect()),
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, -0.75))).withTimeout(2.5),
          new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(-0.5)).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.open()).withTimeout(0.1),
          new ParallelCommandGroup(
          new LLAim(),
          new RunCommand(() -> RobotContainer.shooter.setShooterLL())).withTimeout(1.5),
          //new WaitCommand(1),
        new ParallelCommandGroup(new  RunCommand(() -> RobotContainer.shooter.setShooterVelocity(3900)),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.25))).withTimeout(3),
        new RunCommand(() -> RobotContainer.tower.stop()),
        new RunCommand(() -> RobotContainer.shooter.stop())
          );
  }
}
