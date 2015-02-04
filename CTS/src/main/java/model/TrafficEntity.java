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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import model.risk.*;
import model.stress.*;
import util.IDrawable;
import util.RandomColor;
import util.Weather;


/**
 * Abstract class containing shared variables between all
 * traffic entities that they must obey.
 *
 * @invariant 0 <= bathroom <= 1000
 * @invariant 0 <= hunger <= 1000
 *
 * All classes that inherits from TrafficEntity must
 * specify its length and max speed in a super
 * consturctor call.
 *
 * @invariant 0 <= LENGTH <= 100
 * @invariant 0 <= SPEED <= 500
 *
 *
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @revision Andreas Löfman <lofman.andreas@gmail.com> 2014-04-07
 *                      Added move and think methods.
 */
public abstract class TrafficEntity {
    
    private boolean alive = true;
    
    private static int timeOfDay = 8;
    private static Weather weather = Weather.SUNSHINE;
    
    private final String ID;
    private double xpos;
    private double ypos;
    
    private Color color;
    private int bathroom;
    private double yaw;
    private int hunger;
    private boolean drunk;
    private int currentSpeed;
    
    /**
     * Constants
     */
    private final int LENGTH;
    private final int SPEED;
    private final static int MIN_LENGTH = 1;
    private final static int MAX_LENGTH = 50;
    private final static int MIN_SPEED = 1;
    private final static int MAX_SPEED = 500;
    private final static int MAX_HUNGER = 1000;
    private final static int MAX_BATHROOM = 1000;
    
    /**
     * Behaviors extracted with Strategy Pattern
     * Under the interfaces there is a Decorator
     * pattern so the risks can be decorated to
     * combine algorithms as well as choose new
     * main algorithms.
     */
    private Risk risk;
    private Stress stress;
    
    /**
     * @param length as an integer in the range from 1 to 50 meters
     * @param max as an integer in the range from 1 to 500 km/h
     */
    protected TrafficEntity(int length, int max){
        if ((length > MAX_LENGTH || length < MIN_LENGTH) || (max > MAX_SPEED || max < MIN_SPEED)){
            throw new IllegalArgumentException();
        }
        Random random = new Random();
        ID = UUID.randomUUID().toString();
        this.LENGTH = length;
        this.SPEED = max;
        currentSpeed = randomSpeed(SPEED);
        List<Color> ignore = new ArrayList<>();
        ignore.add(Color.GRAY);
        color = RandomColor.generateColor(ignore);
        drunk  = false;
        bathroom = random.nextInt(MAX_BATHROOM -1);
        hunger = random.nextInt(MAX_HUNGER -1);
        this.risk = new Average();
        this.stress = new Normal();
    }
    
    /**
     * Returns a drawable representation of an entity
     * that the GUI can use.
     *
     * @return a drawable object
     */
    protected abstract IDrawable getGraphics();
    
    /**
     * Called on each tick.
     * This is where the entity moves or changes states
     * @return True if successful, False if entity doesn't think or something went wrong
     */
    protected abstract boolean think();
    
    private int randomSpeed(int speed){
        return speed/2 + ((int) (Math.random()*(speed/2))) ;
    }
    
    protected final void setDrunk(){
        drunk = true;
        // This will wrap the Normal Risk evaluation with a Drunk evaluation.
        risk = new Drunk(risk);
    }
    
    public final boolean isDrunk(){
        return drunk;
    }
    
    /**
     * Update the current time.
     * A time is represented in hours by an integer value
     * from 0 to 23. 0 is equal to 24.00.
     *
     * @param time to update to.
     */
    protected final static void setTimeOfDay(int time){
        if (time < 24 && time >= 0){
            timeOfDay = time;
        }
    }
    
    /**
     * Changes the Risk Category for the
     * trafficEntity. Updates the algorithm that
     * calculates the risk taking for the entity.
     *
     * @param risk object containing the algorithm.
     */
    protected final void setRisk(Risk risk){
        if (risk != null){
            this.risk = risk;
        }
    }
    
    /**
     * Changes the Stress Category for the
     * trafficEntity. Updates the algorithm that
     * calculates the stress levels.
     *
     * @param stress object with the new algorithm.
     */
    protected final void setStress(Stress stress){
        if (stress != null){
            this.stress = stress;
        }
    }
    /**
     * Gets the current time in the simulation.
     *
     * @return the current hour as an integer.
     */
    protected final static int getTimeOfDay(){
        return timeOfDay;
    }
    
    /**
     * Update current weather.
     *
     * @param current weather.
     */
    protected final static void setWeather(Weather current){
        if (current != null){
            weather = current;
        }
    }
    
    /**
     * Gets the current weather in the simulation.
     *
     * @return the current weather.
     */
    protected final static Weather getWeather(){
        return weather;
    }
    
    /**
     * Get the x coordinate of the entity.
     *
     * @return the x coordinate as an integer.
     */
    public final double getXpos(){
        return xpos;
    }
    
    /**
     * Get the y coordinate of the entity.
     *
     * @return the y coordinate as an integer.
     */
    public final double getYpos(){
        return ypos;
    }
    
