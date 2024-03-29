/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.robot.Constants;
import frc.robot.Robot;

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

    //Helixlogger setup
    setupLogs();

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

  public void checkMotor(){
    String result = " ";
    double kCurrentThresh = 3;
    double kVelocityThresh = 1000;
    stop();
    double testSpeed = 0.2;
    double testTime = 0.5;

    //test leader
    setSpeed(testSpeed);
    Timer.delay(testTime);
    final double currentL = leftMotor.getOutputCurrent();
    final double velocityL = leftMotor.getEncoder().getVelocity();

    final double currentR = rightMotor.getOutputCurrent();
    final double velocityR = rightMotor.getEncoder().getVelocity();

    if(currentL >= kCurrentThresh){
      result = result + "!!!!!LeftFunnelNEO Voltage Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/PowerdV/MotorL", false);
    }

    if(velocityL >= kVelocityThresh){
      result = result + "!!!!!LeftFunnelNEO Velocity Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/PoweredV/MotorL", false);
    }

    if(currentR >= kCurrentThresh){
      result = result + "!!!!!RightFunnelNEO Voltage Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/PoweredV/MotorR", false);
    }

    if(velocityR >= kVelocityThresh){
      result = result + "!!!!!RightFunnelNEO Velocity Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/PoweredV/MotorR", false);
    }
    stop();
    printWarning(result);
    
  }

  //Set up HelixLogger sources here
  private void setupLogs() {

  }

  public static void printDebug(String msg){
    Debugger.println(msg, Robot._funnel, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, Robot._funnel, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, Robot._funnel, Debugger.warning4);
  }



}
