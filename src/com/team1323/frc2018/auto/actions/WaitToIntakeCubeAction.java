package com.team1323.frc2018.auto.actions;

import com.team1323.frc2018.subsystems.Intake;

import edu.wpi.first.wpilibj.Timer;

public class WaitToIntakeCubeAction implements Action{
	Intake intake;
	double startTime;
	double timeout;
	
	public WaitToIntakeCubeAction(double timeout){
		intake = Intake.getInstance();
		this.timeout = timeout;
	}

	@Override
	public boolean isFinished() {
		return intake.getState() == Intake.State.CLAMPING || intake.hasCube()
				|| ((Timer.getFPGATimestamp() - startTime) > timeout);
	}

	@Override
	public void start() {
		startTime = Timer.getFPGATimestamp();
		intake.intake();
	}

	@Override
	public void update() {
		
	}

	@Override
	public void done() {
		
	}

}
