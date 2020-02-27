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
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class DJ extends SubsystemBase {
  /**
   * Creates a new DJ.
   */
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 m_ColorSensor = new ColorSensorV3(i2cPort);
  private final Color kBlueTarget = ColorMatch.makeColor(0.148, 0.436, 0.420);
  private final Color kGreenTarget = ColorMatch.makeColor(0.202, 0.547, 0.250);
  private final Color kRedTarget = ColorMatch.makeColor(0.450, 0.385, 0.162);
  private final Color kYellowTarget = ColorMatch.makeColor(0.323, 0.540, 0.138);
  private final ColorMatch m_colorMatcher = new ColorMatch();

  private String colorString = "Unknown";
  private ColorMatchResult match;

  private Color detectedColor = m_ColorSensor.getColor();
  private double IR = m_ColorSensor.getIR();
  private int proximity = m_ColorSensor.getProximity();

  private static final int deviceID = Constants.DJConstants.kMotor;
  private CANSparkMax m_motor;
  private CANPIDController m_pidController;
  private CANEncoder m_encoder;
  public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM, maxVel, maxAcc, minVel, allowedErr, cwRPM,cwMaxVel;


  
  public DJ() {
    m_colorMatcher.addColorMatch(kBlueTarget);
    m_colorMatcher.addColorMatch(kGreenTarget);
    m_colorMatcher.addColorMatch(kRedTarget);
    m_colorMatcher.addColorMatch(kYellowTarget);
    match = m_colorMatcher.matchClosestColor(detectedColor);
    detectedColor = m_ColorSensor.getColor();
    IR = m_ColorSensor.getIR();
    proximity = m_ColorSensor.getProximity();

    // initialize motor
    m_motor = new CANSparkMax(deviceID, MotorType.kBrushless);

    /**
     * The RestoreFactoryDefaults method can be used to reset the configuration parameters
     * in the SPARK MAX to their factory default state. If no argument is passed, these
     * parameters will not persist between power cycles
     */
    m_motor.restoreFactoryDefaults();

    /**
     * In order to use PID functionality for a controller, a CANPIDController object
     * is constructed by calling the getPIDController() method on an existing
     * CANSparkMax object
     */
    m_pidController = m_motor.getPIDController();

    // Encoder object created to display position values
    m_encoder = m_motor.getEncoder();

    // PID coefficients
    kP = 0.00001; 
    kI = 0;
    kD = 0.001; 
    kIz = 0; 
    kFF = 0.000083; 
    kMaxOutput = 1; 
    kMinOutput = -1;
    cwRPM = 60;
    maxRPM = cwRPM * 64;

    //Smart Motion coefficients
    cwMaxVel = 60; //rpm
    maxVel = cwMaxVel * 64;
    maxAcc = 1500;

    // set PID coefficients
    m_pidController.setP(kP);
    m_pidController.setI(kI);
    m_pidController.setD(kD);
    m_pidController.setIZone(kIz);
    m_pidController.setFF(kFF);
    m_pidController.setOutputRange(kMinOutput, kMaxOutput);

    //Smart Motion
    m_pidController.setSmartMotionMaxVelocity(maxVel, 0);
    m_pidController.setSmartMotionMinOutputVelocity(minVel, 0);
    m_pidController.setSmartMotionMaxAccel(maxAcc, 0);
    m_pidController.setSmartMotionAllowedClosedLoopError(allowedErr, 0);

    SmartDash();
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
  public void findColor(){
    match = m_colorMatcher.matchClosestColor(detectedColor);
    detectedColor = m_ColorSensor.getColor();
    IR = m_ColorSensor.getIR();
    proximity = m_ColorSensor.getProximity();

    if (match.color == kBlueTarget) {
      colorString = "Blue";
    } else if (match.color == kRedTarget) {
      colorString = "Red";
    } else if (match.color == kGreenTarget) {
      colorString = "Green";
    } else if (match.color == kYellowTarget) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }
    CSmartDash();
  }
  public void SmartDash(){
    // display PID coefficients on SmartDashboard
    SmartDashboard.putNumber("DJ P Gain", kP);
    SmartDashboard.putNumber("DJ I Gain", kI);
    SmartDashboard.putNumber("DJ D Gain", kD);
    SmartDashboard.putNumber("DJ I Zone", kIz);
    SmartDashboard.putNumber("DJ Feed Forward", kFF);
    SmartDashboard.putNumber("DJ Max Output", kMaxOutput);
    SmartDashboard.putNumber("DJ Min Output", kMinOutput); 
    
    SmartDashboard.putNumber("DJ Max Vel", maxVel);
    SmartDashboard.putNumber("DJ Min Vel", minVel);
    SmartDashboard.putNumber("DJ Max acc", maxAcc);
    SmartDashboard.putNumber("DJ Allowed Closed Loop Error", allowedErr);
    SmartDashboard.putNumber("DJ Set Position", 0);
    SmartDashboard.putNumber("DJ Set Velocity", 0);

  }
  public void CSmartDash(){
    SmartDashboard.putNumber("Red", detectedColor.red);
    SmartDashboard.putNumber("Green", detectedColor.green);
    SmartDashboard.putNumber("Blue", detectedColor.blue);
    SmartDashboard.putNumber("Confidence", match.confidence);
    SmartDashboard.putString("Detected Color", colorString);
    SmartDashboard.putBoolean("Confidencebool", (match.confidence > 98));
    SmartDashboard.putNumber("IR", IR);
    SmartDashboard.putNumber("Proximity", proximity);
  }
  public void spin(){
    // read PID coefficients from SmartDashboard
    double p = SmartDashboard.getNumber("P Gain", 0);
    double i = SmartDashboard.getNumber("I Gain", 0);
    double d = SmartDashboard.getNumber("D Gain", 0);
    double iz = SmartDashboard.getNumber("I Zone", 0);
    double ff = SmartDashboard.getNumber("Feed Forward", 0);
    double max = SmartDashboard.getNumber("Max Output", 0);
    double min = SmartDashboard.getNumber("Min Output", 0);
    double maxV = SmartDashboard.getNumber("Max Velocity", 0);
    double minV = SmartDashboard.getNumber("Min Velocity", 0);
    double maxA = SmartDashboard.getNumber("Max Acceleration", 0);
    double allE = SmartDashboard.getNumber("Allowed Closed Loop Error", 0);

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
    if((maxV != maxVel)) { m_pidController.setSmartMotionMaxVelocity(maxV,0); maxVel = maxV; }
    if((minV != minVel)) { m_pidController.setSmartMotionMinOutputVelocity(minV,0); minVel = minV; }
    if((maxA != maxAcc)) { m_pidController.setSmartMotionMaxAccel(maxA,0); maxAcc = maxA; }
    if((allE != allowedErr)) { m_pidController.setSmartMotionAllowedClosedLoopError(allE,0); allowedErr = allE; }

    double setPoint, processVariable;
    boolean mode = SmartDashboard.getBoolean("Mode", false);
    if(mode) {
      setPoint = SmartDashboard.getNumber("Set Velocity", 0);
      m_pidController.setReference(setPoint, ControlType.kVelocity);
      processVariable = m_encoder.getVelocity();
    } else {
      setPoint = SmartDashboard.getNumber("Set Position", 0);
      /**
       * As with other PID modes, Smart Motion is set by calling the
       * setReference method on an existing pid object and setting
       * the control type to kSmartMotion
       */
      m_pidController.setReference(setPoint, ControlType.kSmartMotion);
      processVariable = m_encoder.getPosition();
    }
    
    SmartDashboard.putNumber("SetPoint", setPoint);
    SmartDashboard.putNumber("Process Variable", processVariable);
    SmartDashboard.putNumber("Output", m_motor.getAppliedOutput());
  
    SmartDash();
  }
  public void end(){
    m_motor.set(0);
  }
}
  

