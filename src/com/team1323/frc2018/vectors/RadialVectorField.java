//package com.team1323.reee;
//import com.team254.lib.util.math;

public class RadialVectorField implements VectorField {
	// Default direction is toward point
	public RadialVectorField(Translation2d where) {
		x_ = where.x();
		y_ = where.y();
	}
	public RadialVectorField(Translation2d where, boolean away) {
		x_ = where.x();
		y_ = where.y();
		if(away) direction = -1;
	}
	
	protected int direction = 1;
	protected double x_;
	protected double y_;
	
	public Translation2d getVector(double x, double y) {
		Translation2d v = new Translation2d(x_-x,y_-y);
		return v.scale(1/v.norm());
	}
}
