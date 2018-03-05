package com.team1323.frc2018.auto;

import com.team1323.frc2018.auto.modes.LeftFrontSwitchMode;
import com.team1323.frc2018.auto.modes.LeftSwitchLeftScaleMode;
import com.team1323.frc2018.auto.modes.LeftSwitchRightScaleMode;
import com.team1323.frc2018.auto.modes.RightSwitchLeftScaleMode;
import com.team1323.frc2018.auto.modes.RightSwitchRightScaleMode;
import com.team1323.frc2018.auto.modes.StandStillMode;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SmartDashboardInteractions {
	private static final String AUTO_OPTIONS = "auto_options";
    private static final String SELECTED_AUTO_MODE = "selected_auto_mode";
    
    private static final AutoOption DEFAULT_MODE = AutoOption.SWITCH_AND_SCALE;
    
    private SendableChooser modeChooser;
    
    public void initWithDefaults(){
    	modeChooser = new SendableChooser();
    	modeChooser.addDefault("Switch and Scale", DEFAULT_MODE);
    	modeChooser.addObject("Switch Only", AutoOption.SWITCH_ONLY);
    	
    	SmartDashboard.putData("Mode Chooser", modeChooser);
    	SmartDashboard.putString(SELECTED_AUTO_MODE, DEFAULT_MODE.name);
    }
    
    public AutoModeBase getSelectedAutoMode(String gameData){
        AutoOption selectedOption =  (AutoOption)  modeChooser.getSelected();
                
        return createAutoMode(selectedOption, gameData);
    }
    
    public String getSelectedMode(){
    	AutoOption option = (AutoOption) modeChooser.getSelected();
    	return option.name;
    }
    
    enum AutoOption{
    	SWITCH_AND_SCALE("Switch and Scale"),
    	SWITCH_ONLY("Switch Only");
    	
    	public final String name;
    	
    	AutoOption(String name){
    		this.name = name;
    	}
    }
    
    private AutoModeBase createAutoMode(AutoOption option, String gameData){
    	switch(option){
    		case SWITCH_AND_SCALE:
    			switch(gameData){
    			case "RR":
    				return new RightSwitchRightScaleMode();
    			case "RL":
    				return new RightSwitchLeftScaleMode();
    			case "LR":
    				return new LeftSwitchRightScaleMode();
    			case "LL":
    				return new LeftSwitchLeftScaleMode();
    			default:
    				System.out.println("ERROR: unexpected auto mode: " + option);
                    return new StandStillMode();
    			}
    		case SWITCH_ONLY:
    			switch(gameData){
    			case "LL":
    				return new LeftFrontSwitchMode();
    			default:
    				System.out.println("ERROR: unexpected auto mode: " + option);
                    return new StandStillMode();
    			}
            default:
                System.out.println("ERROR: unexpected auto mode: " + option);
                return new StandStillMode();
    	}
    }
    
    public void output(){
    	SmartDashboard.putString(SELECTED_AUTO_MODE, getSelectedMode());
    }
}
