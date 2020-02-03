/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.team319.trajectory.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
import frc.lib.controllers.SpectrumXboxController;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumLogger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Funnel;
import frc.robot.subsystems.Tower;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.drive.Drive;
import frc.robot.commands.auto.*;
import frc.robot.commands.ballpath.*;
import frc.paths.*;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  public static Drivetrain Drivetrain = new Drivetrain();
  public static Intake Intake = new Intake();
  public static Tower Tower = new Tower();
  public static Funnel Funnel = new Funnel();
  public static DriverStation DS;

  public static SpectrumLogger logger = SpectrumLogger.getInstance();
  public static SpectrumPreferences prefs = SpectrumPreferences.getInstance();

  SpectrumXboxController driverController = new SpectrumXboxController(0, .17, .05);
  SpectrumXboxController operatorController = new SpectrumXboxController(1, .17, .05);
  
  public static AHRS navX;

  public static Path drive6 = new DriveStraight6();

  // Add Debug flags
  // You can have a flag for each subsystem, etc
  public static final String _controls = "CONTROL";
  public static final String _general = "GENERAL";
  public static final String _auton = "AUTON";
  public static final String _commands = "COMMAND";
  public static final String _drive = "DRIVE";

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings
    DS = DriverStation.getInstance();
    initDebugger(); //Init Debugger
		printInfo("Start robotInit()");
    Dashboard.intializeDashboard();
    try {
      navX = new AHRS(SPI.Port.kMXP);
    } catch(RuntimeException ex) {
      printInfo("Error instantiating navX-MXP");
    }
    if(navX != null) {
      navX.zeroYaw();
    }
    
    Drivetrain.setDefaultCommand(
      new Drive(Drivetrain, driverController)
    );

    Intake.setDefaultCommand(
      new InstantCommand(Intake::stop,Intake)
    );

    Funnel.setDefaultCommand(
      new InstantCommand(Funnel::stop,Funnel)
    );

    Tower.setDefaultCommand(
      new InstantCommand(Tower::stop,Tower)
    );

    configureButtonBindings();
    
    printInfo("End robotInit()");
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * inst antiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    //driverController.aButton.whenPressed(new PathFollower(new DriveStraight6(), Drivetrain));
    driverController.leftTriggerButton.whileHeld(new IntakeBalls());
    driverController.aButton.whileHeld(new FunnelToTower());
    driverController.bButton.whileHeld(new FunnelStore());
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public CommandBase getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return new PathFollower(new DriveStraight6(), Drivetrain);
  }

  private static void initDebugger(){
    Debugger.setLevel(Debugger.debug2); //Set the initial Debugger Level
    Debugger.flagOn(_general); //Set all the flags on, comment out ones you want off
    Debugger.flagOn(_controls);
    Debugger.flagOn(_auton);
    Debugger.flagOn(_commands);
    Debugger.flagOn(_drive);
  }

  public static void printDebug(String msg){
    Debugger.println(msg, _general, Debugger.debug2);
  }
  
  public static void printInfo(String msg){
    Debugger.println(msg, _general, Debugger.info3);
  }
  
  public static void printWarning(String msg) {
    Debugger.println(msg, _general, Debugger.warning4);
  }
}
