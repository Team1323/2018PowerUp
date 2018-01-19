package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.subsystems.Swerve;

public class WaitToFinishPathAction implements Action{
	Swerve swerve;
	
	public WaitToFinishPathAction(){
		swerve = Swerve.getInstance();
	}
	
	@Override
	public boolean isFinished(){
		return swerve.hasFinishedPath();
	}
	
	@Override
	public void start(){
	}
	
	@Override
	public void update(){
	}
	
	@Override
	public void done(){
	}
}
