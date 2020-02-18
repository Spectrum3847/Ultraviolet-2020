/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.ballpath;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;

public class TowerBack extends CommandBase {
  /**
   * Creates a new TowerBack.
   */
  private Boolean trip;
  private Boolean isFinished;

    
  public TowerBack() {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.tower, RobotContainer.funnel);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    SmartDashboard.putBoolean("TopSensorTower", false);
    SmartDashboard.putBoolean("BotSensorTower", false);
    trip = false;
    RobotContainer.tower.close();
    RobotContainer.funnel.intakeTower();
    RobotContainer.tower.indexUp();
    isFinished = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  RobotContainer.tower.SmartDash();
  if(RobotContainer.tower.getTop() && RobotContainer.tower.getBot()){
    trip = true;
    RobotContainer.tower.indexDown();
    }
  else if (trip){
  RobotContainer.tower.stop();
  RobotContainer.funnel.stop();
  RobotContainer.tower.open();
  isFinished = true;
  }
  
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    trip = false;
    RobotContainer.tower.open();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
