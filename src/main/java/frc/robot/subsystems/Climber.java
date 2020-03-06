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
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMax.SoftLimitDirection;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.robot.Robot;
import frc.robot.RobotContainer;

public class Climber extends SubsystemBase {

  public static final class Constants {
    public static final int kLeaderMotor = 50;
    public static final int kFollowerMotor = 51;
    public static final int kSBrake = 7;
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
    leaderMotor = new CANSparkMax(Constants.kLeaderMotor, MotorType.kBrushless);
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

    m_encoder.setPosition(0);

    leaderMotor.enableSoftLimit(SoftLimitDirection.kForward, true);
    leaderMotor.enableSoftLimit(SoftLimitDirection.kReverse, true);

    leaderMotor.setSoftLimit(SoftLimitDirection.kForward, 240);
    leaderMotor.setSoftLimit(SoftLimitDirection.kReverse, 0);

    //Setup Default Command
    this.setDefaultCommand(new RunCommand(() -> setManualOutput(RobotContainer.operatorController.rightStick.getY()), this));
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

  public void dashboard() {
    SmartDashboard.putNumber("Climber/Posiition", m_encoder.getPosition());
    SmartDashboard.putNumber("Climber/Velocity", m_encoder.getVelocity());
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
