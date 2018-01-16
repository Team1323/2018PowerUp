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
	double rotationalOffset;
	double rotationSetpoint = 0;
	Pigeon pigeon;
	
	private double previousEncDistance = 0;
	private RigidTransform2d pose;
	private RigidTransform2d previousPose;
	private RigidTransform2d startingPose;
	
	public SwerveDriveModule(int rotationSlot, int driveSlot, int moduleID, 
			double rotationalOffset, RigidTransform2d startingPose){
		rotationMotor = new TalonSRX(rotationSlot);
		driveMotor = new TalonSRX(driveSlot);
		configureMotors();
		this.moduleID = moduleID;
		name += (moduleID + " ");
		this.rotationalOffset = rotationalOffset;
		pigeon = Pigeon.getInstance();
		previousEncDistance = 0;
		pose = startingPose;
		previousPose = startingPose;
		this.startingPose = startingPose;
	}
	
	public void invertDriveMotor(boolean invert){
		driveMotor.setInverted(invert);
	}
	
	public void invertRotationMotor(boolean invert){
		rotationMotor.setInverted(invert);
	}
	
	public void reverseDriveSensor(boolean reverse){
		driveMotor.setSensorPhase(reverse);
	}
	
	public void reverseRotationSensor(boolean reverse){
		rotationMotor.setSensorPhase(reverse);
	}
	
	private void configureMotors(){
    	rotationMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, 0, 10);
    	int absolutePosition = rotationMotor.getSensorCollection().getAnalogInRaw();
    	rotationMotor.setSelectedSensorPosition(absolutePosition, 0, 10);
    	rotationMotor.setSensorPhase(false);
    	rotationMotor.setInverted(false);
    	rotationMotor.configNominalOutputForward(0.0, 10);
    	rotationMotor.configNominalOutputReverse(0.0, 10);
    	rotationMotor.configPeakOutputForward(7.0/12.0, 10);
    	rotationMotor.configPeakOutputReverse(-7.0/12.0, 10);
    	rotationMotor.configAllowableClosedloopError(0, 0, 10);
    	rotationMotor.configMotionAcceleration(63070*10, 10);
    	rotationMotor.configMotionCruiseVelocity(63070, 10);
    	rotationMotor.selectProfileSlot(0, 0);
    	rotationMotor.config_kP(0, 5.0, 10);
    	rotationMotor.config_kI(0, 0.0, 10);
    	rotationMotor.config_kD(0, 160.0, 10);
    	rotationMotor.config_kF(0, 1.705, 10);
    	rotationMotor.set(ControlMode.MotionMagic, rotationMotor.getSelectedSensorPosition(0));
    	driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
    	driveMotor.setSelectedSensorPosition(0, 0, 10);
    	driveMotor.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, 10);
    	driveMotor.configNominalOutputForward(0.0, 10);
    	driveMotor.configNominalOutputReverse(0.0, 10);
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
	
	public double getModuleAngle(){
		return Util.boundAngle0to360Degrees(getRawAngle() - rotationalOffset);
	}
	
	public Rotation2d getFieldCentricAngle(){
		return Rotation2d.fromDegrees(getModuleAngle() + pigeon.getAngle());
	}
	
	public void setModuleAngle(double goalAngle){
		double newAngle = Util.placeInAppropriate0To360Scope(getRawAngle(), goalAngle + rotationalOffset);
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
		return (int) (degrees/360.0*1024.0);
	}
	
	public double encUnitsToDegrees(int encUnits){
		return encUnits/1024.0*360.0;
	}
	
	public RigidTransform2d getPose(){
		return pose;
	}
	
	public void updatePose(){
		double currentEncDistance = getDriveDistanceInches();
		double deltaEncDistance = currentEncDistance - previousEncDistance;
		Rotation2d angle = getFieldCentricAngle();
		RigidTransform2d deltaPose = new RigidTransform2d(new Translation2d(angle.cos()*deltaEncDistance, 
				angle.sin()*deltaEncDistance), new Rotation2d());
		RigidTransform2d updatedPose = pose.transformBy(deltaPose);
		pose = updatedPose;
		previousEncDistance = currentEncDistance;
	}
	
	public void resetPose(double robotHeading){
		RigidTransform2d robotPose = new RigidTransform2d(new Translation2d(), Rotation2d.fromDegrees(robotHeading));
		RigidTransform2d modulePose = robotPose.transformBy(startingPose);
		pose = modulePose;
	}
	
	public void resetPose(){
		pose = startingPose;
	}
	
	@Override
	public synchronized void stop(){
		setRotationOpenLoop(0.0);
		setDriveOpenLoop(0.0);
	}

	@Override
	public void zeroSensors() {
		driveMotor.setSelectedSensorPosition(0, 0, 0);
		resetPose();
	}

	@Override
	public void registerEnabledLoops(Looper enabledLooper) {
		
	}

	@Override
	public void outputToSmartDashboard() {
		SmartDashboard.putNumber(name + "Angle", getModuleAngle());
		SmartDashboard.putNumber(name + "Drive Voltage", driveMotor.getMotorOutputVoltage());
	}
	
	
}
