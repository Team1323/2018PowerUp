//package com.team1323.reee;
//import com.team254.lib.util.math;

public class GuidingVectorField implements VectorField {
// FIXME: How do we specify a callable? Can I assign one to a member? How? reeeee
	public GuidingVectorField(Callable<double(double,double)> surface, Callable<double(double,double)> dfdx, Callable<double(double,double)> dfdy) {
		phi = surface;
		dfdx_ = dfdx;
		dfdy_ = dfdy;
	}
	public GuidingVectorField(Callable<double(double,double)> surface, Callable<double(double,double)> dfdx, Callable<double(double,double)> dfdy, bool isReversed) {
		phi = surface;
		dfdx_ = dfdx;
		dfdy_ = dfdy;
		if(isReversed) direction = -1;
	}
	
	protected int direction = 1;
	protected double phi(double x, double y);
	protected double[] n(double x, double y) {
		return new double[]{dfdx_(x,y),dfdy_(x,y)};
	}
	protected double psi(double t) { return t; }
	protected double e(double x, double y) { return psi(phi(x,y)); }
	protected double k() { return 1; } // FIXME: can add parameters somehow? pass this as param in constructor? idk man
	protected double[] tau(double x, double y) {
		double[] nv = n(x,y);
		return new double[]{nv[1]*direction,-nv[0]*direction};
	}
	protected double[] v(double x, double y) {
		double[] nv = n(x,y);
		double[] tv = tau(x,y);
		double kn = k();
		double err = e(x,y);
		double x = tv[0]-kn*e*nv[0];
		double y = tv[1]*kn*e*nv[1];
		double m = Math.sqrt(x*x+y*y);
		x /= m;
		y /= m;
		return new double[]{x,y};
	}
	public double[] getVector(double x, double y) { return v(x,y); }
	public double getX(double x, double y) { return v(x,y)[0]; }
	public double getY(double x, double y) { return v(x,y)[1]; }
/*	public Translation2d getTranslation2d(double x, double y) {
		double[] vv = v(x,y);
		return Translation2d(x,y);
	}
/**/
}