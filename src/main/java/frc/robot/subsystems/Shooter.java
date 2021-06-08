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

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.team2363.logger.HelixLogger;

public class Shooter extends SubsystemBase {

  public final TalonFX leaderTalonFX;

  public final TalonFX follower2TalonFX;
  public final TalonFX acceleratorTalonFX;
  /**
   * Creates a new Shooter.
   */
  private double kP, kI, kD, kF;
  private int iZone;

  private double AkP, AkI, AkD, AkF;
  private int AiZone;
  private double shooterSetpoint, accelSetpoint;

  public Shooter() {
    kP = SpectrumPreferences.getInstance().getNumber("Shooter kP",0.0465);
    kI = SpectrumPreferences.getInstance().getNumber("Shooter kI",0.0005);
    kD = SpectrumPreferences.getInstance().getNumber("Shooter kD",0.0);
    kF = SpectrumPreferences.getInstance().getNumber("Shooter kF",0.048);
    iZone = (int) SpectrumPreferences.getInstance().getNumber("Shooter I-Zone", 150);

    AkP = SpectrumPreferences.getInstance().getNumber("Accelerator kP",0.045);
    AkI = SpectrumPreferences.getInstance().getNumber("Accelerator kI",0.0005);
    AkD = SpectrumPreferences.getInstance().getNumber("Accelerator kD",0.0);
    AkF = SpectrumPreferences.getInstance().getNumber("Accelerator kF",0.05);
    AiZone = (int) SpectrumPreferences.getInstance().getNumber("Accelerator I-Zone", 150);
    
    shooterSetpoint = 0;
    accelSetpoint = 0;
        
<<<<<<< Updated upstream
    leaderTalonFX = new TalonFX(Constants.ShooterConstants.kLeftlMotor);
    follower2TalonFX = new TalonFX(Constants.ShooterConstants.kRightBotMotor);
=======
    leaderTalonFX = new TalonFX(Constants.ShooterConstants.kShooterMotor);
    follower2TalonFX = new TalonFX(Constants.ShooterConstants.kFollowerMotor);
>>>>>>> Stashed changes
    acceleratorTalonFX = new TalonFX(Constants.ShooterConstants.kAcceleratorMotor);


    
    leaderTalonFX.setInverted(false);
    follower2TalonFX.setInverted(true);
    acceleratorTalonFX.setInverted(true);
    
    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 40, 45, 0.5);
    leaderTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    follower2TalonFX.configSupplyCurrentLimit(supplyCurrentLimit);
    acceleratorTalonFX.configSupplyCurrentLimit(supplyCurrentLimit);

    leaderTalonFX.configClosedloopRamp(0.2);
    follower2TalonFX.configClosedloopRamp(0.2);
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

    follower2TalonFX.follow(leaderTalonFX);
    
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

  public void setPercentOutput(double output){
    leaderTalonFX.set(ControlMode.PercentOutput, output);
  }

  public void setAcceleratorPercentOutput(double output){
    acceleratorTalonFX.set(ControlMode.PercentOutput, output);
  }

  public void setVelocity(double velocity){
    leaderTalonFX.set(ControlMode.Velocity, velocity);
    shooterSetpoint = ((velocity * 1.5) / 2048) * 600;
  }

  public void setAcceleratorVelocity(double velocity){
    acceleratorTalonFX.set(ControlMode.Velocity, velocity);
    accelSetpoint = ((velocity * 1.5) / 2048) * 600;
  }

  public void stop(){
    leaderTalonFX.set(ControlMode.PercentOutput, 0);
    acceleratorTalonFX.set(ControlMode.PercentOutput, 0);
  }

  public void dashboardVelocity(){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter

    double wheelRPM = SpectrumPreferences.getInstance().getNumber("Shooter Setpoint",5000);
    double AwheelRPM = SpectrumPreferences.getInstance().getNumber("Accelerator Setpoint",5000);
    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AmotorVelocity = (AwheelRPM / 600 * 2048) / 1.5;
    setVelocity(motorVelocity);
    setAcceleratorVelocity(AmotorVelocity);

  }

