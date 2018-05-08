//package com.team1323.reee;
//import com.team254.lib.util.math;

public class GuidingPolynomialVectorField implements VectorField {

	// Create a directed line that contains the point (x,y) and has a given heading
	public GuidingPolynomialVectorField(double x, double y, double heading) {
		// assume or ensure heading is bounded [0,2pi]
		fc = new double[]{y - Math.tan(heading)*x,Math.tan(heading)};
		setDfc(fc);
		if(heading >= Math.PI) direction = -1;
	}
	public GuidingPolynomialVectorField(double[] coeffs, bool isReversed) {
		fc = coeffs;
		setDfc(fc);
		if(isReversed) direction = -1;
	}
	public GuidingPolynomialVectorField(double[] coeffs) {
		fc = coeffs;
		setDfc(fc);
		}
	protected void setDfc(double[] fc) {
		dfc = new double[fc.length - 1];
		for(int i = 0; i < dfc.length; i++) {
			dfc[i] = fc[i+1]*(i+1);
		}
	}
	protected int direction = 1;
	protected double[] fc; // f[n] is coefficient for x^n term
	protected double[] dfc;
	protected double f(double[] cs, double x) {
		double deg = cs.length-1;
		double val = cs[deg];
		while(deg > 0) {
			val *= x;
			val += cs[--deg];
		}
		return val;
	}

	protected double phi(double x, double y) { return y - f(fc,x); }
	protected double[] n(double x, double y) {
		double[] nv = {-f(dfc,x),1};
		return nv;
	}
	protected double psi(double t) { return t; }
	protected double e(double x, double y) { return psi(phi(x,y)); }
	protected double k() { return 1; } // FIXME: can add parameters somehow? pass this as param in constructor? idk man
	protected double[] tau(double x, double y) {
		double[] nv = n(x,y);
		double[] tv = {nv[1]*direction,-nv[0]*direction};
		return tv;
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
		double[] vv = {x,y};
		return vv;
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