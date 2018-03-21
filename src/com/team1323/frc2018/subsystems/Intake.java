package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
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
	
	private Intake(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		pinchers = new Solenoid(20, Ports.INTAKE_PINCHERS);
		clampers = new Solenoid(20, Ports.INTAKE_CLAMPERS);
		banner = new DigitalInput(Ports.INTAKE_BANNER);
		
		leftIntake.setInverted(false);
		rightIntake.setInverted(true);
		
		leftIntake.configPeakOutputForward(1.0, 10);
		rightIntake.configPeakOutputForward(1.0, 10);
		leftIntake.configPeakOutputReverse(-1.0, 10);
		rightIntake.configPeakOutputReverse(-1.0, 10);
		
		leftIntake.configVoltageCompSaturation(12.0, 10);
		rightIntake.configVoltageCompSaturation(12.0, 10);
		leftIntake.enableVoltageCompensation(true);
		rightIntake.enableVoltageCompensation(true);
		
		leftIntake.configOpenloopRamp(0.0, 10);
		rightIntake.configOpenloopRamp(0.0, 10);
		
		leftIntake.configContinuousCurrentLimit(20, 10);
		leftIntake.configPeakCurrentLimit(30, 10);
		leftIntake.configPeakCurrentDuration(10, 10);
		leftIntake.enableCurrentLimit(false);
		rightIntake.configContinuousCurrentLimit(20, 10);
		rightIntake.configPeakCurrentLimit(30, 10);
		rightIntake.configPeakCurrentDuration(10, 10);
		rightIntake.enableCurrentLimit(false);
		leftIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, 10);
		leftIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20, 10);
		rightIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, 10);
		rightIntake.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 20, 10);
		// 11.3 V 6.6 V 58%
	}
	
	private void setRampRate(double secondsToFull){
		leftIntake.configOpenloopRamp(secondsToFull, 0);
		rightIntake.configOpenloopRamp(secondsToFull, 0);
	}
	
	public enum State{
		OFF, INTAKING, CLAMPING, EJECTING, SPINNING, OPEN, WEAK_EJECT, INTAKING_WIDE, NONCHALANT_INTAKING, 
		GROUND_CLAMPING, STRONG_EJECT, FORCED_INTAKE
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
	
	public double getAverageCurrent(){
		return (leftIntake.getOutputCurrent() + rightIntake.getOutputCurrent()) / 2.0;
	}
	
	public void firePinchers(boolean fire){
		pinchers.set(!fire);
	}
	
	public void fireClampers(boolean fire){
		clampers.set(!fire);
	}
	
	private void forwardRollers(){
		setRampRate(0.5);
		leftIntake.set(ControlMode.PercentOutput, 1.0);
		rightIntake.set(ControlMode.PercentOutput, 1.0);
	}
	
	private void reverseRollers(){
		setRampRate(0.0);
		leftIntake.set(ControlMode.PercentOutput, -0.7);
		rightIntake.set(ControlMode.PercentOutput, -0.7);
	}
	
	private void weakReverseRollers(){
		setRampRate(0.0);
		leftIntake.set(ControlMode.PercentOutput, -0.4);
		rightIntake.set(ControlMode.PercentOutput, -0.4);
	}
	
	private void strongReverseRollers(){
		setRampRate(0.0);
		leftIntake.set(ControlMode.PercentOutput, -1.0);
		rightIntake.set(ControlMode.PercentOutput, -1.0);
	}
	
	private void spinRollers(){
		//leftIntake.set(ControlMode.PercentOutput, 0.8);
		//rightIntake.set(ControlMode.PercentOutput, -0.33);
	}
	
	private void holdRollers(){
		setRampRate(0.0);
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
			hasCube = false;
			needsToNotifyDrivers = false;
			setState(State.OFF);
			stop();
		}

		@Override
		public void onLoop(double timestamp) {
			switch(currentState){
			case OFF:
				
				break;
			case INTAKING:
				/*if(banner.get()){
					hasCube = true;
					needsToNotifyDrivers = true;
					clamp();
				}*/
				if(banner.get()){
					if(bannerSensorBeganTimestamp == Double.POSITIVE_INFINITY){
						bannerSensorBeganTimestamp = timestamp;
					}else{
						if(timestamp - bannerSensorBeganTimestamp > 0.25){
							hasCube = true;
							needsToNotifyDrivers = true;
							clamp();
						}
					}
				}else if(bannerSensorBeganTimestamp != Double.POSITIVE_INFINITY){
					bannerSensorBeganTimestamp = Double.POSITIVE_INFINITY;
				}
				break;
			case NONCHALANT_INTAKING:
				if(banner.get()){
					if(bannerSensorBeganTimestamp == Double.POSITIVE_INFINITY){
						bannerSensorBeganTimestamp = timestamp;
					}else{
						if(timestamp - bannerSensorBeganTimestamp > 0.25){
							hasCube = true;
							needsToNotifyDrivers = true;
							groundClamp();
						}
					}
				}else if(bannerSensorBeganTimestamp != Double.POSITIVE_INFINITY){
					bannerSensorBeganTimestamp = Double.POSITIVE_INFINITY;
				}
				break;
			case INTAKING_WIDE:
				
				break;
			case FORCED_INTAKE:
				
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
				setRampRate(0.0);
				hasCube = false;
				if(timestamp - stateEnteredTimestamp > 2.0){
					stop();
					setRampRate(0.5);
				}
				break;
			case WEAK_EJECT:
				setRampRate(0.0);
				hasCube = false;
				if(timestamp - stateEnteredTimestamp > 2.0){
					stop();
					setRampRate(0.5);
				}
				break;
			case STRONG_EJECT:
				setRampRate(0.0);
				hasCube = false;
				if(timestamp - stateEnteredTimestamp > 2.0){
					stop();
					setRampRate(0.5);
				}
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
		hasCube = false;
		setState(State.INTAKING);
		forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void nonchalantIntake(){
		hasCube = false;
		setState(State.NONCHALANT_INTAKING);
		forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void intakeWide(){
		hasCube = false;
		setState(State.INTAKING_WIDE);
		firePinchers(false);
		fireClampers(false);
		forwardRollers();
	}
	
	public void forceIntake(){
		hasCube = false;
		setState(State.FORCED_INTAKE);
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
	
	public void strongEject(){
		setState(State.STRONG_EJECT);
		strongReverseRollers();
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
		//SmartDashboard.putString("Intake State", currentState.toString());
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
