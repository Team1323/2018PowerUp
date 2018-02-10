package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightSwitchDropoffPath extends PathfinderPath{
	
	public RightSwitchDropoffPath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH,Pathfinder.d2r(50)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x() - 1.0, Constants.kRightSwitchCloseCorner.y() + Constants.ROBOT_HALF_LENGTH + 0.5, Pathfinder.d2r(-5.0)),
				//new Waypoint(Constants.kRightSwitchCloseCorner.x() + 2.5, Constants.kRightSwitchCloseCorner.y() + Constants.ROBOT_HALF_LENGTH + 0.6, 0),
				new Waypoint(Constants.kRightSwitchFarCorner.x(), Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH + 0.0, 0),
				
				new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.kCubeWidth + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.0, Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH - (23.5/4.0/12.0) + 1.5, Pathfinder.d2r(-45.0)),
				new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.kCubeWidth + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.0, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0) + (23.5/4.0/12.0) + 0.5, Pathfinder.d2r(-135.0)),
				
				//new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.kCubeWidth + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 0.8, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0) + 0.5, Pathfinder.d2r(-45)),
				new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH + 1.0, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0) + 0.75, Pathfinder.d2r(180))
		};
		super.maxAccel = 10.0;
		super.maxSpeed = 6.0;
		super.dt = 0.02;
		super.lookaheadPoints = 6;
	}
	
}
