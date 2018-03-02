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
		if(intake.hasCube()){
			System.out.println("Intake recognizes cube");
			return true;
		}else if(intake.getState() == Intake.State.GROUND_CLAMPING || intake.getState() == Intake.State.CLAMPING){
			System.out.println("Intake state changed to clamping");
			return true;
		}else if((Timer.getFPGATimestamp() - startTime) > timeout){
			System.out.println("Wait for intake timed out");
			return true;
		}
		return false;
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
		if(intake.getState() == Intake.State.INTAKING)
			intake.clamp();
		else if(intake.getState() == Intake.State.NONCHALANT_INTAKING)
			intake.groundClamp();
	}

}
