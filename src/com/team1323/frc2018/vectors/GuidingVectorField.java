//package com.team1323.reee;
//import com.team254.lib.util.math;

public class GuidingVectorField implements VectorField {
// FIXME: How do we specify a callable? Can I assign one to a member? How? reeeee
	public GuidingVectorField(Callable<double(Translation2d)> surface, Callable<double(Translation2d)> dfdx, Callable<double(Translation2d)> dfdy) {
		phi = surface;
		dfdx_ = dfdx;
		dfdy_ = dfdy;
	}
	public GuidingVectorField(Callable<double(Translation2d)> surface, Callable<double(Translation2d)> dfdx, Callable<double(Translation2d)> dfdy, bool isReversed) {
		phi = surface;
		dfdx_ = dfdx;
		dfdy_ = dfdy;
		if(isReversed) direction = -1;
	}
	
	protected int direction = 1;
	protected double phi(Translation2d here);
	protected Translation2d n(Translation2d here) {
		return new Translation2d(dfdx_(here),dfdy_(here));
	}
	protected double psi(double t) { return t; }
	protected double e(Translation2d here) { return psi(phi(here)); }
	protected double k() { return 1; } // FIXME: can add parameters somehow? pass this as param in constructor? idk man
	protected Translation2d tau(Translation2d here) {
		Translation2d nv = n(here);
		return new Translation2d(nv.y()*direction,-nv.x()*direction);
	}
	public Translation2d getVector(Translation2d here) {
		Translation2d nv = n(here);
		Translation2d tv = tau(here);
		double kn = k();
		double err = e(here);
		double x = tv.x()-kn*e*nv.x();
		double y = tv.y()-kn*e*nv.y();
		Translation2d vv = new Translation2d(nv.scale(kn*e),tv);
		return vv.scale(1/vv.norm());
	}
}
