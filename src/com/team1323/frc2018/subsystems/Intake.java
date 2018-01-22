package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake extends Subsystem{
	private static Intake instance = null;
	public static Intake getInstance(){
		if(instance == null)
			instance = new Intake();
		return instance;
	}
	
	boolean hasCube = false;
	public boolean hasCube(){
		return hasCube;
	}
	
	TalonSRX leftIntake, rightIntake, slider;
	Solenoid pinchers, clampers;
	DigitalInput leftBanner, rightBanner;
	
	public TalonSRX getPigeonTalon(){
		return slider;
	}
	
	public Intake(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		slider = new TalonSRX(Ports.INTAKE_SLIDER);
		pinchers = new Solenoid(20, Ports.INTAKE_PINCHERS);
		clampers = new Solenoid(20, 2);
		leftBanner = new DigitalInput(Ports.INTAKE_LEFT_BANNER);
		rightBanner = new DigitalInput(Ports.INTAKE_RIGHT_BANNER);
		
		leftIntake.configContinuousCurrentLimit(25, 10);
		leftIntake.configPeakCurrentLimit(30, 10);
		leftIntake.configPeakCurrentDuration(100, 10);
		leftIntake.enableCurrentLimit(true);
		rightIntake.configContinuousCurrentLimit(25, 10);
		rightIntake.configPeakCurrentLimit(30, 10);
		rightIntake.configPeakCurrentDuration(100, 10);
		rightIntake.enableCurrentLimit(true);
	}
	
	public enum State{
		OFF, INTAKING, CLAMPING, EJECTING
	}
	private State currentState = State.OFF;
	public State getState(){
		return currentState;
	}
	public synchronized void setState(State newState){
		currentState = newState;
		stateEnteredTimestamp = Timer.getFPGATimestamp();
	}
	private double stateEnteredTimestamp = 0;
	
	public void firePinchers(boolean fire){
		pinchers.set(!fire);
	}
	
	public void fireClampers(boolean fire){
		clampers.set(!fire);
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
				firePinchers(true);
				fireClampers(false);
				break;
			case INTAKING:
				forward();
				firePinchers(true);
				fireClampers(false);
				if(leftBanner.get()){
					hasCube = true;
					setState(State.CLAMPING);
				}
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
				if(timestamp - stateEnteredTimestamp > 1.0)
					setState(State.OFF);
				break;
			}
		}

		@Override
		public void onStop(double timestamp) {
			setState(State.OFF);
			stop();
		}
		
	};
	
	public void intake(){
		setState(State.INTAKING);
	}
	
	public void eject(){
		setState(State.EJECTING);
	}
	
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
		SmartDashboard.putString("Intake State", currentState.toString());
	}
}
