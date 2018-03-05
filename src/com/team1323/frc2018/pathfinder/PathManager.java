package com.team1323.frc2018.pathfinder;

import java.util.Arrays;
import java.util.List;

public class PathManager {
	public static RightSwitchDropoffPath mRightSwitchDropoff = new RightSwitchDropoffPath();
	public static RightmostCubePickupPath mRightmostCubePickup = new RightmostCubePickupPath();
	public static LeftSwitchDropoffPath mLeftSwitchDropoff = new LeftSwitchDropoffPath();
	public static LeftmostCubePickupPath mLeftmostCubePickup = new LeftmostCubePickupPath();
	public static RightCubeToRightScalePath mRightCubeToRightScale = new RightCubeToRightScalePath();
	public static RightScaleToFirstCubePath mRightScaleToFirstCube = new RightScaleToFirstCubePath();
	public static RightScaleToSecondCubePath mRightScaleToSecondCube = new RightScaleToSecondCubePath();
	public static RightCubeToLeftScalePath mRightCubeToLeftScale = new RightCubeToLeftScalePath();
	public static LeftScaleToFirstCubePath mLeftScaleToFirstCube = new LeftScaleToFirstCubePath();
	public static LeftCubeToLeftScalePath mLeftCubeToLeftScale = new LeftCubeToLeftScalePath();
	public static LeftScaleToSecondCubePath mLeftScaleToSecondCube = new LeftScaleToSecondCubePath();
	public static SecondLeftCubeToScalePath mSecondLeftCubeToScale = new SecondLeftCubeToScalePath();
	public static LeftScaleToThirdCubePath mLeftScaleToThirdCube = new LeftScaleToThirdCubePath();
	public static LeftCubeToRightScalePath mLeftCubeToRightScale = new LeftCubeToRightScalePath();
	public static SecondRightCubeToScalePath mSecondRightCubeToScale = new SecondRightCubeToScalePath();
	public static FrontLeftSwitchPath mFrontLeftSwitchPath = new FrontLeftSwitchPath();
	
	//public static TestPath mTestPath = new TestPath();
	
	public static List<PathfinderPath> paths = Arrays.asList(mRightSwitchDropoff, mRightmostCubePickup, mLeftSwitchDropoff, 
			mLeftmostCubePickup, mRightCubeToRightScale, mRightScaleToFirstCube, mRightScaleToSecondCube, mRightCubeToLeftScale,
			mLeftScaleToFirstCube, mLeftCubeToLeftScale,mLeftScaleToSecondCube, mSecondLeftCubeToScale, mLeftScaleToThirdCube,
			mLeftCubeToRightScale, mSecondRightCubeToScale, mFrontLeftSwitchPath);
	
	public static void buildAllPaths(){
		paths.forEach((p) -> p.buildPath());
	}
	
}
