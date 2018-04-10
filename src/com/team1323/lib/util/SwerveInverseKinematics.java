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
			directions.add(moduleRelativePositions.get(i).rotateBy(Rotation2d.fromDegrees(90)));
		}
		return directions;
	}
	
	public void setCenterOfRotation(Translation2d center){
		List<Translation2d> positions = new ArrayList<>(kNumberOfModules);
		double maxMagnitude = 0.0;
		for(int i = 0; i < kNumberOfModules; i++){
			Translation2d position = Constants.kModulePositions.get(i).translateBy(center.inverse());
			positions.add(position);
			double magnitude = position.norm();
			if(magnitude > maxMagnitude){
				maxMagnitude = magnitude;
			}
		}
		for(int i = 0; i < kNumberOfModules; i++){
			Translation2d position = positions.get(i);
			positions.set(i, position.scale(1.0/maxMagnitude));
		}
		moduleRelativePositions = positions;
		moduleRotationDirections = updateRotationDirections();
	}
	
	public List<Translation2d> updateDriveVectors(Translation2d translationalVector, double rotationalMagnitude, RigidTransform2d robotPose){
		translationalVector = translationalVector.rotateBy(robotPose.getRotation().inverse());
		List<Translation2d> driveVectors = new ArrayList<>(kNumberOfModules);
		for(int i = 0; i < kNumberOfModules; i++){
			driveVectors.add(translationalVector.translateBy(moduleRotationDirections.get(i).scale(rotationalMagnitude)));
		}
		double maxMagnitude = 1.0;
		for(Translation2d t : driveVectors){
			double magnitude = t.norm();
			if(magnitude > maxMagnitude){
				maxMagnitude = magnitude;
			}
		}
		for(int i = 0; i < kNumberOfModules; i++){
			Translation2d driveVector = driveVectors.get(i);
			driveVectors.set(i, driveVector.scale(1.0/maxMagnitude));
		}
		return driveVectors;
	}
	
}
