package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem{
	private static Intake instance = null;
	public static Intake getInstance(){
		if(instance == null)
			instance = new Intake();
		return instance;
	}
	
	boolean hasCube = false;
	
	TalonSRX leftIntake, rightIntake, slider;
	Solenoid pinchers, clampers;
	DigitalInput leftBanner, rightBanner;
	
	public Intake(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		slider = new TalonSRX(Ports.INTAKE_SLIDER);
		pinchers = new Solenoid(Ports.INTAKE_PINCHERS);
		clampers = new Solenoid(Ports.INTAKE_CLAMPERS);
		leftBanner = new DigitalInput(Ports.INTAKE_LEFT_BANNER);
		rightBanner = new DigitalInput(Ports.INTAKE_RIGHT_BANNER);
	}
	
	public enum State{
		OFF, INTAKING, CLAMPING, EJECTING
	}
	private State currentState = State.OFF;
	public State getState(){
		return currentState;
	}
	public void setState(State newState){
		currentState = newState;
	}
	
	public void firePinchers(boolean fire){
		pinchers.set(fire);
	}
	
	public void fireClampers(boolean fire){
		clampers.set(fire);
	}
	
	public void forward(){
		leftIntake.set(ControlMode.PercentOutput, 1.0);
		rightIntake.set(ControlMode.PercentOutput, 1.0);
		slider.set(ControlMode.PercentOutput, 1.0);
	}
	
	public void reverse(){
		leftIntake.set(ControlMode.PercentOutput, -1.0);
		rightIntake.set(ControlMode.PercentOutput, -1.0);
		slider.set(ControlMode.PercentOutput, 0.0);
	}
	
	public void stopRollers(){
		leftIntake.set(ControlMode.PercentOutput, 0.0);
		rightIntake.set(ControlMode.PercentOutput, 0.0);
		slider.set(ControlMode.PercentOutput, 0.0);
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			setState(State.OFF);
			stop();
		}

		@Override
		public void onLoop(double timestamp) {
			switch(currentState){
			case OFF:
				stop();
				break;
			case INTAKING:
				forward();
				firePinchers(true);
				fireClampers(false);
				if(leftBanner.get() && rightBanner.get())
					hasCube = true;
					setState(State.CLAMPING);
				break;
			case CLAMPING:
				stopRollers();
				firePinchers(true);
				fireClampers(true);
				break;
			case EJECTING:
				reverse();
				firePinchers(false);
				fireClampers(false);
				hasCube = false;
				break;
			}
		}

		@Override
		public void onStop(double timestamp) {
			setState(State.OFF);
			stop();
		}
		
	};
	
	@Override
	public synchronized void stop(){
		if(getState() != State.OFF)
			setState(State.OFF);
		leftIntake.set(ControlMode.PercentOutput, 0);
		rightIntake.set(ControlMode.PercentOutput, 0);
		slider.set(ControlMode.PercentOutput, 0);
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
		SmartDashboard.putNumber("Left Intake Current", leftIntake.getOutputCurrent());
		SmartDashboard.putNumber("Right Intake Current", rightIntake.getOutputCurrent());
		SmartDashboard.putNumber("Intake Slider Current", slider.getOutputCurrent());
	}
}
