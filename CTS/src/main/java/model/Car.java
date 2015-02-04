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
import util.DrawableObject;


/**
 * Class representing the Car objects.
 *
 * The class only holds the current state of a car.
 * 
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @revision Andreas Löfman <lofman.andreas@gmail.com>
 */
public class Car extends Vehicle {
    
    /**
     * Private constructor means no inheritance. 
     */
    private Car(){
        super(2,160);
    }
    
    /**
     * Factory Method that creates new instances of cars.
     * Cars are 2 meters in length
     * 
     * @return a newly created car
     */
    protected final static Vehicle newInstance(){
        return new Car();
    }
    
    /**
     * This should be split into more methods if possible. 
     * It is too clumped up, hard to test and debug 
     * and might be a victim of redundant code.
     * 
     * @return 
     */
    @Override
    protected boolean think(){
        int tailgateDistance = 10;
        if (this.getCrashed()){
            return false;
        }
        
        //TODO: make all of this more exact
        //      Acceleration
        //      Vehicle lengths
        //      Collissions
        
        double moveDistance = getCurrentSpeed()/50;
        if (this.getVehicleInFront() != null){
            //We must look at where the vehicle in front is.
            if (this.getFrontDistance() > moveDistance+tailgateDistance+this.getVehicleInFront().getLength()){
                this.aimAtTarget();
                this.move(moveDistance);
            }
            //If the vehicle in front is too close, dont move
            else {
                //TODO: implement braking and brakedistance to check if vehicles collide
            }
        }
        else {
            //System.out.println(this.hashCode()+": "+this.getXpos()+", "+this.getYpos());
            //We have no vehicle in front, just move as you wish, based on the target.
            if (this.getTargetDistance() > moveDistance){
                this.aimAtTarget();
                this.move(moveDistance);
            }
            //Not enough distance to move, just set the position to the target.
            else {
                this.setXpos(this.getTargetX());
                this.setYpos(this.getTargetY());
                // wait for the crossing, which routes us through to the next road
            }
        }
        
        return true;
    }

    @Override
    protected IDrawable getGraphics() {
        return DrawableObject.getRectGraphics(getXpos(), getYpos(), getDirection(), getColor(), 3, 3);
    }
}