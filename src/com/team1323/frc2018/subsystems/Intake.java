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
	
	TalonSRX leftIntake, rightIntake;
	Solenoid pinchers, clampers;
	DigitalInput leftBanner, rightBanner;
	
	public TalonSRX getPigeonTalon(){
		return leftIntake;
	}
	
	private Intake(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		pinchers = new Solenoid(20, Ports.INTAKE_PINCHERS);
		clampers = new Solenoid(20, 2);
		leftBanner = new DigitalInput(Ports.INTAKE_LEFT_BANNER);
		rightBanner = new DigitalInput(Ports.INTAKE_RIGHT_BANNER);
		
		leftIntake.setInverted(false);
		rightIntake.setInverted(true);
		
		leftIntake.configContinuousCurrentLimit(25, 10);
		leftIntake.configPeakCurrentLimit(30, 10);
		leftIntake.configPeakCurrentDuration(100, 10);
		leftIntake.enableCurrentLimit(false);
		rightIntake.configContinuousCurrentLimit(25, 10);
		rightIntake.configPeakCurrentLimit(30, 10);
		rightIntake.configPeakCurrentDuration(100, 10);
		rightIntake.enableCurrentLimit(false);
	}
	
	public enum State{
		OFF, INTAKING, CLAMPING, EJECTING, SPINNING
	}
	private State currentState = State.OFF;
	public State getState(){
		return currentState;
	}
	private synchronized void setState(State newState){
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
	
	private void forwardRollers(){
		leftIntake.set(ControlMode.PercentOutput, 0.8);
		rightIntake.set(ControlMode.PercentOutput, 0.8);
	}
	
	private void reverseRollers(){
		leftIntake.set(ControlMode.PercentOutput, -1.0);
		rightIntake.set(ControlMode.PercentOutput, -1.0);
	}
	
	private void spinRollers(){
		leftIntake.set(ControlMode.PercentOutput, 0.8);
		rightIntake.set(ControlMode.PercentOutput, -0.33);
	}
	
	private void holdRollers(){
		leftIntake.set(ControlMode.PercentOutput, 2.0/12.0);
		rightIntake.set(ControlMode.PercentOutput, 2.0/12.0);
	}
	
	private void stopRollers(){
		leftIntake.set(ControlMode.PercentOutput, 0.0);
		rightIntake.set(ControlMode.PercentOutput, 0.0);
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
				
				break;
			case INTAKING:
				if(leftBanner.get() && rightBanner.get()){
					hasCube = true;
					//clamp();
				}
				break;
			case SPINNING:
				if(timestamp - stateEnteredTimestamp > 0.25)
					intake();
				break;
			case CLAMPING:
				
				break;
			case EJECTING:
				if(timestamp - stateEnteredTimestamp > 1.0)
					stop();
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
		forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void spin(){
		setState(State.SPINNING);
		spinRollers();
	}
	
	public void clamp(){
		setState(State.CLAMPING);
		//topRollers();
		holdRollers();
		firePinchers(true);
		fireClampers(true);
	}
	
	public void eject(){
		setState(State.EJECTING);
		reverseRollers();
		firePinchers(false);
		fireClampers(false);
		hasCube = false;
	}
	
	@Override
	public synchronized void stop(){
		if(getState() != State.OFF)
			setState(State.OFF);
		leftIntake.set(ControlMode.PercentOutput, 0);
		rightIntake.set(ControlMode.PercentOutput, 0);
		firePinchers(true);
		fireClampers(false);
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
		SmartDashboard.putNumber("Left Intake Voltage", leftIntake.getMotorOutputVoltage());
		SmartDashboard.putNumber("Right Intake Voltage", rightIntake.getMotorOutputVoltage());
		SmartDashboard.putString("Intake State", currentState.toString());
	}
	
	public boolean checkSystem(){
		double currentMinimum = 3.0;
		double currentMaximum = 20.0;
		
		boolean passed = true;
		
		leftIntake.set(ControlMode.PercentOutput, 1.0);
		Timer.delay(2.0);
		double current = leftIntake.getOutputCurrent();
		leftIntake.set(ControlMode.PercentOutput, 0.0);
		if(current < currentMinimum){
			System.out.println("Left intake motor current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Left intake current too high: " + current);
			passed = false;
		}
		
		rightIntake.set(ControlMode.PercentOutput, 1.0);
		Timer.delay(2.0);
		current = rightIntake.getOutputCurrent();
		rightIntake.set(ControlMode.PercentOutput, 0.0);
		if(current < currentMinimum){
			System.out.println("Right intake current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Right intake current too high: " + current);
			passed = false;
		}
		
		return passed;
	}
}
