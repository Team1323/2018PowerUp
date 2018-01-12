package com.team1323.frc2018.subsystems;

public class Superstructure {
	private static Superstructure instance = null;
	public static Superstructure getInstance(){
		if(instance == null)
			instance = new Superstructure();
		return instance;
	}
	
	private Intake intake;
	private Wrist wrist;
	private Elevator elevator;
	
	public Superstructure(){
		intake = Intake.getInstance();
		wrist = Wrist.getInstance();
		elevator = Elevator.getInstance();
	}
	
	public enum SystemState{
		IDLE
	}
}
