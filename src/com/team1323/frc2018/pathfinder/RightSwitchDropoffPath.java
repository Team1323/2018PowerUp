package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightSwitchDropoffPath extends PathfinderPath{
	
	public RightSwitchDropoffPath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRobotHalfLength, Constants.kAutoStartingCorner.y() + Constants.kRobotHalfWidth,Pathfinder.d2r(50)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - 1.0, Constants.kRightSwitchCloseCorner.y() + Constants.kRobotHalfLength + 1.25, Pathfinder.d2r(-5.0)),
				new Waypoint(Constants.kRightSwitchFarCorner.x(), Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength + 0.25, 0),
				
				//new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.kCubeWidth + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.5, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH - (23.5/4.0/12.0) + 2.0, Pathfinder.d2r(-45.0)),
				//new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.kCubeWidth + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.5, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0) + (23.5/4.0/12.0) + 0.5, Pathfinder.d2r(-135.0)),
				
				//new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 0.5, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0), Pathfinder.d2r(180))
				//new Waypoint(Constants.kRightMostCube.x() + (1.4*Math.sqrt(3.0)), Constants.kRightMostCube.y() + 1.4, Pathfinder.d2r(-45.0)),//1.65
				new Waypoint(Constants.kRightSwitchFarCorner.x() + 2.0, Constants.kRightSwitchFarCorner.y() + Constants.kRobotHalfLength + 1.0, Pathfinder.d2r(15))
		};
		super.maxAccel = 3.25;
		super.maxSpeed = 8.0;
		super.lookaheadPoints = 20;
		super.defaultSpeed = 7.5;
		super.rotationScalar = 0.3;
	}
	
}
