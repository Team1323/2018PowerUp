/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team1323.frc2018;

import java.util.Arrays;

import com.team1323.frc2018.auto.AutoModeExecuter;
import com.team1323.frc2018.auto.SmartDashboardInteractions;
import com.team1323.frc2018.loops.LimelightProcessor;
import com.team1323.frc2018.loops.Looper;
import com.team1323.frc2018.loops.PathTransmitter;
import com.team1323.frc2018.loops.RobotStateEstimator;
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

import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode.PixelFormat;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
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
	private Swerve swerve;
	private Superstructure superstructure;
	private SubsystemManager subsystems;
	
	private AutoModeExecuter autoModeExecuter = null;
	private PathTransmitter transmitter = PathTransmitter.getInstance();
	private SmartDashboardInteractions smartDashboardInteractions = new SmartDashboardInteractions();
	
	private Looper swerveLooper = new Looper();
	private Looper enabledLooper = new Looper();
	private Looper disabledLooper = new Looper();
	
	private RobotState robotState = RobotState.getInstance();
	private LimelightProcessor limelight = LimelightProcessor.getInstance();
	private CameraServer cam;
	
	private Xbox driver, coDriver;
	

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		swerve = Swerve.getInstance();
		superstructure = Superstructure.getInstance();
		subsystems = new SubsystemManager(
				Arrays.asList(Intake.getInstance(), Elevator.getInstance(), 
						Wrist.getInstance(), Superstructure.getInstance()));
		
		driver = new Xbox(0);
		coDriver = new Xbox(1);
		driver.setDeadband(0.0);
		coDriver.setDeadband(0.4);
		
		Logger.clearLog();
		
		swerve.registerEnabledLoops(swerveLooper);
		subsystems.registerEnabledLoops(enabledLooper);
		enabledLooper.register(LimelightProcessor.getInstance());
		enabledLooper.register(RobotStateEstimator.getInstance());
		enabledLooper.register(PathTransmitter.getInstance());
		disabledLooper.register(LimelightProcessor.getInstance());
		disabledLooper.register(RobotStateEstimator.getInstance());
		disabledLooper.register(PathTransmitter.getInstance());
		
		subsystems.zeroSensors();
		swerve.zeroSensors();
		
		smartDashboardInteractions.initWithDefaults();
		initCamera();
		
		PathManager.buildAllPaths();
		
		/*transmitter.addPaths(Arrays.asList(PathManager.mLeftSwitchDropoff, PathManager.mLeftmostCubePickup,
				PathManager.mLeftCubeToLeftScale, PathManager.mLeftScaleToSecondCube, PathManager.mSecondLeftCubeToScale,
				PathManager.mLeftScaleToThirdCube));*/
		/*transmitter.addPaths(Arrays.asList(PathManager.mRightSwitchDropoff, PathManager.mRightmostCubePickup,
				PathManager.mRightCubeToRightScale, PathManager.mRightScaleToSecondCube, PathManager.mSecondRightCubeToScale));*/
		/*transmitter.addPaths(Arrays.asList(PathManager.mRightSwitchDropoff, PathManager.mRightmostCubePickup,
				PathManager.mRightCubeToLeftScale, PathManager.mLeftScaleToFirstCube));*/
		/*transmitter.addPaths(Arrays.asList(PathManager.mLeftSwitchDropoff, PathManager.mLeftmostCubePickup,
				PathManager.mLeftCubeToRightScale, PathManager.mRightScaleToFirstCube));*/
		/*transmitter.addPaths(Arrays.asList(PathManager.mFrontLeftSwitch, PathManager.mFrontLeftSwitchToOuterCube, 
				PathManager.mOuterCubeToFrontLeftSwitch, PathManager.mFrontLeftSwitchToMiddleCube, PathManager.mMiddleCubeToFrontLeftSwitch,
				PathManager.mFrontLeftSwitchToDropoff));*/
		/*transmitter.addPaths(Arrays.asList(PathManager.mFrontRightSwitch, PathManager.mFrontRightSwitchToOuterCube, 
				PathManager.mOuterCubeToFrontRightSwitch, PathManager.mFrontRightSwitchToMiddleCube, PathManager.mMiddleCubeToFrontRightSwitch,
				PathManager.mFrontRightSwitchToDropoff));*/
		/*transmitter.addPaths(Arrays.asList(PathManager.mStartToLeftScale, PathManager.mAlternateLeftmostCube,
				PathManager.mDerpLeftCubeToLeftScale, PathManager.mAlternateLeftScaleToSecondCube,
				PathManager.mAlternateSecondLeftCubeToScale));*/
		transmitter.addPaths(Arrays.asList(PathManager.mStartToRightScale, PathManager.mRightScaleToFirstCube,
				PathManager.mAlternateRightCubeToRightScale, PathManager.mAlternateRightScaleToSecondCube));
		
		PathfinderPath path = PathManager.mStartToRightScale;
		double maxSpeed = 0.0;
		int points = 0;
		
		for (int i = 0; i < path.getTrajectory().length(); i++) {
		    Trajectory.Segment seg = path.getTrajectory().get(i);
		    String coordinates = "(" + Double.toString(seg.y) + ", " + Double.toString(seg.x) + ")";
		    if(i != (path.getTrajectory().length() - 1))
		    	coordinates += ", ";
		    Logger.log(coordinates);
		    maxSpeed = (seg.velocity > maxSpeed) ? seg.velocity : maxSpeed;
		    points++;
		}
		System.out.println("Max Path Velocity: " + maxSpeed + ", Number of Points: " + points);
	}
	
	public void allPeriodic(){
		subsystems.outputToSmartDashboard();
		swerve.outputToSmartDashboard();
		robotState.outputToSmartDashboard();
		//SmartDashboard.putNumber("Swerve dt", swerveLooper.dt_);
		//enabledLooper.outputToSmartDashboard();
		
	}
	
	public void initCamera(){
		cam = CameraServer.getInstance();
    	UsbCamera usbCamera = new UsbCamera("USB Camera 0", 0);
    	usbCamera.setVideoMode(PixelFormat.kMJPEG, 320, 240, 30);
    	MjpegServer mjpegServer2 = new MjpegServer("serve_Blur", 1182);
    	mjpegServer2.setSource(usbCamera);
	}
	
	@Override
	public void autonomousInit() {
		try{
			if(autoModeExecuter != null)
				autoModeExecuter.stop();
			
			subsystems.zeroSensors();
			swerve.zeroSensors();
			swerve.setNominalDriveOutput(1.5);
			swerve.requireModuleConfiguration();
			transmitter.transmitCachedPaths();
			
			disabledLooper.stop();
			swerveLooper.start();
			enabledLooper.start();
			
			limelight.setVisionMode();
			limelight.ledOn(true);
			
			superstructure.elevator.setCurrentLimit(20);
			superstructure.elevator.configForAutoSpeed();
			
			superstructure.enableCompressor(false);
			
			//SmartDashboard.putBoolean("Auto", true);
			
			String gameData = DriverStation.getInstance().getGameSpecificMessage();
			autoModeExecuter = new AutoModeExecuter();
			autoModeExecuter.setAutoMode(smartDashboardInteractions.getSelectedAutoMode(gameData.substring(0, 2)));
			autoModeExecuter.start();
			SmartDashboard.putString("Game Data", gameData);
			System.out.println(gameData);
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
			disabledLooper.stop();
			swerveLooper.stop();
			swerveLooper.start();
			enabledLooper.start();
			superstructure.enableCompressor(true);
			limelight.setDriverMode();
			limelight.ledOn(false);
			swerve.setNominalDriveOutput(0.0);
			superstructure.elevator.setCurrentLimit(30);
			superstructure.elevator.configForTeleopSpeed();
			superstructure.setManualElevatorSpeed(Constants.kElevatorTeleopManualSpeed);
			//limelight.setVisionMode();
			//limelight.ledOn(true);
			//SmartDashboard.putBoolean("Auto", false);
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
			coDriver.update();
			
			if(coDriver.backButton.isBeingPressed()){
				superstructure.requestIntakeIdle();
			}
			
			double swerveYInput = (superstructure.getState() == Superstructure.State.HANGING) ? 0.0 : driver.getX(Hand.kLeft);
			double swerveXInput = (superstructure.getState() == Superstructure.State.HANGING) ? 0.0 : -driver.getY(Hand.kLeft);
			double swerveRotationInput = (driver.rightCenterClick.isBeingPressed()) ? 0.0 : driver.getX(Hand.kRight);
			
			swerve.sendInput(swerveXInput, swerveYInput, swerveRotationInput, false, driver.leftTrigger.isBeingPressed());
			if(driver.yButton.isBeingPressed())
				swerve.rotate(0);
			else if(driver.bButton.isBeingPressed())
				swerve.rotate(90);
			else if(driver.aButton.isBeingPressed())
				swerve.rotate(180);
			else if(driver.xButton.isBeingPressed())
				swerve.rotate(270);
			else if(driver.leftCenterClick.isBeingPressed())
				swerve.rotate(-135);
			else if(driver.rightBumper.isBeingPressed())
				swerve.rotate(25);
			if(driver.backButton.wasPressed()){
				swerve.temporarilyDisableHeadingController();
				swerve.zeroSensors(Constants.kRobotStartingPose);
				//robotState.resetRobotPosition(Constants.kRightSwitchTarget);
			}else if(driver.backButton.longPressed()){
				swerve.temporarilyDisableHeadingController();
				swerve.zeroSensors(Constants.kRobotStartingPose);
			}
			
			if(coDriver.rightBumper.wasPressed()){
				superstructure.requestIntakeOn();
			}else if(coDriver.leftTrigger.wasPressed() || driver.leftBumper.wasPressed()){
				superstructure.requestIntakeOpen();
			}else if(coDriver.rightTrigger.wasPressed() || driver.rightTrigger.wasPressed()){
				superstructure.requestIntakeScore();
			}else if(coDriver.rightTrigger.longPressed() || driver.rightTrigger.longPressed()){
				superstructure.requestIntakeWeakScore();
			}else if(coDriver.leftBumper.wasPressed()){
				superstructure.requestOpenIntakingConfig();
			}
			
			if(!superstructure.driveTrainFlipped()){
				if(coDriver.aButton.wasPressed()){
					superstructure.requestIntakingConfig();
				}else if(coDriver.rightBumper.longPressed()){
					superstructure.requestForceIntake();
				}else if(coDriver.rightBumper.longReleased()){
					superstructure.requestIntakeOn();
				}else if(coDriver.xButton.wasPressed()){
					superstructure.requestSwitchConfig();
				}else if(coDriver.bButton.wasPressed()){
					superstructure.requestPrimaryWristStow();
				}else if(coDriver.yButton.wasPressed()){
					superstructure.requestBalancedScaleConfig();
				}else if(coDriver.POV0.wasPressed()){
					superstructure.requestHighScaleConfig();
				}else if(coDriver.POV180.wasPressed()){
					superstructure.requestLowScaleConfig();
				}else if(coDriver.bButton.longPressed()){
					superstructure.requestGroundStowedConfig();
				}else if(coDriver.rightCenterClick.wasPressed()){
					superstructure.requestHighIntakingConfig();
				}else if(coDriver.xButton.longPressed()){
					superstructure.requestHumanLoadingConfig();
				}else if(coDriver.aButton.longPressed()){
					superstructure.requestExchangeConfig();
				}else if(coDriver.POV90.wasPressed()){
					superstructure.requestTippingCubeConfig();
				}
			}else{
				if(coDriver.POV0.isBeingPressed()){
					superstructure.requestWinchOpenLoop(0.75);
				}else if(coDriver.POV180.isBeingPressed()){
					superstructure.requestWinchOpenLoop(-0.75);
				}else{
					superstructure.requestWinchOpenLoop(0.0);
				}
			}
			
			if(coDriver.startButton.longPressed()){
				superstructure.setManualElevatorSpeed(0.25);
				superstructure.elevator.enableLimits(false);
			}else if(!superstructure.elevator.limitsEnabled() && coDriver.getY(Hand.kLeft) == 0){
				superstructure.elevator.zeroSensors();
				superstructure.elevator.enableLimits(true);
				superstructure.setManualElevatorSpeed(Constants.kElevatorTeleopManualSpeed);
			}
			
			if(coDriver.leftBumper.isBeingPressed()){
				limelight.blink();
			}else{
				limelight.ledOn(false);
			}
			
			if(superstructure.driveTrainFlipped() && coDriver.leftTrigger.isBeingPressed())
				superstructure.requestElevatorOpenLoop(-coDriver.getY(Hand.kLeft)*0.5);
			else
				superstructure.requestElevatorOpenLoop(-coDriver.getY(Hand.kLeft));
			
			superstructure.requestWristOpenLoop(-coDriver.getY(Hand.kRight));
			
			if(Intake.getInstance().needsToNotifyDrivers()){
				driver.rumble(1.0, 1.0);
				coDriver.rumble(1.0, 1.0);
			}else if(superstructure.hasDriverAlert()){
				coDriver.rumble(1.0, 1.0);
			}
			
			if(driver.POV0.wasPressed()){
				if(superstructure.getState() == Superstructure.State.HANGING)
					superstructure.requestFinalHungConfig();
				else
					superstructure.requestHangingConfig();
			}else if(driver.POV180.wasPressed()){
				if(!superstructure.elevator.isHighGear())
					superstructure.requestHungConfig();
			}else if(driver.POV90.wasPressed() && !Elevator.getInstance().isHighGear()){
				superstructure.flipDriveTrain();
			}
			
			allPeriodic();
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}
	
	@Override
	public void disabledInit(){
		try{
			if(autoModeExecuter != null)
				autoModeExecuter.stop();
			enabledLooper.stop();
			subsystems.stop();
			swerveLooper.stop();
			swerveLooper.start();
			disabledLooper.start();
			Elevator.getInstance().fireGasStruts(false);
			Elevator.getInstance().fireLatch(false);
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}
	
	@Override
	public void disabledPeriodic(){
		try{
			allPeriodic();
			smartDashboardInteractions.output();
		}catch(Throwable t){
			CrashTracker.logThrowableCrash(t);
			throw t;
		}
	}
	
	@Override
	public void testInit(){
		Timer.delay(2.0);
		boolean passed = true;
		//passed &= Intake.getInstance().checkSystem();
		//passed &= Wrist.getInstance().checkSystem();
		passed &= Elevator.getInstance().checkSystem();
		if(passed)
			System.out.println("All systems passed");
		else
			System.out.println("Some systems failed, check above output for details");
	}
	
	@Override
	public void testPeriodic() {
	}
}
