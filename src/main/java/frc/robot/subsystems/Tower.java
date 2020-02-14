/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.drivers.SpectrumSolenoid;
import frc.robot.Constants;

public class Tower extends SubsystemBase {

  public final TalonFX motor;
  public final SpectrumSolenoid gate;

  public final DigitalInput TowerTop = new DigitalInput(0);
  public final DigitalInput TowerBot = new DigitalInput(1);

  /**
   * Creates a new Intake.
   */
  public Tower() {
    motor = new TalonFX(Constants.TowerConstants.kTowerMotor);
    motor.setInverted(true);
    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 40, 45, 0.5);
    motor.configSupplyCurrentLimit(supplyCurrentLimit);
    motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    gate = new SpectrumSolenoid(Constants.TowerConstants.kTowerGate);
  }

  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double speed){
    motor.set(ControlMode.PercentOutput, speed);
  }

  public void feed(){
    setSpeed(1.0);
  }

  public void fullDown(){
    setSpeed(-1.0);
  }

  public void indexUp(){
    setSpeed(0.5);
  }

  public void indexDown(){
    setSpeed(-0.5);
  }

  public void stop(){
    motor.set(ControlMode.PercentOutput,0);
  }

  public void close(){
    gate.set(true);
  }

  public void open(){
    gate.set(false);
  }

  public void slowDown(){
    setSpeed(-0.25);
  }

  public void SmartDash() {
  SmartDashboard.putBoolean("TopSensorTower", TowerTop.get());
  SmartDashboard.putBoolean("BotSensorTower", TowerBot.get());
  }

  public Boolean getTop(){
    return !TowerTop.get();
  }
  public Boolean getBot(){
    return !TowerBot.get();
  }

}
