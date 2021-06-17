// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/*package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class FullSend extends SequentialCommandGroup {
   Creates a new FullSend.
  public FullSend() {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      //Spin UP
      new ParallelCommandGroup(
        new shooterVel(4000),  //Shooter spinup
        new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 1)).withTimeout(1) ,//Move to balls 
        new SequentialCommandGroup(
        new WaitCommand(1),
        new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.2)).withTimeout(0.2) //fire
        ).withTimeout(1.2)
      ),

      
      new ParallelCommandGroup( //Reset subsystems  
        new RunCommand(() -> RobotContainer.shooter.stop()),
        new RunCommand(() -> RobotContainer.intake.up()),
        new RunCommand(() -> RobotContainer.drivetrain.stop())
      ).withTimeout(0.1), //End Parallel

      new ParallelCommandGoup(
        new shooterVel(0), //stop shooter
        new FunnelToTower(),
        new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.35)),
        new IntakeBalls(),
        new SequentialCommandGroup(
          new Turn(-20).withTimeout(0.5),
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 1)).withTimeout(1),
          new WaitCommand(0.25),
          new Turn(20).withTimeout(0.5), //turn to tower
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, -1)).withTimeout(1),
          new RunCommand(() -> RobotContainer.drivetrain.stop().withTimeout(0.1))
        )
      ).withTimeout(2.6),
      
      new ParallelCommandGroup( //Reset subsystems  
        new RunCommand(() -> RobotContainer.shooter.stop()),
        new RunCommand(() -> RobotContainer.drivetrain.stop())
      ).withTimeout(0.1), //End Parallel
      
      new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(-0.1)).withTimeout(0.2),

      new ParallelCommandGroup(
        new shooterVel(4000),
        new RunCommand(() -> RobotContainer.tower.open()),
        new SequentialCommandGroup(
          new LLAim().withTimeout(1), //limelight takeover
          new WaitCommand(1),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(1.0)).withTimeout(2)
        )
      ).withTimeout(4), //End Parallel
      
      new ParallelCommandGoup(
        new shooterVel(0), //stop shooter
        new FunnelToTower(),
        new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(0.35)),
        new IntakeBalls(),
        new SequentialCommandGroup(
          new Turn(-65).withTimeout(0.5),
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, 1)).withTimeout(0.25),
          new WaitCommand(0.5),
          new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0, -1)).withTimeout(0.3),
          new Turn(-20).withTimeout(0.5)
        ).withTimeout(2.25)
      ),

      new ParallelCommandGroup(
        new shooterVel(4000),
        new RunCommand(() -> RobotContainer.tower.open()),
        new SequentialCommandGroup(
          new LLAim().withTimeout(1), //limelight takeover
          new WaitCommand(1),
          new RunCommand(() -> RobotContainer.tower.setPercentModeOutput(1.0)).withTimeout(2)
        )
      ).withTimeout(4) //End Parallel

    );
  }
}
*/