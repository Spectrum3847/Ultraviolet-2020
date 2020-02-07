/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ballpath;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.StartEndCommand;
import frc.robot.RobotContainer;

/**
 * Please to create commands for the intake, funnel, and tower
 */
public class BallPathCommands {

    //Intake balls, by droping the intake and collecting, the intake will come up when it's finished.
    public static Command intakeBalls = new StartEndCommand(
        () -> RobotContainer.intake.collect(), () -> RobotContainer.intake.up(), RobotContainer.intake);

    //Feed Shooter, feed the tower and reverse the belt for .25sec and then feed the funnel
    public static Command feedShooter = new ParallelCommandGroup(
        new RunCommand(() -> RobotContainer.tower.feed(), RobotContainer.tower),
        new SequentialCommandGroup(
            new RunCommand(() -> RobotContainer.funnel.intakeHold(), RobotContainer.funnel).withTimeout(.5),
            new RunCommand(() -> RobotContainer.funnel.feed(), RobotContainer.funnel)
        )
    );

    //funnel balls from the funnel to the tower
    public static Command funnelToTower = new RunCommand(() -> RobotContainer.funnel.intakeTower(), RobotContainer.funnel).alongWith(
        new RunCommand(() -> RobotContainer.tower.indexUp(), RobotContainer.tower)
    );

    //Reverse the funnel
    public static Command funnelStore = new RunCommand(() -> RobotContainer.funnel.intakeHold(), RobotContainer.funnel);     
}