
/*
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * @author Felix Bärring <felixbarring@gmail.com>
 */



package view;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A Panel that can contain different GUI items that it will display.
 * The class is responsible to manage the items it contains, like for instance moving them around.
 * @author Felix Bärring <felixbarring@gmail.com>
 *
 */
public class Panel extends TargetMoveItem implements IFDrawTickAndInput{

	int WIDTH;
	int HEIGHT;
	String name;
	BufferedImage image;

	public Panel(int x, int y, int width, int height, String name, RelativeLocation mAd[]){
		xLocation = x;
		yLocation = y;
		xTarget = (int)xLocation;
		yTarget = (int)yLocation;
		speed = 10;
		this.WIDTH = width;
		this.HEIGHT = height;
		this.name = name;
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D)image.getGraphics();
		g.setColor(PublicConstants.normalColor);
		g.fillRect(0, 0, width, height);
		g.setFont(PublicConstants.font20);
		g.setColor(PublicConstants.textColor);
		g.drawString(name, 20, 20);  

		if (mAd != null){
			for(RelativeLocation md : mAd){
				movableAndDrawable.add(md);
			}
		}
	}

	@Override
	public void draw(Graphics2D g){
		g.drawImage(image, (int)xLocation, (int)yLocation, WIDTH, HEIGHT, null);
		super.draw(g);
	}

}
