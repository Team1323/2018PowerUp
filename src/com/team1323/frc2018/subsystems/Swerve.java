package com.team1323.frc2018.subsystems;

import java.util.Arrays;
import java.util.List;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.lib.util.SwerveKinematics;
import com.team1323.lib.util.Util;

public class Swerve extends Subsystem{
	private static Swerve instance = null;
	public static Swerve getInstance(){
		if(instance == null)
			instance = new Swerve();
		return instance;
	}
	
	SwerveDriveModule frontRight, frontLeft, rearLeft, rearRight;
	List<SwerveDriveModule> modules;
	
	public Swerve(){
		frontRight = new SwerveDriveModule(Ports.FRONT_RIGHT_ROTATION, Ports.FRONT_RIGHT_DRIVE,
				1, Constants.FRONT_RIGHT_TURN_OFFSET);
		frontLeft = new SwerveDriveModule(Ports.FRONT_LEFT_ROTATION, Ports.FRONT_LEFT_DRIVE,
				2, Constants.FRONT_LEFT_TURN_OFFSET);
		rearLeft = new SwerveDriveModule(Ports.REAR_LEFT_ROTATION, Ports.REAR_LEFT_DRIVE,
				3, Constants.REAR_LEFT_TURN_OFFSET);
		rearRight = new SwerveDriveModule(Ports.REAR_RIGHT_ROTATION, Ports.REAR_RIGHT_DRIVE,
				4, Constants.REAR_RIGHT_TURN_OFFSET);
		
		modules = Arrays.asList(frontRight, frontLeft, rearLeft, rearRight);
	}
	
	private double xInput = 0;
	private double yInput = 0;
	private double rotationalInput = 0;
	
	private SwerveKinematics kinematics = new SwerveKinematics();
	
	public enum ControlState{
		NEUTRAL, MANUAL, POSITION
	}
	private ControlState currentState = ControlState.NEUTRAL;
	public ControlState getState(){
		return currentState;
	}
	public void setState(ControlState newState){
		currentState = newState;
	}
	
	public void sendInput(double x, double y, double rotate, boolean robotCentric, boolean lowPower){
		double angle = 0;
		if(robotCentric){
			xInput = x;
			yInput = y;
		}
		else{
			double tmp = (y* Math.cos(angle)) + (x * Math.sin(angle));
			xInput = (-y * Math.sin(angle)) + (x * Math.cos(angle));
			yInput = tmp;			
		}
		
		if((x != 0 || y != 0) && currentState != ControlState.MANUAL){
			setState(ControlState.MANUAL);
		}
	}
	
	public void setPositionTarget(double directionDegrees, double magnitudeInches){
		setState(ControlState.POSITION);
		modules.forEach((m) -> m.setModuleAngle(directionDegrees));
		modules.forEach((m) -> m.setDrivePositionTarget(magnitudeInches));
	}
	
	public void update(){
		switch(currentState){
		case MANUAL:
			kinematics.calculate(xInput, yInput, rotationalInput);
			if(xInput == 0 && yInput == 0 && rotationalInput == 0){
				stop();
			}else{
				for(int i=0; i<modules.size(); i++){
		    		if(Util.shouldReverse(kinematics.wheelAngles[i], modules.get(i).getModuleAngle())){
		    			modules.get(i).setModuleAngle(kinematics.wheelAngles[i] + 180);
		    			modules.get(i).setDriveOpenLoop(-kinematics.wheelSpeeds[i]);
		    		}else{
		    			modules.get(i).setModuleAngle(kinematics.wheelAngles[i]);
		    			modules.get(i).setDriveOpenLoop(kinematics.wheelSpeeds[i]);
		    		}
		    	}
			}
			break;
		case POSITION:
			
			break;
		case NEUTRAL:
			stop();
			break;
		}
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			xInput = 0;
			yInput = 0;
			rotationalInput = 0;
			stop();
		}

		@Override
		public void onLoop(double timestamp) {
			update();
		}

		@Override
		public void onStop(double timestamp) {
			xInput = 0;
			yInput = 0;
			rotationalInput = 0;
			stop();
		}
		
	};
	
	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(loop);
	}

	@Override
	public synchronized void stop() {
		modules.forEach((m) -> m.stop());
		setState(ControlState.NEUTRAL);
	}

	@Override
	public void zeroSensors() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void outputToSmartDashboard() {
		modules.forEach((m) -> m.outputToSmartDashboard());
	}
}
