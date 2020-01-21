package frc.lib.drivers;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
 
//Based on Code form Bob319 2017
public class LeaderTalonSRX extends SpectrumTalonSRX{
	// a list of talons to follow the leader
	private final List<BaseMotorController> followerList;
	
	// the constructor
	/**
	 * @param leaderDeviceNumber
	 * @param followerDeviceNumbers
	 */
	public LeaderTalonSRX(int leaderDeviceNumber, BaseMotorController... followers) {
		// the superconstructor
		super(leaderDeviceNumber);
		
		// create an empty list of followers
		followerList = new ArrayList<BaseMotorController>(); 
		this.configFactoryDefault();
		// for each controller in the array,
		// tell it to follow the leader, and add it to the list
		// of followers
		for (BaseMotorController follower : followers) {
			follower.follow(this);
			followerList.add(follower);
		}
	}	
	
	/* (non-Javadoc)
	 * @see com.ctre.phoenix.motorcontrol.can.BaseMotorController#setNeutralMode(com.ctre.phoenix.motorcontrol.NeutralMode)
	 */
	@Override
	public void setNeutralMode(NeutralMode neutralMode) {
		super.setNeutralMode(neutralMode);
		for (BaseMotorController follower : followerList) {
			follower.setNeutralMode(neutralMode);
		}
	}
	
	@Override
	public void setInverted(boolean invert) {
		super.setInverted(invert);
		for (BaseMotorController follower : followerList) {
			follower.setInverted(invert);
		}
	}
	
	public ArrayList<Double> getStatorCurrents() {
		ArrayList<Double> outputCurrents = new ArrayList<Double>();
		outputCurrents.add(super.getStatorCurrent());
		for (BaseMotorController follower : followerList) {
			outputCurrents.add(((SpectrumTalonSRX)follower).getStatorCurrent());
		}		
		return outputCurrents;
	}

}
