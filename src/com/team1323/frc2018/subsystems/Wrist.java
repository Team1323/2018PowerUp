package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Wrist extends Subsystem{
	private static Wrist instance = null;
	public static Wrist getInstance(){
		if(instance == null)
			instance = new Wrist();
		return instance;
	}

	TalonSRX wrist;
	
	private Wrist(){
		wrist = new TalonSRX(Ports.WRIST);
		wrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		resetToAbsolutePosition();
		wrist.selectProfileSlot(0, 0);
		wrist.config_kP(0, 0.0, 10);
		wrist.config_kI(0, 0.0, 10);
		wrist.config_kD(0, 0.0, 10);
		wrist.config_kF(0, 0.0, 10);
	}
	
	public void setOpenLoop(double output){
		wrist.set(ControlMode.PercentOutput, output);
	}
	
	public void setAngle(double angle){
		if(isSensorConnected())
			wrist.set(ControlMode.MotionMagic, degreesToEncUnits(angle));
		else{
			DriverStation.reportError("Wrist encoder not detected!", false);
			stop();
		}
	}
	
	public double encUnitsToDegrees(int encUnits){
		//TODO
		return encUnits;
	}
	
	public int degreesToEncUnits(double degrees){
		//TODO
		return (int) (degrees);
	}
	
	public boolean isSensorConnected(){
		int pulseWidthPeriod = wrist.getSensorCollection().getPulseWidthRiseToRiseUs();
		return pulseWidthPeriod != 0;
	}
	
	public void resetToAbsolutePosition(){
		wrist.setSelectedSensorPosition(wrist.getSensorCollection().getPulseWidthPosition(), 0, 10);
	}

	@Override
	public void stop() {
		wrist.set(ControlMode.PercentOutput, 0.0);
	}

	@Override
	public void zeroSensors() {
		resetToAbsolutePosition();
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Wrist Current", wrist.getOutputCurrent());
	}
}
