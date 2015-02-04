
/*
 * The MIT License
 *
 * Copyright 2014 Gustaf Ringius <Gustaf@linux.com>.
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
import java.util.List;

public class ConfirmPopUp extends TargetMoveItem{

	public final int WIDTH;
	public final int HEIGHT; 

	BufferedImage backGround;

	ConfirmPopUp(double x, double y, int w, int h, int buttonW, 
			int buttonH, List<String> buttonNames, List<String> text, IFViewMode vm){

		speed = 10;
		xLocation = x;
		yLocation = y;
		WIDTH = w;
		HEIGHT = h;

		movableAndDrawable.add(new TextArea(text, 10, 10, 10, 10, WIDTH-20, HEIGHT-buttonH-25));

		int xOff = (WIDTH-buttonNames.size()*(buttonW+5))/2;

		int i = 0;
		for(String s : buttonNames){
			Button b = new Button(xOff+(buttonW*i)+5*i, 
					HEIGHT-(buttonH+10), xOff+(buttonW*i)+5*i, HEIGHT-(buttonH+10), s, buttonW, buttonH);
			movableAndDrawable.add(b);
			b.addListener(vm);
			i++;
		}

		backGround = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) backGround.getGraphics();
		g.setColor(PublicConstants.normalColor);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(PublicConstants.highlightColor);
		g.fillRect(5, 5, WIDTH-10, HEIGHT-10);
		g.dispose();
	}

	@Override
	public void draw(Graphics2D g) {
		if(isActive || !isAtTarget){
			g.drawImage(backGround, (int)xLocation, (int)yLocation, null);
		}
		super.draw(g);
	}


}
