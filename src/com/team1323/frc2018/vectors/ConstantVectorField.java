//package com.team1323.reee;
//import com.team254.lib.util.math;

public class ConstantVectorField implements VectorField {
	public ConstantVectorField(double x, double y) {
		double m = Math.sqrt(x*x+y*y);
		x_ = x/m;
		y_ = y/m;
	}
	public ConstantVectorField(double x, double y, bool away) {
		double m = Math.sqrt(x*x+y*y);
		x_ = x/m;
		y_ = y/m;
		if(away) direction = -1;
	}
	
	protected int direction = 1;
	protected double x_;
	protected double y_;
	
	public double[] getVector(double x, double y) {
		return new double[]{x_,y_};
	}
	public double getX(double x, double y) { return getVector(x,y)[0]; }
	public double getY(double x, double y) { return getVector(x,y)[1]; }
/*	public Translation2d getTranslation2d(double x, double y) {
		double[] v = getVector(x,y);
		return new Translation2d(v[0],v[1]);
	} */
	public double[] getVector() { return new double[]{x_,y_}; }
	public double getX() { return getVector(x,y)[0]; }
	public double getY() { return getVector(x,y)[1]; }
/*	public Translation2d getTranslation2d() {
		double[] v = getVector(x,y);
		return new Translation2d(v[0],v[1]);
	} */
}