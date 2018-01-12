package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Wrist {
	private static Wrist instance = null;
	public static Wrist getInstance(){
		if(instance == null)
			instance = new Wrist();
		return instance;
	}

	TalonSRX wrist;
	
	public Wrist(){
		
	}
}
