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

package controller;

import model.WorldMap;
import util.IDrawable;
import model.IWorldMap;
import util.Weather;


/**
 * Main Controller class that will handle all calls and
 * requests between the GUI and the model.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>,
 * @author Andreas Löfman <lofman.andreas@gmail.com>
 * @author Felix Bärring <felixbarring@gmail.com>
 */
public class MainController {
    private volatile static IWorldMap worldMap;
    private static SimulationThread simThread;
    
    /**
     * Gets a list of a immutable DrawAbleObject representation
     * of the model parts that needs to be drawn by
     * the gui.
     *
     * @return a list of DrawAbleObjects if any, otherwise null.
     */
    public final static IDrawable[] getGraphics(){
        return worldMap.getGraphics();
    }
    
    /**
     * Generates a new map and starts a simulation.
     * 
     * @param width of the new map as an integer.
     * @param height of the new map as an integer.
     */
    public final static void generatMap(int width, int height){
        // Generate a map.
        WorldMap.destroyMap();
        worldMap = WorldMap.getInstance();
        worldMap.generate(width, height, 50, 5, 100);
        resumeSimulation();
    }
    
    /**
     * Loads a saved map and starts a simulation.
     * 
     * @param map to be loaded. Only name necessary.
     */
    public final static void loadMap(String map){
        // Load an existing map.
        worldMap = WorldMap.getInstance();
        resumeSimulation();
    }
    
    public synchronized final static void endSimulation(){
        pauseSimulation();
        WorldMap.destroyMap();
        worldMap = null;
    }
    
    public synchronized final static void pauseSimulation(){
        if(simThread != null){
            simThread.stop();
            simThread = null;
        }
    }
    
    public synchronized final static void clear(){
        worldMap.clearMap();
    }
    
    public synchronized final static void resumeSimulation(){
        if(simThread != null) return;
        simThread = new SimulationThread(worldMap);
        new Thread(simThread).start();
    }
    
    /**
     * Updates the delay the between each car spawn.
     * 
     * @param delay represented as an integer.
     */
    public synchronized static void setSpawnDelay(int delay){
        worldMap.updateSpawnDelay(delay);
    }
    
    /**
     * Updates the current time in the simulation if
     * the new time is valid.
     * 
     * @param time represented as an integer between 0 and 23.
     */
    public synchronized static void setTimeOfDay(int time){
        worldMap.setTimeOfDay(time);
    }
    
    /**
     * Updates the current Weather if valid.
     * 
     * @param current is the new weather in the simulation.
     */
    public synchronized static void setWeather(Weather current){
        worldMap.setWeather(current);
    }
    
    public synchronized static int getTimeOfDay(){
        return worldMap.getTimeOfDay();
    }
    
    public synchronized static Weather getWeather(){
        return worldMap.getWeather();
    }
    
    public static void setLightsYellow(){
        worldMap.setLightsYellow();
    }
    public static void removeYellowLights(){
        worldMap.removeYellowLights();
    }
    
}