    /**
     * Update the entity's y coordinate.
     *
     * @param ypos of the new position as an integer.
     */
    protected final void setYpos(double ypos){
        this.ypos = ypos;
    }
    
    /**
     * Update the entity's x coordinate.
     *
     * @param xpos of the new position as an integer.
     */
    protected final void setXpos(double xpos){
        this.xpos = xpos;
    }
    
    protected final void kill(){
        alive = false;
    }
    
    public final boolean alive(){
        return alive;
    }
    
    /**
     * Get the unique identification number for the entity.
     *
     * @return the unique identifier.
     */
    public final String getID() {
        return this.ID;
    }
    
    /**
     * Returns a hashcode representation of the entity.
     *
     * @return the hash value of the entity object.
     */
    @Override
    public int hashCode() {
        return 71 * 5 + Objects.hashCode(this.ID);
    }
    
    /**
     * Checks equality of two entities.
     *
     * @param obj compared to this car.
     * @return true if the entities are the same
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrafficEntity other = (TrafficEntity) obj;
        if (!Objects.equals(this.ID, other.getID())) {
            return false;
        }
        return true;
    }
    
    /**
     * Shows the Entity object in a string representation.
     *
     * @return the String object representation.
     */
    @Override
    public String toString() {
        return "TrafficID: " + this.ID;
    }
    
    /**
     * Moves the vehicle for a set distance in its current direction(yaw)
     * @param distance
     */
    protected final void move(double distance){
        double angle = this.getDirection()/(180/Math.PI);
        double dx = distance*Math.cos(angle);
        double dy = distance*Math.sin(angle);
        this.setXpos(this.getXpos()+dx);
        this.setYpos(this.getYpos()+dy);
    }
    
    /**
     * @return the size of the TrafficEntity as an integer
     */
    protected final int getLength() {
        return LENGTH;
    }
    
    /**
     * This method returns the maximum allowed
     * speed that a certain type of entity can travel
     * in.
     *
     * It does not return the current speed. Use getCurrentSpeed()
     * to get that information.
     *
     * @return the max speed as an integer
     */
    protected final int maxSpeed() {
        return SPEED;
    }
    
    /**
     * This method returns the speed that
     * the entity is currently traveling in.
     *
     * @return the current speed as an integer.
     */
    protected final int getCurrentSpeed(){
        return currentSpeed;
    }
    
    /**
     * Updates the color of the TrafficEntity.
     *
     * @param color that the TrafficEntity should have.
     */
    protected final void setColor(Color color) {
        if (color != null){
            this.color = color;
        }
    }
    
    /**
     * Gets the current color of the TrafficEntity
     *
     * @return the current Color of the TrafficEntity.
     */
    protected final Color getColor() {
        return new Color(color.getRed(),color.getGreen(),color.getBlue());
    }
    
    /**
     * Shows the stress level of the driver of the TrafficEntity
     *
     * @return an integer representation of the current stress level.
     */
    protected final int getStress() {
        return stress.calculateStress(bathroom, hunger, timeOfDay);
    }
    
    /**
     * Shows the driver's bladder level.
     *
     * @return an integer representation of the entity's
     *                  current need to use a restroom.
     */
    protected final int getBathroom() {
        return bathroom;
    }
    
    /**
     * Updates the need for the entity to use a restroom.
     *
     * Valid ranges are 0 up to 1000
     *
     * @param bathroom is the new bladder level.
     */
    protected final void setBathroom(int bathroom) {
        if (bathroom >= 0 && bathroom <= MAX_BATHROOM){
            this.bathroom = bathroom;
        }
    }
    
    protected final int getHunger() {
        return hunger;
    }
    
    /**
     * Updates the need hunger for the entity.
     *
     * Valid ranges are 0 up to 1000
     *
     * @param hunger is the new hunger level.
     */
    protected final void setHunger(int hunger) {
        if (hunger >= 0 && hunger <= MAX_HUNGER){
            this.hunger = hunger;
        }
    }
    
    /**
     * Get the direction that the entity is traveling in.
     *
     * @return the current direction (degrees) as a double.
     */
    protected final double getDirection(){
        return yaw;
    }
    
    /**
     * Update the direction the entity is traveling in.
     *
     * @param direction in degrees represented as a double.
     */
    protected final void setDirection(double direction){
        yaw = direction%360;
    }
    
    /**
     * Calculates based on the current state of
     * the trafficEntity if it will take a risk if an
     * opportunity is found.
     *
     * @return true if the traffic entity is prepared to do something stupid,
     * false otherwise.
     */
    protected final boolean takesRisk(){
        return risk.willTakeRisk(getStress(), 0, getWeather(), timeOfDay);
    }
    
    protected final String getRiskDescription(){
        return risk.getDescription();
    }
    
    protected final String getStressStatus(){
        return stress.getName();
    }
    
    /**
     * Gives the Entity a snickers and the effect will vary
     * based on the state of the entity.
     */
    protected final void giveSnickers(){
        if (hunger != 0){
            hunger = (int) hunger / 2;
            if ((bathroom + 10) <= MAX_BATHROOM){
                bathroom += 10;
            }
        }
    }
}
