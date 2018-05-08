//package com.team1323.reee;
//import com.team254.lib.util.math;

public class RadialVectorField implements VectorField {
	// Default direction is toward point
	public RadialVectorField(double x, double y) {
		x_ = x;
		y_ = y;
	}
	public RadialVectorField(double x, double y, bool away) {
		x_ = x;
		y_ = y;
		if(away) direction = -1;
	}
	
	protected int direction = 1;
	protected double x_;
	protected double y_;
	
	public double[] getVector(double x, double y) {
		double dx = x_ - x;
		double dy = y_ - y;
		double m = Math.sqrt(dx*dx+dy*dy);
		return new double[]{dx/m,dy/m};
	}
	public double getX(double x, double y) { return getVector(x,y)[0]; }
	public double getY(double x, double y) { return getVector(x,y)[1]; }
/*	public Translation2d getTranslation2d(double x, double y) {
		double[] v = getVector(x,y);
		return new Translation2d(v[0],v[1]);
	} */
}