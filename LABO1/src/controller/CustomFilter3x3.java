
package controller;

import model.*;

/**
 * <p>Title: MeanFilter3x3</p>
 * <p>Description: A mean filter implementation.</p>
 * <p>Copyright: Copyright (c) 2004 Colin Barr�-Brisebois, �ric Paquette</p>
 * <p>Company: ETS - �cole de Technologie Sup�rieure</p>
 * @author unascribed
 * @version $Revision: 1.11 $
 */
public class CustomFilter3x3 extends Filter {	
	private double filterMatrix[][] = null;

	/**
	 * Default constructor.
	 * @param paddingStrategy PaddingStrategy used 
	 * @param conversionStrategy ImageConversionStrategy used
	 */
	public CustomFilter3x3(PaddingStrategy paddingStrategy, 
			ImageConversionStrategy conversionStrategy, float[][] customVals) {
		super(paddingStrategy, conversionStrategy);	
		filterMatrix = new double[3][3];

		filterMatrix[0][0] = (double)customVals[0][0] ;
		filterMatrix[0][1] = (double)customVals[0][1] ;
		filterMatrix[0][2] = (double)customVals[0][2] ;	
		filterMatrix[1][0] = (double)customVals[1][0] ;
		filterMatrix[1][1] = (double)customVals[1][1] ;
		filterMatrix[1][2] = (double)customVals[1][2] ;
		filterMatrix[2][0] = (double)customVals[2][0] ;
		filterMatrix[2][1] = (double)customVals[2][1] ;
		filterMatrix[2][2] = (double)customVals[2][2] ;
	}

	/**
	 * Filters an ImageX and returns a ImageDouble.
	 */
	public ImageDouble filterToImageDouble(ImageX image) {
		return filter(conversionStrategy.convert(image));
	}

	/**
	 * Filters an ImageDouble and returns a ImageDouble.
	 */	
	public ImageDouble filterToImageDouble(ImageDouble image) {
		return filter(image);
	}

	/**
	 * Filters an ImageX and returns an ImageX.
	 */	
	public ImageX filterToImageX(ImageX image) {
		ImageDouble filtered = filter(conversionStrategy.convert(image)); 
		return conversionStrategy.convert(filtered);
	}

	/**
	 * Filters an ImageDouble and returns a ImageX.
	 */	
	public ImageX filterToImageX(ImageDouble image) {
		ImageDouble filtered = filter(image); 
		return conversionStrategy.convert(filtered);		
	}

	/*
	 * Filter Implementation 
	 */
	private ImageDouble filter(ImageDouble image) {
		int imageWidth = image.getImageWidth();
		int imageHeight = image.getImageHeight();
		ImageDouble newImage = new ImageDouble(imageWidth, imageHeight);
		PixelDouble newPixel = null;
		double result = 0; 

		for (int x = 0; x < imageWidth; x++) {
			for (int y = 0; y < imageHeight; y++) {
				newPixel = new PixelDouble();

				//*******************************
				// RED
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += filterMatrix[i][j] * getPaddingStrategy().pixelAt(image, 
								x+(i-1), 
								y+(j-1)).getRed();
					}
				}

				newPixel.setRed(result);
				result = 0;

				//*******************************
				// Green
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += filterMatrix[i][j] * getPaddingStrategy().pixelAt(image, 
								x+(i-1), 
								y+(j-1)).getGreen();
					}
				}

				newPixel.setGreen(result);
				result = 0;

				//*******************************
				// Blue
				for (int i = 0; i <= 2; i++) {
					for (int j = 0; j <= 2; j++) {
						result += filterMatrix[i][j] * getPaddingStrategy().pixelAt(image,
								x+(i-1), 
								y+(j-1)).getBlue();
					}
				}

				newPixel.setBlue(result);
				result = 0;

				//*******************************
				// Alpha - Untouched in this filter
				newPixel.setAlpha(getPaddingStrategy().pixelAt(image, x,y).getAlpha());

				//*******************************
				// Done
				newImage.setPixel(x, y, newPixel);
			}
		}

		return newImage;
	}
}
