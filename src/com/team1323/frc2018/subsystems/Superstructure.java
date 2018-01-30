package com.team1323.frc2018.subsystems;

import edu.wpi.first.wpilibj.Compressor;

public class Superstructure {
	private static Superstructure instance = null;
	public static Superstructure getInstance(){
		if(instance == null)
			instance = new Superstructure();
		return instance;
	}
	
	public Intake intake;
	public Wrist wrist;
	public Elevator elevator;
	
	private Swerve swerve;
	
	private Compressor compressor;
	
	private Superstructure(){
		intake = Intake.getInstance();
		wrist = Wrist.getInstance();
		elevator = Elevator.getInstance();
		swerve = Swerve.getInstance();
		//compressor = new Compressor(0);
	}
	
	public enum SystemState{
		IDLE
	}
}
