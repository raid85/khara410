package view;

import java.awt.image.BufferedImage;
import model.ObserverIF;
import model.Pixel;

class HSVColorMediator extends Object implements SliderObserver, ObserverIF{
	
	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;
	double hue; //might be double
	double saturation; // might be double
	double value; // might be double
		
	BufferedImage  hueImage;
	BufferedImage  saturationImage;
	BufferedImage  valueImage;
		
	int imagesWidth;
	int imagesHeight;
	ColorDialogResult result;
	
HSVColorMediator(ColorDialogResult result, int imagesWidth, int imagesHeight) {
		
		this.imagesWidth = imagesWidth;
		this.imagesHeight = imagesHeight;
		//this.hue = result.getPixel().getHue();
		//this.saturation = result.getPixel().getSaturation();
		//this.value = result.getPixel().getValue();
		this.result = result;
		result.addObserver(this);
		
		
		hueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		saturationImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		valueImage = new BufferedImage(imagesWidth, imagesHeight, BufferedImage.TYPE_INT_ARGB);
		
		//Ajouts des couleurs requises pour les images des sélecteurs de CMYK et HSV
		
				
		computeHueImage(hue, saturation, value );
		computeSaturationImage(hue, saturation, value);
		computeValueImage(hue, saturation, value); 	
	}


	public void computeHueImage(double hue2, double saturation2, double value2 ){
	
		
		int [] rgb2 = new int [2];
		rgb2 = hsv2rgb(hue2, saturation2, value2);
		Pixel p = new Pixel(rgb2[0], rgb2[1], rgb2[2]);
		
		for (int i=0; i<imagesWidth; ++i){
			p.setRed((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			
		for (int j=0;j<imagesHeight;++j){
			hueImage.setRGB(i, j, rgb);
		}	
		}
		
		
		if (hueCS != null) {
			hueCS.update(hueImage);}
	}
	
	public void computeSaturationImage(double hue2, double saturation2, double value2){
		
		int [] rgb2 = new int [2];
		rgb2 = hsv2rgb(hue2, saturation2, value2);
		Pixel p = new Pixel(rgb2[0], rgb2[1], rgb2[2]);
		
		for (int i=0; i<imagesWidth; ++i){
			p.setGreen((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			
			for (int j=0;j<imagesHeight;++j){
				saturationImage.setRGB(i, j, rgb);
			}
		}
		if (saturationCS != null) {
			saturationCS.update(saturationImage);}
	}
	
	public void computeValueImage(double hue2, double saturation2, double value2){
		
		int [] rgb2 = new int [2];
		rgb2 = hsv2rgb(hue2, saturation2, value2);
		Pixel p = new Pixel(rgb2[0], rgb2[1], rgb2[2]);
		
		for (int i=0; i<imagesWidth; ++i){
			p.setBlue((int)(((double)i / (double)imagesWidth)*255.0));
			int rgb = p.getARGB();
			
			for (int j=0;j<imagesHeight;++j){
				valueImage.setRGB(i, j, rgb);
			}
		}
		
		
		if (valueCS != null) {
			valueCS.update(valueImage);}
	}
	
	
	private double[] rgb2hsv(int R, int G, int B){
		
		double h = R/255;
		double s = G/255;
		double v = B/255;
		double[] hsv = new double[2];
		double varMin = Math.min(Math.min(h,s),v);
		double varMax = Math.max(Math.max(h,s),v);
		double delta = varMax - varMin;
		double deltaR, deltaG, deltaB;
		
		double Val=varMax;
		
		delta = varMax - varMin;
		
		
		if (varMax != 0)
			s = delta / varMax;
		else {
			s = 0;
			h = -1;
			hsv[0]=h;
			hsv[1]=s;
			hsv[2]=v;
			return hsv;
		}
		if (R == varMax)
			h = ( G - B) / delta;
		else if ( G == varMax)
			h = 2 + (B - R) / delta;
		else h= 4+ (R - G) / delta;
		h*=60;
		if (h< 0)
			h+=360;
/**		
 
		if (delta == 0){
			h=0;
			s=0;
			v=0;
			hsv[0]=h;
			hsv[1]=s;
			hsv[2]=v;
			return hsv;
		}
		else 
		{s = delta / varMax;
		
		deltaR = (((varMax - R)/6)+(delta/2)) /delta;
		deltaG = (((varMax - G)/6)+(delta/2)) /delta;
		deltaB = (((varMax - B)/6)+(delta/2)) /delta;

		   if      ( R == varMax ) {
			   h = B - G;
		   }
				   else if ( G == varMax ) {
					   h = ( 1 / 3 ) + R - B;
						   }
				   else if ( B == varMax ) {
					   h = ( 2 / 3 ) + G - R;
						   }

				   if ( h < 0 ) h += 1;
				   if ( h > 1 ) h -= 1;



		}
		hsv[0]=h;
		hsv[1]=s;
		hsv[2]=v;
	**/	hsv[0] = h;
		hsv[1] = s;
		hsv[2] = v;
		return hsv;
		
	}
	private int[] hsv2rgb(double H, double S, double V){
		
		int r=0;
		int g=0;
		int b=0;
		int[] rgb = new int[2];
		
		return rgb;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub

		int [] rgb = new int [3];
		
		rgb = hsv2rgb(hue,saturation,value);
		Pixel currentColor = new Pixel(rgb[0], rgb[1], rgb[2],255); //,255 missing.
		
		if(currentColor.getARGB() == result.getPixel().getARGB()) return;
		
		double hsv[] = new double[2] ;
		
		hsv = rgb2hsv(result.getPixel().getRed(),result.getPixel().getGreen(),result.getPixel().getBlue());
		
		hue = hsv[0];
		saturation = hsv[1];
		value = hsv[2];
		
		
		hueCS.setValue((int) (hue*255.0));
		saturationCS.setValue((int) (saturation*255.0));
		valueCS.setValue((int) (value*255.0));
		computeHueImage(hue,saturation,value);
		computeSaturationImage(hue,saturation,value);
		computeValueImage(hue,saturation,value);
		
		
		
		
	}

	
	
	
	
	@Override
	public void update(ColorSlider cs, int v) {
		// TODO Auto-generated method stub
		
		boolean updateHue=false;
		boolean updateSaturation=false;
		boolean updateValue=false;
		
		
		if (cs == hueCS && v != (int)(hue*255)) {
			//System.out.println("vCyan = "+v);
			hue = v/255.0;
			updateSaturation = true;
			updateValue = true;
			
		}
		if (cs == saturationCS && v !=(int) (saturation*255)) {
			//System.out.println("vMagenta = "+v);
			saturation = v/255.0;
			updateHue = true;
			updateValue = true;
			
		}
		if (cs == valueCS && v != (int)(value*255)) {
			//System.out.println("vYellow = "+v);
			value = v/255.0;
			updateHue = true;
			updateSaturation = true;
			
		}
		
		if (updateHue) {
			computeHueImage(hue,saturation,value);
		}
		if (updateSaturation) {
			computeSaturationImage(hue,saturation,value);
		}
		if (updateValue) {
			computeValueImage(hue,saturation,value);
		}
		
		int [] rgb = new int [2];
		

		
		rgb = hsv2rgb (hue,saturation,value);



		Pixel pixel = new Pixel(rgb[0], rgb[1], rgb[2], 255);
		result.setPixel(pixel);
		
		
		
		
		
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
