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

package model;

import util.IDrawable;
import model.path.Path;
import util.Weather;

/**
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public interface IWorldMap {
    
    /**
     * Returns generic graphical representations of all
     * components in the model.
     * 
     * @return an array with Drawable objects
     */
    public IDrawable[] getGraphics();
    
    /**
     * To be called each tick
     *
     * @return returns false if error
     */
    public boolean think();
    
    /**
     * Finds the best route over the map from a
     * starting point to a goal.
     * 
     * @param from is the INode to start from
     * @param to is the target INode 
     * @return the best route to between from and to. If there is
     *                  no route available then null will be returned.
     */
    public Path<INode> findRoute(INode from, INode to);
    
    /**
     * Removes everything from the Map into the initial state.
     */
    public void clearMap();
    
    /**
     * Updates the delay the between each car spawn.
     * 
     * @param delay represented as an integer.
     */
    public void updateSpawnDelay(int delay);
    /**
     * 
     * @return 
     */
    public boolean saveToFile();
    /**
     * 
     * @param time 
     */
    public void setTimeOfDay(int time);
    /**
     * 
     * @param current 
     */
    public void setWeather(Weather current);
    /**
     * 
     * @return 
     */
    public int getTimeOfDay();
    /**
     * 
     * @return 
     */
    public Weather getWeather();
    /**
     * 
     * @param w width of the generated map
     * @param h Height of the generated map
     * @param density
     * @param maxLength
     * @param passes number of times to iterate through the map and add nodes
     * @return 
     */
    public boolean generate(int w, int h, int density, int maxLength, int passes);
    
    /**
     * 
     */
    public void setLightsYellow();
    
    /**
     * 
     */
    public void removeYellowLights();
}
