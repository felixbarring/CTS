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

import java.awt.Color;
import util.DrawableObject;
import util.IDrawable;

/**
 * A Class representing a driving lane in the map.
 * @author Andreas Löfman <lofman.andreas@gmail.com>
*/
public class Lane {
    private double startX=0.0, startY=0.0, endX=0.0, endY=0.0;
    
    private Vehicle first, last;
    
    private INode start,end;
    
    protected Lane(){
        first = null;
        last = null;
    }
    
    protected void setStartNode(INode s){
        start = s;
    }
    
    protected void setEndNode(INode e){
        end = e;
    }
    
    protected INode getStartNode(){
        return start;
    }
    
    protected INode getEndNode(){
        return end;
    }
    
    protected void setStartX(double n){
        this.startX = n;
    }
    
    protected void setStartY(double n){
        this.startY = n;
    }
    
    protected void setEndX(double n){
        this.endX = n;
    }
    
    protected void setEndY(double n){
        this.endY = n;
    }
    
    public double getStartX(){
        return startX;
    }
    
    public double getStartY(){
        return startY;
    }
    
    public double getEndX(){
        return endX;
    }
    
    public double getEndY(){
        return endY;
    }
    
    /**
     * Get the total number of vehicles in the lane.
     * 
     * @return the number of vehicles in the lane represented
     * as an integer. 0 if no vehicles in the lane.
     */
    public int getNumberOfVehicles(){
        int num = 0;
        Vehicle cur = first;
        while (cur != null){
            num++;
            cur = cur.getVehicleBehind();
        }
        return num;
    }
    
    /**
     * Get the weight of this lane
     * @return 
     */
    public int getWeight(){
        return getNumberOfVehicles();
    }
    
    protected void clearCars(){
        last = null;
        first = null;
    }
    
    /**
     * Peeks at the first Vehicle in the lane if any.
     * 
     * Peek will only show the first Vehicle and not remove it
     * from the lane.
     * 
     * @return the first vehicle is any, otherwise null.
     */
    protected Vehicle peekVehicle(){
        if (getNumberOfVehicles() == 0){
            return null;
        }
        if (this.first.getTargetDistance() == 0){
            return this.first;
        }
        return null;
    }
    
    /**
     * Poll gets a vehicle from the lane and removes
     * it from the lane.
     * 
     * TODO: Might need null check here. (fixed) /Gustaf
     * 
     * @return the first vehicle in the lane, if any otherwise null.
     */
    protected Vehicle pollVehicle(){
        if (getNumberOfVehicles() == 0){
            return null;
        }
        if (this.first.getTargetDistance() == 0){
            Vehicle temp = this.first;
            if (temp.getVehicleBehind() == null){
                this.first = null;
                this.last = null;
                temp.setVehicleBehind(null);
                temp.setVehicleInFront(null);
            }
            else {
                this.first = temp.getVehicleBehind();
                this.first.setVehicleInFront(null);
                temp.setVehicleBehind(null);
                temp.setVehicleInFront(null);
            }
            return temp;
        }
        return null;
    }
    
    /**
     * Check if there's room for the vehicle in the current
     * line. 
     * 
     * TODO Length check since a truck is longer than a car for example.
     * 
     * @param v is the vehicle that wants to get into the lane.
     * @return true if there's enough space for the vehicle. False otherwise.
     */
    protected boolean hasRoomForVehicle(Vehicle v){
        if (last == null){
            return true;
        }
        double dx = Math.abs(this.startX - this.last.getXpos());
        double dy = Math.abs(this.startY - this.last.getYpos());
        double distance = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
        if (distance > this.last.getLength()+v.getLength()){
            return true;
        }
        return false;
    }
    
    /**
     * Try to insert a vehicle into the lane.
     * 
     * TODO: Does not work to put more than one car in the lane
     * 
     * @param v is the vehicle to insert into the lane.
     * @return true if vehicle was successfully added to the lane,
     *                  false otherwise.
     */
    protected boolean offerVehicle(Vehicle v){
        if (!this.hasRoomForVehicle(v) || v == null){
            return false;
        }
        if (this.last != null){
            this.last.setVehicleBehind(v);
            v.setVehicleInFront(this.last);
            v.setVehicleBehind(null);
            this.last = v;
        }
        else {
            v.setVehicleBehind(null);
            v.setVehicleInFront(null);
            this.last = v;
            this.first = v;
        }
        v.setCurrentLane(this);
        v.setTargetPosition(this.endX, this.endY);
        v.setXpos(this.startX);
        v.setYpos(this.startY);
        return true;
    }
    
    
    protected IDrawable getGraphics() {
        return DrawableObject.getLineGraphics(Color.gray, startX, startY, endX, endY);
    }
}
