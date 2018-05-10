//package com.team1323.reee;
//import com.team254.lib.util.math;

public class GuidingPolynomialVectorField implements IVectorField {

	// Create a directed line that contains the point (x,y) and has a given heading
	public GuidingPolynomialVectorField(Translation2d where, Rotation2d heading) {
		fc = new double[]{where.y() - heading.tan()*where.x(),heading.tan()};
		setDfc(fc);
		if(heading.getRadians() >= Math.PI) direction = -1;
	}
	public GuidingPolynomialVectorField(double[] coeffs, boolean isReversed) {
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

	protected double phi(Translation2d here) { return here.y() - f(fc,here.x()); }
	protected Translation2d n(Translation2d here) { return new Translation2d(-f(dfc,here.x()),1); }
	protected double psi(double t) { return t; }
	protected double e(Translation2d here) { return psi(phi(here)); }
	protected double k() { return 1; } // FIXME: can add parameters somehow? pass this as param in constructor? idk man
	protected Translation2d tau(Translation2d here) {
		Translation2d nv = n(here);
		return new Translation2d(nv.y()*direction,-nv.x()*direction);
	}
	public Translation2d getVector(Translation2d here) {
		Translation2d vv = new Translation2d(nv(here).scale(k()*e(here)),tau(here));
		return vv.scale(1/vv.norm());
	}
}
