package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem{
	private static Intake instance = null;
	public static Intake getInstance(){
		if(instance == null)
			instance = new Intake();
		return instance;
	}
	
	TalonSRX leftIntake, rightIntake, slider;
	
	public Intake(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		slider = new TalonSRX(Ports.INTAKE_SLIDER);
	}
	
	public void forward(){
		leftIntake.set(ControlMode.PercentOutput, 1.0);
		rightIntake.set(ControlMode.PercentOutput, 1.0);
		slider.set(ControlMode.PercentOutput, 1.0);
	}
	
	public void reverse(){
		leftIntake.set(ControlMode.PercentOutput, -1.0);
		rightIntake.set(ControlMode.PercentOutput, -1.0);
	}
	
	@Override
	public synchronized void stop(){
		leftIntake.set(ControlMode.PercentOutput, 0);
		rightIntake.set(ControlMode.PercentOutput, 0);
		slider.set(ControlMode.PercentOutput, 0);
	}

	@Override
	public void zeroSensors() {
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Left Intake Current", leftIntake.getOutputCurrent());
		SmartDashboard.putNumber("Right Intake Current", rightIntake.getOutputCurrent());
		SmartDashboard.putNumber("Intake Slider Current", slider.getOutputCurrent());
	}
}
