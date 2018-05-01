package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.subsystems.Intake.State;
import com.team1323.frc2018.subsystems.IntakeV2.IntakeState;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeV2 extends Subsystem{
	private static IntakeV2 instance = null;
	public static IntakeV2 getInstance(){
		if(instance == null)
			instance = new IntakeV2();
		return instance;
	}
	
	boolean hasCube = false;
	public synchronized boolean hasCube(){
		return hasCube;
	}
	
	private TalonSRX leftIntake, rightIntake;
	private Solenoid pinchers, clampers;
	private DigitalInput banner;
	
	private IntakeV2(){
		leftIntake = new TalonSRX(Ports.INTAKE_LEFT);
		rightIntake = new TalonSRX(Ports.INTAKE_RIGHT);
		pinchers = new Solenoid(20, Ports.INTAKE_PINCHERS);
		clampers = new Solenoid(20, Ports.INTAKE_CLAMPERS);
		banner = new DigitalInput(Ports.INTAKE_BANNER);
		
		leftIntake.setInverted(false);
		rightIntake.setInverted(true);
		
		leftIntake.setNeutralMode(NeutralMode.Brake);
		rightIntake.setNeutralMode(NeutralMode.Brake);
		
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
	
	public enum IntakeState{
		OFF(0,true,false), INTAKING(Constants.kIntakingOutput,true,false), CLAMPING(Constants.kIntakeWeakHoldingOutput,true,true), 
		EJECTING(Constants.kIntakeWeakEjectOutput,true,false), OPEN(0,false,false), INTAKING_WIDE(Constants.kIntakingOutput,false,false), 
		NONCHALANT_INTAKING(Constants.kIntakingOutput,true,false), GROUND_CLAMPING(Constants.kIntakeWeakHoldingOutput,true,true), FORCED_INTAKE(Constants.kIntakingOutput,true,false);
		
		public double leftIntakeOutput = 0;
		public double rightIntakeOutput = 0;
		public boolean pinched = true;
		public boolean clamped = false;
		
		private IntakeState(double output, boolean pinch, boolean clamp){
			this(output, output, pinch, clamp);
		}
		
		private IntakeState(double left, double right, boolean pinch, boolean clamp){
			leftIntakeOutput = left;
			rightIntakeOutput = right;
			pinched = pinch;
			clamped = clamp;
		};
	}
	private IntakeState currentState = IntakeState.OFF;
	public IntakeState getState(){
		return currentState;
	}
	private synchronized void setState(IntakeState newState){
		currentState = newState;
		stateEnteredTimestamp = Timer.getFPGATimestamp();
	}
	private double stateEnteredTimestamp = 0;
	
	private double bannerSensorBeganTimestamp = Double.POSITIVE_INFINITY;
	
	private boolean isResucking = false;
	
	private double holdingOutput = Constants.kIntakeWeakHoldingOutput;
	public void setHoldingOutput(double output){
		holdingOutput = output;
	}
	
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
	
	public double getHigherCurrent(){
		double leftCurrent = leftIntake.getOutputCurrent();
		double rightCurrent = rightIntake.getOutputCurrent();
		if(leftCurrent > rightCurrent)
			return leftCurrent;
		return rightCurrent;
	}
	
	public void firePinchers(boolean fire){
		pinchers.set(!fire);
	}
	
	public void fireClampers(boolean fire){
		clampers.set(!fire);
	}
	
	private void forwardRollers(){
		setRampRate(Constants.kIntakeRampRate);
		leftIntake.set(ControlMode.PercentOutput, Constants.kIntakingOutput);
		rightIntake.set(ControlMode.PercentOutput, Constants.kIntakingOutput);
	}
	
	private void setRollers(double output){
		setRollers(output, output);
	}
	
	private void setRollers(double left, double right){
		setRampRate(0.0);
		leftIntake.set(ControlMode.PercentOutput, left);
		rightIntake.set(ControlMode.PercentOutput, right);
	}
	
	private void spinRollers(){
		//leftIntake.set(ControlMode.PercentOutput, 0.8);
		//rightIntake.set(ControlMode.PercentOutput, -0.33);
	}
	
	private void holdRollers(){
		setRollers(holdingOutput);
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
			setState(IntakeState.OFF);
			stop();
		}

		@Override
		public void onLoop(double timestamp) {
			switch(currentState){
			case OFF:
				
				break;
			case INTAKING:
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
			case CLAMPING:
				if(banner.get()){
					if(isResucking){
						holdRollers();
						isResucking = false;
					}
				}else{
					if(!isResucking){
						setRollers(Constants.kIntakingResuckingOutput);
						isResucking = true;
					}
				}
				break;
			case GROUND_CLAMPING:
				
				break;
			case EJECTING:
				setRampRate(0.0);
				hasCube = false;
				if(timestamp - stateEnteredTimestamp > 2.0){
					stop();
					setRampRate(Constants.kIntakeRampRate);
				}
				break;
			default:
				break;
			}
		}

		@Override
		public void onStop(double timestamp) {
			setState(IntakeState.OFF);
			stop();
		}
		
	};
	
	public void intake(){
		hasCube = false;
		setState(IntakeState.INTAKING);
		forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void nonchalantIntake(){
		hasCube = false;
		setState(IntakeState.NONCHALANT_INTAKING);
		forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void intakeWide(){
		hasCube = false;
		setState(IntakeState.INTAKING_WIDE);
		firePinchers(false);
		fireClampers(false);
		forwardRollers();
	}
	
	public void forceIntake(){
		hasCube = false;
		setState(IntakeState.FORCED_INTAKE);
		if(banner.get()) holdRollers();
		else forwardRollers();
		firePinchers(true);
		fireClampers(false);
	}
	
	public void clamp(){
		setState(IntakeState.CLAMPING);
		holdRollers();
		firePinchers(true);
		fireClampers(true);
	}
	
	public void groundClamp(){
		setState(IntakeState.GROUND_CLAMPING);
		holdRollers();
		firePinchers(true);
		fireClampers(true);
	}
	
	public void eject(double output){
		setState(IntakeState.EJECTING);
		setRollers(output);
		firePinchers(true);
		fireClampers(false);
		hasCube = false;
	}
	
	public void open(){
		setState(IntakeState.OPEN);
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
	
	public class IntakeStateRequest extends Request{
		
		IntakeState newState;
		
		public IntakeStateRequest(IntakeState desiredState){
			newState = desiredState;
		}
		
		@Override
		public void act() {
			setState(newState);
			setRollers(newState.leftIntakeOutput, newState.rightIntakeOutput);
			firePinchers(newState.pinched);
			fireClampers(newState.clamped);
		}

		@Override
		public boolean isFinished() {
			return true;
		}
		
	}
	
	public Request intakeStateRequest(IntakeState desiredState) {
		return new IntakeStateRequest(desiredState);
	}
	
	@Override
	public synchronized void stop(){
		if(getState() != IntakeState.OFF)
			setState(IntakeState.OFF);
		setRollers(0);
		firePinchers(true);
		fireClampers(false);
		setRampRate(0.0);
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

}
