/*
* The MIT License
*
Copyright 2014 Gustaf Ringius <Gustaf@linux.com>, Felix Bärring <felixbarring@gmail.com>
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

import model.path.Path;

/**
 * Gathered functionality and shared variables for
 * all vehicles which they must fulfill.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @revision Andreas Löfman <lofman.andreas@gmail.com>
 */
public abstract class Vehicle extends TrafficEntity{
    
    private final int YEAR_MODEL;
    private boolean crashed;
    
    /*
    Vehicles are supposed to act like linked lists
    A vehicle has knowledge of what vehicle is in front / behind of itself.
    Based on this the vehicle can drive faster/slower or wait in queues.
    */
    private Vehicle front;
    private Vehicle behind;
    
    /*
    the position the vehicle moves towards
    */
    private double targetX;
    private double targetY;
    private Lane currentLane;
    
    private INode destination;
    private Path<INode> path;
    
    protected Vehicle(int length, int max){
        super(length, max);
        YEAR_MODEL = randomYear(1940, 2014);
        targetX = 0.0;
        targetY = 0.0;
        crashed = false;
        front = null;
        behind = null;
    }
    
    protected final void setDestination(INode d){
        destination = d;
    }
    
    protected final INode getDestination(){
        return destination;
    }
    
    /**
     * @return  the current path of this entity
     */
    protected final Path<INode> getPath(){
        return path;
    }
    
    /**
     * Recalculates the path this entity should follow.
     * 
     * @param from is the starting node.
     * @return false if no path was found, true otherwise
     */
    protected final boolean calculatePath(INode from){
        Path<INode> p = WorldMap.getInstance().findRoute(from, destination); 
        if (p == null){
            return false;
        }
        else {
            path = p;
            return true;
        }
    }
    
    protected final void aimAtTarget(){
        double dx = this.getTargetX() - this.getXpos();
        double dy = this.getTargetY() - this.getYpos();
        double angle = Math.atan2(dy,dx);
        this.setDirection(angle*(180/Math.PI));
    }
    
    /**
     * Changes the lane that the Vehicle is traveling in.
     *
     * @param l the lane that this vehicle is currently on
     */
    protected final void setCurrentLane(Lane l){
        this.currentLane = l;
    }
    
    /**
     * Gets the lane that the Vehicle is traveling in.
     *
     * @return the lane that this vehicle is currently on
     */
    protected final Lane getCurrentLane(){
        return this.currentLane;
    }
    
    /**
     * Updates the Vehicle behind this Vehicle.
     *
     * @param v the vehicle behind this vehicle.
     */
    protected final void setVehicleBehind(Vehicle v){
        if (v == null){
            behind = null;
        }else if (!this.equals(v) && !v.equals(getVehicleInFront())){
            behind = v;
        }
    }
    
    /**
     * Updates the Vehicle in front of this Vehicle.
     *
     * @param v the vehicle in front of this vehicle.
     */
    protected final void setVehicleInFront(Vehicle v){
        if (v == null){
            front = null;
        }else if (!this.equals(v) && !v.equals(getVehicleBehind())){
            front = v;
        }
    }
    
    /**
     * Gets the current Vehicle behind this Vehicle
     * if there is any.
     *
     * @return the Vehicle behind this Vehicle if any, otherwise null.
     */
    protected final Vehicle getVehicleBehind(){
        return behind;
    }
    
    /**
     * Gets the current Vehicle in front of this Vehicle
     * if there is any.
     *
     * @return the vehicle in front of this vehicle if any, otherwise null.
     */
    protected final Vehicle getVehicleInFront(){
        return front;
    }
    
    /**
     * Updates the target position to new coordinates.
     *
     * @param x coordinate represented as a double.
     * @param y coordinate represented as a double.
     */
    protected final void setTargetPosition(double x, double y){
        this.targetX = x;
        this.targetY = y;
    }
    
    /**
     * Gets the current targeted x coordinate.
     *
     * @return target x position represented as a double.
     */
    protected final double getTargetX(){
        return targetX;
    }
    
    /**
     * Gets the current targeted y coordinate.
     *
     * @return target y position represented as a double.
     */
    protected final double getTargetY(){
        return targetY;
    }
    
    /**
     * Calculates and returns the distance to the closest
     * vehicle in front of this one.
     *
     * @return the distance to the vehicle in front of this one,
     *                  represented as a double. 0.0 if there is no
     *                  vehicle in front of this one.
     */
    protected final double getFrontDistance(){
        if (front != null){
            double dx = Math.abs(front.getXpos() - this.getXpos());
            double dy = Math.abs(front.getYpos() - this.getYpos());
            return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        }
        return 0.0;
    }
    
    /**
     * Calculates and returns the distance to the closest
     * vehicle behind this one.
     *
     * @return the distance to the vehicle behind this one,
     *                  represented as a double. 0.0 if there is no
     *                  vehicle behind this one.
     */
    protected final double getBehindDistance(){
        if (behind != null){
            double dx = Math.abs(behind.getXpos() - this.getXpos());
            double dy = Math.abs(behind.getYpos() - this.getYpos());
            return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        }
        return 0.0;
    }
    
    /**
     * Calculates and returns the target distance.
     *
     * @return the distance to the vehicle's target represented as a double.
     */
    protected final double getTargetDistance(){
        double dx = Math.abs(targetX - this.getXpos());
        double dy = Math.abs(targetY - this.getYpos());
        return Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
    }
    
    /**
     * Returns the crash status of a vehicle.
     *
     * @return true if vehicle is currently crashed, false otherwise.
     */
    protected final boolean getCrashed() {
        return crashed;
    }
    
    /**
     * Updates the crash state of a vehicle.
     *
     * @param crashed should be true if the vehicle has crashed,
     *                                  false otherwise.
     */
    protected final void setCrashed(boolean crashed) {
        this.crashed = crashed;
    }
    
    /**
     * All Vehicles have a year of production.
     *
     * @return the year model of a vehicle represented as an integer.
     */
    protected final int getYearModel(){
        return YEAR_MODEL;
    }
    
    /**
     * Returns a randomly generated year
     * between the first and second parameter.
     *
     * @param start is the minimum year as a positive integer
     * @param end is the maximum year as a positive integer
     * @return a random year between start and end if
     * start is smaller than end and start is non negative.
     * Otherwise returns 0
     */
    private int randomYear(int start, int end) {
        if (end > start && 0 < start){
            return (start + (int)Math.round(Math.random() * (end - start)));
        }
        return 0;
    }
    
}
