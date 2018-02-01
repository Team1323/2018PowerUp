package com.team1323.frc2018.subsystems;

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
	
	public enum SystemState{
		IDLE, INTAKING, SWITCH_HEIGHT, SWITCH_SCORING, SCALE_HEIGHT, SCALE_SCORING,
		MANUAL_ELEVATOR, MANUAL_SCORING,
		WAITING_FOR_ELEVATOR, WAITING_FOR_WRIST
	}
	SystemState systemState = SystemState.IDLE;
	public SystemState getSystemState(){
		return systemState;
	}
	private void setSystemState(SystemState newState){
		systemState = newState;
	}
	
	public enum WantedState{
		IDLE, INTAKING, SWITCH_HEIGHT, SCALE_HEIGHT, SCORING,
		MANUAL_ELEVATOR
	}
	WantedState wantedState = WantedState.IDLE;
	public WantedState getWantedState(){
		return wantedState;
	}
	public void setWantedState(WantedState newState){
		wantedState = newState;
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			synchronized(Superstructure.this){
				
			}
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized(Superstructure.this){
				switch(systemState){
				case IDLE:
					handleIdle();
					break;
				case WAITING_FOR_WRIST:
					handleWaitingForWrist();
					break;
				case WAITING_FOR_ELEVATOR:
					handleWaitingForElevator();
					break;
				case INTAKING:
					handleIntaking();
					break;
				case SWITCH_HEIGHT:
					handleSwitchHeight();
					break;
				case SWITCH_SCORING:
					handleSwitchScoring();
					break;
				case SCALE_HEIGHT:
					handleScaleHeight();
					break;
				case SCALE_SCORING:
					handleScaleScoring();
					break;
				case MANUAL_SCORING:
					handleManualScoring();
					break;
				default:
					break;
				}
			}
		}

		@Override
		public void onStop(double timestamp) {
			stop();
		}
		
	};
	
	private void handleIdle(){
		switch(wantedState){
		case INTAKING:
			wrist.setAngle(0);
			elevator.goToIntakingHeight();
			setSystemState(SystemState.WAITING_FOR_WRIST);
			break;
		case SWITCH_HEIGHT:
			wrist.setAngle(0);
			elevator.goToSwitchHeight();
			setSystemState(SystemState.WAITING_FOR_WRIST);
			break;
		case SCALE_HEIGHT:
			wrist.setAngle(0);
			elevator.goToScaleHeight();
			setSystemState(SystemState.WAITING_FOR_WRIST);
			break;
		case SCORING:
			wrist.setAngle(0);
			setSystemState(SystemState.WAITING_FOR_WRIST);
			break;
		case MANUAL_ELEVATOR:
			setSystemState(SystemState.MANUAL_ELEVATOR);
			break;
		default:
			break;
		}
	}
	
	private void handleWaitingForWrist(){
		if(wrist.hasReachedTargetAngle() || !wrist.isSensorConnected()){
			setSystemState(SystemState.WAITING_FOR_ELEVATOR);
		}
	}
	
	private void handleWaitingForElevator(){
		if(elevator.hasReachedTargetHeight() || elevator.getState() == Elevator.ControlState.Locked ||
				!elevator.isSensorConnected()){
			switch(wantedState){
			case INTAKING:
				intake.intake();
				setSystemState(SystemState.INTAKING);
				break;
			case SWITCH_HEIGHT:
				setSystemState(SystemState.SWITCH_HEIGHT);
				break;
			case SCALE_HEIGHT:
				setSystemState(SystemState.SCALE_HEIGHT);
				break;
			case SCORING:
				intake.eject();
				setSystemState(SystemState.MANUAL_SCORING);
				break;
			default:
				setSystemState(SystemState.IDLE);
				break;
			}
		}
	}
	
	private void handleIntaking(){
		switch(wantedState){
		case INTAKING:
			if(intake.getState() == Intake.State.CLAMPING)
				setSystemState(SystemState.IDLE);
			break;
		default:
			setSystemState(SystemState.IDLE);
			break;
		}
	}
	
	private void handleSwitchHeight(){
		switch(wantedState){
		case SWITCH_HEIGHT:
			
			break;
		case SCORING:
			intake.eject();
			setSystemState(SystemState.SWITCH_SCORING);
			break;
		default:
			setSystemState(SystemState.IDLE);
			break;
		}
	}
	
	private void handleSwitchScoring(){
		if(intake.getState() == Intake.State.OFF){
			setSystemState(SystemState.SWITCH_HEIGHT);
		}
	}
	
	private void handleScaleHeight(){
		switch(wantedState){
		case SCALE_HEIGHT:
			
			break;
		case SCORING:
			intake.eject();
			setSystemState(SystemState.SCALE_SCORING);
			break;
		default:
			setSystemState(SystemState.IDLE);
			break;
		}
	}
	
	private void handleScaleScoring(){
		if(intake.getState() == Intake.State.OFF){
			setSystemState(SystemState.SCALE_HEIGHT);
		}
	}
	
	private void handleManualScoring(){
		if(intake.getState() == Intake.State.OFF){
			setSystemState(SystemState.IDLE);
		}
	}
	
	public synchronized void sendManualElevatorInput(double input){
		if(input != 0){
			elevator.setOpenLoop(input);
			setWantedState(WantedState.MANUAL_ELEVATOR);
			setSystemState(SystemState.IDLE);
		}else if(systemState == SystemState.MANUAL_ELEVATOR){
			if(elevator.getVelocityFeetPerSecond() < 1.0){
				elevator.lockHeight();
				setWantedState(WantedState.IDLE);
				setSystemState(SystemState.IDLE);
			}
		}
	}
	
	@Override
	public synchronized void stop() {
		setWantedState(WantedState.IDLE);
		setSystemState(SystemState.IDLE);
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
		SmartDashboard.putString("System State", systemState.toString());
		SmartDashboard.putString("Wanted State", wantedState.toString());
	}
	
}
