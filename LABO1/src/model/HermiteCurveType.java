package model;

import java.awt.Point;
import java.util.List;

public class HermiteCurveType extends CurveType {

	public HermiteCurveType(String name) {
		super(name);
	}

	@Override
	public Point evalCurveAt(List controlPoints, double t) {		
		
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		List gVector = Matrix.buildColumnVector4(((ControlPoint)controlPoints.get(0)).getCenter(), 
				((ControlPoint)controlPoints.get(1)).getCenter(), 
				((ControlPoint)controlPoints.get(2)).getCenter(),
				((ControlPoint)controlPoints.get(3)).getCenter());
		System.out.println("Coordon�es : " + gVector );	
		Point p = Matrix.eval(tVector, hermiteMatrix, gVector);
			return p;
		
	}

	@Override
	public ControlPoint getControlPoint(List controlPoints, int segmentNumber,
			int controlPointNumber) {
		int controlPointIndex = segmentNumber * 3 + controlPointNumber;
		return (ControlPoint)controlPoints.get(controlPointIndex);
		
	}

	@Override
	public int getNumberOfControlPointsPerSegment() {
		return 4;
	}

	@Override
	public int getNumberOfSegments(int numberOfControlPoints) {
		if (numberOfControlPoints >= 4) {
			return (numberOfControlPoints - 1) / 3;
		} else {
			return 0;
		}
	}
	
	private List hermiteMatrix = 
		Matrix.buildMatrix4(2,  -2, 1, 1, 
							 -3, 3,  -2, -1, 
							0,  0,  1, 0, 
							 1,  0,  0, 0);
							 
	private List matrix = hermiteMatrix;

}
