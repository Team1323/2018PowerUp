package com.team1323.frc2018.loops;

import java.util.ArrayList;
import java.util.List;

import com.team1323.frc2018.pathfinder.PathfinderPath;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Trajectory;

public class PathTransmitter implements Loop{
	private static PathTransmitter instance = new PathTransmitter();
	public static PathTransmitter getInstance(){
		return instance;
	}
	
	public PathTransmitter(){
	}
	
	private List<PathfinderPath> remainingPaths = new ArrayList<>();
	private PathfinderPath currentPath;
	private int currentPointIndex = 0;
	private List<PathfinderPath> cachedPaths = remainingPaths;
	
	public void addPaths(List<PathfinderPath> paths){
		remainingPaths = new ArrayList<>(paths.size());
		cachedPaths = remainingPaths;
		
		for(PathfinderPath path: paths){
			remainingPaths.add(path);
		}
		
		currentPath = null;
	}
	
	public void transmitCachedPaths(){
		addPaths(cachedPaths);
	}

	@Override
	public void onStart(double timestamp) {
		
	}

	@Override
	public void onLoop(double timestamp) {
		if(currentPath == null){
			if(remainingPaths.isEmpty()){
				return;
			}
			
			currentPath = remainingPaths.remove(0);
			currentPointIndex = 0;
		}
		
		Trajectory.Segment seg = currentPath.getTrajectory().get(currentPointIndex);
	    SmartDashboard.putNumber("Path X", seg.x);
	    SmartDashboard.putNumber("Path Y", seg.y);
	    currentPointIndex += 1;
	    
	    if(currentPointIndex >= currentPath.getTrajectory().length()){
	    	currentPath = null;
	    }
	}

	@Override
	public void onStop(double timestamp) {
		
	}

}
