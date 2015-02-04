
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.awt.image.BufferedImage;

import controller.SoundController;

/*
	A graphical area that should contain text
	Much implementation left to do...
 */

public class TextArea extends RelativeLocation{

	protected final int WIDTH;
	protected final int HEIGHT;
	protected final int FONT_SIZE = 20;
	protected final int MAX_CHARS;
	protected final int MAX_ROWS;
	protected boolean needScroll;

	List<char[]> lines;

	BufferedImage image;

	ArrowButton up;
	ArrowButton down;
	
	protected int topLine = 0;

	public TextArea(List<String> l, int x, int y, int offX, int offY, int w, int h){
		this.xLocation = x;
		this.yLocation = y;
		this.lines = new LinkedList<>();
		xOffSet = offX;
		yOffSet = offY;
		WIDTH = w;
		HEIGHT = h;
		MAX_CHARS =  (int)((WIDTH-20)/FONT_SIZE*1.7 - 1);
		MAX_ROWS = (int)((HEIGHT-20)/25);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);

		for(String s : l){
			addString(s);
		}

		up = new ArrowButton(WIDTH-17, 7, 6, 5, 2, 0);
		down = new ArrowButton(WIDTH-17, HEIGHT-15, 6, 5, 2, 1);

	}
	
	protected void addString(String s){
		char[] chars = s.toCharArray();
		while(chars.length > MAX_CHARS){
			int cut = MAX_CHARS;
			while(chars[cut] != ' ' ){
				cut--;
				if(cut < 0) {
					lines.add((Arrays.copyOfRange(chars, 0, MAX_CHARS-1)));
					if(needScroll){
						topLine++;
					}
					addString(new String(Arrays.copyOfRange(chars, MAX_CHARS, chars.length-1)));
					return;
				}
			}
			cut++;
			lines.add(fillCutsWithNull(cut, chars));
			chars = Arrays.copyOfRange(chars, cut, chars.length);
		}
		lines.add(fillCutsWithNull(chars.length, chars));
		needScroll = lines.size() > MAX_ROWS;
		if(needScroll){
			topLine = lines.size() - MAX_ROWS;
		}
	}
	
	private char[] fillCutsWithNull(int cut, char[] chars){
		char[] line = new char[MAX_CHARS];
		for(int i = 0; i < MAX_CHARS; i++){
			if(i < cut){
				line[i] = chars[i];
			} else {
				line[i] = '\0';
			}
		}
		return line;
	}
	
	public void clear(){
		lines = new LinkedList<char[]>();
		topLine = 0;
	}
	
	protected String lineToString(int i){
		StringBuilder sb = new StringBuilder();
		for(char c : lines.get(i)){
			if( c == '\0') break;
			sb.append(c);
		}
		return sb.toString();
	}


	@Override
	public void draw(Graphics2D g) {
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setFont(PublicConstants.regularFont20);
		g2.setColor(PublicConstants.BGColor);
		g2.fillRect(0, 0, WIDTH, HEIGHT);
		g2.setColor(PublicConstants.normalColor);
		g2.fillRect(5, 5, WIDTH-10, HEIGHT-10);
		g2.setColor(PublicConstants.textColor);

		for(int i = topLine; i < lines.size() && i < MAX_ROWS+topLine; i++){
			// Need to be optimized
			g2.drawString(lineToString(i), 10, 25*(i+1-topLine)+5);
		}

		if(needScroll){
			up.drawArrow(g2);
			down.drawArrow(g2);
		}
		g.drawImage(image, (int)xLocation, (int)yLocation, WIDTH, HEIGHT, null);
		g2.dispose();
	}

	@Override
	public void tick() {}

	@Override
	public void mouseClicked(int button, int x, int y) {
		if(needScroll){
			up.mouseClicked(button, (int)(x-xLocation), (int)(y-yLocation));
			down.mouseClicked(button, (int)(x-xLocation), (int)(y-yLocation));
		}
	}

	@Override
	public void mouseMoved(int x, int y) {
		if(needScroll){
			up.mouseMoved((int)(x-xLocation), (int)(y-yLocation));
			down.mouseMoved((int)(x-xLocation), (int)(y-yLocation));
		}
	}

	@Override
	public void keyPressed(int key) {}

	class ArrowButton{

		int xLocation;
		int yLocation;
		int width;
		int height;
		int scale;

		BufferedImage img;
		BufferedImage hover;
		boolean beingHovered = false;

		int upOrDown;

		ArrowButton(int x, int y, int w, int h, int s, int uod){

			xLocation = x;
			yLocation = y;
			width = w;
			height = h;
			scale = s;
			upOrDown = uod;

			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g2 = (Graphics2D) img.getGraphics();
			g2.setColor(PublicConstants.normalColor);
			g2.fillRect(0, 0, width, height);
			int b = PublicConstants.textColor.getRGB();
			if(upOrDown == 0){
				img.setRGB(2, 0, b);	img.setRGB(3, 0, b);
				img.setRGB(1, 1, b);	img.setRGB(2, 1, b);	img.setRGB(3, 1, b); img.setRGB(4, 1, b);
				img.setRGB(0, 2, b);	img.setRGB(1, 2, b);	img.setRGB(2, 2, b);	img.setRGB(3, 2, b); img.setRGB(4, 2, b); img.setRGB(5, 2, b);
				img.setRGB(2, 3, b);	img.setRGB(3, 3, b);
				img.setRGB(2, 4, b);	img.setRGB(3, 4, b);
			} else {
				img.setRGB(2, 4, b);	img.setRGB(3, 4, b);
				img.setRGB(1, 3, b);	img.setRGB(2, 3, b);	img.setRGB(3, 3, b);	img.setRGB(4, 3, b);
				img.setRGB(0, 2, b);	img.setRGB(1, 2, b);	img.setRGB(2, 2, b);	img.setRGB(3, 2, b); img.setRGB(4, 2, b); img.setRGB(5, 2, b);
				img.setRGB(2, 1, b);	img.setRGB(3, 1, b);
				img.setRGB(2, 0, b);	img.setRGB(3, 0, b);
			}

			hover = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			Graphics2D g3 = (Graphics2D) hover.getGraphics();
			g3.setColor(PublicConstants.normalColor);
			g3.fillRect(0, 0, width, height);
			b = PublicConstants.highlightColor.getRGB();
			if(upOrDown == 0){
				hover.setRGB(2, 0, b);	hover.setRGB(3, 0, b);
				hover.setRGB(1, 1, b);	hover.setRGB(2, 1, b);	hover.setRGB(3, 1, b); 	hover.setRGB(4, 1, b);
				hover.setRGB(0, 2, b);	hover.setRGB(1, 2, b);	hover.setRGB(2, 2, b);	hover.setRGB(3, 2, b); hover.setRGB(4, 2, b); hover.setRGB(5, 2, b);
				hover.setRGB(2, 3, b);	hover.setRGB(3, 3, b);
				hover.setRGB(2, 4, b);	hover.setRGB(3, 4, b);
			} else {
				hover.setRGB(2, 4, b);	hover.setRGB(3, 4, b);
				hover.setRGB(1, 3, b);	hover.setRGB(2, 3, b);	hover.setRGB(3, 3, b);	hover.setRGB(4, 3, b);
				hover.setRGB(0, 2, b);	hover.setRGB(1, 2, b);	hover.setRGB(2, 2, b);	hover.setRGB(3, 2, b); hover.setRGB(4, 2, b); hover.setRGB(5, 2, b);
				hover.setRGB(2, 1, b);	hover.setRGB(3, 1, b);
				hover.setRGB(2, 0, b);	hover.setRGB(3, 0, b);
			}
		}

		public void drawArrow(Graphics2D g){
			if(beingHovered){
				g.drawImage(hover, xLocation, yLocation, width*scale, height*scale, null);
			}else{
				g.drawImage(img, xLocation, yLocation, width*scale, height*scale, null);
			}
		}

		public void mouseMoved(int x, int y){
			if( x > xLocation && x < xLocation+(width*scale) &&
					y > yLocation && y < yLocation+(height*scale)){
				beingHovered = true;
			} else {
				beingHovered = false;
			}
		}

		public void mouseClicked(int button, int x, int y){
			if( x > xLocation && x < xLocation+(width*scale) &&
					y > yLocation && y < yLocation+(height*scale)){
				if(upOrDown == 0 && topLine > 0){
					topLine--;
					SoundController.getInstance().playButtonSound();
				} else if(upOrDown == 1 && topLine < lines.size()-MAX_ROWS){
					topLine++;
					SoundController.getInstance().playButtonSound();
				}
			}
		}
	}

}



