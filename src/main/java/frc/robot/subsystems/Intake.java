/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.drivers.SpectrumSolenoid;
import frc.robot.Constants;

public class Intake extends SubsystemBase {

  public final CANSparkMax motor;
  public final SpectrumSolenoid solUp;
  public final SpectrumSolenoid solDown;
  /**
   * Creates a new Intake.
   */
  public Intake() {
    motor = new CANSparkMax(Constants.IntakeConstants.kIntakeMotor, MotorType.kBrushless);
    motor.restoreFactoryDefaults();
    motor.setSmartCurrentLimit(30);
    motor.setInverted(false);
    motor.burnFlash();

    solUp = new SpectrumSolenoid(Constants.IntakeConstants.kIntakeUp);
    solDown = new SpectrumSolenoid(Constants.IntakeConstants.kIntakeDown);
  }

  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setSpeed(double speed){
    motor.set(speed);
  }

  public void collect(){
    setSpeed(1.0);
  }

  public void reverse(){
    setSpeed(-1.0);
  }

  public void stop(){
    motor.stopMotor();
  }

  public void up(){
    solDown.set(false);
    solUp.set(true);
  }

  public void down(){
    solDown.set(true);
    solUp.set(false);
  }

}
