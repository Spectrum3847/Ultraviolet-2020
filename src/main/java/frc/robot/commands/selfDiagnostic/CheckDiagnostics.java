/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.selfDiagnostic;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class CheckDiagnostics extends InstantCommand {
  public CheckDiagnostics() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if(SmartDashboard.getBoolean("Diagnosing/Intake/Pneumatics", false) && 
    SmartDashboard.getBoolean("Diagnosing/Intake/Motor", false) &&
    SmartDashboard.getBoolean("Diagnosing/PoweredV/MotorL", false) &&
    SmartDashboard.getBoolean("Diagnosing/PoweredV/MotorR", false) &&
    SmartDashboard.getBoolean("Diagnosing/Tower/Pneumatics", false) &&
    SmartDashboard.getBoolean("Diagnosing/Tower/Motor", false) &&
    SmartDashboard.getBoolean("Diagnosing/Accelerator/Motor", false) &&
    SmartDashboard.getBoolean("Diagnosing/Shooter/MotorL", false) &&
    SmartDashboard.getBoolean("Diagnosing/Shooter/MotorR", false) &&
    SmartDashboard.getBoolean("Diagnosing/Sensors/BotSensor", false) &&
    SmartDashboard.getBoolean("Diagnosing/Sensors/TopSensor", false)
    ){
        SmartDashboard.putBoolean("Diagnosing/Success",true);
    }
    else{
      SmartDashboard.putBoolean("Diagnosing/Success",false);
    }
    SmartDashboard.putBoolean("Diagnosing/InProgress",false);
  }
}
