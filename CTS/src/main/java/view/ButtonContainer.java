
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

import java.util.List;

/**
 * This class creates a number of buttons that it will manage.
 * Duties include: Sending mouse information, telling them to draw themselves, changing location  
 * @author Felix Bärring <felixbarring@gmail.com>
 */
public class ButtonContainer extends TargetMoveItem {

	public enum ButtonContainerType{
		Vertical, Horizontal 
	}

	public ButtonContainer(int x, int y, int width, int height, int rows,
			int spacing, List<String> buttonSources, IFViewMode mm, ButtonContainerType t){
		xLocation = x;
		yLocation = y;
		xTarget = (int)xLocation;
		yTarget = (int)yLocation;
		speed = 5;
		for(int i = 0; i < rows; i++){
			Button btn;
			if(t.equals(ButtonContainerType.Vertical)){
				btn = new Button((int)xLocation, (int)yLocation, 0, (height+spacing)*i,
						buttonSources.get(i), width, height);
			} else {
				btn = new Button((int)xLocation, (int)yLocation, (width+spacing)*i, 0,
						buttonSources.get(i), width, height);
			}
			btn.addListener(mm);
			movableAndDrawable.add(btn);
		}
	}

}
