package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Looper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Subsystem{
	private static Elevator instance = null;
	public static Elevator getInstance(){
		if(instance == null)
			instance = new Elevator();
		return instance;
	}
	
	TalonSRX master, motor2, motor3, motor4;
	int encoderOffset = 0;
	
	private Elevator(){
		master = new TalonSRX(Ports.ELEVATOR_1);
		motor2 = new TalonSRX(Ports.ELEVATOR_2);
		motor3 = new TalonSRX(Ports.ELEVATOR_3);
		motor4 = new TalonSRX(Ports.ELEVATOR_4);
		
		master.enableVoltageCompensation(true);
		
		master.setInverted(true);
		motor2.setInverted(true);
		motor3.setInverted(true);
		motor4.setInverted(true);
		
		master.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		master.setSensorPhase(true);
		resetToAbsolutePosition();
		master.setNeutralMode(NeutralMode.Brake);
		configForLifting();
		master.set(ControlMode.PercentOutput, 0.0);
		motor2.set(ControlMode.Follower, Ports.ELEVATOR_1);
		motor3.set(ControlMode.Follower, Ports.ELEVATOR_1);
		motor4.set(ControlMode.Follower, Ports.ELEVATOR_1);
	}
	
	private void configForLifting(){
		master.selectProfileSlot(0, 0);
		master.config_kP(0, 2.5, 10);
		master.config_kI(0, 0.0, 10);
		master.config_kD(0, 80.0, 10);
		master.config_kF(0, 1023.0/Constants.ELEVATOR_MAX_SPEED_HIGH_GEAR, 10);
		
		master.config_kP(1, 2.5, 10);
		master.config_kI(1, 0.0, 10);
		master.config_kD(1, 80.0, 10);
		master.config_kF(1, 1023.0/Constants.ELEVATOR_MAX_SPEED_HIGH_GEAR, 10);
		
		master.configMotionCruiseVelocity((int)(Constants.ELEVATOR_MAX_SPEED_HIGH_GEAR*0.8), 10);
		master.configMotionAcceleration((int)(Constants.ELEVATOR_MAX_SPEED_HIGH_GEAR*2.0), 10);
	}
	
	private void configForHanging(){
		master.selectProfileSlot(1, 0);
		master.config_kP(1, 8.0, 10);
		master.config_kI(1, 0.0, 10);
		master.config_kD(1, 160.0, 10);
		master.config_kF(1, 1023.0/Constants.ELEVATOR_MAX_SPEED_LOW_GEAR, 10);
		master.configMotionCruiseVelocity((int)(Constants.ELEVATOR_MAX_SPEED_LOW_GEAR*0.75), 10);
		master.configMotionAcceleration((int)(Constants.ELEVATOR_MAX_SPEED_LOW_GEAR), 10);
	}
	
	public void setOpenLoop(double output){
		master.set(ControlMode.PercentOutput, output);
	}
	
	public void setTargetHeight(double heightFeet){
		if(isSensorConnected()){
			if(heightFeet > getHeight())
				master.selectProfileSlot(0, 0);
			else
				master.selectProfileSlot(1, 0);
			master.set(ControlMode.MotionMagic, feetToEncUnits(heightFeet));
		}else{
			DriverStation.reportError("Elevator encoder not detected!", false);
			stop();
		}
	}
	
	public void changeHeight(double deltaHeightFeet){
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
	
	public double getHeight(){
		return encUnitsToFeet(master.getSelectedSensorPosition(0) - encoderOffset);
	}
	
	public int feetToEncUnits(double feet){
		//TODO
		return (int) (feet * Constants.ELEVATOR_TICKS_PER_FOOT);
	}
	
	public double encUnitsToFeet(int encUnits){
		//TODO
		return encUnits / Constants.ELEVATOR_TICKS_PER_FOOT;
	}
	
	public boolean isSensorConnected(){
		int pulseWidthPeriod = master.getSensorCollection().getPulseWidthRiseToRiseUs();
		return pulseWidthPeriod != 0;
	}
	
	private void resetToAbsolutePosition(){
		int absolutePosition = master.getSensorCollection().getPulseWidthPosition();
		master.setSelectedSensorPosition(absolutePosition, 0, 10);
		encoderOffset = absolutePosition;
	}

	@Override
	public void stop() {
		setOpenLoop(0.0);
	}

	@Override
	public void zeroSensors() {
		resetToAbsolutePosition();
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber("Elevator 1 Current", master.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 2 Current", motor2.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 3 Current", motor3.getOutputCurrent());
		SmartDashboard.putNumber("Elevator 4 Current", motor4.getOutputCurrent());
		SmartDashboard.putNumber("Elevator Height", getHeight());
		SmartDashboard.putNumber("Elevator Height Graph", master.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elevator Encoder", master.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elevator Velocity", master.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("Elevator Error", master.getClosedLoopError(0));
		if(master.getControlMode() == ControlMode.MotionMagic)
			SmartDashboard.putNumber("Elevator Setpoint", master.getClosedLoopTarget(0));
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
			System.out.println("Master elevator motor current too low: " + current);
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
		
		startingEncPosition = master.getSelectedSensorPosition(0);
		motor4.set(ControlMode.PercentOutput, -4.0/12.0);
		Timer.delay(timeInterval);
		current = motor4.getOutputCurrent();
		motor4.set(ControlMode.Follower, Ports.ELEVATOR_1);
		if(Math.signum(master.getSelectedSensorPosition(0) - startingEncPosition) != -1.0){
			System.out.println("Elevator motor 4 needs to be reversed");
			passed = false;
		}
		if(current < currentMinimum){
			System.out.println("Elevator motor 4 current too low: " + current);
			passed = false;
		}else if(current > currentMaximum){
			System.out.println("Elevator motor 4 current too high: " + current);
			passed = false;
		}else{
			System.out.println("Elevator motor 4 current good: " + current);
		}
		
		return passed;
	}
}
