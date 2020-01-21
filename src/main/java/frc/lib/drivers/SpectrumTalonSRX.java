package frc.lib.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.Constants;
import frc.robot.Robot;

//Based on Code form Bob319 2017
public class SpectrumTalonSRX extends WPI_TalonSRX {

	private static final int DEFAULT_TIMEOUT_MS = Constants.CANconfigTimeOut;

	public SpectrumTalonSRX(int deviceNumber) {
		super(deviceNumber);
		this.configFactoryDefault();
	}
	
	public ErrorCode configPIDF(int slotIdx, double P, double I, double D, double F){
		ErrorCode errorCode = ErrorCode.OK;
		
		errorCode = this.config_kP(slotIdx, P);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.config_kI(slotIdx, I);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.config_kD(slotIdx, D);
		if (errorCode != ErrorCode.OK) {
			return errorCode;
		}
		
		errorCode = this.config_kF(slotIdx, F);
		return errorCode;
	}
	
	public ErrorCode configPIDF(int slotIdx, double P, double I, double D, double F, int iZone) {
		ErrorCode eCode = this.configPIDF(slotIdx, P, I, D, F);
		eCode = this.config_IntegralZone(slotIdx, iZone);
		return eCode;
	}
	
	public ErrorCode config_IntegralZone(int slotIdx, int izone) {
		return super.config_IntegralZone(slotIdx, izone, DEFAULT_TIMEOUT_MS);
	}

