package com.team1323.lib.util;

import java.util.ArrayList;
import java.util.List;

import com.team1323.frc2018.Constants;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

public class SwerveInverseKinematics {
	
	public SwerveInverseKinematics(){
		setCenterOfRotation(new Translation2d());
	}
	
	private final int kNumberOfModules = 4;
	
	private List<Translation2d> moduleRelativePositions = Constants.kModulePositions;
	private List<Translation2d> moduleRotationDirections = updateRotationDirections();
			
	private List<Translation2d> updateRotationDirections(){
		List<Translation2d> directions = new ArrayList<>(kNumberOfModules);
		for(int i = 0; i < kNumberOfModules; i++){
			directions.add(i, moduleRelativePositions.get(i).rotateBy(Rotation2d.fromDegrees(90)));
		}
		return directions;
	}
	
	public void setCenterOfRotation(Translation2d center){
		List<Translation2d> positions = new ArrayList<>(kNumberOfModules);
		for(int i = 0; i < kNumberOfModules; i++){
			positions.add(i, Constants.kModulePositions.get(i).translateBy(center.inverse()));
		}
		moduleRelativePositions = positions;
		moduleRotationDirections = updateRotationDirections();
	}
	
	public List<Translation2d> updateDriveVectors(Translation2d translationalVector, double rotationalMagnitude, RigidTransform2d robotPose){
		translationalVector = translationalVector.rotateBy(robotPose.getRotation().inverse());
		List<Translation2d> rotationalVectors = new ArrayList<>(kNumberOfModules);
		for(int i = 0; i < kNumberOfModules; i++){
			rotationalVectors.add(i, moduleRotationDirections.get(i).scale(rotationalMagnitude));
		}
		List<Translation2d> driveVectors = new ArrayList<>(4);
		for(int i = 0; i < kNumberOfModules; i++){
			driveVectors.add(i, translationalVector.translateBy(rotationalVectors.get(i)));
		}
		double maxMagnitude = driveVectors.get(0).norm();
		for(Translation2d t : driveVectors){
			double magnitude = t.norm();
			if(magnitude > maxMagnitude){
				maxMagnitude = magnitude;
			}
		}
		for(Translation2d t : driveVectors){
			t = t.scale(1.0/maxMagnitude);
		}
		return driveVectors;
	}
	
}
