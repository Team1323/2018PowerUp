//package com.team1323.reee;
//import com.team254.lib.util.math;

public class ConstantVectorField implements IVectorField {
	public ConstantVectorField(Translation2d whichWay) {
		thatWay = whichWay.scale(1/whichWay.norm());
	}
	
	protected Translation2d thatWay;
	
	public Translation2d getVector(Translation2d here) {
		return thatWay;
	}
	public Translation2d getVector() {
		return thatWay;
	}
}
