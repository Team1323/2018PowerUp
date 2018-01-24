/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team1323.frc2018;

import java.util.Arrays;

import com.team1323.frc2018.auto.AutoModeExecuter;
import com.team1323.frc2018.auto.modes.LeftSwitchLeftScaleMode;
import com.team1323.frc2018.auto.modes.LeftSwitchRightScaleMode;
import com.team1323.frc2018.auto.modes.RightSwitchLeftScaleMode;
import com.team1323.frc2018.auto.modes.RightSwitchRightScaleMode;
import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.pathfinder.PathManager;
import com.team1323.frc2018.pathfinder.PathfinderPath;
import com.team1323.frc2018.subsystems.Elevator;
import com.team1323.frc2018.subsystems.Intake;
import com.team1323.frc2018.subsystems.SubsystemManager;
import com.team1323.frc2018.subsystems.Superstructure;
import com.team1323.frc2018.subsystems.Swerve;
import com.team1323.frc2018.subsystems.Wrist;
import com.team1323.io.Xbox;
import com.team1323.lib.util.CrashTracker;
import com.team1323.lib.util.Logger;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Trajectory;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private Swerve swerve = Swerve.getInstance();
	private Superstructure superstructure = Superstructure.getInstance();
	private SubsystemManager subsystems = new SubsystemManager(
			Arrays.asList(Swerve.getInstance(), Intake.getInstance(), 
					Elevator.getInstance(), Wrist.getInstance()));
	
	private AutoModeExecuter autoModeExecuter = null;
	
	private Looper enabledLooper = new Looper();
	
	private Xbox driver, coDriver;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		driver = new Xbox(0);
		coDriver = new Xbox(1);
		
		Logger.clearLog();
		
		subsystems.registerEnabledLoops(enabledLooper);
		
		PathManager.buildAllPaths();
		
		PathfinderPath path = PathManager.mRightSwitchDropoff;
		
		for (int i = 0; i < path.getTrajectory().length(); i++) {
		    Trajectory.Segment seg = path.getTrajectory().get(i);
		    String coordinates = "(" + Double.toString(seg.y) + ", " + Double.toString(seg.x) + ")";
		    if(i != (path.getTrajectory().length() - 1))
		    	coordinates += ", ";
		    Logger.log(coordinates);
		}
	}
	
	public void allPeriodic(){
		subsystems.outputToSmartDashboard();
		enabledLooper.outputToSmartDashboard();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		try{
			if(autoModeExecuter != null)
				autoModeExecuter.stop();
			
			subsystems.zeroSensors();
			
			enabledLooper.start();
			
			SmartDashboard.putBoolean("Auto", true);
			
			autoModeExecuter = new AutoModeExecuter();
			String gameData = DriverStation.getInstance().getGameSpecificMessage().substring(0, 2);
			switch(gameData){
			case "RR":
				autoModeExecuter.setAutoMode(new RightSwitchRightScaleMode());
				break;
			case "RL":
				autoModeExecuter.setAutoMode(new RightSwitchLeftScaleMode());
				break;
			case "LR":
				autoModeExecuter.setAutoMode(new LeftSwitchRightScaleMode());
				break;
			case "LL":
				autoModeExecuter.setAutoMode(new LeftSwitchLeftScaleMode());
				break;
			}
			autoModeExecuter.start();
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		allPeriodic();
	}
	
	@Override
	public void teleopInit(){
		try{
			enabledLooper.start();
			SmartDashboard.putBoolean("Auto", false);
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		try{
			driver.update();
			//coDriver.update();
			
			if(driver.startButton.isBeingPressed()){
				superstructure.intake.stop();
			}
			
			swerve.sendInput(driver.getX(Hand.kLeft), -driver.getY(Hand.kLeft), driver.getX(Hand.kRight), false, false);
			if(driver.yButton.wasPressed())
				swerve.rotate(0);
			else if(driver.bButton.wasPressed())
				swerve.rotate(90);
			else if(driver.aButton.wasPressed())
				swerve.rotate(180);
			else if(driver.xButton.wasPressed())
				swerve.rotate(270);
			if(driver.backButton.isBeingPressed()){
				swerve.temporarilyDisableHeadingController();
				swerve.zeroSensors(new RigidTransform2d(new Translation2d(18.393, 19.354), Rotation2d.fromDegrees(-270)));
			}
			
			if(driver.POV180.wasPressed()){
				swerve.followPath(PathManager.mRightCubeToLeftScale, -450);
			}
			
			if(driver.rightBumper.wasPressed())
				superstructure.intake.intake();
			else if(driver.leftBumper.wasPressed())
				superstructure.intake.eject();
			
			allPeriodic();
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}
	
	@Override
	public void disabledInit(){
		try{
			enabledLooper.stop();
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}
	
	@Override
	public void disabledPeriodic(){
		try{
			allPeriodic();
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
