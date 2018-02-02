package com.team1323.frc2018.subsystems;

import java.util.Arrays;
import java.util.List;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.pathfinder.PathfinderPath;
import com.team1323.lib.util.SwerveHeadingController;
import com.team1323.lib.util.SwerveKinematics;
import com.team1323.lib.util.Util;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.followers.DistanceFollower;

public class Swerve extends Subsystem{
	private static Swerve instance = null;
	public static Swerve getInstance(){
		if(instance == null)
			instance = new Swerve();
		return instance;
	}
	
	public SwerveDriveModule frontRight, frontLeft, rearLeft, rearRight;
	List<SwerveDriveModule> modules;
	
	Pigeon pigeon;
	SwerveHeadingController headingController = new SwerveHeadingController();
	public void temporarilyDisableHeadingController(){
		headingController.temporarilyDisable();
	}
	
	RigidTransform2d pose;
	double distanceTraveled;
	double currentVelocity = 0;
	double lastUpdateTimestamp = 0;
	public RigidTransform2d getPose(){
		return pose;
	}
	
	DistanceFollower pathFollower;
	PathfinderPath currentPath;
	Trajectory currentPathTrajectory;
	int currentPathSegment = 0;
	double pathMotorOutput = 0;
	Rotation2d lastSteeringDirection;
	boolean hasFinishedPath = false;
	public boolean hasFinishedPath(){
		return hasFinishedPath;
	}
	
	private Swerve(){
		frontRight = new SwerveDriveModule(Ports.FRONT_RIGHT_ROTATION, Ports.FRONT_RIGHT_DRIVE,
				0, Constants.FRONT_RIGHT_ENCODER_STARTING_POS, Constants.kVehicleToModuleOne);
		frontLeft = new SwerveDriveModule(Ports.FRONT_LEFT_ROTATION, Ports.FRONT_LEFT_DRIVE,
				1, Constants.FRONT_LEFT_ENCODER_STARTING_POS, Constants.kVehicleToModuleTwo);
		rearLeft = new SwerveDriveModule(Ports.REAR_LEFT_ROTATION, Ports.REAR_LEFT_DRIVE,
				2, Constants.REAR_LEFT_ENCODER_STARTING_POS, Constants.kVehicleToModuleThree);
		rearRight = new SwerveDriveModule(Ports.REAR_RIGHT_ROTATION, Ports.REAR_RIGHT_DRIVE,
				3, Constants.REAR_RIGHT_ENCODER_STARTING_POS, Constants.kVehicleToModuleFour);
		
		modules = Arrays.asList(frontRight, frontLeft, rearLeft, rearRight);
		
		rearLeft.invertDriveMotor(true);
		frontLeft.invertDriveMotor(true);
		
		modules.forEach((m) -> m.reverseRotationSensor(true));
		
		pigeon = Pigeon.getInstance();
		
		pose = new RigidTransform2d();
		distanceTraveled = 0;
	}
	
	private double xInput = 0;
	private double yInput = 0;
	private double rotationalInput = 0;
	
	private SwerveKinematics kinematics = new SwerveKinematics();
	
	public enum ControlState{
		NEUTRAL, MANUAL, POSITION, PATH_FOLLOWING
	}
	private ControlState currentState = ControlState.NEUTRAL;
	public ControlState getState(){
		return currentState;
	}
	public void setState(ControlState newState){
		currentState = newState;
	}
	
	public void sendInput(double x, double y, double rotate, boolean robotCentric, boolean lowPower){
		Rotation2d angle = pigeon.getAngle();
		if(robotCentric){
			xInput = x;
			yInput = y;
		}
		else{
			double tmp = (y* angle.cos()) + (x * angle.sin());
			xInput = (-y * angle.sin()) + (x * angle.cos());
			yInput = tmp;			
		}
		
		if(lowPower){
			xInput *= 0.5;
			yInput *= 0.5;
		}
		
		if(rotate != 0 && rotationalInput == 0){
			headingController.disable();
		}else if(rotate == 0 && rotationalInput != 0){
			headingController.temporarilyDisable();
		}
		
		rotationalInput = rotate;
		
		if((x != 0 || y != 0 || rotate != 0) && currentState != ControlState.MANUAL)
			setState(ControlState.MANUAL);
	}
	
	public void rotate(double goalHeading){
		if(xInput == 0 && yInput == 0)
			rotateInPlace(Rotation2d.fromDegrees(goalHeading));
		else
			headingController.setSnapTarget(
					Util.placeInAppropriate0To360Scope(pose.getRotation().getUnboundedDegrees(), goalHeading));
	}
	
	public void rotateInPlace(Rotation2d goalHeading){
		setState(ControlState.POSITION);
		kinematics.calculate(0, 0, 1);
		modules.forEach((m) -> m.setModuleAngle(kinematics.wheelAngles[m.moduleID]));
		Rotation2d deltaAngle = goalHeading.rotateBy(pose.getRotation().inverse());
		modules.forEach((m) -> m.setDrivePositionTarget(deltaAngle.getRadians()*Constants.SWERVE_DIAGONAL/2));
	}
	
	public void setPathHeading(double goalHeading){
		headingController.setStabilizationTarget(
				Util.placeInAppropriate0To360Scope(
						pose.getRotation().getUnboundedDegrees(), goalHeading));
	}
	
	public void setAbsolutePathHeading(double absoluteHeading){
		headingController.setStabilizationTarget(absoluteHeading);
	}
	
	public void setPositionTarget(double directionDegrees, double magnitudeInches){
		setState(ControlState.POSITION);
		modules.forEach((m) -> m.setModuleAngle(directionDegrees));
		modules.forEach((m) -> m.setDrivePositionTarget(magnitudeInches));
	}
	
