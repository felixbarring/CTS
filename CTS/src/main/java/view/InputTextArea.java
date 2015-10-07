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
package view;

import java.awt.Graphics2D;
import java.util.List;
/**
 * @author Felix Bärring <felixbarring@gmail.com>
 *
 */
public class InputTextArea extends TextArea {

	private int flash = 80;
	private int counter;

	private Terminal terminal;

	private StringBuilder strB = new StringBuilder("> ");

	public InputTextArea(List<String> l, int x, int y, int offX, int offY, int w,	int h, 
			Terminal terminal) {
		super(l, x, y, offX, offY, w, h);
		this.terminal = terminal;
	}

	public void finishLine(String s){
		strB = new StringBuilder(s);
	}

	public void addToLine(String s){
		strB.append(s);
	}

	public void removeFromLine(){
		if(strB.length() > 2){
			strB.deleteCharAt(strB.length()-1);
		}
	}
	
	public void replaceStringBuilder(String s){
		strB = new StringBuilder(s);
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
			int unt = strB.length()-MAX_CHARS;
			if(unt < 0){
				unt = 0;
			}
			g2.drawString(strB.toString(), (int)(10-(unt*FONT_SIZE/1.7)), 25*(i+1-topLine)+5);
		}
		g.drawImage(image, (int)xLocation, (int)yLocation, WIDTH, HEIGHT, null);
		g2.dispose();
	}

	@Override
	public void keyPressed(int key) {
		if(key == 8){
			removeFromLine();
		} else if(key == 10){
			terminal.stringCreated(strB.toString());
			finishLine("> ");
		} else if (key == 16){
			terminal.completeSequence(strB.toString());
		} else {
			addToLine(""+(char)key);
		}
	}

	@Override
	public void tick() {
		counter = (counter+1)%flash;
	}

}
