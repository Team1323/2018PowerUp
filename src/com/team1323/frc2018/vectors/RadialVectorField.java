//package com.team1323.reee;
//import com.team254.lib.util.math;

public class RadialVectorField implements VectorField {
	// Default direction is toward point
	public RadialVectorField(Translation2d where) {
		there = where;
	}
	public RadialVectorField(Translation2d where, boolean away) {
		there = where;
		if(away) direction = -1;
	}
	
	protected int direction = 1;
	protected Translation2d there;
	
	public Translation2d getVector(Translation2d here) {
		Translation2d v = new Translation2d(here,there);
		return v.scale(direction/v.norm());
	}
}
