//package com.team1323.reee;
//import com.team254.lib.util.math;
import java.util.function;

public class GuidingVectorField implements IVectorField {
	// Implement as e.g.
	// GuidingVectorField(Translation2d here -> here.x()+here.y(), ... )
	public GuidingVectorField(Function<Translation2d,double> surface,
				  Function<Translation2d,double> dfdx,
				  Function<Translation2d,double> dfdy) {
		phi = surface;
		dfdx_ = dfdx;
		dfdy_ = dfdy;
	}
	public GuidingVectorField(Function<Translation2d,double> surface,
				  Function<Translation2d,double> dfdx,
				  Function<Translation2d,double>dfdy,
				  boolean isReversed) {
		phi = surface;
		dfdx_ = dfdx;
		dfdy_ = dfdy;
		if(isReversed) direction = -1;
	}
	
	protected int direction = 1;
	protected Function<Translation2d,double> phi;
	protected Translation2d n(Translation2d here) {
		return new Translation2d(dfdx_.apply(here),dfdy_.apply(here));
	}
	protected double psi(double t) { return t; } // FIXME: pass this in constructor, definitely (type is known)
	protected double e(Translation2d here) { return psi(phi.apply(here)); }
	protected double k() { return 1; } // FIXME: can add parameters somehow? pass this as param in constructor? idk man
	protected Translation2d tau(Translation2d here) {
		Translation2d nv = n(here);
		return new Translation2d(nv.y()*direction,-nv.x()*direction);
	}
	public Translation2d getVector(Translation2d here) {
		Translation2d vv = new Translation2d(n(here).scale(k()*e(here)),tau(here));
		return vv.scale(1/vv.norm());
	}
}
