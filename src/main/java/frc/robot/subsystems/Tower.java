/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.drivers.SpectrumSolenoid;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.team2363.logger.HelixLogger;

public class Tower extends SubsystemBase {

  public final TalonFX motor;
  public final SpectrumSolenoid gate;

  public final DigitalInput TowerTop = new DigitalInput(0);
  public final DigitalInput TowerBot = new DigitalInput(1);

  private double kP, kI, kD, kF;
  private int iZone;

  /**
   * Creates a new Intake.
   */
  public Tower() {
    //Pid

    kP = SpectrumPreferences.getInstance().getNumber("Tower kP",0.05);
    kI = SpectrumPreferences.getInstance().getNumber("Tower kI",0.001);
    kD = SpectrumPreferences.getInstance().getNumber("Tower kD",0.07);
    kF = SpectrumPreferences.getInstance().getNumber("Tower kF",0.0472);
    iZone = (int) SpectrumPreferences.getInstance().getNumber("Tower I-Zone", 150);

    
    motor = new TalonFX(Constants.TowerConstants.kTowerMotor);
    motor.setInverted(true);
    SupplyCurrentLimitConfiguration supplyCurrentLimit = new SupplyCurrentLimitConfiguration(true, 40, 45, 0.5);
    motor.configSupplyCurrentLimit(supplyCurrentLimit);

    motor.config_kP(0, kP);
    motor.config_kI(0, kI);   
    motor.config_kD(0, kD);  
    motor.config_kF(0, kF);  
    motor.config_IntegralZone(0, iZone);

    motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

    gate = new SpectrumSolenoid(Constants.TowerConstants.kTowerGate);

    SpectrumPreferences.getInstance().getNumber("Tower Setpoint", 1000);

    //Helixlogger setup
    setupLogs();

    this.setDefaultCommand(new RunCommand(() -> stop(), this));
  }

  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setPercentModeOutput(double speed){
    motor.set(ControlMode.PercentOutput, speed);
  }

  public void setVelocity( double velocity){
    motor.set(ControlMode.Velocity, velocity);
  }

  public void DashboardVelocity(){
    double wheelRpm = SpectrumPreferences.getInstance().getNumber("Tower Setpoint", 1000);
    double motorVelocity = (wheelRpm * 30 / 8);
    motor.set(ControlMode.Velocity, motorVelocity);
  }

  public int getWheelRPM() {
    return motor.getSelectedSensorVelocity() * 8 / 30;
  }
  public void feed(){
    setPercentModeOutput(1.0);
  }

  public void fullDown(){
    setPercentModeOutput(-1.0);
  }

  public void indexUp(){
    //setVelocity(500);
    setPercentModeOutput(0.4);
  }

  public void indexDown(){
    //setVelocity(500);
    setPercentModeOutput(-0.4);
  }

  public void stop(){
    motor.set(ControlMode.PercentOutput,0);
  }

  public void close(){
    gate.set(true);
  }

  public void open(){
    gate.set(false);
  }

  public Boolean getTop(){
    return !TowerTop.get();
  }
  public Boolean getBot(){
    return !TowerBot.get();
  }

  public void SmartDash() {
    SmartDashboard.putBoolean("TopSensorTower", !TowerTop.get());
    SmartDashboard.putBoolean("BotSensorTower", !TowerBot.get());
    SmartDashboard.putNumber("Tower/Velocity", motor.getSelectedSensorVelocity());
    SmartDashboard.putNumber("Tower/WheelRPM", getWheelRPM());
    SmartDashboard.putNumber("Tower/OutputPercentage", motor.getMotorOutputPercent());
    SmartDashboard.putNumber("Tower/LeftCurrent", motor.getSupplyCurrent());
    }
    
    public static void checkMotor(){
      
    }
    
  //Set up Helixlogger sources here
  private void setupLogs() {
    HelixLogger.getInstance().addSource("TOWER Vel", motor::getSelectedSensorVelocity);
    HelixLogger.getInstance().addSource("TOWER RPM", this::getWheelRPM);
    HelixLogger.getInstance().addSource("TOWER Output%", motor::getMotorOutputPercent);
    HelixLogger.getInstance().addSource("TOWER Current", motor::getSupplyCurrent);
  }

  public static void printDebug(String msg){
    Debugger.println(msg, Robot._tower, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, Robot._tower, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, Robot._tower, Debugger.warning4);
  }

}
