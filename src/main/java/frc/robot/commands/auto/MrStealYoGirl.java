// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.ballpath.FunnelToTower;
import frc.robot.commands.ballpath.IntakeBalls;
import frc.robot.commands.ballpath.shooterVel;
import frc.robot.commands.drive.LLAim;
import frc.robot.commands.drive.Turn;
import frc.robot.commands.ballpath.FeedShooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class MrStealYoGirl extends SequentialCommandGroup {
  // Creates a new MrStealYoGirl. 
  public MrStealYoGirl() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ParallelCommandGroup(
        //new shooterVel(4000),  //Shooter spinup
        new SequentialCommandGroup(
          new Turn(20).withTimeout(1),
          new LLAim().withTimeout(1),
          new WaitCommand(3),//switch to 1
          //new FeedShooter().withTimeout(2) //tower feed
          new Turn(-20).withTimeout(2)

        )).withTimeout(7),
      
        new ParallelCommandGroup( //Reset subsystems  
        new RunCommand(() -> RobotContainer.shooter.stop()),
        new RunCommand(() -> RobotContainer.intake.up()),
        new RunCommand(() -> RobotContainer.drivetrain.stop())
      ).withTimeout(0.1), //End Parallel

      new ParallelCommandGroup(  //Shooter to newSpeed, prep & start intake, collect trench balls, 
        new shooterVel(0), //New shooter speed
        //new FunnelToTower(),
        //new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.35)),
        //new IntakeBalls(),
        new SequentialCommandGroup( //collect & shoot trench balls
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 1)).withTimeout(3),
          new Turn(0)
        )
      ).withTimeout(6)
    );
  }
}
