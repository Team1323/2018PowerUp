package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.subsystems.Intake;

public class WaitToIntakeCubeAction implements Action{
	Intake intake;
	
	public WaitToIntakeCubeAction(){
		intake = Intake.getInstance();
	}

	@Override
	public boolean isFinished() {
		return intake.hasCube();
	}

	@Override
	public void start() {
		intake.intake();
	}

	@Override
	public void update() {
		
	}

	@Override
	public void done() {
		
	}

}
