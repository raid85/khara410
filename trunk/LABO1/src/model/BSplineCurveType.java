package model;

import java.awt.Point;
import java.util.List;

public class BSplineCurveType extends CurveType{

	public BSplineCurveType(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public int getNumberOfSegments(int numberOfControlPoints) {
		if (numberOfControlPoints >= 4) {
			return (numberOfControlPoints - 1) / 3;
		} else {
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see model.CurveType#getNumberOfControlPointsPerSegment()
	 */
	public int getNumberOfControlPointsPerSegment() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see model.CurveType#getControlPoint(java.util.List, int, int)
	 */
	public ControlPoint getControlPoint(
		List controlPoints,
		int segmentNumber,
		int controlPointNumber) {
		int controlPointIndex = segmentNumber * 3 + controlPointNumber;
		return (ControlPoint)controlPoints.get(controlPointIndex);
	}

	/* (non-Javadoc)
	 * @see model.CurveType#evalCurveAt(java.util.List, double)
	 */
	public Point evalCurveAt(List controlPoints, double t) {
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		List gVector = Matrix.buildColumnVector4(((ControlPoint)controlPoints.get(0)).getCenter(), 
			((ControlPoint)controlPoints.get(1)).getCenter(), 
			((ControlPoint)controlPoints.get(2)).getCenter(),
			((ControlPoint)controlPoints.get(3)).getCenter());
	
		Point p = Matrix.eval(tVector, matrix, gVector);
		
		return p;
	}

	private List bSplineMatrix = 
		//Matrice B-Spline, voir notes de cours.
	    Matrix.buildMatrix4((float)-1/6,  (float)3/6, (float)-3/6, (float)1/6, 
                (float)3/6, (float)-6/6,  (float)3/6, (float)0/6, 
                (float)-3/6,  (float)0/6,  (float)3/6, (float)0/6, 
                 (float)1/6,  (float)4/6,  (float)1/6, (float)0/6);
							 
	private List matrix = bSplineMatrix;

}
