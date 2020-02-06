/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.drivers.SpectrumSolenoid;

public class Tower extends SubsystemBase {

  public static final class Constants{
    public static final int kTowerMotor = 43;
    public static final int kTowerGate = 5;
  }

  public final CANSparkMax motor;
  public final SpectrumSolenoid gate;

  /**
   * Creates a new Intake.
   */
  public Tower() {
    motor = new CANSparkMax(Constants.kTowerMotor, MotorType.kBrushed);
    motor.restoreFactoryDefaults();
    motor.setSmartCurrentLimit(30);
    motor.setInverted(false);
    motor.burnFlash();

    gate = new SpectrumSolenoid(Constants.kTowerGate);

    //Establish Default Command for This Subsystem
    this.setDefaultCommand(new RunCommand(() -> stop(), this));
  }

  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double speed){
    motor.set(speed);
  }

  public void feed(){
    open();
    setSpeed(1.0);
  }

  public void fullDown(){
    setSpeed(-1.0);
  }

  public void indexUp(){
    close();
    setSpeed(0.5);
  }

  public void indexDown(){
    setSpeed(-0.5);
  }

  public void stop(){
    motor.stopMotor();
  }

  public void close(){
    gate.set(true);
  }

  public void open(){
    gate.set(false);
  }
}
