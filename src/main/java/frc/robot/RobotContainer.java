/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
import frc.lib.controllers.SpectrumXboxController;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Funnel;
import frc.robot.subsystems.Tower;
import frc.team2363.logger.HelixEvents;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.DriveCommands;
import frc.robot.commands.ColorWheel;
import frc.robot.commands.auto.*;
import frc.robot.commands.ballpath.*;
import frc.paths.*;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  // The robot's subsystems and commands are defined here...
  public static final Drivetrain Drivetrain = new Drivetrain();
  public static final Intake intake = new Intake();
  public static final Tower tower = new Tower();
  public static final Funnel funnel = new Funnel();
  public static final Shooter shooter = new Shooter();
  public static DriverStation DS;

  public static SpectrumPreferences prefs = SpectrumPreferences.getInstance();

  public static SpectrumXboxController driverController = new SpectrumXboxController(0, .1, .05);
  //SpectrumXboxController operatorController = new SpectrumXboxController(1, .06, .05);

  public static AHRS navX;

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
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    DS = DriverStation.getInstance();
    initDebugger(); // Init Debugger
    HelixEvents.getInstance().startLogging();
    printInfo("Start robotInit()");
    Dashboard.intializeDashboard();
    try {
      navX = new AHRS(SPI.Port.kMXP);
    } catch (RuntimeException ex) {
      printWarning("Error instantiating navX-MXP");
    }
    if (navX != null) {
      navX.zeroYaw();
    }

    // Configure the button bindings
    configureButtonBindings();

    printInfo("End robotInit()");
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by inst antiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // driverController.aButton.whenPressed(new PathFollower(new DriveStraight6(),
    // Drivetrain));
    driverController.rightBumper.whileHeld(DriveCommands.highGear);
    driverController.leftBumper.whileHeld(new ParallelCommandGroup(
      new SequentialCommandGroup(
        new TowerBack(), 
        new FunnelStore()), 
      new IntakeBalls()));
    
    driverController.Dpad.Left.whileHeld(new IntakeBalls());
    //driverController.Dpad.Right.whileHeld(new FunnelToTower());
    driverController.bButton.whileHeld(new FunnelStore());
    driverController.yButton.whileHeld(BallPathCommands.feedShooter);
    //driverController.xButton.whileHeld(new ColorWheel());
    driverController.Dpad.Right.whileHeld(new IntakeUpRunning());
    driverController.Dpad.Down.whileHeld(new IntakeDown());
    driverController.Dpad.Up.whileHeld(new TowerPneumatic());
    driverController.aButton.whileHeld(new TowerBack());
    //Set Shooter to the DashboardVelocity when right bumper is pressed.
    driverController.startButton.whileHeld(new RunCommand(() -> shooter.dashboardVelocity(), shooter));
    driverController.selectButton.whileHeld(new RunCommand(()-> shooter.dashboardVelocity(1000,750), shooter));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public CommandBase getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    //return new PathFollower(new DriveStraight6(), Drivetrain);
    return null;
  }

  private static void initDebugger(){
    if(DS.isFMSAttached()) {
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
