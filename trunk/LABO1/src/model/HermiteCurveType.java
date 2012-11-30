package model;

import java.awt.Point;
import java.util.List;

public class HermiteCurveType extends CurveType {

	public HermiteCurveType(String name) {
		super(name);
	}

	@Override
	public Point evalCurveAt(List controlPoints, double t) {		
		ControlPoint p1 = ((ControlPoint)controlPoints.get(0)) ;
		ControlPoint p4 = ((ControlPoint)controlPoints.get(1)) ;
		
		ControlPoint r1 = new ControlPoint(((ControlPoint)controlPoints.get(2)).getCenter().x - ((ControlPoint)controlPoints.get(0)).getCenter().x,((ControlPoint)controlPoints.get(2)).getCenter().y -((ControlPoint)controlPoints.get(0)).getCenter().y) ;	
		ControlPoint r4 = new ControlPoint(((ControlPoint)controlPoints.get(3)).getCenter().x - ((ControlPoint)controlPoints.get(1)).getCenter().x,((ControlPoint)controlPoints.get(3)).getCenter().y -((ControlPoint)controlPoints.get(1)).getCenter().y);
		System.out.println("p1 : " +p1.getCenter().toString() );
		System.out.println("p4 : " +p4.getCenter().toString() );
		System.out.println("clic3 : " +((ControlPoint)controlPoints.get(2)).getCenter().toString());
		System.out.println("clic4 : " +((ControlPoint)controlPoints.get(3)).getCenter().toString());
		System.out.println("r1 = " +r1.getCenter().toString() );
		System.out.println("r4 = " +r4.getCenter().toString() );
		
		
		List tVector = Matrix.buildRowVector4(t*t*t, t*t, t, 1);
		List gVector = Matrix.buildColumnVector4(p1.getCenter(),p4.getCenter(),r1.getCenter(),r4.getCenter());
			
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
