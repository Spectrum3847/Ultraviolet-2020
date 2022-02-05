/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
//import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.lib.drivers.SpectrumSolenoid;
import frc.lib.util.Debugger;
import frc.robot.Robot;
import frc.robot.commands.drive.Drive;
import frc.team2363.logger.HelixLogger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {

  public static final class Constants {
    public static final int kLeftFrontMotor = 10;
    public static final int kLeftRearMotor = 11;
    public static final int kRightFrontMotor = 20;
    public static final int kRightRearMotor = 21;

    public static final int kShifter = 1;
  }

  /**
   * Creates a new Drivetrain.
   */
  
  public final WPI_TalonFX leftRearTalonFX;
  public final WPI_TalonFX leftFrontTalonFX;
  public final WPI_TalonFX rightRearTalonFX;
  public final WPI_TalonFX rightFrontTalonFX;
  public final SpectrumSolenoid shifter;

  private double kP, kI, kD, kF, kIz;
  private double imu_kP, kAngleSetpoint, turningValue;

  public Drivetrain() {

    leftRearTalonFX = new WPI_TalonFX(Constants.kLeftRearMotor);
    leftFrontTalonFX = new WPI_TalonFX(Constants.kLeftFrontMotor);
    rightRearTalonFX = new WPI_TalonFX(Constants.kRightRearMotor);
    rightFrontTalonFX = new WPI_TalonFX(Constants.kRightFrontMotor);

    // PID Coefficients
    kP = 0.042;
    kI = 0;
    kD = 0;
    kF = 0.0452;
    kIz = 0;

    // imu PID Coefficients
    imu_kP = 0.042;
    leftRearTalonFX.configFactoryDefault();
    leftFrontTalonFX.configFactoryDefault();
    rightRearTalonFX.configFactoryDefault();
    rightFrontTalonFX.configFactoryDefault();

    leftRearTalonFX.setInverted(true);
    leftFrontTalonFX.setInverted(true);
    rightRearTalonFX.setInverted(true);
    rightFrontTalonFX.setInverted(true);

    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 60, 65, 3);
    leftFrontTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    rightFrontTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    leftRearTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    rightRearTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);

    leftFrontTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    rightFrontTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    setPIDFValues();

    leftRearTalonFX.follow(leftFrontTalonFX);
    rightRearTalonFX.follow(rightFrontTalonFX);

    brakeMode();

    //Shifter Setup
    shifter = new SpectrumSolenoid(Constants.kShifter);

    //HelixLogger Setup
    setupLogs();

    //Set the Default Command for this subsystem
    setDefaultCommand(new Drive(this));
  }

  private void setPIDFValues() {
    leftFrontTalonFX.config_kP(0, kP);
    leftFrontTalonFX.config_kI(0, kI);
    leftFrontTalonFX.config_kD(0, kD);
    leftFrontTalonFX.config_kF(0, kF);
    leftFrontTalonFX.config_IntegralZone(0, (int) kIz);

    rightFrontTalonFX.config_kP(0, kP);
    rightFrontTalonFX.config_kI(0, kI);
    rightFrontTalonFX.config_kD(0, kD);
    rightFrontTalonFX.config_kF(0, kF);
    rightFrontTalonFX.config_IntegralZone(0, (int) kIz);
  }

  protected double limit(double value) {
    if (value > 1.0) {
      return 1.0;
    }
    if (value < -1.0) {
      return -1.0;
    }
    return value;
  }

  public void arcadeDrive(double moveSpeed, double rotateSpeed) {
    //Cube rotation speed to give us better low end performance espeically after deadzone
    rotateSpeed = Math.copySign(Math.pow(rotateSpeed,3), rotateSpeed);
    rotateSpeed = limit(rotateSpeed) * 0.6;
    
    moveSpeed = limit(moveSpeed);

    //Make the deadzone bigger if we are driving fwd or backwards and not turning in place
    if (Math.abs(moveSpeed) > 0.1 && Math.abs(rotateSpeed)< 0.05){
      rotateSpeed = 0;
    }
    double leftMotorOutput;
    double rightMotorOutput;

    double maxInput = Math.copySign(Math.max(Math.abs(moveSpeed), Math.abs(rotateSpeed)), moveSpeed);
    if (moveSpeed == 0){
      leftMotorOutput = rotateSpeed;
      rightMotorOutput = -rotateSpeed; 
    } else {
      if (moveSpeed >= 0.0) {
        // First quadrant, else second quadrant
        if (rotateSpeed >= 0.0) {
          leftMotorOutput = maxInput;
          rightMotorOutput = moveSpeed - rotateSpeed;
        } else {
          leftMotorOutput = moveSpeed + rotateSpeed;
          rightMotorOutput = maxInput;
        }
      } else {
        // Third quadrant, else fourth quadrant
        if (rotateSpeed >= 0.0) {
          leftMotorOutput = moveSpeed + rotateSpeed;
          rightMotorOutput = maxInput;
        } else {
          leftMotorOutput = maxInput;
          rightMotorOutput = moveSpeed - rotateSpeed;
        }
      }
    }

    leftFrontTalonFX.set(limit(leftMotorOutput));
    rightFrontTalonFX.set(limit(rightMotorOutput));

    //leftFrontTalonFX.set(TalonFXControlMode.Velocity, leftMotorOutput * 15000);
    //rightFrontTalonFX.set(TalonFXControlMode.Velocity, rightMotorOutput * 15000);
  }

  public double getHeading() {
    return 0;
  }

  public double getAngleSetpoint(){
    return kAngleSetpoint;
  }

  public void setSetpoint(final double left, final double right) {
    setVelocityOutput(fpsToTicksPer100ms(left), fpsToTicksPer100ms(right));
  }
  
  public void resetDistance() {
    rightFrontTalonFX.getSensorCollection().setIntegratedSensorPosition(0, 0);
    leftFrontTalonFX.getSensorCollection().setIntegratedSensorPosition(0, 0);
  }


  public double getCurrentDistance() {
    return (
              ticksToFeet(rightFrontTalonFX.getSensorCollection().getIntegratedSensorPosition())
              + ticksToFeet(leftFrontTalonFX.getSensorCollection().getIntegratedSensorPosition())
          ) / 2;
  }


  //Motor encoder ticks(2048) / encoder ticks per motor rotation / low gear ratio * wheel circumference(feet) per wheel rotation 
  private double ticksToFeet(double ticks) {
    return ticks / 2048  / 16 * (9 * Math.PI / 12);
  }



  public void setAngle(final double angle){
    kAngleSetpoint = angle;
    turningValue = (kAngleSetpoint * imu_kP);
  }

  public void turn(){
    arcadeDrive(turningValue, 0);
  }

  //CHECK AGAIN
  // fps * (ft per wheel rotation) * low gear ratio * ticks per shaft rev / 100ms per second
  private double fpsToTicksPer100ms(double fps) {
    return fps * (12 / 9 * Math.PI) * 16 * 2048 / 10;
  }

  private void setVelocityOutput(final double leftVelocity, final double rightVelocity) {
    leftFrontTalonFX.set(TalonFXControlMode.Velocity, leftVelocity);
    rightFrontTalonFX.set(TalonFXControlMode.Velocity, rightVelocity);
  }
  public void setPositionOutput(final double leftPosition, final double rightPosition){
    leftFrontTalonFX.set(TalonFXControlMode.Position, fpsToTicksPer100ms(leftPosition));
    rightFrontTalonFX.set(TalonFXControlMode.Position, fpsToTicksPer100ms(-rightPosition));
  }
  public void stop(){
    leftFrontTalonFX.set(TalonFXControlMode.PercentOutput, 0);
    rightFrontTalonFX.set(TalonFXControlMode.PercentOutput, 0);
  }
  public void highGear(){
    shifter.set(true);
    printDebug("HighGear Engaged");
  }

  public void lowGear(){
    shifter.set(false);
    printDebug("HighGear Disengaged");
  }

  public void brakeMode() {
    leftFrontTalonFX.setNeutralMode(NeutralMode.Brake);
    rightFrontTalonFX.setNeutralMode(NeutralMode.Brake);
    leftRearTalonFX.setNeutralMode(NeutralMode.Coast);
    rightRearTalonFX.setNeutralMode(NeutralMode.Coast);
  }

  public void coastMode() {
    leftFrontTalonFX.setNeutralMode(NeutralMode.Coast);
    rightFrontTalonFX.setNeutralMode(NeutralMode.Coast);
    leftRearTalonFX.setNeutralMode(NeutralMode.Coast);
    rightRearTalonFX.setNeutralMode(NeutralMode.Coast);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  //Set up Helixlogger sources here
  private void setupLogs() {
    HelixLogger.getInstance().addSource("DRIVE HighGear", shifter::get);

    HelixLogger.getInstance().addSource("DRIVE LeftVel", leftFrontTalonFX.getSensorCollection()::getIntegratedSensorVelocity);
    HelixLogger.getInstance().addSource("DRIVE RightVel", rightFrontTalonFX.getSensorCollection()::getIntegratedSensorVelocity);

    HelixLogger.getInstance().addSource("DRIVE LeftStator", leftFrontTalonFX::getStatorCurrent);
    HelixLogger.getInstance().addSource("DRIVE RightStator", rightFrontTalonFX::getStatorCurrent);

    HelixLogger.getInstance().addSource("DRIVE LeftSupply", leftFrontTalonFX::getSupplyCurrent);
    HelixLogger.getInstance().addSource("DRIVE RightSupply", rightFrontTalonFX::getSupplyCurrent);
  }

  public void dashboard() {
    SmartDashboard.putNumber("Drive/LeftPos", leftFrontTalonFX.getSensorCollection().getIntegratedSensorPosition());
    SmartDashboard.putNumber("Drive/RightPos", rightFrontTalonFX.getSensorCollection().getIntegratedSensorPosition());
    SmartDashboard.putNumber("Drive/RightStator", rightFrontTalonFX.getStatorCurrent());
    SmartDashboard.putNumber("Drive/LeftStator", leftFrontTalonFX.getStatorCurrent());
    SmartDashboard.putNumber("Drive/RightVel", rightFrontTalonFX.getSensorCollection().getIntegratedSensorVelocity());
    SmartDashboard.putNumber("Drive/LeftVel", leftFrontTalonFX.getSensorCollection().getIntegratedSensorVelocity());
    SmartDashboard.putBoolean("Drive/HighGear", shifter.get());
    SmartDashboard.putBoolean("Limelight-LED Toggle", false);
    SmartDashboard.putNumber("imu", getHeading());
  }

  public static void printDebug(String msg){
    Debugger.println(msg, Robot._drive, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, Robot._drive, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, Robot._drive, Debugger.warning4);
  }
}

//https://www.youtube.com/watch?v=oHg5SJYRHA0