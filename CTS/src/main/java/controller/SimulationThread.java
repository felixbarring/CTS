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
import model.IWorldMap;

/**
 * Thread that will run the simulation logic.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @author Andreas Löfman <lofman.andreas@gmail.com>
 * @author Felix Bärring <felixbarring@gmail.com>
 */
public final class SimulationThread implements Runnable{
    
    private final IWorldMap map;
    private volatile boolean running = false;
    
    protected SimulationThread(IWorldMap map) {
        this.map = map;
        this.running = true;
    }
    
    @Override
    public void run() {
        int loop = 20;
        while (running){
            long startTime = System.currentTimeMillis();
            map.think();
            long sleepTime = loop-(System.currentTimeMillis()-startTime);
            //System.out.println(sleepTime);
            if (sleepTime > 0){
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    protected void stop(){
        this.running = false;
    }
    
    public boolean getRunning(){
        return this.running;
    }
}
