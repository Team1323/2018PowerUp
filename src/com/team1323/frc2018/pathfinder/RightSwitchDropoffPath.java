package com.team1323.frc2018.pathfinder;

import com.team1323.frc2018.Constants;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

public class RightSwitchDropoffPath extends PathfinderPath{
	
	public RightSwitchDropoffPath(){
		super.points = new Waypoint[]{
				new Waypoint(Constants.ROBOT_HALF_LENGTH, Constants.kAutoStartingCorner.y() + Constants.ROBOT_HALF_WIDTH,Pathfinder.d2r(50)),
				new Waypoint(Constants.kRightSwitchCloseCorner.x(), Constants.kRightSwitchCloseCorner.y() + Constants.ROBOT_HALF_LENGTH, 0),
				new Waypoint(Constants.kRightSwitchFarCorner.x(), Constants.kRightSwitchFarCorner.y() + Constants.ROBOT_HALF_LENGTH, 0),
				new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.kCubeWidth + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0), Pathfinder.d2r(-45)),
				//new Waypoint(20.25, 20.49475, Pathfinder.d2r(-45)),
				//new Waypoint(20.25, 19.73425, Pathfinder.d2r(-135)),
				new Waypoint(Constants.kRightSwitchFarCorner.x() + Constants.ROBOT_INTAKE_EXTRUSION + Constants.ROBOT_HALF_LENGTH, Constants.kRightSwitchFarCorner.y() - (Constants.kCubeWidth/2.0), Pathfinder.d2r(180))
		};
		super.maxAccel = 10.0;
		super.dt = 0.015;
		super.lookaheadPoints = 3;
	}
	
}
