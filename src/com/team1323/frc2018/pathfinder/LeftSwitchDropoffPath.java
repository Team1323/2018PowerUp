package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftSwitchDropoffPath extends PathfinderPath{
	
	public LeftSwitchDropoffPath(){
		super.points = new Waypoint[]{
			new Waypoint(Constants.kRobotHalfLength, Constants.kAutoStartingCorner.y() + Constants.kRobotHalfWidth,Pathfinder.d2r(-50)),
			new Waypoint(Constants.kLeftSwitchCloseCorner.x() - 1.0, Constants.kLeftSwitchCloseCorner.y() - Constants.kRobotHalfLength - 1.0/*0.5*/, Pathfinder.d2r(5.0)),
			new Waypoint(Constants.kLeftSwitchFarCorner.x(), Constants.kLeftSwitchFarCorner.y() - Constants.kRobotHalfLength, 0),
			
			//new Waypoint(Constants.kLeftSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH, Constants.kLeftSwitchFarCorner.y() - Constants.ROBOT_HALF_LENGTH - 0.5, 0),
			//new Waypoint(Constants.kLeftSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 2.0, Constants.kLeftSwitchFarCorner.y() + (Constants.kCubeWidth/2.0) + 0.25, Pathfinder.d2r(90))
			
			new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.0, Constants.kLeftSwitchFarCorner.y() - Constants.kRobotHalfLength - 0.2, Pathfinder.d2r(-15.0))
		};
		super.maxAccel = 3.25;
		super.maxSpeed = 8.0;
		super.dt = 0.02;
		super.lookaheadPoints = 20;
		super.defaultSpeed = 7.5;
		super.rotationScalar = 0.3;
	}
	
}
