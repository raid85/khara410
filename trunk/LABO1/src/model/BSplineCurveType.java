package model;

import java.awt.Point;
import java.util.List;

public class BSplineCurveType extends CurveType{

	public BSplineCurveType(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Point evalCurveAt(List controlPoints, double t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ControlPoint getControlPoint(List controlPoints, int segmentNumber,
			int controlPointNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfControlPointsPerSegment() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfSegments(int numberOfControlPoints) {
		// TODO Auto-generated method stub
		return 0;
	}

}
