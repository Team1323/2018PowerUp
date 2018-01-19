package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem{
	private static Elevator instance = null;
	public static Elevator getInstance(){
		if(instance == null)
			instance = new Elevator();
		return instance;
	}
	
	TalonSRX master, motor2, motor3, motor4;
	
	public Elevator(){
		master = new TalonSRX(Ports.ELEVATOR_1);
		motor2 = new TalonSRX(Ports.ELEVATOR_2);
		motor3 = new TalonSRX(Ports.ELEVATOR_3);
		motor4 = new TalonSRX(Ports.ELEVATOR_4);
		
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		master.selectProfileSlot(0, 0);
		master.config_kP(0, 0.0, 10);
		master.config_kI(0, 0.0, 10);
		master.config_kD(0, 0.0, 10);
		master.config_kF(0, 0.0, 10);
		
		master.set(ControlMode.PercentOutput, 0.0);
		motor2.set(ControlMode.Follower, Ports.ELEVATOR_1);
		motor3.set(ControlMode.Follower, Ports.ELEVATOR_1);
		motor4.set(ControlMode.Follower, Ports.ELEVATOR_1);
	}

	@Override
	public void stop() {
		master.set(ControlMode.PercentOutput, 0.0);
	}

	@Override
	public void zeroSensors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Elevator 1 Current", master.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 2 Current", motor2.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 3 Current", motor3.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 4 Current", motor4.getOutputCurrent());
	}
}
