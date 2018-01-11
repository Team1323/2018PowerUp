package com.team1323.frc2018.pathfinder;

import java.util.Arrays;
import java.util.List;

public class PathManager {
	public static RightSwitchDropoffPath mRightSwitchDropoff = new RightSwitchDropoffPath();
	public static LeftSwitchDropoffPath mLeftSwitchDropoff = new LeftSwitchDropoffPath();
	
	public static List<PathfinderPath> paths = Arrays.asList(mRightSwitchDropoff, mLeftSwitchDropoff);
	
	public static void buildAllPaths(){
		paths.forEach((p) -> p.buildPath());
	}
	
}
