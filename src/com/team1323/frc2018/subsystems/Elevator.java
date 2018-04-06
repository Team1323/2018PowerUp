package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem{
	private static Elevator instance = null;
	public static Elevator getInstance(){
		if(instance == null)
			instance = new Elevator();
		return instance;
	}
	
	TalonSRX master, motor2, motor3;
	Solenoid shifter, latch, gasStruts;
	private double targetHeight = 0.0;
	private boolean isHighGear = true;
	public boolean isHighGear(){
		return isHighGear;
	}
	private boolean limitsEnabled = false;
	public boolean limitsEnabled(){
		return limitsEnabled;
	}
	
	public TalonSRX getPigeonTalon(){
		return motor2;
	}
	
	public enum ControlState{
		Neutral, Position, OpenLoop, Locked
	}
	private ControlState state = ControlState.Neutral;
	public ControlState getState(){
		return state;
	}
	public void setState(ControlState newState){
		state = newState;
	}
	
	private Elevator(){
		master = new TalonSRX(Ports.ELEVATOR_1);
		motor2 = new TalonSRX(Ports.ELEVATOR_2);
		motor3 = new TalonSRX(Ports.ELEVATOR_3);
		
		shifter = new Solenoid(20, Ports.ELEVATOR_SHIFTER);
		latch = new Solenoid(20, Ports.ELEVATOR_RELEASE_PISTON);
		gasStruts = new Solenoid(20, Ports.GAS_STRUTS);
		
		master.configVoltageCompSaturation(12.0, 10);
		motor2.configVoltageCompSaturation(12.0, 10);
		motor3.configVoltageCompSaturation(12.0, 10);
		master.enableVoltageCompensation(true);
		motor2.enableVoltageCompensation(true);
		motor3.enableVoltageCompensation(true);
		
		master.setInverted(true);
		motor2.setInverted(true);
		motor3.setInverted(true);
		
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		master.setSensorPhase(true);
		master.configReverseSoftLimitThreshold(Constants.kElevatorEncoderStartingPosition, 10);
		master.configForwardSoftLimitThreshold(Constants.kElevatorEncoderStartingPosition + feetToEncUnits(Constants.kElevatorMaxHeight), 10);
		master.configForwardSoftLimitEnable(true, 10);
		master.configReverseSoftLimitEnable(true, 10);
		enableLimits(true);
		
		setCurrentLimit(Constants.kELevatorCurrentLimit);
		
		master.configClosedloopRamp(0.0, 10);
		motor2.configClosedloopRamp(0.0, 10);
		motor3.configClosedloopRamp(0.0, 10);
		
		//resetToAbsolutePosition();
		master.setNeutralMode(NeutralMode.Brake);
		motor2.setNeutralMode(NeutralMode.Brake);
		motor3.setNeutralMode(NeutralMode.Brake);
		configForLifting();
		master.set(ControlMode.PercentOutput, 0.0);
		motor2.set(ControlMode.Follower, Ports.ELEVATOR_1);
		motor3.set(ControlMode.Follower, Ports.ELEVATOR_1);
	}
	
	private void setHighGear(boolean high){
		shifter.set(!high);
		isHighGear = high;
	}
	
	private void configForLifting(){
		setHighGear(true);
		
		master.selectProfileSlot(0, 0);
		master.config_kP(0, 4.0, 10);
		master.config_kI(0, 0.0, 10);
		master.config_kD(0, 160.0, 10);
		master.config_kF(0, 1023.0/Constants.kElevatorMaxSpeedHighGear, 10);
		
		master.config_kP(1, 1.5, 10);
		master.config_kI(1, 0.0, 10);
		master.config_kD(1, 70.0, 10);
		master.config_kF(1, 1023.0/Constants.kElevatorMaxSpeedHighGear, 10);
		
		master.configMotionCruiseVelocity((int)(Constants.kElevatorMaxSpeedHighGear*1.0), 10);//0.9
		master.configMotionAcceleration((int)(Constants.kElevatorMaxSpeedHighGear*5.0), 10);//3.0
	}
	
	public void configForTeleopSpeed(){
		master.configMotionCruiseVelocity((int)(Constants.kElevatorMaxSpeedHighGear*1.0), 10);//0.9
		master.configMotionAcceleration((int)(Constants.kElevatorMaxSpeedHighGear*5.0), 10);//3.0
	}
	
	public void configForAutoSpeed(){
		master.configMotionCruiseVelocity((int)(Constants.kElevatorMaxSpeedHighGear*1.0), 10);//0.9
		master.configMotionAcceleration((int)(Constants.kElevatorMaxSpeedHighGear*3.0), 10);//3.0
	}
	
	public void configForHanging(){
		setHighGear(false);
		
		master.selectProfileSlot(1, 0);
		master.config_kP(1, 8.0, 10);
		master.config_kI(1, 0.0, 10);
		master.config_kD(1, 160.0, 10);
		master.config_kF(1, 1023.0/Constants.kElevatorMaxSpeedLowGear, 10);
		master.configMotionCruiseVelocity((int)(Constants.kElevatorMaxSpeedLowGear*0.9), 10);
		master.configMotionAcceleration((int)(Constants.kElevatorMaxSpeedLowGear*1.0), 10);
	}
	
	public void setHangingLimits(){
		master.configReverseSoftLimitThreshold(feetToEncUnits(Constants.kElevatorMinimumHangingHeight), 10);
		master.configForwardSoftLimitThreshold(feetToEncUnits(Constants.kElevatorMaximumHangingHeight), 10);
		System.out.println("Hanging limits set for the elevator");
	}
	
	public void enableLimits(boolean enable){
		master.overrideSoftLimitsEnable(enable);
		limitsEnabled = enable;
	}
	
	public void setCurrentLimit(int amps){
		master.configContinuousCurrentLimit(amps, 10);
		master.configPeakCurrentLimit(amps, 10);
		master.configPeakCurrentDuration(10, 10);
		master.enableCurrentLimit(true);
		motor2.configContinuousCurrentLimit(amps, 10);
		motor2.configPeakCurrentLimit(amps, 10);
		motor2.configPeakCurrentDuration(10, 10);
		motor2.enableCurrentLimit(true);
		motor3.configContinuousCurrentLimit(amps, 10);
		motor3.configPeakCurrentLimit(amps, 10);
		motor3.configPeakCurrentDuration(10, 10);
		motor3.enableCurrentLimit(true);
	}
	
	public void fireGasStruts(boolean fire){
		gasStruts.set(fire);
	}
	
	public void fireLatch(boolean fire){
		latch.set(fire);
	}
	
	public void setOpenLoop(double output){
		setState(ControlState.OpenLoop);
		master.set(ControlMode.PercentOutput, output);
	}
	
	public synchronized void setTargetHeight(double heightFeet){
		setState(ControlState.Position);
		if(!isHighGear)
			configForLifting();
		if(isSensorConnected()){
			if(heightFeet > getHeight())
				master.selectProfileSlot(0, 0);
			else
				master.selectProfileSlot(1, 0);
			targetHeight = heightFeet;
			master.set(ControlMode.MotionMagic, Constants.kElevatorEncoderStartingPosition + feetToEncUnits(heightFeet));
		}else{
			DriverStation.reportError("Elevator encoder not detected!", false);
			stop();
		}
	}
	
	public synchronized void setHanigngTargetHeight(double heightFeet){
		setState(ControlState.Position);
		if(isHighGear)
			configForHanging();
		if(isSensorConnected()){
			master.selectProfileSlot(1, 0);
			targetHeight = heightFeet;
			master.set(ControlMode.MotionMagic, Constants.kElevatorEncoderStartingPosition + feetToEncUnits(heightFeet));
		}else{
			DriverStation.reportError("Elevator encoder not detected!", false);
			stop();
		}
	}
	
	public synchronized void changeHeight(double deltaHeightFeet){
		setState(ControlState.Position);
		if(isSensorConnected()){
			if(deltaHeightFeet > 0)
				master.selectProfileSlot(0, 0);
			else
				master.selectProfileSlot(1, 0);
			master.set(ControlMode.MotionMagic, master.getSelectedSensorPosition(0) + feetToEncUnits(deltaHeightFeet));
		}else{
			DriverStation.reportError("Elevator encoder not detected!", false);
			stop();
		}
	}
	
	public synchronized void lockHeight(){
		setState(ControlState.Locked);
		if(isSensorConnected()){
			targetHeight = getHeight();
			master.set(ControlMode.MotionMagic, master.getSelectedSensorPosition(0));
		}else{
			DriverStation.reportError("Elevator encoder not detected!", false);
			stop();
		}
	}
	
	public double getHeight(){
		return encUnitsToFeet(master.getSelectedSensorPosition(0) - Constants.kElevatorEncoderStartingPosition);
	}
	
	public double getVelocityFeetPerSecond(){
		return encUnitsToFeet(master.getSelectedSensorVelocity(0)) * 10.0;
	}
	
	public boolean hasReachedTargetHeight(){
		if(master.getControlMode() == ControlMode.MotionMagic)
			return (Math.abs(targetHeight - getHeight()) <= Constants.kElevatorHeightTolerance);
		return false;
	}
	
	public void goToIntakingHeight(){
		setTargetHeight(Constants.kElevatorIntakingHeight);
	}
	
	public void goToSwitchHeight(){
		setTargetHeight(Constants.kElevatorSwitchHeight);
	}
	
	public void goToScaleHeight(){
		setTargetHeight(Constants.kELevatorBalancedScaleHeight);
	}
	
	public int feetToEncUnits(double feet){
		//TODO
		return (int) (feet * Constants.kElevatorTicksPerFoot);
	}
	
	public double encUnitsToFeet(int encUnits){
		//TODO
		return encUnits / Constants.kElevatorTicksPerFoot;
	}
	
	private boolean getMotorsWithHighCurrent(){
		int motors = 0;
		if(master.getOutputCurrent() >= Constants.kElevatorMaxCurrent)
			motors++;
		if(motor2.getOutputCurrent() >= Constants.kElevatorMaxCurrent)
			motors++;
		if(motor3.getOutputCurrent() >= Constants.kElevatorMaxCurrent)
			motors++;
		return motors > 1;
	}
	
	private final Loop loop  = new Loop(){

		@Override
		public void onStart(double timestamp) {
			
		}

		@Override
		public void onLoop(double timestamp) {
			if(getMotorsWithHighCurrent()){
				DriverStation.reportError("Elevator current too high", false);
				//stop();
			}
		}

		@Override
		public void onStop(double timestamp) {
			
		}
		
	};
	
	public boolean isSensorConnected(){
		int pulseWidthPeriod = master.getSensorCollection().getPulseWidthRiseToRiseUs();
		return pulseWidthPeriod != 0;
	}
	
	private void resetToAbsolutePosition(){
		int absolutePosition = master.getSensorCollection().getPulseWidthPosition();
		master.setSelectedSensorPosition(absolutePosition, 0, 10);
	}

	@Override
	public void stop() {
		setOpenLoop(0.0);
	}

	@Override
	public void zeroSensors() {
		master.setSelectedSensorPosition(0, 0, 10);
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(loop);
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Elevator 1 Current", master.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 2 Current", motor2.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 3 Current", motor3.getOutputCurrent());
		//SmartDashboard.putNumber("Elevator Voltage", master.getMotorOutputVoltage());
		SmartDashboard.putNumber("Elevator Height", /*Math.round(getHeight()*1000.0)/1000.0*/getHeight());
		//SmartDashboard.putNumber("Elevator Height Graph", getHeight());
		//SmartDashboard.putNumber("Elevator Pulse Width Position", master.getSensorCollection().getPulseWidthPosition());
		SmartDashboard.putNumber("Elevator Encoder", master.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elevator Velocity", master.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Elevator Error", master.getClosedLoopError(0));
		/*if(master.getControlMode() == ControlMode.MotionMagic)
			SmartDashboard.putNumber("Elevator Setpoint", master.getClosedLoopTarget(0));*/
	}
	
	public boolean checkSystem(){
		double currentMinimum = 0.5;
		double currentMaximum = 20.0;
		double timeInterval = 1.0;
		
		boolean passed = true;
		
		if(!isSensorConnected()){
			System.out.println("Elevator sensor not connected, connect and retest");
			return false;
		}
		
		master.configForwardSoftLimitEnable(false, 10);
		master.configReverseSoftLimitEnable(false, 10);
		
		motor2.set(ControlMode.PercentOutput, 0.0);
		motor3.set(ControlMode.PercentOutput, 0.0);
		
		double startingEncPosition = master.getSelectedSensorPosition(0);
		master.set(ControlMode.PercentOutput, 4.0/12.0);
		Timer.delay(timeInterval);
		double current = master.getOutputCurrent();
		master.set(ControlMode.PercentOutput, 0.0);
		if(Math.signum(master.getSelectedSensorPosition(0) - startingEncPosition) != 1.0){
			System.out.println("Master elevator motor needs to be reversed");
			passed = false;
		}
		if(current < currentMinimum){
			System.out.println("Master elevator motor current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Master elevator motor current too high: " + current);
			passed = false;
		}else{
			System.out.println("Master elevator motor current good: " + current);
		}
		
		startingEncPosition = master.getSelectedSensorPosition(0);
		motor2.set(ControlMode.PercentOutput, -4.0/12.0);
		Timer.delay(timeInterval);
		current = motor2.getOutputCurrent();
		motor2.set(ControlMode.Follower, Ports.ELEVATOR_1);
		if(Math.signum(master.getSelectedSensorPosition(0) - startingEncPosition) != -1.0){
			System.out.println("Elevator motor 2 needs to be reversed");
			passed = false;
		}
		if(current < currentMinimum){
			System.out.println("Elevator motor 2 current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Elevator motor 2 current too high: " + current);
			passed = false;
		}else{
			System.out.println("Elevator motor 2 current good: " + current);
		}
		
		startingEncPosition = master.getSelectedSensorPosition(0);
		motor3.set(ControlMode.PercentOutput, 4.0/12.0);
		Timer.delay(timeInterval);
		current = motor3.getOutputCurrent();
		motor3.set(ControlMode.Follower, Ports.ELEVATOR_1);
		if(Math.signum(master.getSelectedSensorPosition(0) - startingEncPosition) != 1.0){
			System.out.println("Elevator motor 3 needs to be reversed");
			passed = false;
		}
		if(current < currentMinimum){
			System.out.println("Elevator motor 3 current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Elevator motor 3 current too high: " + current);
			passed = false;
		}else{
			System.out.println("Elevator motor 3 current good: " + current);
		}
		
		master.configForwardSoftLimitEnable(true, 10);
		master.configReverseSoftLimitEnable(true, 10);
		
		configForLifting();
		
		return passed;
	}
}
