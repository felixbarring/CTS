
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
 * @author Felix Bärring <felixbarring@gmail.com>
 *
 */

public class Title {
  
  final int width = 800;
  final int height = 150;
  BufferedImage car;
  BufferedImage image;
  int x = 0;
  
  public Title(IFButtonListener btnl){
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g = (Graphics2D) image.getGraphics();
    g.setColor(PublicConstants.normalColor);
    g.fillRect(0, 0, width, height);
    g.setColor(PublicConstants.textColor);
    g.setFont(PublicConstants.font60);
    g.drawString("Chaotic Traffic Simulator", 20, 80);
    
    car = new BufferedImage(15,5,BufferedImage.TYPE_INT_RGB);
    
    Graphics2D g2 = (Graphics2D) car.getGraphics();
    g2.setColor(PublicConstants.textColor);
    g2.fillRect(0, 0, 15, 5);
    
    int n = PublicConstants.normalColor.getRGB();
    int b = PublicConstants.textColor.getRGB();
    car.setRGB(0, 0, n);    car.setRGB(0, 1, n);    car.setRGB(0, 4, n);
    car.setRGB(1, 0, n);    car.setRGB(1, 1, n);    car.setRGB(1, 4, n);
    car.setRGB(2, 0, n);    car.setRGB(2, 1, n);    car.setRGB(2, 4, b);
    car.setRGB(3, 0, n);    car.setRGB(3, 1, b);    car.setRGB(3, 4, n);
    car.setRGB(4, 0, b);    car.setRGB(4, 1, b);    car.setRGB(4, 4, n);
    car.setRGB(5, 0, b);    car.setRGB(5, 1, n);    car.setRGB(5, 4, n);
    car.setRGB(6, 0, b);    car.setRGB(6, 1, n);    car.setRGB(6, 4, n);
    car.setRGB(7, 0, b);    car.setRGB(7, 1, b);    car.setRGB(7, 4, n);
    car.setRGB(8, 0, b);    car.setRGB(8, 1, n);    car.setRGB(8, 4, n);
    car.setRGB(9, 0, b);    car.setRGB(9, 1, n);    car.setRGB(9, 4, n);
    car.setRGB(10, 0, b);   car.setRGB(10, 1, n);   car.setRGB(10, 4, n);
    car.setRGB(11, 0, n);   car.setRGB(11, 1, b);   car.setRGB(11, 4, n);
    car.setRGB(12, 0, n);   car.setRGB(12, 1, n);   car.setRGB(12, 4, b);
    car.setRGB(13, 0, n);   car.setRGB(13, 1, n);   car.setRGB(13, 4, n);
    car.setRGB(14, 0, n);   car.setRGB(14, 1, n);   car.setRGB(14, 4, n);
    
    g.dispose();
    g2.dispose();
    
  }
  
  public void tick(){
    x += 5;
    if(x > 800){
      x = -50;
    }
  }
  
  public void draw(Graphics2D g){
    g.drawImage(image, 0, 0, width, height, null);
    g.drawImage(car, x, 10, car.getWidth()*5, car.getHeight()*5, null);
  }
  

}
