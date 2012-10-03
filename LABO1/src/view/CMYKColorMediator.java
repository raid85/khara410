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

package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;
/**Cette classe représente le panneau de sélection dans l'espace de couleur CMYK**/
class CMYKColorMediator extends Object implements SliderObserver, ObserverIF {	

	// Les sliders
	ColorSlider cyanCS;
	ColorSlider magentaCS;
	ColorSlider yellowCS;
	ColorSlider keyCS ;
	// Les valeurs de l'espace couleur
	double cyan;
	double magenta;
	double yellow;
	double key ;	
	//Les images derrière les sliders
	BufferedImage cyanImage;
	BufferedImage magentaImage;
	BufferedImage yellowImage;
	BufferedImage keyImage;	
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;

	CMYKColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {

		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		double[] cmyk = new double[4];
		//Conversion en CMYK des valeurs reçues en RGB
		cmyk = rgb2cmyk(result.getPixel().getRed(),result.getPixel().getGreen(),result.getPixel().getBlue());

		this.cyan = cmyk[0];		
		this.magenta = cmyk[1];
		this.yellow = cmyk[2];
		this.key = cmyk[3];		


		this.result = result;

		result.addObserver(this);

		//Ajouts des couleurs requises pour les images des sélecteurs de CMYK

		cyanImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		magentaImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		yellowImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		keyImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);

		//création des images  CMYK

