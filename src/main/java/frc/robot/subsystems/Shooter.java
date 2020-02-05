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
import frc.lib.util.SpectrumPreferences;
import frc.robot.Constants;

public class Shooter extends SubsystemBase {

  public final TalonFX leaderTalonFX;
  public final TalonFX followerTalonFX;
  /**
   * Creates a new Shooter.
   */
  private double kP, kI, kD, kF;
  private int iZone;

  public Shooter() {
    kP = SpectrumPreferences.getInstance().getNumber("Shooter kP",0.05);
    kI = SpectrumPreferences.getInstance().getNumber("Shooter kI",0.001);
    kD = SpectrumPreferences.getInstance().getNumber("Shooter kD",0.07);
    kF = SpectrumPreferences.getInstance().getNumber("Shooter kF",0.0472);
    iZone = (int) SpectrumPreferences.getInstance().getNumber("Shooter I-Zone", 150;


    

    leaderTalonFX = new TalonFX(Constants.ShooterConstants.kLeftlMotor);
    followerTalonFX = new TalonFX(Constants.ShooterConstants.kRightMotor);

    //leftTalonFX.configFactoryDefault();
    //rightTalonFX.configFactoryDefault();
    
    leaderTalonFX.setInverted(false);
    followerTalonFX.setInverted(true);
    
    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 40, 45, 0.5);
    leaderTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    followerTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);

    leaderTalonFX.config_kP(0, kP);
    leaderTalonFX.config_kI(0, kI);   
    leaderTalonFX.config_kD(0, kD);  
    leaderTalonFX.config_kF(0, kF);  
    leaderTalonFX.config_IntegralZone(0, iZone);

    leaderTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    followerTalonFX.follow(leaderTalonFX);
    SmartDashboard.putNumber("Shooter/Setpoint", 5000);
  }

  public void setPercentOutput(double output){
    leaderTalonFX.set(ControlMode.PercentOutput, output);
  }

  public void setVelocity(double velocity){
    leaderTalonFX.set(ControlMode.Velocity, velocity);
  }

  public void stop(){
    leaderTalonFX.set(ControlMode.PercentOutput, 0);
  }

  public void dashboardVelocity(){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter

    double wheelRPM = 5000; // SmartDashboard.getNumber("Shooter/Setpoint", 6000);

    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    leaderTalonFX.set(ControlMode.Velocity, motorVelocity);
  }

  public double getWheelRPM(){
    return (leaderTalonFX.getSelectedSensorVelocity() * 600) / 2048 * 1.5;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void dashboard() {
    SmartDashboard.putNumber("Shooter/Velocity", leaderTalonFX.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Shooter/WheelRPM", getWheelRPM());
    SmartDashboard.putNumber("Shooter/OutputPercentage", leaderTalonFX.getMotorOutputPercent());
    SmartDashboard.putNumber("Shooter/LeftCurrent", leaderTalonFX.getSupplyCurrent());
    SmartDashboard.putNumber("Shooter/RightCurrent", followerTalonFX.getSupplyCurrent());
  }
}
