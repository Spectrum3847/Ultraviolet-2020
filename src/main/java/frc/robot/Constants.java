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
        public static final int kRightFrontMotor = 15;
        public static final int kRightRearMotor = 16;
        public static final int kLeftFrontMotor = 25;
        public static final int kLeftRearMotor = 26;
    }

    public static final class RobotConstants {
        public static final int minBatteryVoltage = 12;
    }
}
