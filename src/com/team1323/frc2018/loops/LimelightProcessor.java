package com.team1323.frc2018.loops;

import java.util.ArrayList;
import java.util.List;

import com.team1323.frc2018.RobotState;
import com.team1323.frc2018.vision.TargetInfo;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LimelightProcessor implements Loop{
	static LimelightProcessor instance = new LimelightProcessor();
	edu.wpi.first.networktables.NetworkTable table;
	RobotState robotState = RobotState.getInstance();
	
	public static LimelightProcessor getInstance(){
		return instance;
	}
	
	public LimelightProcessor(){
	}
	
	@Override 
	public void onStart(double timestamp){
		table = NetworkTableInstance.getDefault().getTable("limelight");
		NetworkTableEntry ledMode = table.getEntry("ledMode");
		ledMode.setNumber(0);
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		pipeline.setNumber(3);
	}
	
	@Override 
	public void onLoop(double timestamp){
		NetworkTableEntry pipeline = table.getEntry("pipeline");
		pipeline.setNumber(3);
		NetworkTableEntry tx = table.getEntry("tx");
		NetworkTableEntry ty = table.getEntry("ty");
		NetworkTableEntry ta = table.getEntry("ta");
		NetworkTableEntry tv = table.getEntry("tv");
		double targetOffsetAngle_Horizontal = tx.getDouble(0);
		double targetOffsetAngle_Vertical = ty.getDouble(0);
		double targetArea = ta.getDouble(0);
		boolean targetInSight = (tv.getDouble(0) == 1.0) ? true : false;
		List<TargetInfo> targets = new ArrayList<TargetInfo>(1);
		if(targetInSight){
			targets.add(new TargetInfo(Math.tan(Math.toRadians(targetOffsetAngle_Horizontal)), Math.tan(Math.toRadians(targetOffsetAngle_Vertical))));
		}
		robotState.addVisionUpdate(timestamp, targets);
		robotState.setAngleToCube(targetOffsetAngle_Horizontal);
		SmartDashboard.putNumber("Limelight Angle", targetOffsetAngle_Horizontal);
	}
	
	@Override
	public void onStop(double timestamp){
		
	}
}
