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
import model.*;

import java.awt.Point;
import java.awt.event.*;
import java.awt.geom.NoninvertibleTransformException;
import java.util.List;
import java.util.Stack;

/**
 * <p>Title: ImageLineFiller</p>
 * <p>Description: Image transformer that inverts the row color</p>
 * <p>Copyright: Copyright (c) 2003 Colin Barré-Brisebois, Éric Paquette</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author unascribed
 * @version $Revision: 1.12 $
 */
/**
 * @author ah45290
 *
 */
public class ImageLineFiller extends AbstractTransformer {
	private ImageX currentImage;
	private int currentImageWidth;
	private Pixel fillColor = new Pixel(0xFF00FFFF);
	private Pixel borderColor = new Pixel(0xFFFFFF00);
	private Pixel currentColor = new Pixel(0xFFFFFF00);
	private boolean floodFill = true;
	private int hueThreshold = 1;
	private int saturationThreshold = 2;
	private int valueThreshold = 3;

	/**
	 * Creates an ImageLineFiller with default parameters.
	 * Default pixel change color is black.
	 */
	public ImageLineFiller() {
	}

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_FLOODER; } 

	protected boolean mouseClicked(MouseEvent e){
		List intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
		if (!intersectedObjects.isEmpty()) {
			Shape shape = (Shape)intersectedObjects.get(0);
			if (shape instanceof ImageX) {
				currentImage = (ImageX)shape;
				currentImageWidth = currentImage.getImageWidth();

				Point pt = e.getPoint();
				Point ptTransformed = new Point();
				try {
					shape.inverseTransformPoint(pt, ptTransformed);
				} catch (NoninvertibleTransformException e1) {
					e1.printStackTrace();
					return false;
				}
				ptTransformed.translate(-currentImage.getPosition().x, -currentImage.getPosition().y);
				if (0 <= ptTransformed.x && ptTransformed.x < currentImage.getImageWidth() &&
						0 <= ptTransformed.y && ptTransformed.y < currentImage.getImageHeight()) {
					currentImage.beginPixelUpdate();
					//horizontalLineFill(ptTransformed);
					if (floodFill){floodFill(ptTransformed);}
					else boundFill(ptTransformed);
				}
				currentImage.endPixelUpdate();											 	
				return true;
			}
		}

		return false;}

	
	/**
	 * @param ptClicked
	 * Fonction qui effectue le remplissage en se basant sur une couleur de bordure prédéterminée, méthode de 
	 * l'algorithme "Boundary Fill" avec la visite de 4 pixels voisins. L'algorithme rempli avec la fillColor choisie
	 * @author ah45290
	 */
	public void boundFill(Point ptClicked){ 

		int[] rgb = new int[3];
		rgb = hsv2rgb((float)hueThreshold/180,(float)saturationThreshold/255,(float)valueThreshold/255);
		//      ***CONVERSION CHECKED		
		//		System.out.println("BORDER COLOR :"+borderColor.toString());
		//		System.out.println("RGB THRESHOLD : "+rgb[0]+"..."+rgb[1]+"..."+rgb[2]);
		int rmax = borderColor.getRed()+rgb[0];
		int rmin = borderColor.getRed()-rgb[0];		
		int gmax = borderColor.getGreen()+rgb[1];
		int gmin = borderColor.getGreen()-rgb[1];
		int bmax = borderColor.getBlue()+rgb[2];
		int bmin = borderColor.getBlue()-rgb[2];	
		if (rmin<0) rmin= 0 ;
		if (gmin<0) gmin= 0 ;
		if (bmin<0) bmin= 0 ;
		if (rmax>255) rmax= 255 ;
		if (gmax>255) gmax= 255 ;
		if (bmax>255) bmax= 255 ;
		//		System.out.println("COLOR RANGE FOR R : "+rmin+" < "+borderColor.getRed()+" > "+rmax);
		//		System.out.println("COLOR RANGE FOR G : "+gmin+" < "+borderColor.getGreen()+" > "+gmax);
		//		System.out.println("COLOR RANGE FOR B : "+bmin+" < "+borderColor.getBlue()+" > "+bmax);

		Stack<Point> stack = new Stack<Point>();
		stack.push(ptClicked);

		while (!stack.empty()) {
			Point current = stack.pop();				

			//Si les coordonnées sont à l'intérieur des limites de l'image et si les coordonnées de ce pixel ne sont pas déjà de la couleur
			//qu'on allait utiliser pour remplir
			if (0 <= current.x && current.x < currentImage.getImageWidth() && 0 < current.y && current.y < (currentImage.getImageHeight()-1)&&
					!currentImage.getPixel(current.x, current.y).equals(fillColor)) {
				currentImage.setPixel(current.x, current.y, fillColor);

				// On met le voisin dans la pile si sa couleur est dans la plage tolérée définie par le threshold et la couleur de  bordure choisie		
				if(!((currentImage.getPixel(current.x-1, current.y).getRed()<=rmax) &&(currentImage.getPixel(current.x-1, current.y).getRed()>=rmin))
						||!((currentImage.getPixel(current.x-1, current.y).getGreen()<=gmax)&& (currentImage.getPixel(current.x-1, current.y).getGreen()>=gmin))
						||!((currentImage.getPixel(current.x-1, current.y).getBlue()<=bmax) && (currentImage.getPixel(current.x-1, current.y).getBlue()>=bmin))){

					Point nextLeft = new Point(current.x-1, current.y);

					stack.push(nextLeft);
				}
				if(!((currentImage.getPixel(current.x+1, current.y).getRed()<=rmax) && (currentImage.getPixel(current.x+1, current.y).getRed()>=rmin))
						||!((currentImage.getPixel(current.x+1, current.y).getGreen()<=gmax) &&(currentImage.getPixel(current.x+1, current.y).getGreen()>=gmin))
						||!((currentImage.getPixel(current.x+1, current.y).getBlue()<=bmax) &&(currentImage.getPixel(current.x+1, current.y).getBlue()>=bmin))){
					Point nextRight = new Point(current.x+1, current.y);
					stack.push(nextRight);
				}
				if(!((currentImage.getPixel(current.x, current.y-1).getRed()<=rmax) &&(currentImage.getPixel(current.x, current.y-1).getRed()>=rmin))
						||!((currentImage.getPixel(current.x, current.y-1).getGreen()<=gmax) &&(currentImage.getPixel(current.x, current.y-1).getGreen()>=gmin))
						||!((currentImage.getPixel(current.x, current.y-1).getBlue()<=bmax) &&(currentImage.getPixel(current.x, current.y-1).getBlue()>=bmin))){
					Point nextUp = new Point (current.x,current.y-1);
					stack.push(nextUp);
				}
				if(!((currentImage.getPixel(current.x, current.y+1).getRed()<=rmax) &&(currentImage.getPixel(current.x, current.y+1).getRed()>=rmin))
						||!((currentImage.getPixel(current.x, current.y+1).getGreen()<=gmax) &&(currentImage.getPixel(current.x, current.y+1).getGreen()>=gmin))
						||!((currentImage.getPixel(current.x, current.y+1).getBlue()<=bmax) &&(currentImage.getPixel(current.x, current.y+1).getBlue()>=bmin))){
					Point nextDown = new Point (current.x,current.y+1);
					stack.push(nextDown);
				}
			}
		}
	}

	
	/**
	 * @param ptClicked
	 * Fonction qui , à partir du point cliqué rempli les pixels voisins (méthode des 4 pixels)
	 * avec la fillColor choisie par l'utilisateur avec l'algorithme de remplissage "Flood Fill"
	 * @author ah45290
	 */
	private void floodFill(Point ptClicked) {
	
		Stack<Point> stack = new Stack<Point>();
		stack.push(ptClicked);
		currentColor = currentImage.getPixel(ptClicked.x, ptClicked.y);
	
	
		while (!stack.empty()) {
			Point current = stack.pop();
			if (0 <= current.x && current.x < currentImage.getImageWidth() && 0 < current.y && current.y < (currentImage.getImageHeight()-1)&&
					!currentImage.getPixel(current.x, current.y).equals(fillColor)) {
				currentImage.setPixel(current.x, current.y, fillColor);
	
				// Next points to fill.			
				if(currentImage.getPixel(current.x-1, current.y).equals(currentColor)){
					Point nextLeft = new Point(current.x-1, current.y);
					stack.push(nextLeft);
				}
				if(currentImage.getPixel(current.x+1, current.y).equals(currentColor)){
					Point nextRight = new Point(current.x+1, current.y);
					stack.push(nextRight);
				}
				if(currentImage.getPixel(current.x, current.y-1).equals(currentColor)){
					Point nextUp = new Point (current.x,current.y-1);
					stack.push(nextUp);
				}
				if(currentImage.getPixel(current.x, current.y+1).equals(currentColor)){
					Point nextDown = new Point (current.x,current.y+1);
					stack.push(nextDown);
				}
			}
		}
	}

	/**
	 * @param h 
	 * @param s
	 * @param v
	 * @return int [3] RGB
	 * Fonction qui reçoit les paramètres HSV normalisée (0...1)
	 * en paramètres et renvoie l'équivalent RGB en valeur de 0 à 255 
	 * @author ah45290
	 */
	private int [] hsv2rgb (float h, float s, float v) {

		// h,s,v in [0,1]
		float rr = 0, gg = 0, bb = 0;
		float hh = (6 * h) % 6;                 
		int   c1 = (int) hh;                     
		float c2 = hh - c1;
		float x = (1 - s) * v;
		float y = (1 - (s * c2)) * v;
		float z = (1 - (s * (1 - c2))) * v;	
		switch (c1) {
		case 0: rr=v; gg=z; bb=x; break;
		case 1: rr=y; gg=v; bb=x; break;
		case 2: rr=x; gg=v; bb=z; break;
		case 3: rr=x; gg=y; bb=v; break;
		case 4: rr=z; gg=x; bb=v; break;
		case 5: rr=v; gg=x; bb=y; break;
		}
		int N = 256;
		int r = Math.min(Math.round(rr*N),N-1);
		int g = Math.min(Math.round(gg*N),N-1);
		int b = Math.min(Math.round(bb*N),N-1);



		int [] rgb = new int[3];
		rgb[0] = r ;
		rgb[1] = g;
		rgb[2] = b ;
		return rgb;
	}


	/**
	 * @return Color 
	 */
	public Pixel getBorderColor() {
		return borderColor;
	}

	/**
	 * @return Color
	 */
	public Pixel getFillColor() {
		return fillColor;
	}

	/**
	 * @param pixel
	 */
	public void setBorderColor(Pixel pixel) {
		borderColor = pixel;
		System.out.println("new border color");
	}

	/**
	 * @param pixel
	 */
	public void setFillColor(Pixel pixel) {
		fillColor = pixel;
		System.out.println("new fill color");
	}
	/**
	 * @return true if the filling algorithm is set to Flood Fill, false if it is set to Boundary Fill.
	 */
	public boolean isFloodFill() {
		return floodFill;
	}

	/**
	 * @param b set to true to enable Flood Fill and to false to enable Boundary Fill.
	 */
	public void setFloodFill(boolean b) {
		floodFill = b;
		if (floodFill) {
			System.out.println("now doing Flood Fill");
		} else {
			System.out.println("now doing Boundary Fill");
		}
	}

	/**
	 * @return
	 */
	public int getHueThreshold() {
		return hueThreshold;
	}

	/**
	 * @return
	 */
	public int getSaturationThreshold() {
		return saturationThreshold;
	}

	/**
	 * @return
	 */
	public int getValueThreshold() {
		return valueThreshold;
	}

	/**
	 * @param i
	 */
	public void setHueThreshold(int i) {
		hueThreshold = i;
		System.out.println("new Hue Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setSaturationThreshold(int i) {
		saturationThreshold = i;
		System.out.println("new Saturation Threshold " + i);
	}

	/**
	 * @param i
	 */
	public void setValueThreshold(int i) {
		valueThreshold = i;
		System.out.println("new Value Threshold " + i);
	}

}
