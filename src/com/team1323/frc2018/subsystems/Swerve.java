package com.team1323.frc2018.subsystems;

import java.util.Arrays;
import java.util.List;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;

public class Swerve {
	private static Swerve instance = null;
	public static Swerve getInstance(){
		if(instance == null)
			instance = new Swerve();
		return instance;
	}
	
	SwerveDriveModule frontRight, frontLeft, rearLeft, rearRight;
	List<SwerveDriveModule> modules;
	
	public Swerve(){
		frontRight = new SwerveDriveModule(Ports.FRONT_RIGHT_ROTATION, Ports.FRONT_RIGHT_DRIVE,
				1, Constants.FRONT_RIGHT_TURN_OFFSET);
		frontLeft = new SwerveDriveModule(Ports.FRONT_LEFT_ROTATION, Ports.FRONT_LEFT_DRIVE,
				2, Constants.FRONT_LEFT_TURN_OFFSET);
		rearLeft = new SwerveDriveModule(Ports.REAR_LEFT_ROTATION, Ports.REAR_LEFT_DRIVE,
				3, Constants.REAR_LEFT_TURN_OFFSET);
		rearRight = new SwerveDriveModule(Ports.REAR_RIGHT_ROTATION, Ports.REAR_RIGHT_DRIVE,
				4, Constants.REAR_RIGHT_TURN_OFFSET);
		
		modules = Arrays.asList(frontRight, frontLeft, rearLeft, rearRight);
	}
	
	public enum ControlState{
		NEUTRAL, MANUAL
	}
}