		computeCyanImage(cyan, magenta, yellow,key);
		computeMagentaImage(cyan, magenta, yellow,key);
		computeYellowImage(cyan, magenta, yellow,key);
		computeKeyImage (cyan, magenta, yellow,key);	
	}




	private void computeCyanImage(double cyan2, double magenta2, double yellow2, double key2) {

		int[] rgb2 = new int[3];
		rgb2 = cmyk2rgb(cyan2, magenta2, yellow2, key2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				cyanImage.setRGB(i, j, rgb);
			}
		}
		if (cyanCS != null) {
			cyanCS.update(cyanImage);
		}

	}

	private void computeMagentaImage(double cyan2, double magenta2, double yellow2,
			double key2) {

		int[] rgb2 = new int[3];
		rgb2 = cmyk2rgb(cyan2, magenta2, yellow2, key2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				magentaImage.setRGB(i, j, rgb);
			}
		}
		if (magentaCS != null) {
			magentaCS.update(magentaImage);
		}

	}

	private void computeYellowImage(double cyan2, double magenta2, double yellow2,
			double key2) {

		int[] rgb2 = new int[3];
		rgb2 = cmyk2rgb(cyan2, magenta2, yellow2, key2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setBlue((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				yellowImage.setRGB(i, j, rgb);
			}
		}
		if (yellowCS != null) {
			yellowCS.update(yellowImage);
		}

	}

	private void computeKeyImage(double cyan2, double magenta2, double yellow2, double key2) {

		int[] rgb2 = new int[3];
		rgb2 = cmyk2rgb(cyan2, magenta2, yellow2, key2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setAlpha((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				keyImage.setRGB(i, j, rgb);
			}
		}
		if (keyCS != null) {
			keyCS.update(keyImage);
		}

	}


	private double[] rgb2cmyk (int R, int G,int B) {

		double C = 0;
		double M = 0;
		double Y = 0;
		double K = 0;
		double [] cmyk = new double[4];

		// BLACK
		if ( R ==0 && G ==0 && B ==0) {
			K = 1;
			cmyk[0] = 0;
			cmyk[1] = 0 ;
			cmyk[2] = 0 ;
			cmyk[3] = 1 ;

			return cmyk;
		}

		C = 1.0 - (R/255.0);
		M = 1.0 - (G/255.0);
		Y = 1.0 - (B/255.0);

		double minCMY = Math.min(C,Math.min(M,Y));
		C = (C - minCMY) / (1 - minCMY) ;
		M = (M - minCMY) / (1 - minCMY) ;
		Y = (Y - minCMY) / (1 - minCMY) ;
		K = minCMY;

		cmyk[0] = C ; 
		cmyk[1] = M ;
		cmyk[2] = Y ;
		cmyk[3] = K ;

		return cmyk;

	}

	private int[] cmyk2rgb (double C,double M,double Y,double K){		

		int[]rgb = new int[3] ;

		C = ( C * ( 1 - K ) + K );
		M = ( M * ( 1 - K ) + K );
		Y = ( Y * ( 1 - K ) + K );	

		rgb[0] = (int) (( 1 - C ) * 255);
		rgb[1] = (int) (( 1 - M ) * 255);
		rgb[2] = (int) (( 1 - Y ) * 255);


		return rgb;

	}

	public void update(ColorSlider s, int v) {

		boolean updateCyan = false;
		boolean updateMagenta = false;
		boolean updateYellow = false;
		boolean updateKey = false;

		if (s == cyanCS && v != (int)(cyan*255)) {			
			cyan = v/255.0;
			updateMagenta = true;
			updateYellow = true;
			updateKey = true ;
		}
		if (s == magentaCS && v !=(int) (magenta*255)) {			
			magenta = v/255.0;
			updateCyan = true;
			updateYellow = true;
			updateKey = true ;
		}
		if (s == yellowCS && v != (int)(yellow*255)) {		
			yellow = v/255.0;
			updateCyan = true;
			updateMagenta = true;
			updateKey = true ;
		}
		if (s == keyCS && v != (int)(key*255)) {		
			key = v/255.0;
			updateCyan = true;
			updateMagenta = true;
			updateYellow = true ;
		}
		if (updateCyan) {
			computeCyanImage(cyan,magenta,yellow,key);
		}
		if (updateMagenta) {
			computeMagentaImage(cyan,magenta,yellow,key);
		}
		if (updateYellow) {
			computeYellowImage(cyan,magenta,yellow,key);
		}
		if (updateKey) {
			computeKeyImage(cyan,magenta,yellow,key);
		}
		int [] rgb = new int [3];

		rgb = cmyk2rgb (cyan,magenta,yellow,key);


		Pixel pixel = new Pixel(rgb[0], rgb[1], rgb[2], 255);
		result.setPixel(pixel);
	}


	public void update() {
		// When updated with the new "result" color, if the "currentColor"
		// is aready properly set, there is no need to recompute the images.

		int [] rgb = new int [3];
		rgb = cmyk2rgb(cyan,magenta,yellow,key);
		Pixel currentColor = new Pixel(rgb[0], rgb[1], rgb[2], 255);

		if(currentColor.getARGB() == result.getPixel().getARGB()) return;

		double cmyk[] = new double[4] ;
		//Conversion des valeur reçues
		cmyk = rgb2cmyk(result.getPixel().getRed(),result.getPixel().getGreen(),result.getPixel().getBlue());

		cyan = cmyk[0];
		magenta = cmyk[1];
		yellow = cmyk[2];
		key = cmyk[3];
		// On place les curseur à la bonne position selon la valeur reçue
		cyanCS.setValue((int) (cyan*255.0));
		magentaCS.setValue((int) (magenta*255.0));
		yellowCS.setValue((int) (yellow*255.0));
		keyCS.setValue((int)(key*255.0));

		computeCyanImage(cyan,magenta,yellow,key);
		computeYellowImage(cyan,magenta,yellow,key);
		computeMagentaImage(cyan,magenta,yellow,key);
		computeKeyImage (cyan,magenta,yellow,key);

	}

	public BufferedImage getCyanImage() {
		return cyanImage;
	}


	public BufferedImage getMagentaImage() {
		return magentaImage;
	}

	public BufferedImage getYellowImage() {
		return yellowImage;
	}

	public BufferedImage getKeyImage() {
		return keyImage;
	}

	public void setCyanCS(ColorSlider slider) {
		cyanCS = slider;
		slider.addObserver(this);
	}

	public void setMagentaCS(ColorSlider slider) {
		magentaCS = slider;
		slider.addObserver(this);
	}

	public void setYellowCS(ColorSlider slider) {
		yellowCS = slider;
		slider.addObserver(this);
	}

	public void setKeyCS(ColorSlider slider) {
		keyCS = slider;
		slider.addObserver(this);
	}

	public double getCyan() {
		return cyan;
	}

	public double getMagenta() {
		return magenta;
	}

	public double getYellow() {
		return yellow;
	}

	public double getKey() {
		return key;
	}




}

