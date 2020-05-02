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
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.Robot;

public class Climber extends SubsystemBase {

  public static final class Constants {
    public static final int kMasterMotor = 50;
    public static final int kFollowerMotor = 51;
  }

  public final CANSparkMax masterMotor;
  public final CANSparkMax followerMotor;
  private CANPIDController m_pidController;
  private CANEncoder m_encoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, minVel, maxAcc, allowedErr, position, setpoint;
  public double top, bot;


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
    kP = SpectrumPreferences.getInstance().addNumber("Climber/ kP", 0);
    kI = SpectrumPreferences.getInstance().addNumber("Climber/ kI", 0);
    kD = SpectrumPreferences.getInstance().addNumber("Climber/ kD", 0);
    kIz = SpectrumPreferences.getInstance().addNumber("Climber/ I Zone", 0);
    kFF = SpectrumPreferences.getInstance().addNumber("Climber/ Feed Forward", 0);
    kMaxOutput = SpectrumPreferences.getInstance().addNumber("Climber/ Max Output", 0);
    kMinOutput = SpectrumPreferences.getInstance().addNumber("Climber/ Min Outout", 0);
    maxRPM = SpectrumPreferences.getInstance().addNumber("Climber/ maxRPM", 0);
    maxVel = SpectrumPreferences.getInstance().addNumber("Climber/ Max Velocity", 0);
    minVel = SpectrumPreferences.getInstance().addNumber("Climber/ Min Velocity", 0);
    maxAcc = SpectrumPreferences.getInstance().addNumber("Climber/ Max Acceleration", 0);
    allowedErr = SpectrumPreferences.getInstance().addNumber("Climber/ Allowed Error", 0);
    setpoint = SpectrumPreferences.getInstance().addNumber("Climber/ setpoint", 0);

    //set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);
    
    m_pidController.setSmartMotionMaxVelocity(maxVel, 0);
    m_pidController.setSmartMotionMinOutputVelocity(minVel, 0);
    m_pidController.setSmartMotionMaxAccel(maxAcc, 0);


    //HelixLogger Setup
    setupLogs();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    position = m_encoder.getPosition()/m_encoder.getPositionConversionFactor();
    SmartDashboard.putNumber("Climber/ position", position);
  }

  public void setPosition(){
    m_pidController.setReference(setpoint, ControlType.kSmartMotion);
  }

  public void setPoisition(double Position){
    m_pidController.setReference(position, ControlType.kSmartMotion);
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
