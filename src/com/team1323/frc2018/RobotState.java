package com.team1323.frc2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.team1323.frc2018.subsystems.Elevator;
import com.team1323.frc2018.subsystems.Swerve;
import com.team1323.frc2018.vision.GoalTracker;
import com.team1323.frc2018.vision.GoalTracker.TrackReport;
import com.team1323.frc2018.vision.ShooterAimingParameters;
import com.team1323.frc2018.vision.TargetInfo;
import com.team1323.lib.util.InterpolatingDouble;
import com.team1323.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotState {
	private static RobotState instance = new RobotState();
	public static RobotState getInstance(){
		return instance;
	}
	
	private static final int kObservationBufferSize = 100;
	
	private InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> field_to_vehicle_;
	private GoalTracker goal_tracker_;
	private Rotation2d camera_pitch_correction_;
    private Rotation2d camera_yaw_correction_;
    private double differential_height_;
	private double distance_driven_;
	private ShooterAimingParameters cached_shooter_aiming_params_ = null;
	
	private double angleToCube = 0;
	public double getAngleToCube(){
		return angleToCube;
	}
	public void setAngleToCube(double angle){
		angleToCube = angle;
	}
	
	public Translation2d getCubePosition(){
		List<TrackReport> reports = goal_tracker_.getTracks();
		if(!reports.isEmpty())
			return goal_tracker_.getTracks().get(0).field_to_goal;
		else
			return new Translation2d();
	}
	
	private final Translation2d kCubePosition = Constants.kLeftSwitchFarCorner.translateBy(new Translation2d(Constants.kCubeWidth, Constants.kCubeWidth/2.0));
	
	private static final RigidTransform2d kVehicleToCamera = new RigidTransform2d(
            new Translation2d(Constants.kCameraXOffset, Constants.kCameraYOffset), new Rotation2d());
	
	private RobotState() {
        reset(0, new RigidTransform2d());
    }
	
	/**
     * Resets the field to robot transform (robot's position on the field)
     */
    public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle) {
        field_to_vehicle_ = new InterpolatingTreeMap<>(kObservationBufferSize);
        field_to_vehicle_.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
        goal_tracker_ = new GoalTracker();
        camera_pitch_correction_ = Rotation2d.fromDegrees(-Constants.kCameraPitchAngleDegrees);
        camera_yaw_correction_ = Rotation2d.fromDegrees(-Constants.kCameraYawAngleDegrees);
        differential_height_ = Constants.kTargetHeight - Constants.kCameraZOffset;
        distance_driven_ = 0.0;
    }
    
    public synchronized void resetDistanceDriven() {
        distance_driven_ = 0.0;
    }
    
    /**
     * Returns the robot's position on the field at a certain time. Linearly interpolates between stored robot positions
     * to fill in the gaps.
     */
    public synchronized RigidTransform2d getFieldToVehicle(double timestamp) {
        return field_to_vehicle_.getInterpolated(new InterpolatingDouble(timestamp));
    }

    public synchronized Map.Entry<InterpolatingDouble, RigidTransform2d> getLatestFieldToVehicle() {
        return field_to_vehicle_.lastEntry();
    }
    
    public synchronized RigidTransform2d getFieldToCamera(double timestamp) {
        return getFieldToVehicle(timestamp).transformBy(kVehicleToCamera);
    }
    
    public void addVisionUpdate(double timestamp, List<TargetInfo> vision_update) {
        List<Translation2d> field_to_goals = new ArrayList<>();
        RigidTransform2d field_to_camera = getFieldToCamera(timestamp);
        if (!(vision_update == null || vision_update.isEmpty())) {
            for (TargetInfo target : vision_update) {
                double ydeadband = target.getY();

                // Compensate for camera yaw
                double xyaw = target.getX() * camera_yaw_correction_.cos() + ydeadband * camera_yaw_correction_.sin();
                double yyaw = ydeadband * camera_yaw_correction_.cos() - target.getX() * camera_yaw_correction_.sin();
                double zyaw = target.getZ();

                // Compensate for camera pitch
                double xr = zyaw * camera_pitch_correction_.sin() + xyaw * camera_pitch_correction_.cos();
                double yr = yyaw;
                double zr = zyaw * camera_pitch_correction_.cos() - xyaw * camera_pitch_correction_.sin();
                
                // find intersection with the goal
                //if (zr < 0) {
                    double scaling = differential_height_ / zr;
                    double distance = Math.hypot(xr, yr) * scaling;
                    Rotation2d angle = new Rotation2d(xr, yr, true);
                    field_to_goals.add(field_to_camera
                            .transformBy(RigidTransform2d
                                    .fromTranslation(new Translation2d(distance * angle.cos(), distance * angle.sin())))
                            .getTranslation());
                //}
            }
        }
        synchronized (this) {
            goal_tracker_.update(timestamp, field_to_goals);
        }
    }
    
    public synchronized Optional<ShooterAimingParameters> getCachedAimingParameters() {
        return cached_shooter_aiming_params_ == null ? Optional.empty() : Optional.of(cached_shooter_aiming_params_);
    }
    
    public synchronized Optional<ShooterAimingParameters> getAimingParameters() {
        List<TrackReport> reports = goal_tracker_.getTracks();
        if (!reports.isEmpty()) {
            TrackReport report = reports.get(0);
            Translation2d robot_to_goal = getLatestFieldToVehicle().getValue().getTranslation().inverse()
                    .translateBy(report.field_to_goal);
            Rotation2d robot_to_goal_rotation = Rotation2d
                    .fromRadians(Math.atan2(robot_to_goal.y(), robot_to_goal.x()));

            ShooterAimingParameters params = new ShooterAimingParameters(robot_to_goal.norm(), robot_to_goal_rotation,
                    report.latest_timestamp, report.stability);
            cached_shooter_aiming_params_ = params;

            return Optional.of(params);
        } else {
            return Optional.empty();
        }
    }
    
    public synchronized void resetRobotPosition(Translation2d cubePosition){
    	List<TrackReport> reports = goal_tracker_.getTracks();
        if (!reports.isEmpty()) {
            TrackReport report = reports.get(0);
            Translation2d robotFrameToFieldFrame = report.field_to_goal.inverse().translateBy(cubePosition);
            if(robotFrameToFieldFrame.norm() <= 5.0){
            	Swerve.getInstance().resetPosition(new RigidTransform2d(Swerve.getInstance().getPose().getTranslation().translateBy(robotFrameToFieldFrame), Swerve.getInstance().getPose().getRotation()));
            	System.out.println("Coordinates corrected by " + robotFrameToFieldFrame.norm() + " feet");
            }else{
            	System.out.println("Coordinate correction too large: " + robotFrameToFieldFrame.norm());
            }
        }else{
        	System.out.println("Vision did not detect cube");
        }
    }
    
    public synchronized List<RigidTransform2d> getCaptureTimeFieldToGoal() {
        List<RigidTransform2d> rv = new ArrayList<>();
        for (TrackReport report : goal_tracker_.getTracks()) {
            rv.add(RigidTransform2d.fromTranslation(report.field_to_goal));
        }
        return rv;
    }
    
    public synchronized void addFieldToVehicleObservation(double timestamp, RigidTransform2d observation) {
        field_to_vehicle_.put(new InterpolatingDouble(timestamp), observation);
        updateOdometer(observation);
    }
    
    public synchronized void updateOdometer(RigidTransform2d current_pose){
    	
    }
    
    public synchronized double getDistanceDriven() {
        return distance_driven_;
    }
    
    public void outputToSmartDashboard(){
    	RigidTransform2d odometry = getLatestFieldToVehicle().getValue();
        //SmartDashboard.putNumber("robot_pose_x", odometry.getTranslation().x());
        //SmartDashboard.putNumber("robot_pose_y", odometry.getTranslation().y());
        //SmartDashboard.putNumber("robot_pose_theta", odometry.getRotation().getDegrees());
        List<RigidTransform2d> poses = getCaptureTimeFieldToGoal();
        for (RigidTransform2d pose : poses) {
            // Only output first goal
            SmartDashboard.putNumber("goal_pose_x", pose.getTranslation().x());
            SmartDashboard.putNumber("goal_pose_y", pose.getTranslation().y());
            break;
        }
        Optional<ShooterAimingParameters> aiming_params = getCachedAimingParameters();
        if (aiming_params.isPresent()) {
            //SmartDashboard.putNumber("goal_range", aiming_params.get().getRange());
            //SmartDashboard.putNumber("goal_theta", aiming_params.get().getRobotToGoal().getDegrees());
        } else {
            //SmartDashboard.putNumber("goal_range", 0.0);
            //SmartDashboard.putNumber("goal_theta", 0.0);
        }
    }
}
