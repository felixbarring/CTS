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
import java.util.List;
import java.util.Random;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public final class RandomColor{
    
    private final static Random random = new Random();
    
    private RandomColor(){
    }
    
    public static Color generateColor(){
        int red, green, blue;
        red = random.nextInt(256);
        green = random.nextInt(256);
        blue = random.nextInt(256);
        return new Color(red, green, blue,1);
    }
    
    /**
     * Generate Colors except for those in the specified list.
     *
     * @param ignoredColors is a list of colors to ignore.
     * @return a Color object representing a random color.
     */
    public static Color generateColor(List<Color> ignoredColors){
        int red, green, blue;
        do{
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
        } while (similarTo(ignoredColors,red,green,blue) || !highContrast(red,green,blue));
        return new Color(red, green, blue,1);
    }
    
    public static boolean similarTo(List<Color> colors, int red, int green, int blue){
        if (colors == null || colors.isEmpty()){
            return false;
        }
        for(Color c : colors){
            double distance = Math.sqrt(sqr(c.getRed(), red) + sqr(c.getGreen(), green) + sqr(c.getBlue(), blue));
            if(distance < 30){
                return true;
            }
        }
        return false;
    }
    
    public static boolean highContrast(int red, int green, int blue){
        return (red + green + blue / 3) > 128;
    }
    
    private static int sqr(int c2, int c1){
        return (c2 - c1) * (c2 - c1);
    }
}
