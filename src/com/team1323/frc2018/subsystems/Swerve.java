package com.team1323.frc2018.subsystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.team1323.frc2018.Constants;
import com.team1323.frc2018.Ports;
import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.loops.Loop;
import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.pathfinder.PathfinderPath;
import com.team1323.lib.util.SwerveHeadingController;
import com.team1323.lib.util.SwerveInverseKinematics;
import com.team1323.lib.util.SwerveKinematics;
import com.team1323.lib.util.Util;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.AnalogInput;
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
	List<SwerveDriveModule> positionModules;
	
	AnalogInput ultrasonic;
	public double getUltrasonicReading(){
		return (((((ultrasonic.getVoltage() * 1000) - 293.0) / 4.88) * 5.0) + 300.0) * 0.0393701;
	}
	boolean ultraSensesWall = false;
	boolean robotXPassed = false;
	
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
	boolean shouldUsePathfinder = false;
	double previousPathfinderVelocity = 0.0;
	Rotation2d lastSteeringDirection;
	boolean hasFinishedPath = false;
	public boolean hasFinishedPath(){
		return hasFinishedPath;
	}
	
	private boolean trackCube = false;
	public void enableCubeTracking(boolean enable){
		trackCube = enable;
	}
	
	private Swerve(){
		frontRight = new SwerveDriveModule(Ports.FRONT_RIGHT_ROTATION, Ports.FRONT_RIGHT_DRIVE,
				0, Constants.kFrontRightEncoderStartingPos, Constants.kVehicleToModuleZero);
		frontLeft = new SwerveDriveModule(Ports.FRONT_LEFT_ROTATION, Ports.FRONT_LEFT_DRIVE,
				1, Constants.kFrontLeftEncoderStartingPos, Constants.kVehicleToModuleOne);
		rearLeft = new SwerveDriveModule(Ports.REAR_LEFT_ROTATION, Ports.REAR_LEFT_DRIVE,
				2, Constants.kRearLeftEncoderStartingPos, Constants.kVehicleToModuleTwo);
		rearRight = new SwerveDriveModule(Ports.REAR_RIGHT_ROTATION, Ports.REAR_RIGHT_DRIVE,
				3, Constants.kRearRightEncoderStartingPos, Constants.kVehicleToModuleThree);
		
		modules = Arrays.asList(frontRight, frontLeft, rearLeft, rearRight);
		positionModules = Arrays.asList(frontRight, frontLeft, rearRight);
		
		rearLeft.invertDriveMotor(true);
		frontLeft.invertDriveMotor(true);
		
		modules.forEach((m) -> m.reverseRotationSensor(true));
		
		ultrasonic = new AnalogInput(0);
		
		pigeon = Pigeon.getInstance();
		
		pose = new RigidTransform2d();
		distanceTraveled = 0;
	}
	
	private Translation2d translationalVector = new Translation2d();
	private double rotationalInput = 0;
	private Rotation2d lastActiveVector = new Rotation2d();
	private final Rotation2d rotationalVector = new Rotation2d();
	private double maxSpeedFactor = 1.0;
	public void setMaxSpeed(double max){
		maxSpeedFactor = max;
	}
	private boolean isInLowPower = false;
	
	private SwerveKinematics kinematics = new SwerveKinematics();
	private SwerveInverseKinematics inverseKinematics = new SwerveInverseKinematics();
	public void setCenterOfRotation(Translation2d center){
		inverseKinematics.setCenterOfRotation(center);
	}
	
	public enum ControlState{
		NEUTRAL, MANUAL, POSITION, PATH_FOLLOWING, ROTATION
	}
	private ControlState currentState = ControlState.NEUTRAL;
	public ControlState getState(){
		return currentState;
	}
	public void setState(ControlState newState){
		currentState = newState;
	}
	
	public void sendInput(double x, double y, double rotate, boolean robotCentric, boolean lowPower){
		Translation2d translationalInput = new Translation2d(x, y);
		double inputMagnitude = translationalInput.norm();
		double deadband = 0.15;
		translationalInput = (inputMagnitude < deadband) ? new Translation2d() : translationalInput;
		rotate = (Math.abs(rotate) < deadband) ? 0 : rotate;
		
		translationalInput = translationalInput.scale(maxSpeedFactor);
		rotate *= maxSpeedFactor;
				
		translationalVector = translationalInput;
		
		isInLowPower = lowPower;
		if(lowPower){
			translationalVector = translationalVector.scale(0.6);
			rotate *= 0.6;
		}else{
			rotate *= 0.8;
		}
		
		if(rotate != 0 && rotationalInput == 0){
			headingController.disable();
		}else if(rotate == 0 && rotationalInput != 0){
			headingController.temporarilyDisable();
		}
		
		rotationalInput = rotate;
		
		if((x != 0 || y != 0 || rotate != 0) && currentState != ControlState.MANUAL)
			setState(ControlState.MANUAL);
		
		if(inputMagnitude > 0.3)
			lastActiveVector = new Rotation2d(x, y, false);
		else if(translationalVector.x() == 0.0 && translationalVector.y() == 0.0 && rotate != 0.0){
			lastActiveVector = rotationalVector;
		}
		
		SmartDashboard.putNumber("Vector Direction", translationalVector.direction().getDegrees());
		SmartDashboard.putNumber("Vector Magnitude", translationalVector.norm());
	}
	
	public synchronized void rotate(double goalHeading){
		if(translationalVector.x() == 0 && translationalVector.y() == 0)
			rotateInPlace(goalHeading);
		else
			headingController.setStabilizationTarget(
					Util.placeInAppropriate0To360Scope(pose.getRotation().getUnboundedDegrees(), goalHeading));
	}
	
	public void rotateInPlace(double goalHeading){
		/*setState(ControlState.POSITION);
		kinematics.calculate(0, 0, 1);
		modules.forEach((m) -> m.setModuleAngle(kinematics.wheelAngles[m.moduleID]));
		Rotation2d deltaAngle = goalHeading.rotateBy(pose.getRotation().inverse());
		modules.forEach((m) -> m.setDrivePositionTarget(deltaAngle.getRadians()*(Constants.SWERVE_DIAGONAL/2)*12.0));*/
		setState(ControlState.ROTATION);
		headingController.setStationaryTarget(
				Util.placeInAppropriate0To360Scope(pose.getRotation().getUnboundedDegrees(), goalHeading));
	}
	
	public void rotateInPlaceAbsolutely(double absoluteHeading){
		setState(ControlState.ROTATION);
		headingController.setStationaryTarget(absoluteHeading);
	}
	
	public void setPathHeading(double goalHeading){
		headingController.setSnapTarget(
				Util.placeInAppropriate0To360Scope(
						pose.getRotation().getUnboundedDegrees(), goalHeading));
	}
	
	public void setAbsolutePathHeading(double absoluteHeading){
		headingController.setSnapTarget(absoluteHeading);
	}
	
	public void setPositionTarget(double directionDegrees, double magnitudeInches){
		setState(ControlState.POSITION);
		modules.forEach((m) -> m.setModuleAngle(directionDegrees));
		modules.forEach((m) -> m.setDrivePositionTarget(magnitudeInches));
	}
	
	public boolean positionOnTarget(){
		boolean onTarget = false;
		for(SwerveDriveModule m : modules){
			onTarget |= m.drivePositionOnTarget();
		}
		return onTarget;
	}
	
	public synchronized void followPath(PathfinderPath path, double goalHeading){
		hasFinishedPath = false;
		shouldUsePathfinder = false;
		distanceTraveled = 0;
		currentPathSegment = 0;
		currentPath = path;
		pathFollower = path.resetFollower();
		currentPathTrajectory = path.getTrajectory();
		headingController.setSnapTarget(goalHeading);
		setState(ControlState.PATH_FOLLOWING);
		
		enableCubeTracking(false);
	}
	
	public synchronized void updatePose(double timestamp){
		double x = 0;
		double y = 0;
		Rotation2d heading = pigeon.getAngle();
		
		double averageDistance = 0.0;
		double[] distances = new double[4];
		for(SwerveDriveModule m : positionModules){
			m.updatePose(heading);
			double distance = m.getEstimatedRobotPose().getTranslation().translateBy(pose.getTranslation().inverse()).norm();
			distances[m.moduleID] = distance;
			averageDistance += distance;
		}
		averageDistance /= positionModules.size();
		
		int minDevianceIndex = 0;
		double minDeviance = 100.0;
		List<SwerveDriveModule> modulesToUse = new ArrayList<>();
		for(SwerveDriveModule m : positionModules){
				double deviance = Math.abs(distances[m.moduleID] - averageDistance);
				if(deviance < minDeviance){
					minDeviance = deviance;
					minDevianceIndex = m.moduleID;
				}
				if(deviance <= 0.01){
					modulesToUse.add(m);
				}
			}
		
		if(modulesToUse.isEmpty()){
			modulesToUse.add(modules.get(minDevianceIndex));
		}
		
		SmartDashboard.putNumber("Modules Used", modulesToUse.size());
		
		for(SwerveDriveModule m : modulesToUse){
			//if(m.moduleID != 0 && m.moduleID != 2 && m.moduleID != 1){
			//m.updatePose(heading);
			x += m.getEstimatedRobotPose().getTranslation().x();
			y += m.getEstimatedRobotPose().getTranslation().y();
			//}
		}
		
		/*for(SwerveDriveModule m : modules){
			if(m.moduleID != 0 && m.moduleID != 2 && m.moduleID != 1){
				m.updatePose(heading);
				x += m.getEstimatedRobotPose().getTranslation().x();
				y += m.getEstimatedRobotPose().getTranslation().y();
			}
		}*/
		RigidTransform2d updatedPose = new RigidTransform2d(new Translation2d(x / modulesToUse.size(), y / modulesToUse.size()), heading);
		//RigidTransform2d updatedPose = new RigidTransform2d(new Translation2d(x / 1.0, y / 1.0), heading);
		double deltaPos = updatedPose.getTranslation().translateBy(pose.getTranslation().inverse()).norm();
		distanceTraveled += deltaPos;
		currentVelocity = deltaPos / (timestamp - lastUpdateTimestamp);
		pose = updatedPose;
		modules.forEach((m) -> m.resetPose(pose));
	}
	
	public synchronized void updateControlCycle(double timestamp){
		if(trackCube && RobotState.getInstance().getAimingParameters().isPresent()){
			headingController.setSnapTarget(Util.placeInAppropriate0To360Scope(pose.getRotation().getUnboundedDegrees(), RobotState.getInstance().getAimingParameters().get().getRobotToGoal().getDegrees()));
			if(RobotState.getInstance().getAimingParameters().isPresent()){
				//System.out.println("Angle To Cube: " + RobotState.getInstance().getAimingParameters().get().getRobotToGoal().getDegrees());
			}
		}
		double rotationCorrection = headingController.updateRotationCorrection(pose.getRotation().getUnboundedDegrees(), timestamp);
		//rotationCorrection = 0.0;
		switch(currentState){
		case MANUAL:
			if(translationalVector.x() == 0.0 && translationalVector.y() == 0.0 && rotationalInput == 0.0){
				if(lastActiveVector.equals(rotationalVector)){
					stop();
				}else{
					List<Translation2d> driveVectors = inverseKinematics.updateDriveVectors(lastActiveVector.toTranslation(),
							rotationCorrection, pose);
					for(int i=0; i<modules.size(); i++){
			    		if(Util.shouldReverse(driveVectors.get(i).direction().getDegrees(), modules.get(i).getModuleAngle().getDegrees())){
			    			modules.get(i).setModuleAngle(driveVectors.get(i).direction().getDegrees() + 180.0);
			    			modules.get(i).setDriveOpenLoop(0);
			    		}else{
			    			modules.get(i).setModuleAngle(driveVectors.get(i).direction().getDegrees());
			    			modules.get(i).setDriveOpenLoop(0);
			    		}
			    	}
				}
			}else{
				List<Translation2d> driveVectors = inverseKinematics.updateDriveVectors(translationalVector,
						rotationalInput + rotationCorrection, pose);
				for(int i=0; i<modules.size(); i++){
		    		if(Util.shouldReverse(driveVectors.get(i).direction().getDegrees(), modules.get(i).getModuleAngle().getDegrees())){
		    			modules.get(i).setModuleAngle(driveVectors.get(i).direction().getDegrees() + 180.0);
		    			modules.get(i).setDriveOpenLoop(-driveVectors.get(i).norm());
		    		}else{
		    			modules.get(i).setModuleAngle(driveVectors.get(i).direction().getDegrees());
		    			modules.get(i).setDriveOpenLoop(driveVectors.get(i).norm());
		    		}
		    	}
			}
			break;
		case POSITION:
			if(positionOnTarget())
				rotate(headingController.getTargetHeading());
			break;
		case ROTATION:
			kinematics.calculate(0.0, 0.0, rotationCorrection);
			for(int i=0; i<modules.size(); i++){
	    		if(Util.shouldReverse(kinematics.wheelAngles[i], modules.get(i).getModuleAngle().getDegrees())){
	    			modules.get(i).setModuleAngle(kinematics.wheelAngles[i] + 180.0);
	    			modules.get(i).setDriveOpenLoop(-kinematics.wheelSpeeds[i]);
	    		}else{
	    			modules.get(i).setModuleAngle(kinematics.wheelAngles[i]);
	    			modules.get(i).setDriveOpenLoop(kinematics.wheelSpeeds[i]);
	    		}
	    	}
			break;
		case PATH_FOLLOWING:
			currentPathSegment = currentPath.getClosestSegmentIndex(pose, currentPathSegment);
			int lookaheadPointIndex = currentPathSegment + currentPath.getLookaheadPoints();
			if(lookaheadPointIndex >= currentPathTrajectory.length())
				lookaheadPointIndex = currentPathTrajectory.length() - 1;
			Segment lookaheadPoint = currentPathTrajectory.get(lookaheadPointIndex);
			Translation2d lookaheadPosition = new Translation2d(lookaheadPoint.x, lookaheadPoint.y);
			Rotation2d angleToLookahead = lookaheadPosition.translateBy(pose.getTranslation().inverse()).direction();
			if(currentPathSegment >= (currentPathTrajectory.length() - 1)){
				double error = currentPath.getFinalPosition().translateBy(pose.getTranslation().inverse()).norm();
				if(error <= (3.0/12.0) || !currentPath.shouldUsePID()){
					hasFinishedPath = true;
					setState(ControlState.NEUTRAL);
					break;
				}
				pathMotorOutput = currentPath.runPID(error);
			}else{
				double velocity = currentPathTrajectory.get(currentPathSegment).velocity;
				if((velocity >= currentPath.defaultSpeed()) /*|| (velocity < previousPathfinderVelocity)*/){
					shouldUsePathfinder = true;
				}
				if(shouldUsePathfinder)
					pathMotorOutput = currentPathTrajectory.get(currentPathSegment).velocity / 12.5;
				else
					pathMotorOutput = currentPath.defaultSpeed() / 12.5;
			}
			if(!currentPath.rotationOverride())
				rotationCorrection = rotationCorrection*pathMotorOutput*currentPath.rotationScalar();
		    List<Translation2d> driveVectors = inverseKinematics.updateDriveVectors(angleToLookahead.toTranslation().scale(((pathMotorOutput == 0) ? 1 : pathMotorOutput)),
		    		rotationCorrection, pose);
			for(int i=0; i<modules.size(); i++){
	    		if(Util.shouldReverse(driveVectors.get(i).direction().getDegrees(), modules.get(i).getModuleAngle().getDegrees())){
	    			modules.get(i).setModuleAngle(driveVectors.get(i).direction().getDegrees() + 180.0);
	    			modules.get(i).setDriveOpenLoop(-driveVectors.get(i).norm());
	    		}else{
	    			modules.get(i).setModuleAngle(driveVectors.get(i).direction().getDegrees());
	    			modules.get(i).setDriveOpenLoop(driveVectors.get(i).norm());
	    		}
	    	}
			/*double x = angleToLookahead.sin();
		    double y = angleToLookahead.cos();
		    double tmp = (y * pose.getRotation().cos()) + (x * pose.getRotation().sin());
			double xInput = (-y * pose.getRotation().sin()) + (x * pose.getRotation().cos());
			double yInput = tmp;
			if(!currentPath.rotationOverride())
				rotationCorrection = rotationCorrection*pathMotorOutput*currentPath.rotationScalar();
		    kinematics.calculate(xInput * pathMotorOutput, yInput * pathMotorOutput, rotationCorrection);
		    for(int i=0; i<modules.size(); i++){
	    		if(Util.shouldReverse(kinematics.wheelAngles[i], modules.get(i).getModuleAngle().getDegrees())){
	    			modules.get(i).setModuleAngle(kinematics.wheelAngles[i] + 180);
	    			modules.get(i).setDriveOpenLoop(-kinematics.wheelSpeeds[i]);
	    		}else{
	    			modules.get(i).setModuleAngle(kinematics.wheelAngles[i]);
	    			modules.get(i).setDriveOpenLoop(kinematics.wheelSpeeds[i]);
	    		}
	    	}*/
			SmartDashboard.putNumber("Vector Direction", angleToLookahead.getDegrees());
			SmartDashboard.putNumber("Vector Magnitude", 1.0);
			lastSteeringDirection = angleToLookahead;
			//System.out.println("Steering direction: " + angleToLookahead.getDegrees());
			//System.out.println("Module 0 direction: " + driveVectors.get(0).direction().getDegrees());
			previousPathfinderVelocity = currentPathTrajectory.get(currentPathSegment).velocity;
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
				translationalVector = new Translation2d();
				lastActiveVector = rotationalVector;
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
				translationalVector = new Translation2d();
				rotationalInput = 0;
				stop();
			}
		}
		
	};
	
	public void setNominalDriveOutput(double voltage){
		modules.forEach((m) -> m.setNominalDriveOutput(voltage));
	}
	
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
		zeroSensors(Constants.kRobotStartingPose);
	}
	
	public synchronized void zeroSensors(RigidTransform2d startingPose){
		pigeon.setAngle(startingPose.getRotation().getUnboundedDegrees());
		modules.forEach((m) -> m.zeroSensors(startingPose));
		pose = startingPose;
		distanceTraveled = 0;
	}
	
	public synchronized void resetPosition(RigidTransform2d newPose){
		modules.forEach((m) -> m.zeroSensors(newPose));
		pose = newPose;
		distanceTraveled = 0;
	}

	@Override
	public void outputToSmartDashboard() {
		modules.forEach((m) -> m.outputToSmartDashboard());
		SmartDashboard.putNumber("Robot X", pose.getTranslation().x());
		SmartDashboard.putNumber("Robot Y", pose.getTranslation().y());
		SmartDashboard.putNumber("Robot Heading", pose.getRotation().getUnboundedDegrees());
		//SmartDashboard.putString("Heading Controller", headingController.getState().toString());
		SmartDashboard.putNumber("Target Heading", headingController.getTargetHeading());
		//SmartDashboard.putNumber("Distance Traveled", distanceTraveled);
		//SmartDashboard.putNumber("Robot Velocity", currentVelocity);
		SmartDashboard.putString("Swerve State", currentState.toString());
		//SmartDashboard.putNumber("Swerve Ultrasonic", getUltrasonicReading());
	}
}
