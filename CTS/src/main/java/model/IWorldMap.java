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
    IDrawable[] getGraphics();
    
    /**
     * To be called each tick
     *
     * @return returns false if error
     */
    boolean think();
    
    /**
     * Finds the best route over the map from a
     * starting point to a goal.
     * 
     * @param from is the INode to start from
     * @param to is the target INode 
     * @return the best route to between from and to. If there is
     *                  no route available then null will be returned.
     */
    Path<INode> findRoute(INode from, INode to);
    
    /**
     * Removes everything from the Map into the initial state.
     */
    void clearMap();
    
    /**
     * Updates the delay the between each car spawn.
     * 
     * @param delay represented as an integer.
     */
    void updateSpawnDelay(int delay);
    /**
     * 
     * @return 
     */
    boolean saveToFile();
    /**
     * 
     * @param time 
     */
    void setTimeOfDay(int time);
    /**
     * 
     * @param current 
     */
    void setWeather(Weather current);
    /**
     * 
     * @return 
     */
    int getTimeOfDay();
    /**
     * 
     * @return 
     */
    Weather getWeather();
    /**
     * 
     * @param w width of the generated map
     * @param h Height of the generated map
     * @param density
     * @param maxLength
     * @param passes number of times to iterate through the map and add nodes
     * @return 
     */
    boolean generate(int w, int h, int density, int maxLength, int passes);
    
    /**
     * 
     */
    void setLightsYellow();
    
    /**
     * 
     */
    void removeYellowLights();
}
