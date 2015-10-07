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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
/**
 * 
 * @author Felix Bärring <felixbarring@gmail.com>
 *
 */
public class PublicConstants{

	// Current color theme
	public static final Color normalColor = new Color(0X292c33); 
	public static final Color highlightColor = new Color(0X3d414b);
	public static final Color BGColor = new Color(0Xadadab);
	public static final Color textColor = new Color(0X8492aa);
	public static final Color black = new Color(0X000000);
	public static final Color clear = new Color(0f,0f,0f,0.0f );

	static{
		try {
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
					Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("cs_regular.ttf")));

			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(
					Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("VeraMono.ttf")));
			
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//for(Font f :GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()){System.out.println(f.getName());}
	}

	public static final Font font20 = new Font("Counter-Strike Regular", Font.BOLD, 20);
	public static final Font font60 = new Font("Counter-Strike Regular", Font.PLAIN, 60);
	public static final Font regularFont20 = new Font("Bitstream Vera Sans Mono", Font.PLAIN, 20);
	
	public static final boolean USE_FULLSCREEN = false;
	public static final boolean IS_LINUX = System.getProperty("os.name").equals("Linux");
	
	public static final int PICTURE_WIDTH = 800;
	public static final int PICTURE_HEIGHT = 600;
	
}
