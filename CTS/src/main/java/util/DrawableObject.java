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

package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.annotation.concurrent.Immutable;

/**
 * Class holding the state of a DrawAble object that
 * the GUI can interpret and put in the buffer.
 *
 * This class should be immutable. Constructor is private so
 * no inheritance. Only getters and no setters.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @author Andreas Löfman <lofman.andreas@gmail.com>               
 */
@Immutable
public class DrawableObject implements IDrawable{
    
    private final Color color;
    private final double xpos;
    private final double ypos;
    private final double yaw;
    private final BufferedImage image;
    
    //The type of this graphic object
    private final Type type;
    
    //For rectangles
    private final double width;
    private final double height;
    
    //For circles
    private final double radius;
    
    //For lines
    private final double xend;
    private final double yend;
    
    //For texts
    private final String text;
    
    
    
    private DrawableObject(DOBuilder builder){
        this.type = builder.getType();
        this.xpos = builder.getXpos();
        this.ypos = builder.getYpos();
        this.yaw = builder.getYaw();
        this.color = builder.getColor();
        this.image = builder.getImage();       
        this.width = builder.getWidth();
        this.height = builder.getHeight();        
        this.radius = builder.getRadius();       
        this.xend = builder.getXend();
        this.yend = builder.getYend();  
        this.text = builder.getText();
    }
    
    /**
     * Factory method for producing DrawAble objects of image type.
     *
     * All parameters have to be specified.
     *
     * @param xpos should be a double representation of the current x coordinate
     * @param ypos should be a double representation of the current y coordinate
     * @param yaw should be a double representation of the current direction;
     * @param color should be a Color object or null if no color.
     * @param image should be a BufferedImage or null if no image.
     * @return a DrawAble object.
     */
    public final static IDrawable getImageGraphics(double xpos, double ypos, double yaw, Color color, BufferedImage image) {
        return new DOBuilder().setType(Type.IMAGE).setXpos(xpos).setYpos(ypos).setYaw(yaw).setColor(color).setImage(image).build();
    }
    
    /**
     * Factory method for producing DrawAble objects of text type.
     * 
     * All parameters have to be specified.
     * 
     * @param xpos position of the text, center aligned
     * @param ypos position of the text, center aligned
     * @param color
     * @param text
     * @return 
     */
    public final static IDrawable getTextGraphics(double xpos, double ypos, Color color, String text) {
        return new DOBuilder().setType(Type.TEXT).setXpos(xpos).setYpos(ypos).setColor(color).setText(text).build();
    }
    
    /**
     * Factory method for producing DrawAble objects of rectangle type.
     * 
     * @param xpos
     * @param ypos
     * @param yaw
     * @param color
     * @param width
     * @param height
     * @return 
     */
    public final static IDrawable getRectGraphics(double xpos, double ypos, double yaw, Color color, double width, double height){
        return new DOBuilder().setType(Type.RECTANGLE).setXpos(xpos).setYpos(ypos).setYaw(yaw).setColor(color).setWidth(width).setHeight(height).build();
    }
    
    /**
     * Factory method for producing DrawAble objects of circle type.
     * 
     * @param xpos
     * @param ypos
     * @param yaw
     * @param color
     * @param radius
     * @return 
     */
    public final static IDrawable getCircleGraphics(double xpos, double ypos, double yaw, Color color, double radius){
        return new DOBuilder().setType(Type.CIRCLE).setXpos(xpos).setYpos(ypos).setYaw(yaw).setColor(color).setWidth(radius).build();
    }
    
    /**
     * Factory method for producing DrawAble objects of dot type.
     * @param xpos
     * @param ypos
     * @param color
     * @return 
     */
    public final static IDrawable getDotGraphics(double xpos, double ypos, Color color){
        return new DOBuilder().setType(Type.DOT).setXpos(xpos).setYpos(ypos).setColor(color).build();
    }
    
    /**
     * Factory method for producing DrawAble objects of line type.
     * @param xpos
     * @param ypos
     * @param color
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     * @return 
     */
    public final static IDrawable getLineGraphics(Color color, double startX, double startY, double endX, double endY){
        return new DOBuilder().setType(Type.LINE).setXpos(startX).setYpos(startY).setXend(endX).setYend(endY).setColor(color).build();
    }
    
    @Override
    public Color getColor() {
        return new Color(color.getRed(), color.getGreen(), color.getBlue());
    }
    
    @Override
    public double getXPos() {
        return xpos;
    }
    
    @Override
    public double getYPos() {
        return ypos;
    }
    
    @Override
    public double getYaw() {
        return yaw;
    }
    
    @Override
    public BufferedImage getImage() {
        if (image == null){
            return null;
        }
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    @Override
    public double getRadius() {
        return radius;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getEndX() {
        return xend;
    }
    
    @Override
    public double getEndY() {
        return yend;
    }
    
    public String getText(){
        return text;
    }

    @Override
    public Type getType() {
        return type;
    }
    
    
    /**
     * A builder class for DrawableObjects.
     */
    private static class DOBuilder{
        private Color color = Color.black;
        private double xpos = 0.0;
        private double ypos = 0.0;
        private double yaw = 0.0;
        private BufferedImage image = null;
        private Type type = null;
        private double width = 0.0;
        private double height = 0.0;
        private double radius = 0.0;
        private double xend = 0.0;
        private double yend = 0.0;
        private String text = "";
        
        public DOBuilder(){
        
        }
        
        public DrawableObject build(){
            return new DrawableObject(this);
        }
        
        public DOBuilder setColor(Color c){
            color = c;
            return this;
        }
        
        public DOBuilder setXpos(double d){
            xpos = d;
            return this;
        }
        
        public DOBuilder setYpos(double d){
            ypos = d;
            return this;
        }
        
        public DOBuilder setYaw(double d){
            yaw = d;
            return this;
        }
        
        public DOBuilder setImage(BufferedImage i){
            image = i;
            return this;
        }
        
        public DOBuilder setType(Type t){
            type = t;
            return this;
        }
        
        public DOBuilder setWidth(double d){
            width = d;
            return this;
        }
        
        public DOBuilder setHeight(double d){
            height = d;
            return this;
        }
        
        public DOBuilder setRadius(double d){
            radius = d;
            return this;
        }
        
        public DOBuilder setXend(double d){
            xend = d;
            return this;
        }
        
        public DOBuilder setYend(double d){
            yend = d;
            return this;
        }
        
        public DOBuilder setText(String s){
            text = s;
            return this;
        }

        public Color getColor() {
            return color;
        }

        public double getXpos() {
            return xpos;
        }

        public double getYpos() {
            return ypos;
        }

        public double getYaw() {
            return yaw;
        }

        public BufferedImage getImage() {
            return image;
        }

        public Type getType() {
            return type;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public double getRadius() {
            return radius;
        }

        public double getXend() {
            return xend;
        }

        public double getYend() {
            return yend;
        }

        public String getText() {
            return text;
        }
    }
    
}
