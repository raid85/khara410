/*
   This file is part of j2dcg.
   j2dcg is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2 of the License, or
   (at your option) any later version.
   j2dcg is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.
   You should have received a copy of the GNU General Public License
   along with j2dcg; if not, write to the Free Software
   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package controller;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.List;

import model.Shape;

/**
 * <p>Title: ShearXCommand</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004 Jean-Fran�ois Barras, �ric Paquette</p>
 * <p>Company: (�TS) - �cole de Technologie Sup�rieure</p>
 * <p>Created on: 2004-03-19</p>
 * @version $Revision: 1.4 $
 */
public class ShearXCommand extends AnchoredTransformationCommand {

	/**
	 * @param angleDegrees The angle to which the vertical lines will be transformed.
	 * @param anchor one of the predefined positions for the anchor point
	 */
	public ShearXCommand(double angleDegrees, int anchor, List aObjects) {
		super(anchor);
		this.angleDegrees = angleDegrees;
		objects = aObjects;
	}
	
	/* (non-Javadoc)
	 * @see controller.Command#execute()
	 */
	public void execute() {

		Iterator iter = objects.iterator();
		Shape shape;
		Point anchorP = getAnchorPoint(objects) ;
		
		System.out.println("ANCHOR = "+getAnchor());		
		int opposedToAnchor = -1;
		int anchori = getAnchor() ;
		switch (anchori) {
		 case 0: opposedToAnchor = 6;  break;
		 case 1: opposedToAnchor = 7 ;break;
		 case 2: opposedToAnchor = 8 ;break;
		 case 3: opposedToAnchor = 5 ;break;
		 case 4: opposedToAnchor = 1 ;break;
		 case 5: opposedToAnchor = 3 ;break;
		 case 6: opposedToAnchor = 0 ;break;
		 case 7: opposedToAnchor = 1 ;break;
		 case 8: opposedToAnchor = 2 ;	break;	 
		 default: System.out.println("Jai mal a la faceeeeeeeeeeeee");
		 }
		double shx = Math.tan(angleDegrees)*(getAnchorPointC(objects,opposedToAnchor).y-anchorP.getY()) ;
		System.out.println("Shx"+ getAnchorPointC(objects,opposedToAnchor).y);
		
		while(iter.hasNext()){
			shape = (Shape)iter.next();
			mt.addMememto(shape);
			AffineTransform t = shape.getAffineTransform();
			
			t.shear(shx, 0);
			shape.setAffineTransform(t);

			System.out.println("command: shearing on x-axis by " + angleDegrees +
					           " degrees anchored on " + getAnchor());
	}

		// voluntarily undefined
	}

	/* (non-Javadoc)
	 * @see controller.Command#undo()
	 */
	public void undo() {
		mt.setBackMementos();
	}

	private MementoTracker mt = new MementoTracker();
	private List objects;
	private double angleDegrees;

}