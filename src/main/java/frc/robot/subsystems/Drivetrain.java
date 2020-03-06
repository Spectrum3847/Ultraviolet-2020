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
import frc.robot.RobotContainer;
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

    public static final int kShifter = 7;

    public static final double minCommand = 0.05;
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

  public Drivetrain() {

    leftRearTalonFX = new WPI_TalonFX(Constants.kLeftRearMotor);
    leftFrontTalonFX = new WPI_TalonFX(Constants.kLeftFrontMotor);
    rightRearTalonFX = new WPI_TalonFX(Constants.kRightRearMotor);
    rightFrontTalonFX = new WPI_TalonFX(Constants.kRightFrontMotor);

    //PID Coefficients
    kP = 0.042;
    kI = 0;
    kD = 0;
    kF = 0.0452;
    kIz = 0;

    leftRearTalonFX.configFactoryDefault();
    leftFrontTalonFX.configFactoryDefault();
    rightRearTalonFX.configFactoryDefault();
    rightFrontTalonFX.configFactoryDefault();

    leftRearTalonFX.setInverted(true);
    leftFrontTalonFX.setInverted(true);
    rightRearTalonFX.setInverted(false);
    rightFrontTalonFX.setInverted(false);

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

    leftFrontTalonFX.setNeutralMode(NeutralMode.Brake);
    rightFrontTalonFX.setNeutralMode(NeutralMode.Brake);
    leftRearTalonFX.setNeutralMode(NeutralMode.Coast);
    rightRearTalonFX.setNeutralMode(NeutralMode.Coast);

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

  public void arcadeDrive(double xSpeed, double zRotation) {
    xSpeed = limit(xSpeed);

    //Make the deadzone bigger if we are driving fwd or backwards and not turning in place
    if (Math.abs(xSpeed) > 0.1 && Math.abs(zRotation)< 0.05){
      zRotation = 0;
    }

    double leftMotorOutput;
    double rightMotorOutput;

    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);
    if (xSpeed == 0){
      leftMotorOutput = zRotation;
      rightMotorOutput = -zRotation;
    } else {
      if (xSpeed >= 0.0) {
        // First quadrant, else second quadrant
        if (zRotation >= 0.0) {
          leftMotorOutput = maxInput;
          rightMotorOutput = xSpeed - zRotation;
        } else {
          leftMotorOutput = xSpeed + zRotation;
          rightMotorOutput = maxInput;
        }
      } else {
        // Third quadrant, else fourth quadrant
        if (zRotation >= 0.0) {
          leftMotorOutput = xSpeed + zRotation;
          rightMotorOutput = maxInput;
        } else {
          leftMotorOutput = maxInput;
          rightMotorOutput = xSpeed - zRotation;
        }
      }
    }

    leftFrontTalonFX.set(limit(leftMotorOutput));
    rightFrontTalonFX.set(limit(rightMotorOutput));
  }

  public void autoArcadeDrive(double xSpeed, double zRotation) {
    double leftMotorOutput;
    double rightMotorOutput;

    double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);
    if (xSpeed == 0){
      leftMotorOutput = zRotation;
      rightMotorOutput = -zRotation;
    } else {
      if (xSpeed >= 0.0) {
        // First quadrant, else second quadrant
        if (zRotation >= 0.0) {
          leftMotorOutput = maxInput;
          rightMotorOutput = xSpeed - zRotation;
        } else {
          leftMotorOutput = xSpeed + zRotation;
          rightMotorOutput = maxInput;
        }
      } else {
        // Third quadrant, else fourth quadrant
        if (zRotation >= 0.0) {
          leftMotorOutput = xSpeed + zRotation;
          rightMotorOutput = maxInput;
        } else {
          leftMotorOutput = maxInput;
          rightMotorOutput = xSpeed - zRotation;
        }
      }
    }

    leftFrontTalonFX.set(limit(leftMotorOutput));
    rightFrontTalonFX.set(limit(rightMotorOutput));
  }

  public void useOutput(double output) {
    if(RobotContainer.visionLL.getLLDegToTarget() > 0.3) {
      RobotContainer.drivetrain.autoArcadeDrive(0, -output + 0.1);
    } else if(RobotContainer.visionLL.getLLDegToTarget() < 0.3) {
      RobotContainer.drivetrain.autoArcadeDrive(0, -output - 0.1);
    }
  }

  public double getHeading() {
    final double yaw = RobotContainer.adis16470.getAngle();
    return yaw;
  }

  public void setSetpoint(final double left, final double right) {
    setVelocityOutput(fpsToTicksPer100ms(left), fpsToTicksPer100ms(right));
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

  public void highGear(){
    shifter.set(true);
    printDebug("HighGear Engaged");
  }

  public void lowGear(){
    shifter.set(false);
    printDebug("HighGear Disengaged");
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