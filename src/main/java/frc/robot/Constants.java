/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int kDriverController = 0;
    public static int CANconfigTimeOut = 0;

    public static final class DriveConstants {
        public static final int kLeftFrontMotor = 10;
        public static final int kLeftRearMotor = 11;
        public static final int kRightFrontMotor = 21;
        public static final int kRightRearMotor = 22;

        public static final int kShifter = 0;
    }

    public static final class ShooterConstants{
        public static final int kLeftlMotor = 30;
        public static final int kRightMotor = 31;
    } 

    public static final class IntakeConstants{
        public static final int kIntakeMotor = 40;
        
        public static final int kIntakeUp = 1;
        public static final int kIntakeDown = 2;
    }

    public static final class FunnelConstants{
        public static final int kLeftMotor = 41;
        public static final int kRightMotor = 42;
    }

    public static final class TowerConstants{
        public static final int kTowerMotor = 43;

        public static final int kTowerGate = 4;
    }

    public static final class RobotConstants {
        public static final int minBatteryVoltage = 12;
    }
}
