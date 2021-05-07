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
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.team2363.logger.HelixLogger;

public class Shooter extends SubsystemBase {
  /**
   * Creates a new Shooter.
   */

  public final TalonFX leaderTalonFX;
  public final TalonFX followerTalonFX;
  public final TalonFX acceleratorTalonFX;
  
  private double kP, kI, kD, kF;
  private int iZone;

  private double AkP, AkI, AkD, AkF;
  private int AiZone;

  private double wheelSetpoint = 0;
  private double accelSetpoint = 0;

  /*
  Protobot working PIDF values
  kP = 0.0465
  kI = 0.0005
  kD = 0
  kF = 0.048
  iz = 150

  AkP = 0.045
  AkI = 0.0005
  AkD = 0
  AkF = 0.05
  iz = 150
  */
  
  public Shooter() {
    kP = SpectrumPreferences.getInstance().getNumber("Shooter kP",0.0465);
    kI = SpectrumPreferences.getInstance().getNumber("Shooter kI",0.0005);
    kD = SpectrumPreferences.getInstance().getNumber("Shooter kD",0.0);
    kF = SpectrumPreferences.getInstance().getNumber("Shooter kF",0.0478);
    iZone = (int) SpectrumPreferences.getInstance().getNumber("Shooter I-Zone", 150);

    AkP = SpectrumPreferences.getInstance().getNumber("Accelerator kP",0.045);
    AkI = SpectrumPreferences.getInstance().getNumber("Accelerator kI",0.0005);
    AkD = SpectrumPreferences.getInstance().getNumber("Accelerator kD",0.0);
    AkF = SpectrumPreferences.getInstance().getNumber("Accelerator kF",0.05);
    AiZone = (int) SpectrumPreferences.getInstance().getNumber("Accelerator I-Zone", 150);
    
    leaderTalonFX = new TalonFX(Constants.ShooterConstants.kShooterMotor);
    followerTalonFX = new TalonFX(Constants.ShooterConstants.kFollowerMotor);
    acceleratorTalonFX = new TalonFX(Constants.ShooterConstants.kAcceleratorMotor);
    
    leaderTalonFX.setInverted(false);
    followerTalonFX.setInverted(true);
    acceleratorTalonFX.setInverted(true);
    
    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 40, 45, 0.5);
    leaderTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    followerTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    acceleratorTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);

    leaderTalonFX.configClosedloopRamp(0.2);
    followerTalonFX.configClosedloopRamp(0.2);
    acceleratorTalonFX.configClosedloopRamp(0.2);

    leaderTalonFX.config_kP(0, kP);
    leaderTalonFX.config_kI(0, kI);   
    leaderTalonFX.config_kD(0, kD);  
    leaderTalonFX.config_kF(0, kF);  
    leaderTalonFX.config_IntegralZone(0, iZone);

    acceleratorTalonFX.config_kP(0, AkP);
    acceleratorTalonFX.config_kI(0, AkI);
    acceleratorTalonFX.config_kD(0, AkD);
    acceleratorTalonFX.config_kF(0, AkF);
    acceleratorTalonFX.config_IntegralZone(0, AiZone);

    leaderTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
    acceleratorTalonFX.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    followerTalonFX.follow(leaderTalonFX);
    
    //SmartDashboard.putNumber("Shooter/Setpoint", 5000);
    //SmartDashboard.putNumber("Acceerator/Setpoint", 5000);

    //Helixlogger setup
    setupLogs();

    //Establish Default Command for This Subsystem
    this.setDefaultCommand(new RunCommand(() -> stop(), this));
  }

  public void setPercentOutput(double output, double acceleratorOutput){
    setAcceleratorPercentOutput(acceleratorOutput);
    leaderTalonFX.set(ControlMode.PercentOutput, output);
  }

  public void setAcceleratorPercentOutput(double output){
    acceleratorTalonFX.set(ControlMode.PercentOutput, output);
  }

  public void setVelocity(double velocity){
    wheelSetpoint = velocity;
    leaderTalonFX.set(ControlMode.Velocity, velocity);
  }

  public void setAcceleratorVelocity(double velocity){
    accelSetpoint = velocity;
    acceleratorTalonFX.set(ControlMode.Velocity, velocity);
  }

  public void stop(){
    wheelSetpoint = 0;
    accelSetpoint = 0;
    leaderTalonFX.set(ControlMode.PercentOutput, 0);
    acceleratorTalonFX.set(ControlMode.PercentOutput, 0);
  }

  public double getWheelSetpoint(){
    return wheelSetpoint;
  }

  public double getAccelSetpoint(){
    return accelSetpoint;
  }

  public double getWheelSetpointRPM() {
    return (wheelSetpoint * 600) / 2048 * 1.5;
  }

  public double getAccelSetpointRPM() {
    return (accelSetpoint * 600) / 2048 * 1.5;
  }

  public void dashboardVelocity(){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter

    double wheelRPM = SpectrumPreferences.getInstance().getNumber("Shooter Setpoint",5000);
    double AccelRPM = SpectrumPreferences.getInstance().getNumber("Accelerator Setpoint",5000);
    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AccelVelocity = (AccelRPM / 600 * 2048) / 1.5;
    setVelocity(motorVelocity);
    setAcceleratorVelocity(AccelVelocity);
  }

  //Set both wheels to the same velocity
  public void setShooterVelocity(double wheelRPM){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter
    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AccelVelocity = (wheelRPM / 600 * 2048) / 1.5;
    setVelocity(motorVelocity);
    setAcceleratorVelocity(AccelVelocity);
  }

  //Velocity for main and accelerator wheels
  public void setShooterVelocity(double wheelRPM, double AcceleratorWheelRPM){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter
    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AccelVelocity = (AcceleratorWheelRPM / 600 * 2048) / 1.5;
    setVelocity(motorVelocity);
    setAcceleratorVelocity(AccelVelocity);
  }

  public double getWheelRPM(){
    return (leaderTalonFX.getSelectedSensorVelocity() * 600) / 2048 * 1.5;
  }

  public double getAccelRPM(){
    return (acceleratorTalonFX.getSelectedSensorVelocity() * 600) / 2048 * 1.5;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  //Set up helixlogger sources here
  private void setupLogs() {
    HelixLogger.getInstance().addSource("SHOOTER Vel", leaderTalonFX::getSelectedSensorVelocity);
    HelixLogger.getInstance().addSource("SHOOTER RPM", this::getWheelRPM);
    HelixLogger.getInstance().addSource("SHOOTER Output%", leaderTalonFX::getMotorOutputPercent);
    HelixLogger.getInstance().addSource("LEADER Current", leaderTalonFX::getSupplyCurrent);
    HelixLogger.getInstance().addSource("FOLLOWER Current", followerTalonFX::getSupplyCurrent);

    HelixLogger.getInstance().addSource("ACCEL Vel", acceleratorTalonFX::getSelectedSensorVelocity);
    HelixLogger.getInstance().addSource("ACCEL RPM", this::getAccelRPM);
    HelixLogger.getInstance().addSource("ACCEL Output%", acceleratorTalonFX::getMotorOutputPercent);
    HelixLogger.getInstance().addSource("ACCEL Current", acceleratorTalonFX::getSupplyCurrent);
  }

  public void dashboard() {
    SmartDashboard.putNumber("Shooter/Velocity", leaderTalonFX.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Shooter/WheelRPM", getWheelRPM());
    SmartDashboard.putNumber("Shooter/OutputPercentage", leaderTalonFX.getMotorOutputPercent());
    SmartDashboard.putNumber("Shooter/LeftCurrent", leaderTalonFX.getSupplyCurrent());
    SmartDashboard.putNumber("Shooter/RightCurrent", followerTalonFX.getSupplyCurrent());

    SmartDashboard.putNumber("Accelerator/Velocity", acceleratorTalonFX.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Accelerator/WheelRPM", getAccelRPM());
    SmartDashboard.putNumber("Accelerator/OutputPercentage", acceleratorTalonFX.getMotorOutputPercent());
    SmartDashboard.putNumber("Accelerator/LeftCurrent", acceleratorTalonFX.getSupplyCurrent());
  }

  public static void printDebug(String msg){
    Debugger.println(msg, Robot._shooter, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, Robot._shooter, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, Robot._shooter, Debugger.warning4);
  }
}
