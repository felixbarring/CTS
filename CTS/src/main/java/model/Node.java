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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.path.Path;
import util.DrawableObject;
import util.IDrawable;



/**
 *
 * @author Robert Wennergren <whoisregor@gmail.com>
 * @author Andreas Löfman <lofman.andreas@gmail.com>
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class Node implements INode, Comparable<INode>{
    
    private final List<Lane> outgoing;
    private final List<Lane> incoming;
    private final List<INode> connectedNodes;
    private final List<INode> connectedNodesCopy;
    private final Lane[] sortedIn;
    private final Lane[] sortedOut;
    private boolean trafficLightWE;
    private boolean trafficLightNS;
    private boolean trafficLightNE;
    private boolean trafficLightES;
    private boolean trafficLightSW;
    private boolean trafficLightWN;
    private boolean activateTrafficLights;
    
    private double minDistance = Double.POSITIVE_INFINITY;
    private INode previous;
    
    private final double[] lanePosInX = {5, -5, 10, -10};
    private final double[] lanePosInY = {10, -10, -5, 5};
    
    private final double[] lanePosOutX = {5, -5, -10, 10};
    private final double[] lanePosOutY = {-10, 10, -5, 5};
    
    
    private int trafficLightDelay;
    private final int DELAY_MAX = 75;
    private final int DELAY_MIN = 50;
    
    private final double xpos, ypos;
    
    private int tick;
    private boolean start;
    
    private int deadlockNORTH;
    private int deadlockSOUTH;
    private int deadlockWEST;
    private int deadlockEAST;
    
    private Vehicle deadlockCheckNORTH;
    private Vehicle deadlockCheckSOUTH;
    private Vehicle deadlockCheckWEST;
    private Vehicle deadlockCheckEAST;
    
    private final static int DEADLOCK_CHECKER = 1000;
    
    boolean closedLane = false;
    boolean yellowLight = false;
    boolean rotatingLight = false;
    
    
    /**
     * Creates a generic Node
     * @param x
     * @param y
     */
    protected Node(double x, double y){
        xpos = x;
        ypos = y;
        Random r = new Random();
        outgoing = new ArrayList<Lane>(3);
        incoming = new ArrayList<Lane>(3);
        connectedNodes = new ArrayList<INode>(3);
        connectedNodesCopy = new ArrayList<INode>(3);
        sortedIn = new Lane[4];
        sortedOut = new Lane[4];
        activateTrafficLights = false;
        trafficLightWE = false;
        trafficLightNS = false;
        trafficLightNE = false;
        trafficLightES = false;
        trafficLightSW = false;
        trafficLightWN = false;
        trafficLightDelay = DELAY_MIN + r.nextInt(DELAY_MAX);
        deadlockCheckNORTH = null;
        deadlockCheckSOUTH = null;
        deadlockCheckWEST = null;
        deadlockCheckEAST = null;
        
    }
    
    @Override
    public double getMinDistance(){
        return this.minDistance;
    }
    
    @Override
    public void setMinDistance(double d){
        this.minDistance = d;
    }
    
    @Override
    public INode getPrevious(){
        return this.previous;
    }
    
    @Override
    public void setPrevious(INode n){
        this.previous = n;
    }
    
    /**
     * Returns the x position of the node
     * @return
     */
    @Override
    public double getXpos(){
        return xpos;
    }
    
    /**
     * Returns the y position of the node
     * @return
     */
    @Override
    public double getYpos(){
        return ypos;
    }
    
    
    /**
     * Returns the X-position of an incoming lane
     * @param lane
     * @return
     */
    public double getLaneInX(int lane){
        
        
        return lanePosOutX[lane] + getXpos();
        
    }
    
    /**
     * Returns the Y-position of an incoming lane
     * @param lane
     * @return
     */
    public double getLaneInY(int lane){
        
        
        return lanePosOutY[lane] + getYpos();
        
    }
    
    /**
     * Returns the X-position of an outgoing lane
     * @param lane
     * @param end
     * @return
     */
    public double getLaneOutX(int lane, INode end){
        
        return lanePosInX[lane] + getXpos();
        
    }
    
    /**
     * Returns the Y-position of an outgoing lane
     * @param lane
     * @param end
     * @return
     */
    public double getLaneOutY(int lane, INode end){
        
        return lanePosInY[lane] + getYpos();
        
    }
    
    /**
     * Inserts a vehicle starting on this node going into the lane leading
     * to the next node in its path.
     *
     * The vehicle must have a calculated path.
     *
     * the path must contain this node.
     *
     * @param e
     * @return
     */
    @Override
    public boolean spawn(Vehicle e){
        Path<INode> p = e.getPath();
        
        if (p == null){
            return false;
        }
        
        INode next = p.next(this);
        
        if (next == null){
            return false;
        }
        
        Lane lane = null;
        
        for (Lane l : outgoing){
            if (l != null && l.getEndNode() == next){
                lane = l;
                break;
            }
        }
        
        if (lane != null && lane.hasRoomForVehicle(e)){
            return lane.offerVehicle(e);
        }
        else {
            return false;
        }
    }
    
    /**
     * Moves vehicles from a lane to another lane conncted to the node
     * Returns true if succesful otherwise false
     * @param from
     * @param to
     * @return
     */
    protected boolean moveIntoQueue(Lane from, Lane to) throws IndexOutOfBoundsException{
        
        for(Lane e : incoming){
            
            if(e.equals(from)){
                Vehicle vehicle = from.peekVehicle(); 
                
                for(Object s : outgoing){
                    
                    if(s.equals(to)){
                        Boolean temp =  to.offerVehicle(vehicle); 
                        if(temp == true){
                            from.pollVehicle();
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    
                }
                return false;
            }
        }
        return false;
    }
    
    
    
    /**
     * Creates a new lane starting from this node ending on another node
     * @param endNode
     * @return
     */
    protected Lane connectTo(Node endNode){
        
        
        if(connectedNodes.size() < 4 && outgoing.size() < 4
                && endNode.getIncomingLanes().size() < 4){
            
            Lane lane = new Lane();
            lane.setStartNode(this);
            lane.setEndNode(endNode);
            
            lane.setStartX(this.getXpos());
            lane.setStartY(this.getYpos());
            
            lane.setEndX(endNode.getXpos());
            lane.setEndY(endNode.getYpos());
            
            int direction = checkLanePosOutgoing(lane);
            
            lane.setStartX(this.getLaneInX(direction));
            lane.setStartY(this.getLaneInY(direction));

            lane.setEndX(endNode.getLaneOutX(direction, endNode));
            lane.setEndY(endNode.getLaneOutY(direction, endNode));
            
            this.connectedNodes.add(endNode);
            addOutgoingLane(lane);
            endNode.addIncommingLane(lane);

            return lane;
   
        }
        else{
            return null;
        }
        
    }
    
    private void addOutgoingLane(Lane lane){
        
        this.outgoing.add(lane);
        
    }
    
    private void addIncommingLane(Lane lane){
        
        this.incoming.add(lane);
        
    }
    
    /**
     * Returns list of connectedNodes for the map
     * @return
     */
    
    @Override
    public List<INode> getNodes(){
        
        return connectedNodes;
    }
    
    public List<Lane> getIncomingLanes(){
        
        return incoming;
    }
    
    @Override
    public List<Lane> getOutgoingLanes(){
        
        return outgoing;
    }
    
    
    /*
    Handles how the node handles the cars. It will activate trafficLights() when run.
    */
    @Override
    public boolean think(){
        
        if(activateTrafficLights == false && incoming.size() > 2){
            activateTrafficLights();
        }
        
        if(!start && !rotatingLight){
            start = true;
            laneSort();
            
        }
        else if(!start && rotatingLight){
            start = true;
            laneSortRotating();
        }
        
        if(rotatingLight){
           rotatingLight();
        }
        
        if(activateTrafficLights == false){
            for (Lane l : sortedIn){
                if(l != null){
                    Vehicle v = l.peekVehicle();
                    if (v != null){
                        if (v.getDestination().equals(this)){
                            v = l.pollVehicle();
                            v.kill();
                            return true;
                        }
                        
                        Path<INode> p = v.getPath();
                        
                        if (p == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        INode next = p.next(this);
                        
                        if (next == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        for (Lane out : sortedOut){
                            
                            if(out != null){
                                if (out.getEndNode() == next){
                                    
                                    
                                    if (out.hasRoomForVehicle(v)){ 
                                        v = l.pollVehicle();
                                        out.offerVehicle(v);
                                    }
                                }
                            }
                        }
                    }
                }
                
            }
            
            return true;
        }
        
        if(activateTrafficLights == true){
            
            if(yellowLight){
                tick = 0;
                return yellowLight();
            }
            
            if(tick >= 4*trafficLightDelay/5 && tick < trafficLightDelay){
                yellowLight();
                return true;
            }
            else if (tick >= trafficLightDelay){
                tick = 0;
                switchLights();
            }
                int lane;
                Lane l;
                
            tick++;
            
            for (int i = 0; i < 2; i++){
                if(trafficLightNS){
                    
                    lane = i % 2;
                    
                }
                
                else if(trafficLightWE){
                    
                    lane = (i % 2) + 2;
                    
                }
                else{
                    break;
                }
                l = sortedIn[lane];
                
                if(l != null){
                    
                    
                    Vehicle v = l.peekVehicle();
                    
                    if(closedLane){
                        v.getPath();
                    }
                    
                    checkDeadlock(i, l, v);
                    
                    if (v != null){
                        if (v.getDestination().equals(this)){
                            v = l.pollVehicle();
                            v.kill();
                            return true;
                        }
                        
                        Path<INode> p = v.getPath();
                        
                        if (p == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        INode next = p.next(this);
                        
                        if (next == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        for (Lane out : outgoing){
                            
                            if(out != null){
                                if (out.getEndNode() == next){
                                    if (out.hasRoomForVehicle(v)){
                                        v = l.pollVehicle();
                                        out.offerVehicle(v);
                                    }
                                }
                            }
                        }
                    }
                }
            }  
        } 
        return true;
    }
      
    private void laneSort(){
        
        int direction;
        
        for(Lane l : incoming){
            direction = checkLanePosIncoming(l);
            if(direction != -1){
                sortedIn[direction] = l;   
            } 
        }
        
        for(Lane l : outgoing){
            direction = checkLanePosOutgoing(l);
            if(direction != -1){
                sortedOut[direction] = l; 
            }
        }
        
    }
    
    private int checkLanePosIncoming(Lane l){
        
        if(l == null){
            return -1;
        }
        
        INode n = l.getStartNode();
        
        if(n.getYpos() < this.getYpos()){ // NORTH
            return 0;
        }
        if(n.getYpos() > this.getYpos()){ // SOUTH
            return 1;
        }
        if(n.getXpos()  < this.getXpos()){ // WEST
            return 2;
        }
        
        if(n.getXpos() > this.getXpos()){ // EAST
            return 3;
        }
        else{
            return 0;
        }
        
    }
    
    private int checkLanePosOutgoing(Lane l){
        
        if(l == null){
            return -1;
        }
        
        INode n = l.getEndNode();
        
        if(n.getYpos() < this.getYpos()){ // NORTH
            return 0;
        }
        if(n.getYpos() > this.getYpos()){ // SOUTH
            return 1;
        }
        if(n.getXpos()  < this.getXpos()){ // WEST
            return 2;
        }
        if(n.getXpos()  > this.getXpos()){ // EAST
            return 3;
        }
        else{
            return -1;
        }
        
    }
    
    private void activateTrafficLights(){
        
        activateTrafficLights = true;
        tick = 0;
        double rotating = Math.random() * 10;
        double light = Math.random() * 10;
        
        if(rotating < 5){
            rotatingLight = true;
            setTrafficLightNorthEast();
        }
        
        else if(light < 5){
            setTrafficLightNorthSouth();
        }
        else{
            setTrafficLightWestEast();
        }
        
        
    }
    /*
    This is never used since all traffic Lights was supposed to always be active.
    This is still here if the program is going to get updated in the future.
    */
    protected void deactivateTrafficLights(){
        
        activateTrafficLights = false;
        
        while(incoming.contains(null)){
            incoming.remove(incoming.lastIndexOf(null));
        }
        
        while(outgoing.contains(null)){
            outgoing.remove(outgoing.lastIndexOf(null));
        }
        
    }
    
    private void setTrafficLightWestEast(){
        
        trafficLightWE = true;
        trafficLightNS = false;
        
    }
    
    private void setTrafficLightNorthSouth(){
        
        trafficLightWE = false;
        trafficLightNS = true;
        
    }
    
     private void setTrafficLightNorthEast(){
        
        trafficLightNE = true;
        trafficLightNS = false;
        
    }
    
    private void setTrafficLightEastSouth(){
        
        trafficLightES = true;
        trafficLightNE = false;
     
    }
    
    private void setTrafficLightSouthWest(){
        
        trafficLightSW = true;
        trafficLightES = false;
        
    }
    
    private void setTrafficLightWestNorth(){
        
        trafficLightWN = true;
        trafficLightSW = false;
        
    }
    
    /*
    Makes the trafficLight swtich which lights are on and off
    */
    protected boolean switchLights(){
        
        if(activateTrafficLights && trafficLightWE){
            setTrafficLightNorthSouth();
            return true;
        }
        
        if(activateTrafficLights && trafficLightNS){
            setTrafficLightWestEast();
            return true;
        }
        
        return true;
    }
    
    protected boolean switchLightsRotating(){
        
        if(activateTrafficLights && trafficLightNE){
            setTrafficLightEastSouth();
            return true;
        }
        
        if(activateTrafficLights && trafficLightES){
            setTrafficLightSouthWest();
            return true;
        }
        
        if(activateTrafficLights && trafficLightSW){
            setTrafficLightWestNorth();
            return true;
        }
        
        if(activateTrafficLights && trafficLightWN){
            setTrafficLightNorthEast();
            return true;
        }
    
        return true;
    }
    
    /*
    A delay function to make the cars stack up a bit.
    Mostly used for testing.
    */
    protected void setTrafficLightDelay(int delay){
        
        trafficLightDelay = delay;
        
    }
    
    private boolean yellowLight(){
        
        tick++;
        
        for (Lane l : incoming){
            if(l != null){
                Vehicle v = l.peekVehicle();
                if (v != null && v.takesRisk()){
                    if (v.getDestination().equals(this)){
                        v = l.pollVehicle();
                        v.kill();
                        return true;
                    }
                    
                    Path<INode> p = v.getPath();
                    
                    if (p == null){
                        v = l.pollVehicle();
                        v.kill();
                        return false;
                    }
                    
                    INode next = p.next(this);
                    
                    if (next == null){
                        v = l.pollVehicle();
                        v.kill();
                        return false;
                    }
                    
                    for (Lane out : outgoing){
                        
                        if(out != null){
                            if (out.getEndNode() == next){
                               
                                if (out.hasRoomForVehicle(v)){
                                    v = l.pollVehicle();
                                    out.offerVehicle(v);
                                }
                            }
                        }
                    }
                }
            }
            
        }
        
        return true;
    }
    
    protected boolean closeLane(INode node){
        
        if(connectedNodes.contains(node)){
            
            for(Lane l: outgoing){
                
                if(l.getEndNode() == node){
                    
                    int check = checkLanePosOutgoing(l);
                    
                    sortedOut[check] = null;
                    connectedNodesCopy.add(node);
                    connectedNodes.remove(node);
                    closedLane = true;
                    return true;
                }
                
            }
            
            return false;
        }
        
        else{
            return false;
        }
    }
    
    protected boolean openLane(INode node){
        
        if(connectedNodesCopy.contains(node)){
            
            for(Lane l: outgoing){
                
                if(l.getEndNode() == node){
                    
                    int check = checkLanePosOutgoing(l);
                    
                    if(sortedOut[check] == null){
                        sortedOut[check] = l;
                        connectedNodes.add(node);
                        connectedNodesCopy.remove(node);
                        closedLane = false;
                        
                        return true;
                    }
                }
                
            }
            
            return false;
        }
        
        else{
            return false;
        }
    }
    
    
    
    /*
    Checks for a deadlock in a node with pararell traffic lights.
    */
    private boolean checkDeadlock(int lane, Lane l, Vehicle v){
        
        if(trafficLightWE && activateTrafficLights){
            lane = 2*lane;
        }
        
        
        if(l!=null && v!=null && l.peekVehicle()!=null){
            
            switch(lane){
                case 0:
                    
                    if(deadlockCheckNORTH == null){
                        deadlockCheckNORTH = v;
                    }
                    
                    
                    if(deadlockCheckNORTH.equals(l.peekVehicle())){
                        deadlockNORTH++;
                        
                        if(deadlockNORTH > DEADLOCK_CHECKER){
                            deadlockNORTH = 0;
                            return true;
                        }
                        return true;
                    }
                    
                    else{
                        deadlockCheckNORTH = v;
                        deadlockNORTH = 0;
                        return false;
                    }
                
                case 1:
                    
                    if(deadlockCheckSOUTH == null){
                        deadlockCheckSOUTH = v;
                    }
                
                    if(deadlockCheckSOUTH.equals(l.peekVehicle())){
                        
                        deadlockSOUTH++;
                        
                        if(deadlockSOUTH > DEADLOCK_CHECKER){ 
                            deadlockSOUTH = 0;
                            return true; 
                        } 
                        return true;
                    }
                    
                    else{
                        deadlockCheckSOUTH = v;
                        deadlockSOUTH = 0;
                        return false;
                    }
                
                case 2:
                    
                    if(deadlockCheckWEST == null){
                        deadlockCheckWEST = v;
                    }
                    
                    if(deadlockCheckWEST.equals(l.peekVehicle())){
                        
                        deadlockWEST++;
                        
                        if(deadlockWEST > DEADLOCK_CHECKER){ 
                            deadlockWEST = 0;
                            return true;  
                        }
                        return true;
                    }
                    
                    else{
                        deadlockCheckWEST = v;
                        deadlockWEST = 0;
                        return false;
                    }
                
                case 3:
                    
                    if(deadlockCheckEAST == null){
                        deadlockCheckEAST = v;
                    }
                  
                    if(deadlockCheckEAST.equals(l.peekVehicle())){
                        
                        deadlockEAST++;
                        
                        if(deadlockEAST > DEADLOCK_CHECKER){  
                            deadlockEAST = 0;
                            return true; 
                        }
                        
                        return true;
                    }
                    else{
                        deadlockCheckEAST = v;
                        deadlockEAST = 0;
                        return false;
                    }
                
                default:
                    return false;   
            }
        }
        return false;
        
    }
    
    @Override
    public IDrawable getGraphics() {
        return DrawableObject.getRectGraphics(getXpos(), getYpos(), 0, Color.GRAY, 15, 15);
    }
    
    @Override
    public void setYellowLight(){
        yellowLight = true;
    }
    
    @Override
    public void removeYellowLight(){
        yellowLight = false;
    }
    
        private boolean rotatingLight(){
        
         if(yellowLight){
                tick = 0;
                return yellowLight();
            }
            
            if(tick >= 4*trafficLightDelay/5 && tick < trafficLightDelay){
                yellowLight();
                return true;
            }
            else if (tick >= trafficLightDelay){
                tick = 0;
                switchLightsRotating();
            }
   
                int lane;
                Lane l;
                int laneOut;
                boolean offlane = false;
                
            tick++;
            
            for (int i = 0; i < 2; i++){
                if(trafficLightNE){
                    
                    lane = i % 2;
                    laneOut = 0;
                    
                }
                
                else if(trafficLightES){
                    
                    lane = (i % 2) + 1;
                    laneOut = 1;
                    
                }
                
                else if(trafficLightSW){
                    
                    lane = (i % 2) + 2;
                    laneOut = 2;
                }
                
                else if(trafficLightWN){
                    
                    lane = (((i % 2)+ 3) % 4);
                    laneOut = 3;
                }
                else{
                    break;
                }
              
                l = sortedIn[lane];
                
                if(l != null && !offlane ){
                    offlane = true;
                    
                    
                    Vehicle v = l.peekVehicle();
                    
                    if(closedLane){
                        v.getPath();
                    }
                    
                    checkDeadlock(lane, l, v);
                    
                    if (v != null){
                        if (v.getDestination().equals(this)){
                            v = l.pollVehicle();
                            v.kill();
                            return true;
                        }
                        
                        Path<INode> p = v.getPath();
                        
                        if (p == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        INode next = p.next(this);
                        
                        if (next == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        for (Lane out : sortedOut){
                            
                            if(out != null){
                                if (out.getEndNode() == next){
                                    if (out.hasRoomForVehicle(v)){
                                        v = l.pollVehicle();
                                        out.offerVehicle(v);
                                    }
                                }
                            }
                        }
                    }
                }
                
                else if(l != null && offlane){
                    offlane = false;
                    
                    Vehicle v = l.peekVehicle();
                    
                    if(closedLane){
                        v.getPath();
                    }
                    
                    if (v != null){
                        if (v.getDestination().equals(this)){
                            v = l.pollVehicle();
                            v.kill();
                            return true;
                        }
                        
                        Path<INode> p = v.getPath();
                        
                        if (p == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        INode next = p.next(this);
                        
                        if (next == null){
                            v = l.pollVehicle();
                            v.kill();
                            return false;
                        }
                        
                        for (Lane out : sortedOut){
                            
                            if(out != null && sortedOut[laneOut] == out){
                                if (out.getEndNode() == next){
                                    if (out.hasRoomForVehicle(v)){
                                        v = l.pollVehicle();
                                        out.offerVehicle(v);
                                    }
                                }
                            }
                        }
                    }         
                }
        
            }
             return true;
        }

    
    private void laneSortRotating(){
        
        int direction;
    
        for(Lane l : incoming){
            direction = checkLanePosIncomingRotating(l);
            if(direction != -1){
                sortedIn[direction] = l;
            } 
        }
    
        for(Lane l : outgoing){ 
            direction = checkLanePosOutgoingRotating(l);
            if(direction != -1){
                sortedOut[direction] = l; 
            }
        } 
    }
    
     private int checkLanePosIncomingRotating(Lane l){
        
        if(l == null){
            return -1;
        }
        
        INode n = l.getStartNode();
        
        if(n.getYpos() < this.getYpos()){ // NORTH
            return 0;
        }
        if(n.getXpos() > this.getXpos()){ // EAST
            return 1;
        }
        if(n.getYpos() > this.getYpos()){ // SOUTH
            return 2;
        }
        if(n.getXpos()  < this.getXpos()){ // WEST
            return 3;
        }
        
        else{
            return 0;
        }
        
    }
    
    private int checkLanePosOutgoingRotating(Lane l){
        
        if(l == null){
            return -1;
        }
        
        INode n = l.getEndNode();
        
        if(n.getYpos() < this.getYpos()){ // NORTH
            return 0;
        }
        if(n.getXpos() > this.getXpos()){ // EAST
            return 1;
        }
        if(n.getYpos() > this.getYpos()){ // SOUTH
            return 2;
        }
        if(n.getXpos() < this.getXpos()){ // WEST
            return 3;
        }
        
        else{
            return -1;
        }
        
    }

    @Override
    public int compareTo(INode other) {
        return Double.compare(minDistance, other.getMinDistance());
    }
}




