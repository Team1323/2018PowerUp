package com.team1323.frc2018.subsystems;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Superstructure extends Subsystem{
	private static Superstructure instance = null;
	public static Superstructure getInstance(){
		if(instance == null)
			instance = new Superstructure();
		return instance;
	}
	
	public Intake intake;
	public Wrist wrist;
	public Elevator elevator;
	
	private Swerve swerve;
	
	private Compressor compressor;
	
	private Superstructure(){
		intake = Intake.getInstance();
		wrist = Wrist.getInstance();
		elevator = Elevator.getInstance();
		swerve = Swerve.getInstance();
		//compressor = new Compressor(0);
	}
	
	public enum State{
		IDLE, ASSUMING_CONFIG, CONFIGURED, ELEVATOR_MANUAL, WRIST_MANUAL, WAITING_FOR_WRIST, WAITING_FOR_ELEVATOR, INTAKING
	}
	private State currentState = State.IDLE;
	public State getState(){
		return currentState;
	}
	private void setState(State newState){
		currentState = newState;
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized(Superstructure.this){
				switch(currentState){
				case IDLE:
					
					break;
				case ASSUMING_CONFIG:
					if(wrist.hasReachedTargetAngle() && elevator.hasReachedTargetHeight())
						setState(State.CONFIGURED);
					break;
				case CONFIGURED:
					
					break;
				case ELEVATOR_MANUAL:
					
					break;
				case INTAKING:
					if(intake.getState() == Intake.State.CLAMPING){
						requestConfig(90);
						setState(State.IDLE);
					}
					break;
				default:
					break;
				}
			}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	};
	
	public synchronized void requestConfig(double wristAngle, double elevatorHeight){
		wrist.setAngle(wristAngle);
		elevator.setTargetHeight(elevatorHeight);
		setState(State.ASSUMING_CONFIG);
	}
	
	public synchronized void requestConfig(double wristAngle){
		requestConfig(wristAngle, elevator.getHeight());
	}
	
	public synchronized void requestIntakingConfig(){
		intake.stop();
		requestConfig(0, Constants.ELEVATOR_INTAKING_HEIGHT);
		setState(State.INTAKING);
	}
	
	public synchronized void requestSwitchConfig(){
		requestIntakeHold();
		requestConfig(0, Constants.ELEVATOR_SWITCH_HEIGHT);
	}
	
	public synchronized void requestScaleConfig(){
		requestIntakeHold();
		requestConfig(0, Constants.ELEVATOR_SCALE_HEIGHT);
	}
	
	public synchronized void requestWristRetract(){
		requestIntakeHold();
		requestConfig(90);
	}
	
	public synchronized void requestIntakeOn(){
		if(getState() != State.ASSUMING_CONFIG)
			intake.intake();
	}
	
	public synchronized void requestIntakeHold(){
		intake.clamp();
	}
	
	public synchronized void requestIntakeScore(){
		if(getState() != State.ASSUMING_CONFIG)
			intake.eject();
	}
	
	public synchronized void requestElevatorOpenLoop(double input){
		if(input != 0 && getState() != State.WRIST_MANUAL){
			setState(State.ELEVATOR_MANUAL);
			elevator.setOpenLoop(input * 0.5);
		}else if(getState() == State.ELEVATOR_MANUAL){
			elevator.setOpenLoop(0);
			if(elevator.getVelocityFeetPerSecond() <= 1.0){
				setState(State.IDLE);
				elevator.lockHeight();
			}
		}
	}
	
	public synchronized void requestWristOpenLoop(double input){
		if(input != 0 && getState() != State.ELEVATOR_MANUAL){
			setState(State.WRIST_MANUAL);
			wrist.setOpenLoop(input * 0.5);
		}else if(getState() == State.WRIST_MANUAL){
			wrist.setOpenLoop(0);
			setState(State.IDLE);
			wrist.lockAngle();
		}
	}
	
	@Override
	public synchronized void stop() {
		setState(State.IDLE);
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
		SmartDashboard.putString("Superstructure State", getState().toString());
	}
	
}
