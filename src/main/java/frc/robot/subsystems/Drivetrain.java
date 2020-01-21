/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
//import com.ctre.phoenix.motorcontrol.StatorCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drivetrain extends SubsystemBase {
  /**
   * Creates a new Drivetrain.
   */
  
  public final WPI_TalonFX leftRearTalonFX;
  public final WPI_TalonFX leftFrontTalonFX;
  public final WPI_TalonFX rightRearTalonFX;
  public final WPI_TalonFX rightFrontTalonFX;

  //public DifferentialDrive differentialDrive;

  public Drivetrain() {

    leftRearTalonFX = new WPI_TalonFX(Constants.DriveConstants.kLeftRearMotor);
    leftFrontTalonFX = new WPI_TalonFX(Constants.DriveConstants.kLeftFrontMotor);
    rightRearTalonFX = new WPI_TalonFX(Constants.DriveConstants.kRightRearMotor);
    rightFrontTalonFX = new WPI_TalonFX(Constants.DriveConstants.kRightFrontMotor);

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

    leftRearTalonFX.follow(leftFrontTalonFX);
    rightRearTalonFX.follow(rightFrontTalonFX);

    leftFrontTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    rightFrontTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

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
    rotateSpeed = Math.copySign(Math.pow(rotateSpeed,2), rotateSpeed);
    rotateSpeed = limit(rotateSpeed) * 0.8;
    
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

    leftFrontTalonFX.set(limit(leftMotorOutput) * 1.0);
    rightFrontTalonFX.set(limit(rightMotorOutput) * 1.0);
  }

  public double getHeading() {
    final double yaw = RobotContainer.navX.getYaw();
    return yaw;
  }

  public void setSetpoint(final double left, final double right) {
    setVelocityOutput(left * 1375.7, right * 1375.7);
  }

  private void setVelocityOutput(final double leftVelocity, final double rightVelocity) {
    leftFrontTalonFX.set(TalonFXControlMode.Velocity, leftVelocity);
    rightFrontTalonFX.set(TalonFXControlMode.Velocity, rightVelocity);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void dashboard() {
    SmartDashboard.putNumber("Drive/LeftPos", leftFrontTalonFX.getSensorCollection().getIntegratedSensorPosition());
    SmartDashboard.putNumber("Drive/RightPos", rightFrontTalonFX.getSensorCollection().getIntegratedSensorPosition());
    SmartDashboard.putNumber("Drive/RightStator", rightFrontTalonFX.getStatorCurrent());
    SmartDashboard.putNumber("Drive/LeftStator", leftFrontTalonFX.getStatorCurrent());
    SmartDashboard.putNumber("Drive/RightVel", rightFrontTalonFX.getSensorCollection().getIntegratedSensorVelocity());
    SmartDashboard.putNumber("Drive/LeftVel", leftFrontTalonFX.getSensorCollection().getIntegratedSensorVelocity());
  }
}