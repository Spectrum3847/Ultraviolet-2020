/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Funnel extends SubsystemBase {

  public final CANSparkMax leftMotor;
  public final CANSparkMax rightMotor;

  /**
   * Creates a new Intake.
   */
  public Funnel() {
    leftMotor = new CANSparkMax(Constants.FunnelConstants.kLeftMotor, MotorType.kBrushless);
    leftMotor.restoreFactoryDefaults();
    leftMotor.setSmartCurrentLimit(20);
    leftMotor.setInverted(false);
    leftMotor.burnFlash();

    rightMotor = new CANSparkMax(Constants.FunnelConstants.kRightMotor, MotorType.kBrushless);
    rightMotor.restoreFactoryDefaults();
    rightMotor.setSmartCurrentLimit(20);
    rightMotor.setInverted(true);
    rightMotor.burnFlash();

    this.setDefaultCommand(new RunCommand(() -> stop(), this));
  }

  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double leftSpeed, double rightSpeed){
    leftMotor.set(leftSpeed);
    rightMotor.set(rightSpeed);
  }

  public void setSpeed(double speed){
    setSpeed(speed,speed);
  }

  public void FullFwd(){
    setSpeed(1.0);
  }

  public void FullRev(){
    setSpeed(-1.0);
  }

  public void FullMix(){
    setSpeed(1,-1);
  }

  public void feed(){
    setSpeed(1, 0.8);
  }

  public void intakeTower(){
    setSpeed(0.5,0.6);
  }

  public void intakeHold(){
    setSpeed(-.5,-.4);
  }

  public void stop(){
    leftMotor.stopMotor();
    rightMotor.stopMotor();
  }

}
