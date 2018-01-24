package com.team1323.frc2018.pathfinder;

import java.util.Arrays;
import java.util.List;

public class PathManager {
	public static RightSwitchDropoffPath mRightSwitchDropoff = new RightSwitchDropoffPath();
	public static LeftSwitchDropoffPath mLeftSwitchDropoff = new LeftSwitchDropoffPath();
	public static RightCubeToRightScalePath mRightCubeToRightScale = new RightCubeToRightScalePath();
	public static RightScaleToFirstCubePath mRightScaleToFirstCube = new RightScaleToFirstCubePath();
	public static RightScaleToSecondCubePath mRightScaleToSecondCube = new RightScaleToSecondCubePath();
	public static RightCubeToLeftScalePath mRightCubeToLeftScale = new RightCubeToLeftScalePath();
	
	//public static TestPath mTestPath = new TestPath();
	
	public static List<PathfinderPath> paths = Arrays.asList(mRightSwitchDropoff, mLeftSwitchDropoff, 
			mRightCubeToRightScale, mRightScaleToFirstCube, mRightScaleToSecondCube, mRightCubeToLeftScale);
	
	public static void buildAllPaths(){
		paths.forEach((p) -> p.buildPath());
	}
	
}
