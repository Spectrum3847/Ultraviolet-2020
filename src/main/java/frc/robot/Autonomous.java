/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj2.command.*;

/**
 * Add your docs here.
 */
public class Autonomous {
    public static int AutoNumber = 2;
    public static String AutoName = "Center SW Pigeon";

    //private static Command spinUp = new RunCommand(()-> RobotContainer.shooter.setShooterVelocity(3500), RobotContainer.shooter).withTimeout(7);
   // private static Command feedShooter = new RunCommand(() -> RobotContainer.tower.feed(), RobotContainer.tower).withTimeout(3);
    //private static Command driveForward = new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(-0.3, 0), RobotContainer.drivetrain).withTimeout(1);
    //private static Command driveBackward = new RunCommand(() -> RobotContainer.drivetrain.arcadeDrive(0.3, 0), RobotContainer.drivetrain).withTimeout(3);
    private static Command spinUp = new StartEndCommand(()-> RobotContainer.shooter.setShooterVelocity(3500), () -> RobotContainer.shooter.stop(), RobotContainer.shooter).withTimeout(7);
    private static Command driveForward = new StartEndCommand(() -> RobotContainer.drivetrain.arcadeDrive(-0.3, 0), () -> RobotContainer.drivetrain.arcadeDrive(0, 0), RobotContainer.drivetrain).withTimeout(.5);
    private static Command feedShooter = new StartEndCommand(() -> RobotContainer.tower.feed(), () -> RobotContainer.tower.stop(), RobotContainer.tower).withTimeout(3);
    private static Command driveBackward = new StartEndCommand(() -> RobotContainer.drivetrain.arcadeDrive(0.3, 0), () -> RobotContainer.drivetrain.arcadeDrive(0, 0), RobotContainer.drivetrain).withTimeout(2);

    public static ParallelCommandGroup Shoot3 = new ParallelCommandGroup(
        spinUp,
        new SequentialCommandGroup(
            new WaitCommand(3),
            feedShooter,
            driveForward,
            new WaitCommand(0.5),
            driveBackward
        )
    );

    public static Command sendAutoCommand() {
        AutoNumber = (int) RobotContainer.prefs.getNumber("1A: AutoNumber", 1);
            
		switch (AutoNumber) {
		    case (1): 
                AutoName = "Shoot 3 Timed Drive";
                return Shoot3;
            default: 
                AutoName = "Nothing Selected";
                return Shoot3;
        }
    }
}
