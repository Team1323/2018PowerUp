package com.team1323.frc2018;

import java.util.Map;

import com.team1323.lib.util.InterpolatingDouble;
import com.team1323.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.math.RigidTransform2d;

public class RobotState {
	private static RobotState instance = new RobotState();
	public static RobotState getInstance(){
		return instance;
	}
	
	private static final int kObservationBufferSize = 100;
	
	private InterpolatingTreeMap<InterpolatingDouble, RigidTransform2d> field_to_vehicle_;
	private double distance_driven_;
	
	private RobotState() {
        reset(0, new RigidTransform2d());
    }
	
	/**
     * Resets the field to robot transform (robot's position on the field)
     */
    public synchronized void reset(double start_time, RigidTransform2d initial_field_to_vehicle) {
        field_to_vehicle_ = new InterpolatingTreeMap<>(kObservationBufferSize);
        field_to_vehicle_.put(new InterpolatingDouble(start_time), initial_field_to_vehicle);
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
    
    public synchronized void addFieldToVehicleObservation(double timestamp, RigidTransform2d observation) {
        field_to_vehicle_.put(new InterpolatingDouble(timestamp), observation);
        updateOdometer(observation);
    }
    
    public synchronized void updateOdometer(RigidTransform2d current_pose){
    	
    }
    
    public synchronized double getDistanceDriven() {
        return distance_driven_;
    }
}
