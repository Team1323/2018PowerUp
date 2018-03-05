package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class LeftmostCubePickupPath extends PathfinderPath{

	public LeftmostCubePickupPath(){
		super.points = new Waypoint[]{
				//new Waypoint(Constants.kLeftMostCube.x() + (1.4*Math.sqrt(3.0)), Constants.kLeftMostCube.y() - 1.4, Pathfinder.d2r(150.0)),//1.65
				//new Waypoint(Constants.kLeftMostCube.x(), Constants.kLeftMostCube.y(), Pathfinder.d2r(150.0))
				//new Waypoint(Constants.kLeftSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 2.0, Constants.kLeftSwitchFarCorner.y() + (Constants.kCubeWidth/2.0) + 0.25, Pathfinder.d2r(180)),
				//new Waypoint(Constants.kLeftSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.0, Constants.kLeftSwitchFarCorner.y() + (Constants.kCubeWidth/2.0) + 0.25, Pathfinder.d2r(180))
				new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.7, Constants.kLeftSwitchFarCorner.y() - Constants.ROBOT_HALF_LENGTH + 0.5, Pathfinder.d2r(90)),
				new Waypoint(Constants.kLeftSwitchFarCorner.x() + 3.7, Constants.kLeftSwitchFarCorner.y() + 0.4, Pathfinder.d2r(90))
			};
			super.defaultSpeed = 4.0;
			super.maxSpeed = 4.0;
			super.maxAccel = 10.0;
			super.rotationScalar = 2.0;
			super.lookaheadPoints = 20;
	}
	
}
