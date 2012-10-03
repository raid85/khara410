
package view;

import java.awt.image.BufferedImage;

import model.ObserverIF;
import model.Pixel;

class HSVColorMediator extends Object implements SliderObserver, ObserverIF {	


	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;

	double hue;
	double saturation;
	double value;

	BufferedImage hueImage;
	BufferedImage saturationImage;
	BufferedImage valueImage;

	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;

	HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {

		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		//Test fragment
		//		System.out.println("Result RGB VALUE :"+result.getPixel().toString());


		double[] hsv = new double[3];
		hsv = rgb2hsv(result.getPixel().getRed(),result.getPixel().getGreen(),result.getPixel().getBlue());

		//TESTS
//				for(int i=0 ; i<hsv.length ;i++){
//					System.out.println("HSV["+i+"] = "+hsv[i]);}
//				System.out.println("**-=...Output from...=-**"+this.getClass().getName());

		this.hue = hsv[0];		
		this.saturation = hsv[1];
		this.value = hsv[2];



		this.result = result;

		result.addObserver(this);

		//Ajouts des couleurs requises pour les images des sélecteurs 

		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);


		//création des images  

		computeHueImage(hue, saturation, value);
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value);

	}




	private void computeHueImage(double hue2, double saturation2, double value2) {

		int[] rgb2 = new int[3];
		rgb2 = hsv2rgb((float) hue2, (float)saturation2,(float) value2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				hueImage.setRGB(i, j, rgb);
			}
		}
		if (hueCS != null) {
			hueCS.update(hueImage);
		}

	}

	private void computeSaturationImage(double hue2, double saturation2, double value2) {

		int[] rgb2 = new int[3];
		rgb2 = hsv2rgb((float) hue2, (float) saturation2, (float) value2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);
		}

	}

	private void computeValueImage(double hue2, double saturation2, double value2) {

		int[] rgb2 = new int[3];
		rgb2 = hsv2rgb((float)hue2, (float)saturation2, (float)value2)  ;               
		Pixel p = new Pixel(rgb2[0],rgb2[1],rgb2[2], 255); 

		for (int i = 0; i<imagesWidth; ++i) {
			p.setBlue((int)(((double)i / (double)imagesWidth)*255.0)); 
			int rgb = p.getARGB();

			for (int j = 0; j<imagesHeight; ++j) {
				valueImage.setRGB(i, j, rgb);
			}
		}
		if (valueCS != null) {
			valueCS.update(valueImage);
		}

	}

	/**
	 * This sample code is made available as part of the book "Digital Image
	 * Processing - An Algorithmic Introduction using Java" by Wilhelm Burger
	 * and Mark J. Burge, Copyright (C) 2005-2008 Springer-Verlag Berlin, 
	 * Heidelberg, New York.
	 * Note that this code comes with absolutely no warranty of any kind.
	 * See http://www.imagingbook.com for details and licensing conditions.
	 * 
	 * Date: 2007/11/10
	 */

	private static double[] rgb2hsv (int R, int G,int B) {
		
		double[] hsv = new double[3];
		// R,G,B in [0,255]
		float H = 0, S = 0, V = 0;
		float cMax = 255.0f;
		int cHi = Math.max(R,Math.max(G,B));	// highest color value
		int cLo = Math.min(R,Math.min(G,B));	// lowest color value
		int cRng = cHi - cLo;				    // color range

		// compute value V
		V = cHi / cMax;

		// compute saturation S
		if (cHi > 0)
			S = (float) cRng / cHi;

		// compute hue H
		if (cRng > 0) {	// hue is defined only for color pixels
			float rr = (float)(cHi - R) / cRng;
			float gg = (float)(cHi - G) / cRng;
			float bb = (float)(cHi - B) / cRng;
			float hh;
			if (R == cHi)                      // r is highest color value
				hh = bb - gg;
			else if (G == cHi)                 // g is highest color value
				hh = rr - bb + 2.0f;
			else                               // b is highest color value
				hh = gg - rr + 4.0f;
			if (hh < 0)
				hh= hh + 6;
			H = hh / 6;
		}


		hsv[0] = H; 
		hsv[1] = S; 
		hsv[2] = V;
		return hsv;

		//http://mjijackson.com/2008/02/rgb-to-hsl-and-rgb-to-hsv-color-model-conversion-algorithms-in-javascript

	}

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
	

	public void update(ColorSlider s, int v) {

		boolean updatehue = false;
		boolean updatesaturation = false;
		boolean updatevalue = false;


		if (s == hueCS && v != (int)(hue*255)) {
			//System.out.println("vhue = "+v);
			hue = v/255.0;
			updatesaturation = true;
			updatevalue = true;

		}
		if (s == saturationCS && v !=(int) (saturation*255)) {
			//System.out.println("vsaturation = "+v);
			saturation = v/255.0;
			updatehue = true;
			updatevalue = true;

		}
		if (s == valueCS && v != (int)(value*255)) {
			//System.out.println("vvalue = "+v);
			value = v/255.0;
			updatehue = true;
			updatesaturation = true;

		}

		if (updatehue) {
			computeHueImage(hue,saturation,value);
		}
		if (updatesaturation) {
			computeSaturationImage(hue,saturation,value);
		}
		if (updatevalue) {
			computeValueImage(hue,saturation,value);
		}

		int [] rgb = new int [3];



		rgb = hsv2rgb ((float)hue,(float)saturation,(float)value);

		//				for(int i=0 ; i<rgb.length ;i++){
		//					System.out.println("CONVERTED_RGB["+i+"] = "+rgb[i]);}

		Pixel pixel = new Pixel(rgb[0], rgb[1], rgb[2], 255);
		result.setPixel(pixel);
	}


	public void update() {
		// When updated with the new "result" color, if the "currentColor"
		// is aready properly set, there is no need to recompute the images.

		int [] rgb = new int [3];
		rgb = hsv2rgb( (float) hue, (float) saturation,(float) value);
		Pixel currentColor = new Pixel(rgb[0], rgb[1], rgb[2], 255);

		if(currentColor.getARGB() == result.getPixel().getARGB()) return;

		double cmyk[] = new double[4] ;

		cmyk = rgb2hsv(result.getPixel().getRed(),result.getPixel().getGreen(),result.getPixel().getBlue());

		hue = cmyk[0];
		saturation = cmyk[1];
		value = cmyk[2];


		hueCS.setValue((int) (hue*255.0));
		saturationCS.setValue((int) (saturation*255.0));
		valueCS.setValue((int) (value*255.0));

		computeHueImage(hue,saturation,value);
		computeValueImage(hue,saturation,value);
		computeSaturationImage(hue,saturation,value);


		// Efficiency issue: When the color is adjusted on a tab in the 
		// user interface, the sliders color of the other tabs are recomputed,
		// even though they are invisible. For an increased efficiency, the 
		// other tabs (mediators) should be notified when there is a tab 
		// change in the user interface. This solution was not implemented
		// here since it would increase the complexity of the code, making it
		// harder to understand.
	}

	public BufferedImage getHueImage() {
		return hueImage;
	}


	public BufferedImage getSaturationImage() {
		return saturationImage;
	}

	public BufferedImage getValueImage() {
		return valueImage;
	}



	public void setHueCS(ColorSlider slider) {
		hueCS = slider;
		slider.addObserver(this);
	}

	public void setSaturationCS(ColorSlider slider) {
		saturationCS = slider;
		slider.addObserver(this);
	}

	public void setValueCS(ColorSlider slider) {
		valueCS = slider;
		slider.addObserver(this);
	}



	public double getHue() {
		return hue;
	}

	public double getSaturation() {
		return saturation;
	}

	public double getValue() {
		return value;
	}






}

