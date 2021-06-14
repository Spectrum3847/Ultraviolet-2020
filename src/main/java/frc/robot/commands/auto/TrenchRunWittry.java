// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.ballpath.FeedShooter;
import frc.robot.commands.ballpath.FunnelStore;
import frc.robot.commands.ballpath.IntakeBalls;
import frc.robot.commands.ballpath.TowerBack;
import frc.robot.commands.ballpath.shooterVel;
import frc.robot.commands.drive.LLAim;
import frc.robot.commands.drive.Turn;
// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TrenchRunWittry extends SequentialCommandGroup {
  /** Creates a new TrenchRun. */
  public TrenchRunWittry() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      //new Turn(25).withInterrupt(visionLL.getLimelightHasValidTarget))
      new ParallelCommandGroup( //Spin up shooter, turn, shoot, turn back
        new shooterVel(4000),  //Shooter spinup
        new SequentialCommandGroup(
          new Turn(20).withTimeout(1), //turn to tower
          new LLAim().withTimeout(1), //limelight takeover
          new FeedShooter().withTimeout(2), //tower feed
          new Turn(0).withTimeout(1) //turn back to start
        ) //End Sequential
      ).withTimeout(5), //End Parallel

      new ParallelCommandGroup( //Reset subsystems  
      new RunCommand(() -> RobotContainer.tower.stop()),
        new RunCommand(() -> RobotContainer.shooter.stop()),
        //new RunCommand(() -> RobotContainer.intake.stop()),
        new RunCommand(() -> RobotContainer.intake.up()),
        new RunCommand(() -> RobotContainer.drivetrain.stop())
      ).withTimeout(0.1), //End Parallel

      new ParallelCommandGroup(  //Shooter to newSpeed, prep & start intake, collect trench balls, 
        new shooterVel(0), //New shooter speed
        new SequentialCommandGroup( //prep intake
          new TowerBack(),
          new FunnelStore()
        ), //End Sequential
        new IntakeBalls(), //start intake
        new SequentialCommandGroup( //collect & shoot trench balls
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 1)).withTimeout(2.75), //Collect Balls
          new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, -1)).withTimeout(1), //Shooting Position
          new RunCommand(() -> RobotContainer.drivetrain.stop()).withTimeout(0.1),
          new RunCommand(() -> RobotContainer.tower.stop())
        )
      ).withTimeout(8),

      new ParallelCommandGroup( //Reset subsystems  
      new RunCommand(() -> RobotContainer.tower.stop()),
        new RunCommand(() -> RobotContainer.shooter.stop()),
        //new RunCommand(() -> RobotContainer.intake.stop()),
        new RunCommand(() -> RobotContainer.intake.up()),
        new RunCommand(() -> RobotContainer.drivetrain.stop())
      ).withTimeout(0.1), //End Parallel

      new ParallelCommandGroup(
        new SequentialCommandGroup(
          new shooterVel(3900),
          new Turn(15).withTimeout(1), //turn to tower
          new LLAim().withTimeout(1), //limelight takeover
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(1.0)).withTimeout(2)
        )
      ).withTimeout(5), //End Parallel
      
      new ParallelCommandGroup( //Reset subsystems
        new RunCommand(() -> RobotContainer.tower.stop()),
        new RunCommand(() -> RobotContainer.shooter.stop()),
        new RunCommand(() -> RobotContainer.intake.stop()),
        new RunCommand(() -> RobotContainer.intake.up())
      ).withTimeout(0.1) //End Parallel

      /*new ParallelCommandGroup(
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
          */

          ); //Stop top level command group
  }
}