	public ErrorCode configNominalOutputForward(double percentOut) {
		return super.configNominalOutputForward(percentOut, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configNominalOutputReverse(double percentOut) {
		return super.configNominalOutputReverse(percentOut, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configPeakOutputForward(double percentOut) {
		return super.configPeakOutputForward(percentOut, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configPeakOutputReverse(double percentOut) {
		return super.configPeakOutputReverse(percentOut, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode setGains(SRXGains gains) {
		return this.configPIDF(gains.parameterSlot, gains.P, gains.I, gains.D, gains.F, gains.iZone);
	}
	
	public ErrorCode configSelectedFeedbackSensor(FeedbackDevice feedbackDevice, int pidIdx) {
		return super.configSelectedFeedbackSensor(feedbackDevice, pidIdx, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configContinuousCurrentLimit(int amps) {
		return super.configContinuousCurrentLimit(amps, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configPeakCurrentDuration(int milliseconds) {
		return super.configPeakCurrentDuration(milliseconds, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configPeakCurrentLimit(int amps) {
		return super.configPeakCurrentLimit(amps,  DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configOpenloopRamp(double secondsFromNeutralToFull) {
		return super.configOpenloopRamp(secondsFromNeutralToFull, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configClosedloopRamp(double secondsFromNeutralToFull) {
		return super.configClosedloopRamp(secondsFromNeutralToFull, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode config_kP(int slotIdx, double value) {
		return super.config_kP(slotIdx, value, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode config_kD(int slotIdx, double value) {
		return super.config_kD(slotIdx, value, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode config_kF(int slotIdx, double value) {
		return super.config_kF(slotIdx, value, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode config_kI(int slotIdx, double value) {
		return super.config_kI(slotIdx, value, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode setSelectedSensorPosition(int sensorPos, int pidIdx) {
		return super.setSelectedSensorPosition(sensorPos, pidIdx, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configMotionAcceleration(int sensorUnitsPer100msPerSec) {
		return super.configMotionAcceleration(sensorUnitsPer100msPerSec, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configMotionCruiseVelocity(int sensorUnitsPer100ms) {
		return super.configMotionCruiseVelocity(sensorUnitsPer100ms, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode setStatusFramePeriod(StatusFrameEnhanced status13BasePidf0, int periodMs) {
		return super.setStatusFramePeriod(status13BasePidf0, periodMs, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configMotionProfileTrajectoryPeriod(int baseTrajDurationMs) {
		return super.configMotionProfileTrajectoryPeriod(baseTrajDurationMs, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configForwardSoftLimitEnable(boolean enable) {
		return super.configForwardSoftLimitEnable(enable, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configForwardSoftLimitThreshold(int forwardSensorLimit) {
		return super.configForwardSoftLimitThreshold(forwardSensorLimit, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configReverseSoftLimitEnable(boolean enable) {
		return super.configReverseSoftLimitEnable(enable, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configReverseSoftLimitThreshold(int reverseSensorLimit) {
		return super.configReverseSoftLimitThreshold(reverseSensorLimit, DEFAULT_TIMEOUT_MS);
	}
	
	public double configGetParameter(ParamEnum param, int ordinal) {
		return super.configGetParameter(param, ordinal, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configForwardLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) {
		return super.configForwardLimitSwitchSource(type, normalOpenOrClose, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configReverseLimitSwitchSource(LimitSwitchSource type, LimitSwitchNormal normalOpenOrClose) {
		return super.configReverseLimitSwitchSource(type, normalOpenOrClose, DEFAULT_TIMEOUT_MS);
	}
	
	public ErrorCode configVoltageCompSaturation(double voltage) {
		return super.configVoltageCompSaturation(voltage, DEFAULT_TIMEOUT_MS);
	}
	
	public void setLeaderFramePeriods() {
		this.setControlFramePeriod(ControlFrame.Control_3_General, 10);
		this.setStatusFramePeriod(StatusFrame.Status_1_General, 10, 0);
		this.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, 0);
		//this.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 250, 0);
		//this.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 250, 0);
		this.setStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 250, 0);
		//this.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 250, 0);
		//this.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 250, 0);
		//this.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 250, 0);
		this.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 250, 0);
	}
	
	public void setFollowerFramePeriods() {
		this.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		this.setStatusFramePeriod(StatusFrame.Status_1_General, 10, 0);
		this.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, 0);
		this.setStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 250, 0);
		this.setStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 250, 0);
		this.setStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 250, 0);
		this.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 250, 0);
		this.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 250, 0);
		this.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 250, 0);
		this.setStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 250, 0);
	}
	
	public void printFramePeriods(String name) {
		Robot.printDebug("SRX Frame Period: " + name);
		Robot.printDebug("S1: " + this.getStatusFramePeriod(StatusFrame.Status_1_General, 0));
		Robot.printDebug("S2: " + this.getStatusFramePeriod(StatusFrame.Status_2_Feedback0, 0));
		Robot.printDebug("S3: " + this.getStatusFramePeriod(StatusFrameEnhanced.Status_3_Quadrature, 0));
		Robot.printDebug("S4: " + this.getStatusFramePeriod(StatusFrame.Status_4_AinTempVbat, 0));
		Robot.printDebug("S6: " + this.getStatusFramePeriod(StatusFrame.Status_6_Misc, 0));
		Robot.printDebug("S7: " + this.getStatusFramePeriod(StatusFrame.Status_7_CommStatus, 0));
		Robot.printDebug("S8: " + this.getStatusFramePeriod(StatusFrameEnhanced.Status_8_PulseWidth, 0));
		Robot.printDebug("S9: " + this.getStatusFramePeriod(StatusFrame.Status_9_MotProfBuffer, 0));
		Robot.printDebug("S10: " + this.getStatusFramePeriod(StatusFrame.Status_10_Targets,  0));
		Robot.printDebug("S12: " + this.getStatusFramePeriod(StatusFrame.Status_12_Feedback1, 0));
		Robot.printDebug("S13: " + this.getStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 0));
		Robot.printDebug("S14: " + this.getStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1,  0));
		Robot.printDebug("S15: " + this.getStatusFramePeriod(StatusFrame.Status_15_FirmwareApiStatus, 0));
	}
	
	/* Default SRX Frame Rates
		2: [GENERAL] S1: 10
		2: [GENERAL] S2: 20
		2: [GENERAL] S3: 160
		2: [GENERAL] S4: 160
		2: [GENERAL] S6: 0
		2: [GENERAL] S7: 0
		2: [GENERAL] S8: 160
		2: [GENERAL] S9: 0
		2: [GENERAL] S10: 0
		2: [GENERAL] S12: 250
		2: [GENERAL] S13: 160
		2: [GENERAL] S14: 250
		2: [GENERAL] S15: 160
*/
	
}
