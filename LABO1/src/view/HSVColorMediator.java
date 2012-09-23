package view;

import java.awt.image.BufferedImage;
import model.ObserverIF;
import model.Pixel;

class HSVColorMediator extends Object implements SliderObserver, ObserverIF{
	
	ColorSlider hueCS;
	ColorSlider saturationCS;
	ColorSlider valueCS;
	int hue;
	int saturation;
	int value;
		
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


	public void computeHueImage(int hue, int saturation, int value ){
	
		if (hueCS != null) {
			hueCS.update(hueImage);}
	}
	
	public void computeSaturationImage(int hue, int saturation, int value){
		if (saturationCS != null) {
			saturationCS.update(saturationImage);}
	}
	
	public void computeValueImage(int hue, int saturation, int value){
		if (valueCS != null) {
			valueCS.update(valueImage);}
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void update(ColorSlider cs, int v) {
		// TODO Auto-generated method stub
		
	}
	

}
