package com.team1323.io;

import com.team1323.lib.util.Util;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;

public class Xbox extends XboxController{
    private static final double PRESS_THRESHOLD = 0.3;
    private double DEAD_BAND = 0.15;
    private boolean rumbling = false;
    public ButtonCheck aButton;
    public ButtonCheck bButton;
    public ButtonCheck xButton;
    public ButtonCheck yButton;
    public ButtonCheck startButton;
    public ButtonCheck backButton;
    public ButtonCheck leftBumper;
    public ButtonCheck rightBumper;
    public ButtonCheck leftCenterClick;
    public ButtonCheck rightCenterClick;
    public ButtonCheck leftTrigger;
    public ButtonCheck rightTrigger;
    public ButtonCheck POV0;
    public ButtonCheck POV90;
    public ButtonCheck POV180;
    public ButtonCheck POV270;
    public static final int A_BUTTON = 1;
    public static final int B_BUTTON = 2;
    public static final int X_BUTTON = 3;
    public static final int Y_BUTTON = 4;
    public static final int LEFT_BUMPER = 5;
    public static final int RIGHT_BUMPER = 6;
    public static final int BACK_BUTTON = 7;
    public static final int START_BUTTON = 8;
    public static final int LEFT_CENTER_CLICK = 9;
    public static final int RIGHT_CENTER_CLICK = 10;
    public static final int LEFT_TRIGGER = -2;
    public static final int RIGHT_TRIGGER = -3;
    public static final int POV_0 = -4;
    public static final int POV_90 = -5;
    public static final int POV_180 = -6;
    public static final int POV_270 = -7;
    
    public void setDeadband(double deadband){
    	DEAD_BAND = deadband;
    }
    
    public Xbox(int usb)   { 
    	super(usb);
    	aButton = new ButtonCheck(A_BUTTON);
        bButton = new ButtonCheck(B_BUTTON);
        xButton = new ButtonCheck(X_BUTTON);
        yButton = new ButtonCheck(Y_BUTTON);
        startButton = new ButtonCheck(START_BUTTON);
        backButton = new ButtonCheck(BACK_BUTTON);
        leftBumper = new ButtonCheck(LEFT_BUMPER);
        rightBumper = new ButtonCheck(RIGHT_BUMPER);
        leftCenterClick = new ButtonCheck(LEFT_CENTER_CLICK);
        rightCenterClick = new ButtonCheck(RIGHT_CENTER_CLICK);     
        leftTrigger = new ButtonCheck(LEFT_TRIGGER);
        rightTrigger = new ButtonCheck(RIGHT_TRIGGER);
        POV0 = new ButtonCheck(POV_0);
        POV90 = new ButtonCheck(POV_90);
        POV180 = new ButtonCheck(POV_180);
        POV270 = new ButtonCheck(POV_270);
   }
    
    @Override
    public double getX(Hand hand) {
        if (hand.equals(Hand.kLeft)) {
          return Util.deadBand(getRawAxis(0), DEAD_BAND);
        } else {
          return Util.deadBand(getRawAxis(4), DEAD_BAND);
        }
      }
    @Override
    public double getY(Hand hand) {
        if (hand.equals(Hand.kLeft)) {
          return Util.deadBand(getRawAxis(1), DEAD_BAND);
        } else {
          return Util.deadBand(getRawAxis(5), DEAD_BAND);
        }
      }
    @Override
    public double getTriggerAxis(Hand hand) {
        if (hand.equals(Hand.kLeft)) {
          return Util.deadBand(getRawAxis(2), PRESS_THRESHOLD);
        } else {
          return Util.deadBand(getRawAxis(3), PRESS_THRESHOLD);
        }
      }
    
    public void rumble(double rumblesPerSecond, double numberOfSeconds){
    	if(!rumbling){
    		RumbleThread r = new RumbleThread(rumblesPerSecond, numberOfSeconds);
    		r.start();
    	}
    }
    public boolean isRumbling(){
    	return rumbling;
    }
    public class RumbleThread extends Thread{
    	public double rumblesPerSec = 1;
    	public long interval = 500;
    	public double seconds = 1;
    	public double startTime = 0;
    	public RumbleThread(double rumblesPerSecond, double numberOfSeconds){
    		rumblesPerSec = rumblesPerSecond;
    		seconds = numberOfSeconds;
    		interval =(long) (1/(rumblesPerSec*2)*1000);
    	}
    	public void run(){
    		rumbling = true;
    		startTime = Timer.getFPGATimestamp();
    		try{
    			while((Timer.getFPGATimestamp() - startTime) < seconds){
		    		setRumble(RumbleType.kLeftRumble, 1);
		    		setRumble(RumbleType.kRightRumble, 1);
		    		sleep(interval);
		    		setRumble(RumbleType.kLeftRumble, 0);
		    		setRumble(RumbleType.kRightRumble, 0);
		    		sleep(interval);
    			}
    		}catch (InterruptedException e) {
				rumbling = false;
				e.printStackTrace();
			}
    		rumbling = false;
    	}
    }
    
    public class ButtonCheck{
    	boolean buttonCheck = false;
    	boolean buttonActive = false;
    	boolean longPressActive = false;
    	boolean hasBeenPressed = false;
    	private double buttonStartTime = 0;
    	private int buttonNumber;
    	
    	public ButtonCheck(int id){
    		buttonNumber = id;
    	}
    	public void update(){
    		if(buttonNumber > 0){
    			buttonCheck = getRawButton(buttonNumber);
    		}else{
    			switch(buttonNumber){
    				case LEFT_TRIGGER:
    					buttonCheck = getTriggerAxis(Hand.kLeft) > 0;
    					break;
    				case RIGHT_TRIGGER:
    					buttonCheck = getTriggerAxis(Hand.kRight) > 0;
    					break;
    				case POV_0:
    					buttonCheck = (getPOV() == 0);
    					break;
    				case POV_90:
    					buttonCheck = (getPOV() == 90);
    					break;
    				case POV_180:
    					buttonCheck = (getPOV() == 180);
    					break;
    				case POV_270:
    					buttonCheck = (getPOV() == 270);
    					break;
    				default:
    					buttonCheck = false;
    					break;
    			}
    		}
    		if(buttonCheck){
	    		if(buttonActive){
	    			if(System.currentTimeMillis() - buttonStartTime > 250){
	    				longPressActive = true;
	    			}
	    		}else{
	    			buttonActive = true;
	    			buttonStartTime = System.currentTimeMillis();
	    		}
    		}else{
    			if(buttonActive){
    				buttonActive = false;
    				if(longPressActive){
    					hasBeenPressed = false;
    					longPressActive = false;
    				}else{
    					hasBeenPressed = true;
    				}
    			}
    		}
    	}
    	public boolean wasPressed(){
    		if(hasBeenPressed){
    			hasBeenPressed = false;
    			return true;
    		}
    		return false;
    	}
    	public boolean longPressed(){
    		return longPressActive;
    	}
    	public boolean isBeingPressed(){
    		return buttonActive;
    	}
    }
    public void update(){
    	aButton.update();
    	bButton.update();
    	xButton.update();
    	yButton.update();
    	startButton.update();
    	backButton.update();
    	leftBumper.update();
    	rightBumper.update();
    	leftCenterClick.update();
    	rightCenterClick.update();
    	leftTrigger.update();
    	rightTrigger.update();
    	POV0.update();
    	POV90.update();
    	POV180.update();
    	POV270.update();
    }
}