/*
* The MIT License
*
* Copyright 2014 Gustaf Ringius <Gustaf@linux.com>, Felix Bärring <felixbarring@gmail.com>
* Andreas Löfman <lofman.andreas@gmail.com>,  Robert Wennergren <whoisregor@gmail.com>
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

/**
 * Simple interface for declaring the type for drawable objects.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public interface IDrawable {
    
    /**
     * Gets the color of the object if any.
     *
     * @return the color in a Color object  if any, otherwise null.
     */
    public Color getColor();
    
    /**
     * Gets the current x coordinate of the object.
     *
     * Can either be the actual position of the object.
     * Or if it is a line, returns the start X position.
     *
     * @return the x coordinate in a double representation.
     */
    public double getXPos();
    
    /**
     * Gets the current y coordinate of the object.
     *
     * Can either be the actual position of the object.
     * Or if it is a line, returns the start Y position.
     *
     * @return the y coordinate in a double representation.
     */
    public double getYPos();
    
    /**
     * Gets the current direction in witch the object is pointing.
     *
     * @return the direction in a double representation.
     */
    public double getYaw();
    
    /**
     * @return The radius of a circle.
     */
    public double getRadius();
    
    /**
     * @return The width of a rectangle.
     */
    public double getWidth();
    
    /**
     * @return The height of a rectangle.
     */
    public double getHeight();
    
    /**
     * @return The X coordinate of the end position of a line.
     */
    public double getEndX();
    
    /**
     * @return The Y coordinate of the end position of a line.
     */
    public double getEndY();
    
    /**
     * @return
     */
    public String getText();
    
    /**
     * @return The type of this object.
     */
    public Type getType();
    
    /**
     * Gets an image representation of the object if any.
     *
     * @return the image linked to the object if any, otherwise null.
     */
    public BufferedImage getImage();
    
    public enum Type {
        IMAGE,
        TEXT,
        RECTANGLE,
        CIRCLE,
        DOT,
        LINE;
    }
}
