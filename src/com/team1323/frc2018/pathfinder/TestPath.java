package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class TestPath extends PathfinderPath{
	
	public TestPath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.kRobotHalfLength, Constants.kAutoStartingCorner.y() + Constants.kRobotHalfWidth,Pathfinder.d2r(-50)),
				new Waypoint(Constants.kLeftSwitchCloseCorner.x() - 1.0, Constants.kLeftSwitchCloseCorner.y() - Constants.kRobotHalfLength - 0.25, Pathfinder.d2r(5.0)),
				new Waypoint(Constants.kLeftSwitchFarCorner.x(), Constants.kLeftSwitchFarCorner.y() - Constants.kRobotHalfLength + 0.5, 0),
				new Waypoint(Constants.kLeftSwitchFarCorner.x() + Constants.kRobotIntakeExtrusion + Constants.kRobotHalfLength, Constants.kLeftSwitchFarCorner.y() - Constants.kRobotHalfLength - 0.5, 0),
				new Waypoint(Constants.kLeftSwitchFarCorner.x() + Constants.kRobotIntakeExtrusion + Constants.kRobotHalfLength + 2.0, Constants.kLeftSwitchFarCorner.y() + (Constants.kCubeWidth/2.0) + 0.25, Pathfinder.d2r(90))
				
			};
			super.maxAccel = 2.0;
			super.maxSpeed = 8.0;
			super.dt = 0.02;
			super.lookaheadPoints = 10;
			super.defaultSpeed = 7.0;
			super.rotationScalar = 0.5;
	}
	
}
