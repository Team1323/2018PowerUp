package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Elevator {
	private static Elevator instance = null;
	public static Elevator getInstance(){
		if(instance == null)
			instance = new Elevator();
		return instance;
	}
	
	TalonSRX motor1, motor2, motor3, motor4;
	
	public Elevator(){
		
	}
}
