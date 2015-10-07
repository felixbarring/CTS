
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
import java.util.LinkedList;
import java.util.List;


/*****************************************
 *  This is the base for some GUI elements.
 *	Subclasses can be given a target destination that 
 *	it will move to in a deaccelerating manner.
 *	Contains a list of other GUI elements that extends 
 *	RelativeLocation that will be moved around together 
 *	with the extending GUI class.
 *	
 *	Subclasses: ButtonContainer, Panel
 *
 *	@author Felix Bärring <felixbarring@gmail.com>
 *****************************************/

//  Contains bugg when moving in more than one axis!!!

public class TargetMoveItem implements IFDrawTickAndInput{

	protected double xLocation;
	protected double yLocation;
	protected int xTarget;
	protected int yTarget;
	protected int speed = 100;       // lower is faster
	private boolean isAtXTarget = true;
	private boolean isAtYTarget = true;
	protected boolean isAtTarget = true;
	protected boolean isActive = false;

	protected List<RelativeLocation> movableAndDrawable = new LinkedList<>();

	public void setTargetHidden(int x, int y){
		isActive = false;
		setTarget(x, y);
	}

	public void setTargetVissible(int x, int y){
		isActive = true;
		setTarget(x, y);
	}

	private void setTarget(int x, int y){
		xTarget = x;
		yTarget = y;
		isAtXTarget = false;
		isAtYTarget = false;
		isAtTarget = false;
	}

	public void move(double x, double y){
		for(RelativeLocation b : movableAndDrawable){
			b.move(x, y);
		}
		xLocation = xLocation+x;
		yLocation = yLocation+y;
	}

	@Override
	public void draw(Graphics2D g) {
		if(isAtTarget && !isActive) return;
		for (RelativeLocation md : movableAndDrawable){
			md.draw(g);
		}
	}

	@Override
	public void tick(){
		if(!isActive && isAtTarget){ return; }
		else{
			double differenceX = xTarget-xLocation;
			if (Math.abs(differenceX) < 1){
				xLocation = xTarget;
				for(RelativeLocation mv : movableAndDrawable){
					mv.setRelativeLocation((int)xLocation, (int)yLocation);
				}
				isAtXTarget = true;
			} else {
				double dummy = differenceX/speed;
				if(Math.abs(dummy) < 1){
					move(Math.signum(dummy), 0);
				} else {
					move(dummy, 0);
				}
			}
			double differenceY = yTarget-yLocation;
			if (Math.abs(differenceY) < 1){
				yLocation = yTarget;
				for(RelativeLocation mv : movableAndDrawable){
					mv.setRelativeLocation((int)xLocation, (int)yLocation);
				}
				isAtYTarget = true;
			} else {
				double dummy = differenceY/speed;
				if(Math.abs(dummy) < 1){
					move(0, Math.signum(dummy));
				} else {
					move(0, dummy);
				}
			}
			isAtTarget = isAtXTarget && isAtYTarget;
			for(RelativeLocation md : movableAndDrawable){
				md.tick();
			}
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y){
		for (RelativeLocation b : movableAndDrawable){
			b.mouseClicked(button, x, y);
		}
	}

	@Override
	public void mouseMoved(int x, int y){
		for (RelativeLocation b : movableAndDrawable){
			b.mouseMoved(x, y);
		}
	}

	@Override
	public void keyPressed(int key) {
		for (RelativeLocation b : movableAndDrawable){
			b.keyPressed(key);;
		}
	}

}
