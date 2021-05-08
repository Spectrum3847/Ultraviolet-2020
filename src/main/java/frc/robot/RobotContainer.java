/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.analog.adis16470.frc.ADIS16470_IMU;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import frc.lib.controllers.SpectrumTwoButton;
import frc.lib.controllers.SpectrumXboxController;
import frc.lib.drivers.EForwardableConnections;
import frc.lib.util.Debugger;
import frc.lib.util.SpectrumPreferences;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Funnel;
import frc.robot.subsystems.Tower;
import frc.robot.subsystems.VisionLL;
import frc.team2363.logger.HelixEvents;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.drive.DriveCommands;
import frc.robot.commands.drive.LLAim;
import frc.robot.commands.ballpath.*;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  //Create Joysticks first so they can be used in defaultCommands
  public static SpectrumXboxController driverController = new SpectrumXboxController(0, .1, .05);
  public static SpectrumXboxController operatorController = new SpectrumXboxController(1, .06, .05);

  // The robot's subsystems and commands are defined here...
  public static final Drivetrain drivetrain = new Drivetrain();
  public static final Intake intake = new Intake();
  public static final Tower tower = new Tower();
  public static final Funnel funnel = new Funnel();
  public static final Shooter shooter = new Shooter();
  public static final VisionLL visionLL = new VisionLL(); 
  public static final Climber climber = new Climber();

  public static DriverStation DS;
  public static PowerDistributionPanel pdp = new PowerDistributionPanel();

  public static SpectrumPreferences prefs = SpectrumPreferences.getInstance();


  public static ADIS16470_IMU adis16470 = new ADIS16470_IMU();

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

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    DS = DriverStation.getInstance();
    portForwarding();
    initDebugger(); // Init Debugger
    //HelixEvents.getInstance().startLogging();
    printInfo("Start robotInit()");
    Dashboard.intializeDashboard();

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

    // Driver Controller
    driverController.xButton.whileHeld(DriveCommands.highGear);
    driverController.rightBumper.whileHeld(new LLAim());

    //Operator Controller
    operatorController.leftTriggerButton.whileHeld(new ParallelCommandGroup(
      new SequentialCommandGroup(
        new FunnelToTowerSensors(), 
        new FunnelStore()), 
      new IntakeBalls()));
    operatorController.leftBumper.whileHeld(new IntakeBalls());
    //operatorController.rightTriggerButton.whileHeld(new RunCommand(() -> shooter.dashboardVelocity(), shooter));

    //Intiantion Line
    new SpectrumTwoButton(operatorController.rightTriggerButton, operatorController.aButton).whileHeld(
      new RunCommand(()-> shooter.setShooterVelocity(3500), shooter));
  
    //Trench
    new SpectrumTwoButton(operatorController.rightTriggerButton, operatorController.bButton).whileHeld(
      new RunCommand(()-> shooter.setShooterVelocity(3900), shooter));

    //Behind Trench
    new SpectrumTwoButton(operatorController.rightTriggerButton, operatorController.xButton).whileHeld(
      new RunCommand(()-> shooter.setShooterVelocity(4200), shooter));

    //Dashboard
    new SpectrumTwoButton(operatorController.rightTriggerButton, operatorController.yButton).whileHeld(
      new RunCommand(()-> shooter.dashboardVelocity(), shooter));

    //operatorController.Dpad.Down.whileHeld(new RunCommand(() -> tower.setPercentModeOutput(-.35), tower));
    operatorController.Dpad.Down.whileHeld(new RunCommand(() -> intake.reverse(), intake));
    operatorController.selectButton.whileHeld(new FeedShooter());
  }

  private void portForwarding() {
    EForwardableConnections.addPortForwarding(EForwardableConnections.LIMELIGHT_CAMERA_FEED);
    EForwardableConnections.addPortForwarding(EForwardableConnections.LIMELIGHT_WEB_VIEW);
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
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
    Debugger.flagOn(_climber);
    Debugger.flagOn(_visionLL);
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
