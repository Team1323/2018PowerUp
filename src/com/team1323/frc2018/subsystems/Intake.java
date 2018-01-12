package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Intake {
	private static Intake instance = null;
	public static Intake getInstance(){
		if(instance == null)
			instance = new Intake();
		return instance;
	}
	
	TalonSRX intake;
	
	public Intake(){
		
	}
}