	public synchronized void followPath(PathfinderPath path, double goalHeading){
		hasFinishedPath = false;
		distanceTraveled = 0;
		currentPathSegment = 0;
		currentPath = path;
		pathFollower = path.resetFollower();
		currentPathTrajectory = path.getTrajectory();
		headingController.setStabilizationTarget(goalHeading);
		setState(ControlState.PATH_FOLLOWING);
	}
	
	public synchronized void updatePose(double timestamp){
		double x = 0;
		double y = 0;
		Rotation2d heading = pigeon.getAngle();
		
		for(SwerveDriveModule m : modules){
			m.updatePose(heading);
			x += m.getEstimatedRobotPose().getTranslation().x();
			y += m.getEstimatedRobotPose().getTranslation().y();
		}
		RigidTransform2d updatedPose = new RigidTransform2d(new Translation2d(x / modules.size(), y / modules.size()), heading);
		double deltaPos = updatedPose.getTranslation().translateBy(pose.getTranslation().inverse()).norm();
		distanceTraveled += deltaPos;
		currentVelocity = deltaPos / (timestamp - lastUpdateTimestamp);
		pose = updatedPose;
	}
	
	public synchronized void updateControlCycle(double timestamp){
		double rotationCorrection = headingController.updateRotationCorrection(pose.getRotation().getUnboundedDegrees(), timestamp);
		switch(currentState){
		case MANUAL:
			if(xInput == 0 && yInput == 0 && rotationalInput == 0){
				stop();
			}else{
				kinematics.calculate(xInput, yInput, rotationalInput + rotationCorrection);
				for(int i=0; i<modules.size(); i++){
		    		if(Util.shouldReverse(kinematics.wheelAngles[i], modules.get(i).getModuleAngle().getDegrees())){
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
		case PATH_FOLLOWING:
			int lookaheadPointIndex = currentPathSegment + currentPath.getLookaheadPoints();
			if(lookaheadPointIndex >= currentPathTrajectory.length())
				lookaheadPointIndex = currentPathTrajectory.length() - 1;
			Segment lookaheadPoint = currentPathTrajectory.get(lookaheadPointIndex);
			Translation2d lookaheadPosition = new Translation2d(lookaheadPoint.x, lookaheadPoint.y);
			Rotation2d angleToLookahead = lookaheadPosition.translateBy(pose.getTranslation().inverse()).direction();
			//angleToLookahead = Rotation2d.fromRadians(pathFollower.getHeading());
			
			if(pathFollower.isFinished()){
				double error = currentPath.getFinalPosition().translateBy(pose.getTranslation().inverse()).norm();
				System.out.println(error);
				//if(Math.abs(angleToLookahead.rotateBy(lastSteeringDirection.inverse()).getDegrees()) > 90.0){
				if(error <= (1.0/12.0)){
					hasFinishedPath = true;
					setState(ControlState.NEUTRAL);
					return;
				}
				pathMotorOutput = currentPath.runPID(error);
			}else{
				pathMotorOutput = pathFollower.calculate(distanceTraveled);
			}
			
			double x = angleToLookahead.sin();
		    double y = angleToLookahead.cos();
		    double tmp = (y * pose.getRotation().cos()) + (x * pose.getRotation().sin());
			xInput = (-y * pose.getRotation().sin()) + (x * pose.getRotation().cos());
			yInput = tmp;	
		    kinematics.calculate(xInput, yInput, rotationCorrection);
			modules.forEach((m) -> m.setModuleAngle(kinematics.wheelAngles[m.moduleID]));
			modules.forEach((m) -> m.setDriveOpenLoop(pathMotorOutput));
			lastSteeringDirection = angleToLookahead;
			currentPathSegment++;
			break;
		case NEUTRAL:
			stop();
			break;
		}
	}
	
	private final Loop loop = new Loop(){

		@Override
		public void onStart(double timestamp) {
			synchronized(Swerve.this){
				xInput = 0;
				yInput = 0;
				rotationalInput = 0;
				headingController.temporarilyDisable();
				stop();
				lastUpdateTimestamp = timestamp;
			}
		}

		@Override
		public void onLoop(double timestamp) {
			synchronized(Swerve.this){
				updatePose(timestamp);
				updateControlCycle(timestamp);
				lastUpdateTimestamp = timestamp;
			}
		}

		@Override
		public void onStop(double timestamp) {
			synchronized(Swerve.this){
				xInput = 0;
				yInput = 0;
				rotationalInput = 0;
				stop();
			}
		}
		
	};
	
	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		enabledLooper.register(loop);
	}

	@Override
	public synchronized void stop() {
		setState(ControlState.NEUTRAL);
		modules.forEach((m) -> m.stop());
	}

	@Override
	public synchronized void zeroSensors() {
		zeroSensors(new RigidTransform2d());
	}
	
	public synchronized void zeroSensors(RigidTransform2d startingPose){
		pigeon.setAngle(startingPose.getRotation().getUnboundedDegrees());
		modules.forEach((m) -> m.zeroSensors(startingPose));
		pose = startingPose;
		distanceTraveled = 0;
	}

	@Override
	public void outputToSmartDashboard() {
		modules.forEach((m) -> m.outputToSmartDashboard());
		SmartDashboard.putNumber("Robot X", pose.getTranslation().x());
		SmartDashboard.putNumber("Robot Y", pose.getTranslation().y());
		SmartDashboard.putNumber("Robot Heading", pose.getRotation().getUnboundedDegrees());
		SmartDashboard.putString("Heading Controller", headingController.getState().toString());
		SmartDashboard.putNumber("Target Heading", headingController.getTargetHeading());
		SmartDashboard.putNumber("Distance Traveled", distanceTraveled);
		SmartDashboard.putNumber("Robot Velocity", currentVelocity);
	}
}
