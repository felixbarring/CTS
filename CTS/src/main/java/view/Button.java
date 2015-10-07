
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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*******************************************
 * This is class represents a button that can:
 *	Receive mouse information and respond to them:
 *		When clicked - send an event to the listeners containing the id of the button.
 *		When a mouse is inside its border - change image to "hover" one so that the user know it is intractable.
 *	It can also be moved around.
 *	The offset is used to simplify use for the ButtonContainer class.
 *	Since button containers can move back and forth many times, moving the buttons with doubles will eventually
 *	lead to the button being displaced.
 *	Therefore, when the ButtonContainer has reached its target destination it will tell all buttons to move
 *	to the ButtonContainer destination + the offset.
 *
 * @author Felix Bärring <felixbarring@gmail.com>
 *******************************************/

public class Button extends RelativeLocation{

	private final int WIDTH;
	private final int HEIGHT;
	private String id;
	private boolean beingHovered = false;
	private ButtonAnimation hover;

	List<IFButtonListener> listeners = new LinkedList<>();

	public Button(int x, int y, int xOff, int yOff, String id, int width, int height){
		xOffSet = xOff;
		yOffSet = yOff;
		this.xLocation = x+xOff;
		this.yLocation = y+yOff;
		this.id = id;
		this.WIDTH = width;
		this.HEIGHT = height;


		hover = new ButtonAnimation(
				width, height, 20, id, PublicConstants.normalColor, PublicConstants.highlightColor);
	}

	public void addListener(IFButtonListener btnl){
		listeners.add(btnl);
	}

	public static final class ButtonAnimation {

		private static final int ALPHA = 255;
		private List<BufferedImage> frames = new ArrayList<>();
		private int currFrame;

		public ButtonAnimation(int width, int height, int number, String id, Color start, Color target){
			// Avoid division by zero
			createFrame(width, height, number, id, start, new Color(target.getRed(), 
					target.getGreen(),target.getBlue(), 0));

			for(int i = 1; i < number; i++){
				createFrame(width, height, number, id, start, new Color(target.getRed(), 
						target.getGreen(),target.getBlue(), (int)(ALPHA*((i)/((float)number)))));
			}
		}

		public void createFrame(int width, int height, int number, String id, Color start, Color target){
			BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = (Graphics2D) frame.getGraphics();
			g.setColor(start);
			g.fillRect(0, 0, width, height);
			g.setColor(target);
			g.fillRect(0, 0, width, height);
			g.setFont(PublicConstants.font20);
			g.setColor(PublicConstants.textColor);
			g.drawString(id, 5, 20);
			g.dispose();
			frames.add(frame);
		}

		public void resetAnimation(){ currFrame = 0; }

		// It is possible to go backwards thru the animation.
		public void update(int x){
			if(currFrame+x < frames.size() && currFrame+x >= 0){
				currFrame = currFrame+x;
			}
		}

		public BufferedImage getImage(){
			return frames.get(currFrame);
		}
	}
	
	public void activate(){
		for(IFButtonListener btnl : listeners){
			btnl.buttonPressed(id);
		}
	}


	/*****************************************
	 * 
	 * 
	 *****************************************/
	@Override
	public void draw(Graphics2D g){
		g.drawImage(hover.getImage(), (int)xLocation, (int)yLocation, WIDTH, HEIGHT,  null);
	}

	@Override
	public void tick() {
		if(beingHovered){
			hover.update(1);
		} else {
			hover.update(-1);
		}
	}

	@Override
	public void mouseClicked(int button, int x, int y){
		if(x > this.xLocation && x < WIDTH+this.xLocation &&
				y > this.yLocation && y < HEIGHT+this.yLocation){
			for(IFButtonListener btnl : listeners){
				btnl.buttonPressed(id);
			}
		}
	}

	@Override
	public void mouseMoved(int x, int y){
		if(x > this.xLocation && x < WIDTH+this.xLocation &&
				y > this.yLocation && y < HEIGHT+this.yLocation){
			beingHovered = true;
		} else {
			beingHovered = false;
		}
	}

	@Override
	public void keyPressed(int key) {
	}

}

