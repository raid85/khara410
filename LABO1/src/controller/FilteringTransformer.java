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

import java.awt.event.MouseEvent;
import java.util.List;

import model.ImageDouble;
import model.ImageX;
import model.KernelModel;
import model.Shape;

/**
 * 
 * <p>Title: FilteringTransformer</p>
 * <p>Description: ... (AbstractTransformer)</p>
 * <p>Copyright: Copyright (c) 2004 Sébastien Bois, Eric Paquette</p>
 * <p>Company: (ÉTS) - École de Technologie Supérieure</p>
 * @author unascribed
 * @version $Revision: 1.6 $
 */
public class FilteringTransformer extends AbstractTransformer{
	
	float[][] customVals = new float [3][3];
	
	Filter filter = new MeanFilter3x3(new PaddingZeroStrategy(), new ImageClampStrategy());
	Filter customfilter = new CustomFilter3x3(new PaddingZeroStrategy(), new ImageClampStrategy(),customVals);
	
	/**
	 * @param _coordinates
	 * @param _value
	 */
	public void updateKernel(Coordinates _coordinates, float _value) {
//		System.out.println("CustomVals "+"[" + (_coordinates.getColumn() - 1) + "]["
//                                   + (_coordinates.getRow() - 1) + "] = " 
//                                   + _value);
		customVals[_coordinates.getColumn() - 1][_coordinates.getRow() - 1] = _value ;
	}
		
	/**
	 * 
	 * @param e
	 * @return
	 */
	protected boolean mouseClicked(MouseEvent e){

		List intersectedObjects = Selector.getDocumentObjectsAtLocation(e.getPoint());
		if (!intersectedObjects.isEmpty()) {			
			Shape shape = (Shape)intersectedObjects.get(0);			
			if (shape instanceof ImageX) {				
				ImageX currentImage = (ImageX)shape;
				ImageDouble filteredImage = filter.filterToImageDouble(currentImage);
				ImageX filteredDisplayableImage = filter.getImageConversionStrategy().convert(filteredImage);
				currentImage.beginPixelUpdate();
				
				for (int i = 0; i < currentImage.getImageWidth(); ++i) {
					for (int j = 0; j < currentImage.getImageHeight(); ++j) {
						currentImage.setPixel(i, j, filteredDisplayableImage.getPixelInt(i, j));
					}
				}
				currentImage.endPixelUpdate();
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see controller.AbstractTransformer#getID()
	 */
	public int getID() { return ID_FILTER; }

	/**
	 * @param string
	 */
	public void setBorder(String string) {
		
		int index = 0;
		for (int i = 0; i < KernelModel.HANDLING_BORDER_ARRAY.length; ++i) {
			if (string.equals(KernelModel.HANDLING_BORDER_ARRAY[i])) {
				index = i;
			}
		}
		switch (index) {
	
			case 0: // 
			{
				System.out.println("0 Strategy");
			} 
			break;
			case 1: // 
			{
				System.out.println("None ");
			} 
			break;
			
			case 2: //
			{
				System.out.println("Copy Border Strategy ");
			} 
			break;
			case 3: // 
			{
				System.out.println("Mirror Border Strategy ");
				
			} 
			break;
			case 4: // Prewitt Horiz
			{
				System.out.println("Circular Border Strategy ");
			} 
			break;
		
			default:
			{
				System.out.println("FUCK YOU VASCO ");
			}
			break;
			}
	}

	/**
	 * @param string
	 */
	public void setClamp(String string) {
		System.out.println(string);
	}
}
