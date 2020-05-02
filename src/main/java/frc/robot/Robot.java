/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
//import edu.wpi.first.wpilibj2.command.RunCommand;
//import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
//import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.lib.util.Debugger;
import frc.team2363.logger.HelixLogger;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer robotContainer;

  public static int brownOutCtn = 0;

  // Add Debug flags
  // You can have a flag for each subsystem, etc
  public static final String _controls = "CONTROL";
  public static final String _general = "GENERAL";
  public static final String _auton = "AUTON";
  public static final String _drive = "DRIVE";
  public static final String _funnel = "FUNNEL";
  public static final String _intake = "INTAKE";
  public static final String _shooter = "SHOOTER";
  public static final String _tower = "TOWER";
  public static final String _climber = "CLIMBER";
  public static final String _visionLL = "LIMELIGHT";
  public static final String _dj = "DJ";

  public enum RobotState {
    DISABLED, AUTONOMOUS, TELEOP, TEST
  }

  public static RobotState s_robot_state = RobotState.DISABLED;

  public static RobotState getState() {
    return s_robot_state;
  }

  public static void setState(final RobotState state) {
    s_robot_state = state;
  }

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.
    robotContainer = new RobotContainer();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    //  block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
    printInfo("Start disabledInit()");

    CommandScheduler.getInstance().cancelAll();
    LiveWindow.setEnabled(false);
    LiveWindow.disableAllTelemetry();
    RobotContainer.visionLL.setLimeLightLED(false);
    RobotContainer.drivetrain.coastMode();
    setState(RobotState.DISABLED);
    printInfo("End disabledInit()");
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = robotContainer.getAutonomousCommand();
    initDebugger();//Used to set debug level lower when FMS attached.
    printInfo("Start autonomousInit()");
    CommandScheduler.getInstance().cancelAll();
    LiveWindow.setEnabled(false);
		LiveWindow.disableAllTelemetry();
    setState(RobotState.AUTONOMOUS);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    printInfo("End autonomousInit()");
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    HelixLogger.getInstance().saveLogs();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    
    printInfo("Start teleopInit()");
    RobotContainer.drivetrain.brakeMode();
    CommandScheduler.getInstance().cancelAll();
		LiveWindow.setEnabled(false);
		LiveWindow.disableAllTelemetry();
    setState(RobotState.TELEOP);
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    printInfo("End teleopInit()");
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    HelixLogger.getInstance().saveLogs();
    /*if (RobotController.isBrownedOut()){
			brownOutCtn++;
		} */
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }

  private static void initDebugger(){
    if(RobotContainer.DS.isFMSAttached()) {
      Debugger.setLevel(Debugger.warning4);
    } else {
      Debugger.setLevel(Debugger.info3);
    }
    Debugger.flagOn(_general); //Set all the flags on, comment out ones you want off
    Debugger.flagOn(_auton);
    Debugger.flagOn(_drive);
    Debugger.flagOn(_funnel);
    Debugger.flagOn(_intake);
    Debugger.flagOn(_shooter);
    Debugger.flagOn(_tower);
    Debugger.flagOn(_climber);
    Debugger.flagOn(_visionLL);
    Debugger.flagOn(_dj);
  }

  public static void printDebug(String msg) {
    Debugger.println(msg, _general, Debugger.debug2);
  }

  public static void printInfo(String msg) {
    Debugger.println(msg, _general, Debugger.info3);
  }

  public static void printWarning(String msg) {
    Debugger.println(msg, _general, Debugger.warning4);
  }

}
