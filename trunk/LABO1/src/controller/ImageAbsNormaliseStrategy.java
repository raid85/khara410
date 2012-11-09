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
 * <p>Title: ImageClampStrategy</p>
 * <p>Description: Image-related strategy</p>
 * <p>Copyright: Copyright (c) 2004 Colin Barré-Brisebois, Eric Paquette</p>
 * <p>Company: ETS - École de Technologie Supérieure</p>
 * @author unascribed
 * @version $Revision: 1.8 $
 */
public class ImageAbsNormaliseStrategy extends ImageConversionStrategy {
	/**
	 * Converts an ImageDouble to an ImageX using a clamping strategy (0-255).
	 */
	public ImageX convert(ImageDouble image) {
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageX newImage = new ImageX(0, 0, imageWidth, imageHeight);
		PixelDouble curPixelDouble = null;

		//Pixel temporaire utilisé pour définir la valeur maximale des canaux RGBA
		PixelDouble maxPixelTemp = null ;
		//Valeurs maximales 
		double maxRed =-10000 ;	
		double maxBlue = -10000 ;
		double maxGreen = -10000 ;
		double maxAlpha = -10000 ;
		
		//Boucle qui parcourt l'image pour déterminer la plus haute valeur pour chaque canal RGBA
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				maxPixelTemp = image.getPixel(x,y);
				if(Math.abs(maxPixelTemp.getRed())> maxRed){maxRed = Math.abs(maxPixelTemp.getRed());}
				if(Math.abs(maxPixelTemp.getGreen())> maxGreen){maxGreen = Math.abs(maxPixelTemp.getGreen());}
				if(Math.abs(maxPixelTemp.getBlue())> maxBlue){maxBlue = Math.abs(maxPixelTemp.getBlue());}
				if(Math.abs(maxPixelTemp.getAlpha())> maxAlpha){maxAlpha = Math.abs(maxPixelTemp.getAlpha());}
			}
		}
		System.out.println("Max red "+maxRed);
		System.out.println("Max green "+maxGreen);
		System.out.println("Max blue "+maxBlue);
		System.out.println("Max Alpha "+maxAlpha);
		
		//Facteurs de normalisation
		double redFactor = 255.0 /maxRed ;
		double greenFactor = 255.0 /maxGreen ;
		double blueFactor = 255.0 / maxBlue ;
		double alphaFactor = 255.0 /maxAlpha ;
		


		newImage.beginPixelUpdate();
		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				curPixelDouble = image.getPixel(x,y);

				newImage.setPixel(x, y, new Pixel((int)(Math.round((curPixelDouble.getRed()))*redFactor),
						(int)(Math.round(curPixelDouble.getGreen())*greenFactor),
						(int)(Math.round(curPixelDouble.getBlue())*blueFactor),
						(int)(Math.round(curPixelDouble.getAlpha())*alphaFactor)));
			}
		}
		newImage.endPixelUpdate();
		return newImage;
	}


}