  public void setShooterLL(){
    double wheelRPM =RobotContainer.visionLL.getRPM();
    
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AccelVelocity = (wheelRPM / 600 * 2048) / 1.5;
    setVelocity(motorVelocity);
    setAcceleratorVelocity(AccelVelocity);

  }
  //Velocity for main and accelerator wheels
  public void setShooterVelocity(double wheelRPM, double AcceleratorWheelRPM){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter

    double AwheelRPM = AcceleratorWheelRPM;
    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AmotorVelocity = (AwheelRPM / 600 * 2048) / 1.5;

    setVelocity(motorVelocity);
    setAcceleratorVelocity(AmotorVelocity);
  }
    //Velocity for main and accelerator wheels
  public void setShooterVelocity(double wheelRPM){
    //Sensor Velocity in ticks per 100ms / Sensor Ticks per Rev * 600 (ms to min) * 1.5 gear ratio to shooter

    double AwheelRPM = wheelRPM;
    //Motor Velocity in RPM / 600 (ms to min) * Sensor ticks per rev / Gear Ratio
    double motorVelocity = (wheelRPM / 600 * 2048) / 1.5;
    double AmotorVelocity = (AwheelRPM / 600 * 2048) / 1.5;

    setVelocity(motorVelocity);
    setAcceleratorVelocity(AmotorVelocity);
  }

  public double getWheelRPM(){
    return (leaderTalonFX.getSelectedSensorVelocity() * 1.5) / 2048 * 600;
  }

  public double getAccelRPM(){
    return (acceleratorTalonFX.getSelectedSensorVelocity() * 1.5) / 2048 * 600;
  }

  public double getShooterSetpointRPM(){
    return shooterSetpoint;
  }

  public double getAccelSetpointRPM(){
    return accelSetpoint;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void checkAcceleratorMotor(){
    String result = " ";
    double kCurrentThresh = 3;
    double kVelocityThresh = 1000;
    stop();
    double testSpeed = 0.2;
    double testTime = 0.5;

    //test leader
    acceleratorTalonFX.set(ControlMode.PercentOutput, testSpeed);
    Timer.delay(testTime);
    final double currentLeader = acceleratorTalonFX.getMotorOutputVoltage();
    final double velocityLeader = acceleratorTalonFX.getSelectedSensorVelocity();


    if(currentLeader >= kCurrentThresh){
      result = result + "!!!!!AcceleratorFalcon Voltage Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/Accelerator/Motor", false);
    }



    if(velocityLeader >= kVelocityThresh){
      result = result + "!!!!!AcceleratorFalcon Velocity Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/Accelerator/Motor", false);
    }
    stop();
    printWarning(result);
  }
  public void checkMotors(){
    String result = " ";
    double kCurrentThresh = 3;
    double kVelocityThresh = 1000;
    stop();
    follower2TalonFX.follow(follower2TalonFX);
    double testSpeed = 0.2;
    double testTime = 0.5;

    //test leader
    leaderTalonFX.set(ControlMode.PercentOutput, testSpeed);
    Timer.delay(testTime);
    final double currentLeader = leaderTalonFX.getMotorOutputVoltage();
    final double velocityLeader = leaderTalonFX.getSelectedSensorVelocity();
    
    //test leader
    follower2TalonFX.set(ControlMode.PercentOutput, testSpeed);
    Timer.delay(testTime);
    final double currentFollower = follower2TalonFX.getMotorOutputVoltage();
    final double velocityFollower = follower2TalonFX.getSelectedSensorVelocity();

    if(currentLeader >= kCurrentThresh){
      result = result + "!!!!!LeftShooterFalcon Voltage Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/Shooter/MotorL", false);
    }

    if(currentFollower >= kCurrentThresh){
      result = result + "!!!!!RightShooterFalcon Voltage Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/Shooter/MotorR", false);
    }

    if(velocityLeader >= kVelocityThresh){
      result = result + "!!!!!LeftShooterFalcon Velocity Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/Shooter/MotorL", false);
    }

    if(velocityFollower >= kVelocityThresh){
      result = result + "!!!!!RightShooterFalcon Velocity Low!!!!!";
      SmartDashboard.putBoolean("Diagnostics/Shooter/MotorR", false);
    }

    stop();
    follower2TalonFX.follow(leaderTalonFX);
    printWarning(result);
  }

  //Set up helixlogger sources here
  private void setupLogs() {
    HelixLogger.getInstance().addSource("SHOOTER Vel", leaderTalonFX::getSelectedSensorVelocity);
    HelixLogger.getInstance().addSource("SHOOTER RPM", this::getWheelRPM);
    HelixLogger.getInstance().addSource("SHOOTER Output%", leaderTalonFX::getMotorOutputPercent);
    HelixLogger.getInstance().addSource("LEADER Current", leaderTalonFX::getSupplyCurrent);
    HelixLogger.getInstance().addSource("FOLLOWER Current", follower2TalonFX::getSupplyCurrent);

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
    SmartDashboard.putNumber("Shooter/RightCurrent", follower2TalonFX.getSupplyCurrent());

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