package com.team1323.frc2018.subsystems;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.lib.util.InterpolatingDouble;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
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
		//compressor = new Compressor();
	}
	
	public enum State{
		IDLE, ASSUMING_CONFIG, CONFIGURED, ELEVATOR_MANUAL, WRIST_MANUAL, WAITING_FOR_WRIST, WAITING_FOR_ELEVATOR, INTAKING, STOWING,
		RAISING
	}
	private State currentState = State.IDLE;
	public State getState(){
		return currentState;
	}
	private void setState(State newState){
		currentState = newState;
	}
	
	public enum WantedState{
		IDLE, INTAKING, STOWED, RAISED, READY_FOR_HANG, HUNG
	}
	private WantedState wantedState = WantedState.IDLE;
	public WantedState getWantedState(){
		return wantedState;
	}
	private void setWantedState(WantedState newState){
		wantedState = newState;
	}
	
	double desiredElevatorHeight = 0.0;
	double desiredWristAngle = 0.0;
	double desiredStowAngle = 90.0;
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized(Superstructure.this){
				double elevatorHeight = elevator.getHeight();
				
				swerve.setMaxSpeed(Constants.kSwerveSpeedTreeMap.getInterpolated(new InterpolatingDouble(elevatorHeight)).value);
				
				if(elevatorHeight >= Constants.WRIST_MAX_STOW_HEIGHT && wrist.getAngle() > Constants.WRIST_SECONDARY_STOW_ANGLE
						&& wantedState != WantedState.READY_FOR_HANG && wantedState != WantedState.HUNG){
					wrist.setAngle(Constants.WRIST_SECONDARY_STOW_ANGLE);
				}
				
				switch(currentState){
				case IDLE:
					handleIdle();
					break;
				case ASSUMING_CONFIG:
					if(wrist.hasReachedTargetAngle() && elevator.hasReachedTargetHeight())
						setState(State.CONFIGURED);
					break;
				case CONFIGURED:
					handleConfigured();
					break;
				case ELEVATOR_MANUAL:
					
					break;
				case INTAKING:
					if(intake.getState() == Intake.State.CLAMPING){
						requestPrimaryWristStow();
					}
					break;
				case WAITING_FOR_ELEVATOR:
					handleWaitingForElevator();
					break;
				case WAITING_FOR_WRIST:
					handleWaitingForWrist();
					break;
				case STOWING:
					handleStowing();
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
	
	private void handleIdle(){
		switch(wantedState){
		case RAISED:
			wrist.setAngle(desiredStowAngle);
			setState(State.STOWING);
			break;
		case STOWED:
			wrist.setAngle(90.0);
			setState(State.STOWING);
			break;
		default:
			break;
		}
	}
	
	private void handleWaitingForElevator(){
		if(elevator.hasReachedTargetHeight()){
			switch(wantedState){
			case RAISED:
				wrist.setAngle(desiredWristAngle);
				setState(State.WAITING_FOR_WRIST);
				break;
			default:
				setState(State.IDLE);
				break;
			}
		}
	}
	
	private void handleWaitingForWrist(){
		if(wrist.hasReachedTargetAngle()){
			switch(wantedState){
			case STOWED:
				setState(State.CONFIGURED);
				break;
			case RAISED:
				setState(State.CONFIGURED);
				break;
			default:
				setState(State.IDLE);
				break;
			}
		}
	}
	
	private void handleStowing(){
		if(wrist.hasReachedTargetAngle()){
			switch(wantedState){
			case STOWED:
				setState(State.CONFIGURED);
				break;
			case RAISED:
				elevator.setTargetHeight(desiredElevatorHeight);
				setState(State.WAITING_FOR_ELEVATOR);
				break;
			default:
				setState(State.IDLE);
				break;
			}
		}
	}
	
	private void handleConfigured(){
		switch(wantedState){
		case INTAKING:
			requestIntakeOn();
			setState(State.INTAKING);
			break;
		case READY_FOR_HANG:
			elevator.configForHanging();
			setState(State.IDLE);
			break;
		default:
			break;
		}
	}
	
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
		setWantedState(WantedState.INTAKING);
	}
	
	public synchronized void requestHighIntakingConfig(){
		intake.stop();
		requestConfig(0, Constants.ELEVATOR_SECOND_CUBE_HEIGHT);
		setWantedState(WantedState.INTAKING);
	}
	
	public synchronized void requestHumanLoadingConfig(){
		intake.stop();
		requestConfig(0, Constants.ELEVATOR_HUMAN_LOAD_HEIGHT);
		setWantedState(WantedState.INTAKING);
	}
	
	public synchronized void requestSwitchConfig(){
		requestIntakeHold();
		/*desiredElevatorHeight = Constants.ELEVATOR_SWITCH_HEIGHT;
		desiredWristAngle = 20.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);*/
		requestConfig(20.0, Constants.ELEVATOR_SWITCH_HEIGHT);
	}
	
	public synchronized void requestBalancedScaleConfig(){
		requestIntakeHold();
		desiredElevatorHeight = Constants.ELEVATOR_BALANCED_SCALE_HEIGHT;
		desiredWristAngle = 35.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);
	}
	
	public synchronized void requestLowScaleConfig(){
		requestIntakeHold();
		desiredElevatorHeight = Constants.ELEVATOR_LOW_SCALE_HEIGHT;
		desiredWristAngle = 15.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);
	}
	
	public synchronized void requestHighScaleConfig(){
		requestIntakeHold();
		desiredElevatorHeight = Constants.ELEVATOR_HIGH_SCALE_HEIGHT;
		desiredWristAngle = 60.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);
	}
	
	public synchronized void requestHangingConfig(){
		requestIntakeOpen();
		requestConfig(90.0, Constants.ELEVATOR_HANGING_HEIGHT);
		setWantedState(WantedState.READY_FOR_HANG);
	}
	
	public synchronized void requestHungConfig(){
		elevator.configForHanging();
		wrist.setAngle(90.0);
		elevator.setHanigngTargetHeight(Constants.ELEVATOR_INTAKING_HEIGHT);
		setState(State.ASSUMING_CONFIG);
		setWantedState(WantedState.HUNG);
	}
	
	public synchronized void requestPrimaryWristStow(){
		if(elevator.getHeight() <= Constants.WRIST_MAX_STOW_HEIGHT){
			desiredStowAngle = Constants.WRIST_PRIMARY_STOW_ANGLE;
		}else{
			desiredStowAngle = Constants.WRIST_SECONDARY_STOW_ANGLE;
		}
		requestIntakeHold();
		setWantedState(WantedState.STOWED);
		setState(State.IDLE);
	}
	
	public synchronized void requestSecondaryWristStow(){
		desiredStowAngle = Constants.WRIST_SECONDARY_STOW_ANGLE;
		requestIntakeHold();
		setWantedState(WantedState.STOWED);
		setState(State.IDLE);
	}
	
	public synchronized void requestGroundStowedConfig(){
		intake.stop();
		requestConfig(90.0, Constants.ELEVATOR_INTAKING_HEIGHT);
	}
	
	public synchronized void requestWristDeploy(){
		requestConfig(90.0);
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
	
	public synchronized void requestIntakeWeakScore(){
		intake.weakEject();
	}
	
	public synchronized void requestIntakeIdle(){
		intake.requestIdle();
	}
	
	public synchronized void requestIntakeOpen(){
		intake.open();
	}
	
	public synchronized void requestElevatorOpenLoop(double input){
		if(input != 0 && getState() != State.WRIST_MANUAL){
			setWantedState(WantedState.IDLE);
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
			setWantedState(WantedState.IDLE);
			setState(State.WRIST_MANUAL);
			wrist.setOpenLoop(input * 0.5);
		}else if(getState() == State.WRIST_MANUAL){
			wrist.setOpenLoop(0);
			setState(State.IDLE);
			wrist.lockAngle();
		}
	}
	
	public void enableCompressor(boolean enable){
		//compressor.setClosedLoopControl(enable);
	}
	
	@Override
	public synchronized void stop() {
		setWantedState(WantedState.IDLE);
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
