/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class Climber extends SubsystemBase {

  public static final class Constants {
    public static final int kMasterMotor = 50;
    public static final int kFollowerMotor = 51;
  }

  public final CANSparkMax leaderMotor;
  public final CANSparkMax followerMotor;
  private CANEncoder m_encoder;
  private CANPIDController m_pidController;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

  /**
   * Creates a new CLimbe.
   */
  public Climber() {
    leaderMotor = new CANSparkMax(Constants.kMasterMotor, MotorType.kBrushless);
    leaderMotor.restoreFactoryDefaults();
    leaderMotor.setSmartCurrentLimit(40);
    leaderMotor.setIdleMode(IdleMode.kBrake);
    //masterMotor.setInverted();
    leaderMotor.burnFlash();

    followerMotor = new CANSparkMax(Constants.kFollowerMotor, MotorType.kBrushless);
    followerMotor.restoreFactoryDefaults();
    followerMotor.setSmartCurrentLimit(40);
    followerMotor.setIdleMode(IdleMode.kBrake);
    //followerMotor.setInverted();
    followerMotor.follow(leaderMotor);
    followerMotor.burnFlash();

    m_pidController = leaderMotor.getPIDController();

    m_encoder = leaderMotor.getEncoder();

    //PID coefficients
    kP = 0;
    kI = 0;
    kD = 0;
    kIz = 0;
    kFF = 0;
    kMaxOutput = 0;
    kMinOutput = 0;
    maxRPM = 0;

    //set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);

    //HelixLogger Setup
    setupLogs();

    //Setup Default Command
    this.setDefaultCommand(new RunCommand(() -> setManualOutput(RobotContainer.operatorController.leftStick.getY()), this));
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setManualOutput(double speed){
    leaderMotor.set(speed);
  }

  public void stop(){
    leaderMotor.stopMotor();
  }

  //Set up HelixLogger sources here
  private void setupLogs() {

  }

  private void dashboard() {

  }

  public static void printDebug(String msg){
    Debugger.println(msg, Robot._climber, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, Robot._climber, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, Robot._climber, Debugger.warning4);
  }
}
