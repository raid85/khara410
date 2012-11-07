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

/**
 * <p>Title: PaddingMirrorStrategy</p>
 * <p>Description: Padding strategy where the values of the nearest pixel (mirroring)
 *  are returned if Pixel values are out of range.</p>
 * <p>Copyright: Copyright (c) 2003 Colin Barré-Brisebois, Éric Paquette</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author Riad Chebli
 * @version $Revision: 1.0 $
 */
public class PaddingMirrorStrategy extends PaddingStrategy {
	Pixel zeroPixel = new Pixel(0,0,0,0);
	PixelDouble zeroPixelDouble = new PixelDouble(0,0,0,0);

	/**
	 * Returns and validates the Pixel at the specified coordinate.
	 * If the Pixel is invalid, a new pixel with same color then the correspondant pixel in the image (mirroribng)
	 * @param image source Image
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the validated Pixel value at the specified coordinates 
	 */
	public Pixel pixelAt(ImageX image, int x, int y) {
		int returnedX = 0 ;
		int returnedY = 0 ;


		if ((x > 0) && (x < image.getImageWidth()) &&
				(y > 0) && (y < image.getImageHeight())) {
			return image.getPixel(x, y);
		} else {

			if(x > image.getImageWidth()){returnedX = x- image.getImageWidth() ;}
			if (y > image.getImageHeight()){returnedY = y - image.getImageHeight() ;}			
			if(x<0){returnedX = Math.abs(x);}
			if (y<0){returnedY = Math.abs(y);}
			
			return image.getPixel(returnedX,returnedY);
		}
	}


	/**
	 * Returns and validates the PixelDouble at the specified coordinate.
	 * Original Pixel is converted to PixelDouble.
	 * If the Pixel is invalid, a new black (0,0,0,0) PixelDouble is returned.
	 * @param image source ImageDouble
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the validated PixelDouble value at the specified coordinates
	 */	
	public PixelDouble pixelAt(ImageDouble image, int x, int y) {
		PixelDouble pixel = null;
		int returnedX = 0 ;
		int returnedY = 0 ;

		if ((x >= 0) && (x < image.getImageWidth()) &&
				(y >= 0) && (y < image.getImageHeight())) {
			return image.getPixel(x, y);
		} else {
			if(x > image.getImageWidth()){returnedX = x- image.getImageWidth() ;}
			if (y > image.getImageHeight()){returnedY = y - image.getImageHeight() ;}			
			if(x<0){returnedX = Math.abs(x);}
			if (y<0){returnedY = Math.abs(y);}

			return image.getPixel(returnedX,returnedY);
		}
	}
}
