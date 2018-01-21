package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.subsystems.Swerve;
import com.team254.lib.util.math.RigidTransform2d;

public class ResetPoseAction extends RunOnceAction{
	private RigidTransform2d newPose;
	
	public ResetPoseAction(RigidTransform2d newPose){
		this.newPose = newPose;
	}

	@Override
	public void runOnce() {
		Swerve.getInstance().zeroSensors(newPose);
	}

}
