package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.subsystems.Superstructure;

public class EjectCubeAction extends RunOnceAction{

	@Override
	public void runOnce() {
		Superstructure.getInstance().intake.eject();
	}

}
