package com.team1323.frc2018.subsystems;

import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.subsystems.IntakeV2.IntakeState;

public class SuperstructureV2 extends Subsystem{
	
	public IntakeV2 intake = IntakeV2.getInstance();
	
	private Request pendingIntakeRequest = intake.intakeStateRequest(IntakeState.OFF);
	
	public void requestIntakeState(IntakeV2.IntakeState desiredState){
		
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void zeroSensors() {
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}

	@Override
	public void outputToSmartDashboard() {
		
	}

}
