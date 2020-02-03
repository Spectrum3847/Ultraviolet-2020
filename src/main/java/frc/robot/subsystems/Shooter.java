/*----------------------------------------------------------------------------*/

/*

Calculate the expect peak sensor velocity (sensor units per 100ms) as:
(kMaxRPM  / 600) * (kSensorUnitsPerRotation / kGearRatio)

(6380 / 600) * (2048) = 21777.0666667

*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  public final TalonFX leaderTalonFX;
  public final TalonFX followerTalonFX;
  /**
   * Creates a new Shooter.
   */
  public Shooter() {

    leaderTalonFX = new TalonFX(Constants.ShooterConstants.kLeftlMotor);
    followerTalonFX = new TalonFX(Constants.ShooterConstants.kRightMotor);

    //leftTalonFX.configFactoryDefault();
    //rightTalonFX.configFactoryDefault();
    
    leaderTalonFX.setInverted(false);
    followerTalonFX.setInverted(true);
    
    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 40, 45, 0.5);
    leaderTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    followerTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);

    leaderTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    followerTalonFX.follow(leaderTalonFX);
  }

  public void setPercentOutput(double output){
    leaderTalonFX.set(ControlMode.PercentOutput, output);
  }

  public void setVelocity(double velocity){
    leaderTalonFX.set(ControlMode.Velocity, velocity);
  }

  public void dashboardVelocity(){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter

    double wheelRPM = SmartDashboard.getNumber("Shooter/Setpoint", 3000);

    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    leaderTalonFX.set(ControlMode.Velocity, motorVelocity);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void dashboard() {
    SmartDashboard.putNumber("Shooter/Velocity", leaderTalonFX.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Shooter/OutputPercentage", leaderTalonFX.getMotorOutputPercent());
    SmartDashboard.putNumber("Shooter/LeftCurrent", leaderTalonFX.getSupplyCurrent());
    SmartDashboard.putNumber("Shooter/RightCurrent", followerTalonFX.getSupplyCurrent());
  }
}
