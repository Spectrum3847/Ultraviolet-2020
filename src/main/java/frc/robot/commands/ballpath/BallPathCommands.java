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
import frc.robot.RobotContainer;

/**
 * Please to create commands for the intake, funnel, and tower
 */
public class BallPathCommands {

    public static Command feedShooter = new ParallelCommandGroup(
        new RunCommand(() -> RobotContainer.tower.feed(), RobotContainer.tower),
        new SequentialCommandGroup(
        new RunCommand(() -> RobotContainer.funnel.intakeHold(), RobotContainer.funnel).withTimeout(.5),
        new RunCommand(() -> RobotContainer.funnel.feed(), RobotContainer.funnel)
        )
    );


}
