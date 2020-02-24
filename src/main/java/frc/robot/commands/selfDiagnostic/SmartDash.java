/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.selfDiagnostic;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class SmartDash extends CommandBase {
  /**
   * Creates a new SmartDash.
   */
  public SmartDash() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putBoolean("Diagnosing/InProgress", true);
    SmartDashboard.putBoolean("Diagnosing/Intake/Pneumatics", true);
    SmartDashboard.putBoolean("Diagnosing/Intake/Motor", true);
    SmartDashboard.putBoolean("Diagnosing/PoweredV/MotorL", true);
    SmartDashboard.putBoolean("Diagnosing/PoweredV/MotorR", true);
    SmartDashboard.putBoolean("Diagnosing/Tower/Pneumatics", true);
    SmartDashboard.putBoolean("Diagnosing/Tower/Motor", true);
    SmartDashboard.putBoolean("Diagnosing/Accelerator/Motor", true);
    SmartDashboard.putBoolean("Diagnosing/Shooter/MotorL", true);
    SmartDashboard.putBoolean("Diagnosing/Shooter/MotorR", true);
    SmartDashboard.putBoolean("Diagnosing/Sensors/BotSensor", true);
    SmartDashboard.putBoolean("Diagnosing/Sensors/TopSensor", true);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putBoolean("Diagnosing/Intake/Pneumatics", false);
    SmartDashboard.putBoolean("Diagnosing/Intake/Motor", false);
    SmartDashboard.putBoolean("Diagnosing/PoweredV/MotorL", false);
    SmartDashboard.putBoolean("Diagnosing/PoweredV/MotorR", false);
    SmartDashboard.putBoolean("Diagnosing/Tower/Pneumatics", false);
    SmartDashboard.putBoolean("Diagnosing/Tower/Motor", false);
    SmartDashboard.putBoolean("Diagnosing/Accelerator/Motor", false);
    SmartDashboard.putBoolean("Diagnosing/Shooter/MotorL", false);
    SmartDashboard.putBoolean("Diagnosing/Shooter/MotorR", false);
    SmartDashboard.putBoolean("Diagnosing/Sensors/BotSensor", false);
    SmartDashboard.putBoolean("Diagnosing/Sensors/TopSensor", false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
