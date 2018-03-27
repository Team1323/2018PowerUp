package com.team1323.frc2018.subsystems;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.lib.util.InterpolatingDouble;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
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
		compressor = new Compressor(20);
	}
	
	public enum State{
		IDLE, ASSUMING_CONFIG, CONFIGURED, ELEVATOR_MANUAL, WRIST_MANUAL, WAITING_FOR_WRIST, WAITING_FOR_ELEVATOR, INTAKING, STOWING,
		HANGING, READY_FOR_HANG
	}
	private State currentState = State.IDLE;
	private State previousState = State.IDLE;
	public State getState(){
		return currentState;
	}
	private void setState(State newState){
		previousState = currentState;
		currentState = newState;
	}
	
	public enum WantedState{
		IDLE, INTAKING, STOWED, RAISED, READY_FOR_HANG, HUNG, EXCHANGE
	}
	private WantedState wantedState = WantedState.IDLE;
	private WantedState previousWantedState = WantedState.IDLE;
	public WantedState getWantedState(){
		return wantedState;
	}
	public void setWantedState(WantedState newState){
		previousWantedState = wantedState;
		wantedState = newState;
	}
	
	private boolean driveTrainFlipped = false;
	public boolean driveTrainFlipped(){
		return driveTrainFlipped;
	}
	
	double desiredElevatorHeight = 0.0;
	double desiredWristAngle = 0.0;
	double desiredStowAngle = Constants.WRIST_PRIMARY_STOW_ANGLE;
	boolean shouldStow = false;
	boolean isConfigured = true;
	public boolean isConfigured(){
		return isConfigured;
	}
	
	double manualElevatorSpeed = 1.0;
	public void setManualElevatorSpeed(double speed){
		manualElevatorSpeed = speed;
	}
	
	boolean driverAlert = false;
	public boolean hasDriverAlert(){
		if(driverAlert){
			driverAlert = false;
			return true;
		}
		return false;
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			driverAlert = false;
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized(Superstructure.this){
				double elevatorHeight = elevator.getHeight();
				
				swerve.setMaxSpeed(Constants.kSwerveSpeedTreeMap.getInterpolated(new InterpolatingDouble(elevatorHeight)).value);
				
				/*if(elevatorHeight >= Constants.WRIST_MAX_STOW_HEIGHT && wrist.getAngle() > Constants.WRIST_SECONDARY_STOW_ANGLE
						&& wantedState != WantedState.READY_FOR_HANG && wantedState != WantedState.HUNG){
					wrist.setAngle(Constants.WRIST_SECONDARY_STOW_ANGLE);
				}*/
				if(driveTrainFlipped()){
					if(elevatorHeight < 2.5){
						intake.stop();
						wrist.setAngle(92.0);
					}
					if(elevatorHeight < (Constants.ELEVATOR_MINIMUM_HANGING_HEIGHT + 0.25)){
						setManualElevatorSpeed(0.25);
					}else{
						setManualElevatorSpeed(1.0);
					}
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
						switch(wantedState){
						case EXCHANGE:
							requestExchangeConfig();
							break;
						default:
							if(shouldStow)
								requestPrimaryWristStow();
							break;
						}
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
			wrist.setAngle(Constants.WRIST_PRIMARY_STOW_ANGLE);
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
		isConfigured = true;
		switch(wantedState){
		case INTAKING:
			requestIntakeOn();
			driverAlert = true;
			setState(State.INTAKING);
			break;
		case READY_FOR_HANG:
			elevator.configForHanging();
			setState(State.READY_FOR_HANG);
			break;
		case HUNG:
			//flipDriveTrain();
			setState(State.HANGING);
			break;
		default:
			break;
		}
	}
	
	public synchronized void requestConfig(double wristAngle, double elevatorHeight){
		isConfigured = false;
		wrist.setAngle(wristAngle);
		elevator.setTargetHeight(elevatorHeight);
		setWantedState(WantedState.IDLE);
		setState(State.ASSUMING_CONFIG);
	}
	
	public synchronized void requestConfig(double wristAngle){
		requestConfig(wristAngle, elevator.getHeight());
	}
	
	public synchronized void requestIntakingConfig(){
		intake.stop();
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		setWantedState(WantedState.INTAKING);
		shouldStow = true;
	}
	
	public synchronized void requestNonchalantIntakeConfig(){
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		setWantedState(WantedState.INTAKING);
		shouldStow = false;
	}
	
	public synchronized void requestOpenIntakingConfig(){
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		intake.intakeWide();
	}
	
	public synchronized void requestForcedIntakeConfig(){
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
		intake.forceIntake();
	}
	
	public synchronized void requestHighIntakingConfig(){
		intake.stop();
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_SECOND_CUBE_HEIGHT);
		setWantedState(WantedState.INTAKING);
		shouldStow = false;
	}
	
	public synchronized void requestHumanLoadingConfig(){
		intake.stop();
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_HUMAN_LOAD_HEIGHT);
		intake.open();
	}
	
	public synchronized void requestSwitchConfig(){
		setWantedState(WantedState.IDLE);
		requestConfig(20.0, Constants.ELEVATOR_SWITCH_HEIGHT);
		requestIntakeHold();
	}
	
	public synchronized void requestBalancedScaleConfig(){
		setWantedState(WantedState.IDLE);
		requestIntakeHold();
		/*desiredElevatorHeight = Constants.ELEVATOR_BALANCED_SCALE_HEIGHT;
		desiredWristAngle = 35.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);*/
		requestConfig(35.0, Constants.ELEVATOR_BALANCED_SCALE_HEIGHT);
	}
	
	public synchronized void requestLowScaleConfig(){
		setWantedState(WantedState.IDLE);
		requestIntakeHold();
		/*desiredElevatorHeight = Constants.ELEVATOR_LOW_SCALE_HEIGHT;
		desiredWristAngle = 15.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);*/
		requestConfig(25.0, Constants.ELEVATOR_LOW_SCALE_HEIGHT);
	}
	
	public synchronized void requestHighScaleConfig(){
		setWantedState(WantedState.IDLE);
		requestIntakeHold();
		/*desiredElevatorHeight = Constants.ELEVATOR_HIGH_SCALE_HEIGHT;
		desiredWristAngle = 60.0;
		desiredStowAngle = 60.0;
		setWantedState(WantedState.RAISED);
		setState(State.IDLE);*/
		requestConfig(60.0, Constants.ELEVATOR_HIGH_SCALE_HEIGHT);
	}
	
	public synchronized void requestHangingConfig(){
		requestIntakeOpen();
		requestConfig(Constants.WRIST_PRIMARY_STOW_ANGLE, Constants.ELEVATOR_HANGING_HEIGHT);
		setWantedState(WantedState.READY_FOR_HANG);
	}
	
	public synchronized void requestHungConfig(){
		if(getState() == State.READY_FOR_HANG){
			elevator.configForHanging();
			wrist.setAngle(Constants.WRIST_PRIMARY_STOW_ANGLE);
			elevator.setHanigngTargetHeight(Constants.ELEVATOR_MINIMUM_HANGING_HEIGHT);
			setState(State.ASSUMING_CONFIG);
			setWantedState(WantedState.HUNG);
		}
	}
	
	public synchronized void requestFinalHungConfig(){
		if(getState() == State.HANGING){
			elevator.configForHanging();
			wrist.setAngle(Constants.WRIST_PRIMARY_STOW_ANGLE);
			elevator.setHanigngTargetHeight(Constants.ELEVATOR_MINIMUM_HANGING_HEIGHT);
		}
	}
	
	/**
	 * Should only be called once per match, when hanging.
	 */
	public synchronized void flipDriveTrain(){
		if(!elevator.isHighGear()){
			elevator.fireGasStruts(true);
			elevator.fireLatch(true);
			elevator.setHangingLimits();
			driveTrainFlipped = true;
		}
	}
	
	public synchronized void requestPrimaryWristStow(){
		if(elevator.getHeight() <= Constants.WRIST_MAX_STOW_HEIGHT){
			desiredStowAngle = /*Constants.WRIST_PRIMARY_STOW_ANGLE*/60.0;
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
		setWantedState(WantedState.IDLE);
		requestConfig(Constants.WRIST_PRIMARY_STOW_ANGLE, Constants.ELEVATOR_INTAKING_HEIGHT);
	}
	
	public synchronized void requestExchangeConfig(){
		if(currentState != State.INTAKING || intake.hasCube()){
			//setWantedState(WantedState.IDLE);
			requestConfig(13.0, 0.31);
		}else{
			setWantedState(WantedState.EXCHANGE);
		}
	}
	
	public synchronized void requestTippingCubeConfig(){
		setWantedState(WantedState.IDLE);
		intake.stop();
		requestConfig(Constants.WRIST_INTAKING_ANGLE, Constants.ELEVATOR_TIPPING_CUBE_HEIGHT);
	}
	
	public synchronized void requestIntakeOn(){
		if(shouldStow)
			intake.intake();
		else
			intake.nonchalantIntake();
	}
	
	public synchronized void requestNonchalantIntake(){
		intake.nonchalantIntake();
	}
	
	public synchronized void requestIntakeHold(){
		intake.clamp();
	}
	
	public synchronized void requestIntakeScore(){
		if(getWantedState() == WantedState.EXCHANGE)
			intake.eject(Constants.kIntakeStrongEjectOutput);
		else
			intake.eject(Constants.kIntakeEjectOutput);
	}
	
	public synchronized void requestIntakeWeakScore(){
		intake.eject(Constants.kIntakeWeakEjectOutput);
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
			elevator.setOpenLoop(input * manualElevatorSpeed);
		}else if(getState() == State.ELEVATOR_MANUAL){
			elevator.setOpenLoop(0);
			if(elevator.getVelocityFeetPerSecond() <= 1.0){
				//setWantedState(previousWantedState);
				if(previousState == State.READY_FOR_HANG)
					setState(previousState);
				else
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
			//setWantedState(previousWantedState);
			if(previousState == State.READY_FOR_HANG)
				setState(previousState);
			else
				setState(State.IDLE);
			wrist.lockAngle();
		}
	}
	
	public void enableCompressor(boolean enable){
		compressor.setClosedLoopControl(enable);
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
