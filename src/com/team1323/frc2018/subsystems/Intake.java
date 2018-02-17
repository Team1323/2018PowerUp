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
	public synchronized boolean hasCube(){
		return hasCube;
	}
	
	private TalonSRX leftIntake, rightIntake;
	private Solenoid pinchers, clampers;
	private DigitalInput banner;
	
	public TalonSRX getPigeonTalon(){
		return leftIntake;
	}
	
	private Intake(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		pinchers = new Solenoid(20, Ports.INTAKE_PINCHERS);
		clampers = new Solenoid(20, Ports.INTAKE_CLAMPERS);
		banner = new DigitalInput(Ports.INTAKE_LEFT_BANNER);
		
		leftIntake.setInverted(false);
		rightIntake.setInverted(true);
		
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
		OFF, INTAKING, CLAMPING, EJECTING, SPINNING, OPEN, WEAK_EJECT, INTAKING_WIDE, NONCHALANT_INTAKING, GROUND_CLAMPING
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
	
	private double bannerSensorBeganTimestamp = Double.POSITIVE_INFINITY;
	
	private boolean needsToNotifyDrivers = false;
	public boolean needsToNotifyDrivers(){
		if(needsToNotifyDrivers){
			needsToNotifyDrivers = false;
			return true;
		}
		return false;
	}
	
	public void firePinchers(boolean fire){
		pinchers.set(!fire);
	}
	
	public void fireClampers(boolean fire){
		clampers.set(!fire);
	}
	
	private void forwardRollers(){
		leftIntake.set(ControlMode.PercentOutput, 1.0);
		rightIntake.set(ControlMode.PercentOutput, 1.0);
	}
	
	private void reverseRollers(){
		leftIntake.set(ControlMode.PercentOutput, -0.7);
		rightIntake.set(ControlMode.PercentOutput, -0.7);
	}
	
	private void weakReverseRollers(){
		leftIntake.set(ControlMode.PercentOutput, -0.4);
		rightIntake.set(ControlMode.PercentOutput, -0.4);
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
				if(banner.get()){
					hasCube = true;
					needsToNotifyDrivers = true;
					clamp();
				}
				/*if(banner.get()){
					if(bannerSensorBeganTimestamp == Double.POSITIVE_INFINITY){
						bannerSensorBeganTimestamp = timestamp;
					}else{
						if(timestamp - bannerSensorBeganTimestamp > 0.5){
							hasCube = true;
							clamp();
						}
					}
				}else if(bannerSensorBeganTimestamp != Double.POSITIVE_INFINITY){
					bannerSensorBeganTimestamp = Double.POSITIVE_INFINITY;
				}*/
				break;
			case NONCHALANT_INTAKING:
				if(banner.get()){
					hasCube = true;
					clamp();
				}
				break;
			case INTAKING_WIDE:
				
				break;
			case SPINNING:
				if(timestamp - stateEnteredTimestamp > 0.25)
					intake();
				break;
			case CLAMPING:
				
				break;
			case GROUND_CLAMPING:
				
				break;
			case EJECTING:
				hasCube = false;
				if(timestamp - stateEnteredTimestamp > 1.0)
					stop();
				break;
			case WEAK_EJECT:
				hasCube = false;
				if(timestamp - stateEnteredTimestamp > 1.0)
					stop();
				break;
			default:
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
	
	public void nonchalantIntake(){
		setState(State.NONCHALANT_INTAKING);
		forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void intakeWide(){
		setState(State.INTAKING_WIDE);
		firePinchers(false);
		fireClampers(false);
		forwardRollers();
	}
	
	public void spin(){
		setState(State.SPINNING);
		spinRollers();
	}
	
	public void clamp(){
		setState(State.CLAMPING);
		holdRollers();
		firePinchers(true);
		fireClampers(true);
	}
	
	public void groundClamp(){
		setState(State.GROUND_CLAMPING);
		holdRollers();
		firePinchers(true);
		fireClampers(true);
	}
	
	public void eject(){
		setState(State.EJECTING);
		reverseRollers();
		firePinchers(true);
		fireClampers(false);
		hasCube = false;
	}
	
	public void weakEject(){
		setState(State.WEAK_EJECT);
		weakReverseRollers();
		firePinchers(true);
		fireClampers(false);
		hasCube = false;
	}
	
	public void open(){
		setState(State.OPEN);
		stopRollers();
		firePinchers(false);
		fireClampers(false);
		hasCube = false;
	}
	
	public void requestIdle(){
		if(!hasCube)
			stop();
		else
			clamp();
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
		SmartDashboard.putBoolean("Intake Has Cube", hasCube);
		SmartDashboard.putBoolean("Intake Banner", banner.get());
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
