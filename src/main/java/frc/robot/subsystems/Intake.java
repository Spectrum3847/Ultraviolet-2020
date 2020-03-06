/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.drivers.SpectrumSolenoid;
import frc.lib.util.Debugger;
import frc.robot.Constants;
import frc.robot.Robot;

public class Intake extends SubsystemBase {

  public final CANSparkMax motor;
  public final SpectrumSolenoid solUp;
  public final SpectrumSolenoid solDown;
  private CANPIDController m_pidController;
  private CANEncoder m_encoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

  /**
   * Creates a new Intake.
   */
  public Intake() {
    motor = new CANSparkMax(Constants.IntakeConstants.kIntakeMotor, MotorType.kBrushless);
    motor.restoreFactoryDefaults();
    motor.setSmartCurrentLimit(30);
    motor.setIdleMode(IdleMode.kCoast);
    motor.setInverted(true);
     /**
     * In order to use PID functionality for a controller, a CANPIDController object
     * is constructed by calling the getPIDController() method on an existing
     * CANSparkMax object
     */
    m_pidController = motor.getPIDController();

    // Encoder object created to display position values
    m_encoder = motor.getEncoder();

    // PID coefficients
    kP = 0.00015; 
    kI = 0;
    kD = 0.001; 
    kIz = 0; 
    kFF = 0.000175; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    maxRPM = 500;

    // set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);

    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("Intake/P Gain", kP);
    SmartDashboard.putNumber("Intake/I Gain", kI);
    SmartDashboard.putNumber("Intake/D Gain", kD);
    SmartDashboard.putNumber("Intake/I Zone", kIz);
    SmartDashboard.putNumber("Intake/Feed Forward", kFF);
    SmartDashboard.putNumber("Intake/Max Output", kMaxOutput);
    SmartDashboard.putNumber("Intake/Min Output", kMinOutput);
    motor.burnFlash();

    solUp = new SpectrumSolenoid(Constants.IntakeConstants.
    kIntakeUp);
    solDown = new SpectrumSolenoid(Constants.IntakeConstants.kIntakeDown);


    //Helixlogger setup
    setupLogs();

    this.setDefaultCommand(new RunCommand(() -> stop(), this));

  }

  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake/Velocity", m_encoder.getVelocity());
  }

  public void setVelocity(double velocity){
        // read PID coefficients from SmartDashboard
        double p = SmartDashboard.getNumber("Intake/P Gain", 0);
        double i = SmartDashboard.getNumber("Intake/I Gain", 0);
        double d = SmartDashboard.getNumber("Intake/D Gain", 0);
        double iz = SmartDashboard.getNumber("Intake/I Zone", 0);
        double ff = SmartDashboard.getNumber("Intake/Feed Forward", 0);
        double max = SmartDashboard.getNumber("Intake/Max Output", 0);
        double min = SmartDashboard.getNumber("Intake/Min Output", 0);
    
        // if PID coefficients on SmartDashboard have changed, write new values to controller
        if((p != kP)) { m_pidController.setP(p); kP = p; }
        if((i != kI)) { m_pidController.setI(i); kI = i; }
        if((d != kD)) { m_pidController.setD(d); kD = d; }
        if((iz != kIz)) { m_pidController.setIZone(iz); kIz = iz; }
        if((ff != kFF)) { m_pidController.setFF(ff); kFF = ff; }
        if((max != kMaxOutput) || (min != kMinOutput)) { 
          m_pidController.setOutputRange(min, max); 
          kMinOutput = min; kMaxOutput = max; 
          
        }
    
        /**
         * PIDController objects are commanded to a set point using the 
         * SetReference() method.
         * 
         * The first parameter is the value of the set point, whose units vary
         * depending on the control type set in the second parameter.
         * .
         *       * The second parameter is the control type can be set to one of four 
         * parameters:
         *  com.revrobotics.ControlType.kDutyCycle
         *  com.revrobotics.ControlType.kPosition
         *  com.revrobotics.ControlType.kVelocity
         *  com.revrobotics.ControlType.kVoltage
         */
        m_pidController.setReference(velocity, ControlType.kVelocity);
        SmartDashboard.putNumber("Intake/Setpoint", velocity);

        

  }

  public void setSpeed(double speed){
    motor.set(speed);
  }

  public void collect(){
    setVelocity(5000);
  }

  public void reverse(){
    setVelocity(-5000);
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

  //Set up helixlogger sources here
  private void setupLogs() {

  }

  public static void printDebug(String msg){
    Debugger.println(msg, Robot._intake, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, Robot._intake, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, Robot._intake, Debugger.warning4);
  }

  public static void checkMotor(){
    
  }

}
