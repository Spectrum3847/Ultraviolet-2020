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

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.robot.Robot;

public class Climber extends SubsystemBase {

  public static final class Constants {
    public static final int kMasterMotor = 50;
    public static final int kFollowerMotor = 51;
  }

  public final CANSparkMax masterMotor;
  public final CANSparkMax followerMotor;
  private CANEncoder m_encoder;
  private CANPIDController m_pidController;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

  /**
   * Creates a new CLimbe.
   */
  public Climber() {
    masterMotor = new CANSparkMax(Constants.kMasterMotor, MotorType.kBrushless);
    masterMotor.restoreFactoryDefaults();
    masterMotor.setSmartCurrentLimit(40);
    //masterMotor.setInverted();
    masterMotor.burnFlash();

    followerMotor = new CANSparkMax(Constants.kFollowerMotor, MotorType.kBrushless);
    followerMotor.restoreFactoryDefaults();
    followerMotor.setSmartCurrentLimit(40);
    //followerMotor.setInverted();
    followerMotor.burnFlash();

    followerMotor.follow(masterMotor);

    m_pidController = masterMotor.getPIDController();

    m_encoder = masterMotor.getEncoder();

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
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  //Set up HelixLogger sources here
  private void setupLogs() {

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
