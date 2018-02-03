package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
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
		wrist.enableVoltageCompensation(true);
		wrist.configNominalOutputForward(0.45/12.0, 10);
		wrist.configNominalOutputForward(-0.45/12.0, 10);
		wrist.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		resetToAbsolutePosition();
		wrist.selectProfileSlot(0, 0);
		wrist.config_kP(0, 3.0, 10);
		wrist.config_kI(0, 0.0, 10);
		wrist.config_kD(0, 120.0, 10);
		wrist.config_kF(0, 1023.0/Constants.WRIST_MAX_SPEED, 10);
		wrist.config_kP(1, 3.0, 10);
		wrist.config_kI(1, 0.0, 10);
		wrist.config_kD(1, 240.0, 10);
		wrist.config_kF(1, 1023.0/Constants.WRIST_MAX_SPEED, 10);
		wrist.configMotionCruiseVelocity((int)(Constants.WRIST_MAX_SPEED*0.9), 10);
		wrist.configMotionAcceleration((int)(Constants.WRIST_MAX_SPEED*3.0), 10);
	}
	
	public void setOpenLoop(double output){
		wrist.set(ControlMode.PercentOutput, output);
	}
	
	public void setAngle(double angle){
		if(isSensorConnected()){
			if(angle > getAngle())
				wrist.selectProfileSlot(1, 0);
			else
				wrist.selectProfileSlot(0, 0);
			wrist.set(ControlMode.MotionMagic, Constants.WRIST_STARTING_ENCODER_POSITION + 
					degreesToEncUnits(angle-Constants.WRIST_STARTING_ANGLE));
		}else{
			DriverStation.reportError("Wrist encoder not detected!", false);
			stop();
		}
	}
	
	public void lockAngle(){
		setAngle(getAngle());
	}
	
	public double getAngle(){
		return Constants.WRIST_STARTING_ANGLE + encUnitsToDegrees(wrist.getSelectedSensorPosition(0) - Constants.WRIST_STARTING_ENCODER_POSITION);
	}
	
	public boolean hasReachedTargetAngle(){
		return encUnitsToDegrees(wrist.getClosedLoopError(0)) <= Constants.WRIST_ANGLE_TOLERANCE;
	}
	
	public double encUnitsToDegrees(int encUnits){
		return encUnits / 4096.0 / Constants.WRIST_ENCODER_TO_OUTPUT_RATIO * 360.0;
	}
	
	public int degreesToEncUnits(double degrees){
		return (int) (degrees / 360.0 * Constants.WRIST_ENCODER_TO_OUTPUT_RATIO * 4096.0);
	}
	
	public boolean isSensorConnected(){
		int pulseWidthPeriod = wrist.getSensorCollection().getPulseWidthRiseToRiseUs();
		return pulseWidthPeriod != 0;
	}
	
	public void resetToAbsolutePosition(){
		wrist.setSelectedSensorPosition(wrist.getSensorCollection().getPulseWidthPosition(), 0, 10);
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			if(wrist.getOutputCurrent() > Constants.WRIST_MAX_CURRENT){
				stop();
			}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	};

	@Override
	public void stop() {
		wrist.set(ControlMode.PercentOutput, 0.0);
	}

	@Override
	public void zeroSensors() {
		
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(loop);
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Wrist Current", wrist.getOutputCurrent());
		SmartDashboard.putNumber("Wrist Voltage", wrist.getMotorOutputVoltage());
		SmartDashboard.putNumber("Wrist Encoder", wrist.getSensorCollection().getPulseWidthPosition());
		SmartDashboard.putNumber("Wrist Angle", getAngle());
		SmartDashboard.putNumber("Wrist Velocity", wrist.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Wrist Error", wrist.getClosedLoopError(0));
		if(wrist.getControlMode() == ControlMode.MotionMagic)
			SmartDashboard.putNumber("Wrist Setpoint", wrist.getClosedLoopTarget(0));
	}
	
	public boolean checkSystem(){
		double currentMinimum = 0.5;
		double currentMaximum = 20.0;
		
		boolean passed = true;
		
		if(!isSensorConnected()){
			System.out.println("Wrist sensor is not connected, connect and retest");
			return false;
		}
		
		double startingEncPosition = wrist.getSelectedSensorPosition(0);
		wrist.set(ControlMode.PercentOutput, 3.0/12.0);
		Timer.delay(1.0);
		double current = wrist.getOutputCurrent();
		wrist.set(ControlMode.PercentOutput, 0.0);
		if(Math.signum(wrist.getSelectedSensorPosition(0) - startingEncPosition) != 1.0){
			System.out.println("Wrist needs to be reversed");
			passed = false;
		}
		if(current < currentMinimum){
			System.out.println("Wrist current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Wrist current too high: " + current);
			passed = false;
		}
		
		return passed;
	}
}
