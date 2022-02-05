/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.selfDiagnostic;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.ballpath.IntakeDown;
import frc.robot.commands.ballpath.TowerPneumatic;

/**
 * Add your docs here.
 */
public class SelfDiagnosticCommands {
    public static Command selfTest = new SequentialCommandGroup(
        new SmartDash().withTimeout(1),
        new IntakeDown().withTimeout(1),
        new RunCommand(() -> RobotContainer.intake.checkMotor(), RobotContainer.intake),
        new RunCommand(() -> RobotContainer.funnel.checkMotor(), RobotContainer.funnel),
        new TowerPneumatic().withTimeout(1),
        new RunCommand(() -> RobotContainer.tower.checkMotor() , RobotContainer.tower),
        new RunCommand(() -> RobotContainer.shooter.checkAcceleratorMotor(), RobotContainer.shooter),
        new RunCommand(() -> RobotContainer.shooter.checkMotors(), RobotContainer.shooter),
        new CheckDiagnostics());
}
