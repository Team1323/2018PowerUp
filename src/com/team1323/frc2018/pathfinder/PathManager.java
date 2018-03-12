package com.team1323.frc2018.pathfinder;

import java.util.Arrays;
import java.util.List;

import com.team1323.frc2018.pathfinder.frontswitch.FrontLeftSwitchPath;
import com.team1323.frc2018.pathfinder.frontswitch.FrontLeftSwitchToMiddleCubePath;
import com.team1323.frc2018.pathfinder.frontswitch.FrontLeftSwitchToOuterCubePath;
import com.team1323.frc2018.pathfinder.frontswitch.FrontLeftSwitchToRearCubePath;
import com.team1323.frc2018.pathfinder.frontswitch.MiddleCubeToFrontLeftSwitchPath;
import com.team1323.frc2018.pathfinder.frontswitch.OuterCubeToFrontLeftSwitchPath;

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
	public static FrontLeftSwitchToOuterCubePath mFrontLeftSwitchToOuterCube = new FrontLeftSwitchToOuterCubePath();
	public static OuterCubeToFrontLeftSwitchPath mOuterCubeToFrontLeftSwitch = new OuterCubeToFrontLeftSwitchPath();
	public static FrontLeftSwitchToMiddleCubePath mFrontLeftSwitchToMiddleCube = new FrontLeftSwitchToMiddleCubePath();
	public static MiddleCubeToFrontLeftSwitchPath mMiddleCubeToFrontLeftSwitch = new MiddleCubeToFrontLeftSwitchPath();
	public static FrontLeftSwitchToRearCubePath mFrontLeftSwitchToRearCube = new FrontLeftSwitchToRearCubePath();
	//public static TestPath mTestPath = new TestPath();
	
	public static List<PathfinderPath> paths = Arrays.asList(mRightSwitchDropoff, mRightmostCubePickup, mLeftSwitchDropoff, 
			mLeftmostCubePickup, mRightCubeToRightScale, mRightScaleToFirstCube, mRightScaleToSecondCube, mRightCubeToLeftScale,
			mLeftScaleToFirstCube, mLeftCubeToLeftScale,mLeftScaleToSecondCube, mSecondLeftCubeToScale, mLeftScaleToThirdCube,
			mLeftCubeToRightScale, mSecondRightCubeToScale, mFrontLeftSwitchPath, mFrontLeftSwitchToOuterCube,
			mOuterCubeToFrontLeftSwitch, mFrontLeftSwitchToMiddleCube, mMiddleCubeToFrontLeftSwitch, mFrontLeftSwitchToRearCube);
	
	public static void buildAllPaths(){
		paths.forEach((p) -> p.buildPath());
	}
	
}
