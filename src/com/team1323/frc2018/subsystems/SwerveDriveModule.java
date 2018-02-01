package com.team1323.frc2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team1323.frc2018.Constants;
import com.team1323.frc2018.loops.Looper;
import com.team1323.lib.util.Util;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveDriveModule extends Subsystem{
	TalonSRX rotationMotor, driveMotor;
	int moduleID;
	String name = "Module ";
	Rotation2d rotationalOffset;
	double rotationSetpoint = 0;
	
	private double previousEncDistance = 0;
	private Translation2d position;
	private Translation2d startingPosition;
	private RigidTransform2d estimatedRobotPose = new RigidTransform2d();
	
	public SwerveDriveModule(int rotationSlot, int driveSlot, int moduleID, 
			Rotation2d rotationalOffset, Translation2d startingPose){
		rotationMotor = new TalonSRX(rotationSlot);
		driveMotor = new TalonSRX(driveSlot);
		configureMotors();
		this.moduleID = moduleID;
		name += (moduleID + " ");
		this.rotationalOffset = rotationalOffset;
		previousEncDistance = 0;
		position = startingPose;
		this.startingPosition = startingPose;
	}
	
	public synchronized void invertDriveMotor(boolean invert){
		driveMotor.setInverted(invert);
	}
	
	public synchronized void invertRotationMotor(boolean invert){
		rotationMotor.setInverted(invert);
	}
	
	public synchronized void reverseDriveSensor(boolean reverse){
		driveMotor.setSensorPhase(reverse);
	}
	
	public synchronized void reverseRotationSensor(boolean reverse){
		rotationMotor.setSensorPhase(reverse);
	}
	
	private void configureMotors(){
    	rotationMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    	resetRotationToAbsolute();
    	rotationMotor.setSensorPhase(true);
    	rotationMotor.setInverted(false);
    	rotationMotor.enableVoltageCompensation(true);
    	rotationMotor.configVoltageCompSaturation(7.0, 10);
    	rotationMotor.configNominalOutputForward(0.0, 10);
    	rotationMotor.configNominalOutputReverse(0.0, 10);
    	rotationMotor.configAllowableClosedloopError(0, 0, 10);
    	rotationMotor.configMotionAcceleration((int)(Constants.SWERVE_ROTATION_MAX_SPEED*10), 10);
    	rotationMotor.configMotionCruiseVelocity((int)(Constants.SWERVE_ROTATION_MAX_SPEED*0.8), 10);
    	rotationMotor.selectProfileSlot(0, 0);
    	rotationMotor.config_kP(0, 4.0, 10);
    	rotationMotor.config_kI(0, 0.0, 10);
    	rotationMotor.config_kD(0, 80.0, 10);
    	rotationMotor.config_kF(0, 1023.0/Constants.SWERVE_ROTATION_MAX_SPEED, 10);
    	rotationMotor.set(ControlMode.MotionMagic, rotationMotor.getSelectedSensorPosition(0));
    	driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    	driveMotor.setSelectedSensorPosition(0, 0, 10);
    	driveMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, 10);
    	driveMotor.configNominalOutputForward(0.0/12.0, 10);
    	driveMotor.configNominalOutputReverse(0.0/12.0, 10);
    	driveMotor.enableVoltageCompensation(true);
    	driveMotor.configOpenloopRamp(0, 10);
    	driveMotor.configAllowableClosedloopError(0, 0, 10);
    	driveMotor.setInverted(false);
    	driveMotor.setSensorPhase(false);
    	driveMotor.setNeutralMode(NeutralMode.Brake);
    	driveMotor.selectProfileSlot(0, 0);
    	driveMotor.config_kP(0, 0.5, 10);
    	driveMotor.config_kI(0, 0.0, 10);
    	driveMotor.config_kD(0, 80.0, 10);
    	driveMotor.config_kF(0, 0.10685189, 10);
    	driveMotor.configMotionCruiseVelocity(3200, 10);
    	driveMotor.configMotionAcceleration(4500, 10);
	}
	
	public double getRawAngle(){
		return encUnitsToDegrees(rotationMotor.getSelectedSensorPosition(0));
	}
	
	public Rotation2d getModuleAngle(){
		return Rotation2d.fromDegrees(getRawAngle()).rotateBy(rotationalOffset.inverse());
	}
	
	public Rotation2d getFieldCentricAngle(Rotation2d robotHeading){
		return getModuleAngle().rotateBy(robotHeading);
	}
	
	public void setModuleAngle(double goalAngle){
		double newAngle = Util.placeInAppropriate0To360Scope(getRawAngle(), goalAngle + rotationalOffset.getDegrees());
		rotationMotor.set(ControlMode.MotionMagic, degreesToEncUnits(newAngle));
		rotationSetpoint = degreesToEncUnits(newAngle);
	}
	
	public void setRotationOpenLoop(double power){
		rotationMotor.set(ControlMode.PercentOutput, power);
		rotationSetpoint = power;
	}
	
	public void setDriveOpenLoop(double power){
		driveMotor.set(ControlMode.PercentOutput, power);
	}
	
	public void setDrivePositionTarget(double deltaDistanceInches){
		driveMotor.set(ControlMode.MotionMagic, driveMotor.getSelectedSensorPosition(0) +
				inchesToEncUnits(deltaDistanceInches));
	}
	
	public double getDriveDistanceFeet(){
		return getDriveDistanceInches() / 12.0;
	}
	
	public double getDriveDistanceInches(){
		return encUnitsToInches(driveMotor.getSelectedSensorPosition(0));
	}
	
	public double encUnitsToInches(int encUnits){
		return encUnits/Constants.SWERVE_ENC_UNITS_PER_INCH;
	}
	
	public int inchesToEncUnits(double inches){
		return (int) (inches*Constants.SWERVE_ENC_UNITS_PER_INCH);
	}
	
	public int degreesToEncUnits(double degrees){
		return (int) (degrees/360.0*Constants.DRIVE_ENCODER_RESOLUTION);
	}
	
	public double encUnitsToDegrees(int encUnits){
		return encUnits/Constants.DRIVE_ENCODER_RESOLUTION*360.0;
	}
	
	public Translation2d getPosition(){
		return position;
	}
	
	public RigidTransform2d getEstimatedRobotPose(){
		return estimatedRobotPose;
	}
	
	public synchronized void updatePose(Rotation2d robotHeading){
		double currentEncDistance = getDriveDistanceFeet();
		double deltaEncDistance = currentEncDistance - previousEncDistance;
		Rotation2d angle = getFieldCentricAngle(robotHeading);
		Translation2d deltaPosition = new Translation2d(angle.cos()*deltaEncDistance, 
				angle.sin()*deltaEncDistance);
		Translation2d updatedPosition = position.translateBy(deltaPosition);
		RigidTransform2d staticWheelPose = new RigidTransform2d(updatedPosition, robotHeading);
		RigidTransform2d robotPose = staticWheelPose.transformBy(RigidTransform2d.fromTranslation(startingPosition).inverse());
		position = updatedPosition;
		estimatedRobotPose =  robotPose;
		previousEncDistance = currentEncDistance;
	}
	
	public synchronized void resetPose(RigidTransform2d robotPose){
		Translation2d modulePosition = robotPose.transformBy(RigidTransform2d.fromTranslation(startingPosition)).getTranslation();
		position = modulePosition;
	}
	
	public synchronized void resetPose(){
		position = startingPosition;
	}
	
	@Override
	public synchronized void stop(){
		setRotationOpenLoop(0.0);
		setDriveOpenLoop(0.0);
	}
	
	public synchronized void resetRotationToAbsolute(){
		rotationMotor.setSelectedSensorPosition(rotationMotor.getSensorCollection().getPulseWidthPosition(), 0, 10);
	}

	@Override
	public synchronized void zeroSensors() {
		zeroSensors(new RigidTransform2d());
	}
	
	public synchronized void zeroSensors(RigidTransform2d robotPose) {
		driveMotor.setSelectedSensorPosition(0, 0, 0);
		resetRotationToAbsolute();
		resetPose(robotPose);
		estimatedRobotPose = robotPose;
		previousEncDistance = 0;
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber(name + "Angle", getModuleAngle().getDegrees());
		SmartDashboard.putNumber(name + "Pulse Width", rotationMotor.getSensorCollection().getPulseWidthPosition());
		SmartDashboard.putNumber(name + "Drive Voltage", driveMotor.getMotorOutputVoltage());
		SmartDashboard.putNumber(name + "Inches Driven", getDriveDistanceInches());
		SmartDashboard.putNumber(name + "Rotation Voltage", rotationMotor.getMotorOutputVoltage());
		if(rotationMotor.getControlMode() == ControlMode.MotionMagic)
			SmartDashboard.putNumber(name + "Error", rotationMotor.getClosedLoopError(0));
	}
	
}
