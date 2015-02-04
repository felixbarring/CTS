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

package model;

import util.IDrawable;
import util.DrawableObject;
import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class DrawAbleObjectTest {
    
    private IDrawable draw;
    
    public DrawAbleObjectTest() {
        double xpos = 1.0;
        double ypos = 2.0;
        double yaw = 90.0;
        Color color = Color.GREEN;
        BufferedImage image = null;
        draw = DrawableObject.getImageGraphics(xpos, ypos, yaw, color, image);
    }

    /**
     * Test of getGraphics method, of class DrawableObject.
     */
    @Test
    public void testGetGraphics() {
        double xpos = 1.0;
        double ypos = 2.0;
        double yaw = 90.0;
        Color color = Color.GREEN;
        BufferedImage image = null;
        IDrawable result = DrawableObject.getImageGraphics(xpos, ypos, yaw, color, image);
        assertNotNull(result);
    }

    /**
     * Test of getColor method, of class DrawableObject.
     */
    @Test
    public void testGetColor() {
        Color result = draw.getColor();
        assertEquals(Color.GREEN, result);
    }

    /**
     * Test of getXPos method, of class DrawableObject.
     */
    @Test
    public void testGetXPos() {
        double expResult = 1.0;
        double result = draw.getXPos();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getYPos method, of class DrawableObject.
     */
    @Test
    public void testGetYPos() {
        double expResult = 2.0;
        double result = draw.getYPos();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getYaw method, of class DrawableObject.
     */
    @Test
    public void testGetYaw() {
        double expResult = 90.0;
        double result = draw.getYaw();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getImage method, of class DrawableObject.
     */
    @Test
    public void testGetImage() {
        BufferedImage result = draw.getImage();
        assertNull(result);
    }
    @Test
    public void testGetImageValid(){
        double xpos = 1.0;
        double ypos = 2.0;
        double yaw = 90.0;
        Color color = Color.GREEN;
        BufferedImage image = new BufferedImage(1,2,3);
        IDrawable drawlocal = DrawableObject.getImageGraphics(xpos, ypos, yaw, color, image);
        BufferedImage result = drawlocal.getImage();
        assertNotEquals(image, result);
    }
    
}
