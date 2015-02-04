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
import util.IDrawable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import model.path.Path;
import util.DrawableObject;
import util.Weather;

/**
 * Central hub of the model. Will handle all communication within the model. 
 *
 * @author Andreas Löfman <lofman.andreas@gmail.com>
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-04-01 Added singleton
 * pattern and factory method.
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-04-03 Added getGraphics()
 * method.
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-04-08 Added thread safety
 * and synchronization to the shared resources.
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-04-28 Fixed singleton again,
 * changed to diamond on ArrayList definition. Fixed so we program against
 * Interface and not concrete classes (INode). First attempt to add a destroyMap
 * method.
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-05-15 fixed so we program
 * against interfaces and return List<type> and not implementation specific lists like
 * ArrayList<type> for example.
 * @revision Gustaf Ringius <Gustaf@linux.com> 2014-05-15 removed all the
 * representation exposure and merges in my Dijkstra class here. Since there are
 * only two methods I do not see the need to make an additional inner class. I made
 * my calculations private and then made a findRoute method.
 * 
 * @invariant 0 <= spawnCooldownCounter < 
 */
public class WorldMap implements IWorldMap {
    
    private static int spawnCoolDown = 5;
    private static final int MAX_VEHICLES = 500;
    
    private int spawnCooldownCounter = 0;
    
    private static WorldMap INSTANCE;
    //Shared resources
    private volatile List<Lane> lanes = new ArrayList<>();
    private volatile List<TrafficEntity> entities = new ArrayList<>();
    private volatile List<INode> nodes = new ArrayList<>();
    private volatile List<INode> ends = new ArrayList<>();
   
    private volatile boolean initialized = false;
    
    private WorldMap() {
    }
    
    /**
     * Returns and IWorldMap
     * 
     * @return the  instance of worldMap
     */
    public static IWorldMap getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WorldMap();
        }
        return INSTANCE;
    }
    
    @Override
    public void setLightsYellow(){
        for (INode n : nodes){
            n.setYellowLight();
        }
    }
    
    @Override
    public void removeYellowLights(){
        for (INode n : nodes){
            n.removeYellowLight();
        }
    }
    
    /**
     * Map destructor.
     */
    public static synchronized void destroyMap() {
        INSTANCE = null;
    }
    
    @Override
    public void clearMap(){
        entities = new ArrayList<>();
        for (Lane l : lanes){
            l.clearCars();
        }
    }
    
    @Override
    public synchronized IDrawable[] getGraphics() {
        IDrawable[] list = new IDrawable[entities.size() + lanes.size() + nodes.size()+(entities.size() > 0 ? 1 : 0)];
        int j = 0;
        
        for (int i = 0; i < lanes.size(); i++) {
            list[j] = lanes.get(i).getGraphics();
            j++;
        }
        for (int i = 0; i < nodes.size(); i++){
            list[j] = nodes.get(i).getGraphics();
            j++;
        }
        for (int i = 0; i < entities.size(); i++) {
            list[j] = entities.get(i).getGraphics();
            j++;
        }
        if (entities.size() > 0){
            list[j] = DrawableObject.getCircleGraphics(entities.get(0).getXpos(), entities.get(0).getYpos(), 0, Color.yellow, 5);
        }
        return list;
    }
    
    @Override
    public void updateSpawnDelay(int delay){
        spawnCoolDown = delay;
    }
    
    @Override
    public synchronized boolean think() {
        if (initialized == true) {
            
            spawnCooldownCounter--;
            
            //If the cooldown has passed, try to spawn a new car
            if (entities.size() < MAX_VEHICLES && spawnCooldownCounter <= 0) {
                spawnCooldownCounter = spawnCoolDown;
                Vehicle c = Car.newInstance();
                INode startNode;
                int choice = (int) Math.floor(Math.random() * ends.size());
                startNode = ends.get(choice);
                choice = (int) Math.floor(Math.random() * ends.size());
                c.setDestination(ends.get(choice));
                c.calculatePath(startNode);
                c.setXpos(50);
                c.setYpos(50);
                
                //Check if this node can take the car, otherwise don't spawn it.
                if (startNode.spawn(c)) {
                    entities.add(c);
                }
            }
            
            int i = 0;
            //Iterate through all entities and make them think.
            while (i < entities.size()) {
                TrafficEntity e = entities.get(i);
                if (e.alive()) {
                    e.think();
                    i++;
                } else {
                    entities.remove(e);
                }
                
            }
            //Iterate through all nodes and make them think.
            for (INode n : nodes) {
                n.think();
            }
        }
        return true;
    }
    
    @Override
    public boolean generate(int w, int h, int density, int maxLength, int passes) {
        int width = (int) Math.floor(w / density);
        int height = (int) Math.floor(h / density);
        RandomEnum<Direction> r = new RandomEnum<>(Direction.class);
        
        //The temporary map matrix
        NElement matrix[][] = new NElement[width][height];
        
        //Fill the array with empty elements
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = new NElement(i, j);
                matrix[i][j].content = NContent.EMPTY;
            }
        }
        
        //Insert a starting node
        int x = (int) (Math.random() * (width - 1));
        int y = (int) (Math.random() * (height - 1));
        matrix[x][y].content = NContent.NODE;
        //System.out.println("Starting at: "+x+","+y);
        
        
        for (int pass=0; pass<passes;pass++){
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    if (matrix[i][j].content == NContent.NODE){
                        
                        //Chance of adding another road based on the number of currently connected nodes
                        int chance = 100-(25*matrix[i][j].connected.size());
                        int random = (int)(Math.random()*100);
                        
                        if (random < chance && matrix[i][j].connected.size() < 4){
                            //Add a node
                            
                            int len = (int)(Math.random() * (maxLength-1))+1;
                            //System.out.println(len);
                            Direction dir = r.random();
                            
                            //Check the directions untill we find one that is free
                            for (int k=0; k<3; k++){
                                if (!checkInside(matrix,i,j,dir)){
                                    dir = rotateDirection(dir);
                                }
                                else if (peekDirection(matrix,i,j,dir) != NContent.EMPTY){
                                    dir = rotateDirection(dir);
                                }
                                else {
                                    break;
                                }
                            }
                            
                            //Check if we actually found a direction that was free
                            if (checkInside(matrix,i,j,dir) && peekDirection(matrix,i,j,dir) == NContent.EMPTY){
                                
                                //Now we have to step one step at a time in the direction and check that its free
                                //If its not, we stop and place a node where we ended up before running in to a node
                                x = i+getXmultiplier(dir);
                                y = j+getYmultiplier(dir);
                                
                                boolean created = false;
                                for (int k=0; k<len; k++){
                                    matrix[x][y].content = NContent.ROAD;
                                    //Either we step one more step
                                    if (checkInside(matrix,x,y,dir) && peekDirection(matrix,x,y,dir) != NContent.ROAD){
                                        x = x+getXmultiplier(dir);
                                        y = y+getYmultiplier(dir);
                                        if (matrix[x][y].content == NContent.NODE && matrix[x][y].connected.size() < 4){
                                            matrix[x][y].connected.add(matrix[i][j]);
                                            matrix[i][j].connected.add(matrix[x][y]);
                                            created = true;
                                            break;
                                        }
                                    }
                                    //Or we create a node where we are
                                    else {
                                        //System.out.println("forced to stop");
                                        matrix[x][y].content = NContent.NODE;
                                        matrix[x][y].connected.add(matrix[i][j]);
                                        matrix[i][j].connected.add(matrix[x][y]);
                                        created = true;
                                        break;
                                    }
                                }
                                // If we could travel the whole distance, create a node
                                if (!created && !(x == i && y == j) && matrix[x][y].content != NContent.ROAD){
                                    matrix[x][y].content = NContent.NODE;
                                    matrix[x][y].connected.add(matrix[i][j]);
                                    matrix[i][j].connected.add(matrix[x][y]);
                                    created = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        NElement current,first,second;
        
        //Do some cleaning
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                if (matrix[i][j].content == NContent.NODE && matrix[i][j].connected.size() == 2){
                    current = matrix[i][j];
                    first = current.connected.get(0);
                    second = current.connected.get(1);
                    //Are the nodes all in a straight line on the X axis?
                    if (first.x == second.x && second.x == current.x){
                        first.connected.remove(current);
                        second.connected.remove(current);
                        if (!first.connected.contains(second)){
                            first.connected.add(second);
                        }
                        if (!second.connected.contains(first)){
                            second.connected.add(first);
                        }
                        current.content = NContent.ROAD;
                    }
                    //Are the nodes all in a straight line on the Y axis?
                    if (first.y == second.y && second.y == current.y){
                        first.connected.remove(current);
                        second.connected.remove(current);
                        if (!first.connected.contains(second)){
                            first.connected.add(second);
                        }
                        if (!second.connected.contains(first)){
                            second.connected.add(first);
                        }
                        current.content = NContent.ROAD;
                    }
                }
            }
        }
        
        
        //Translate the matrix into actual nodes
        Node ns[][] = new Node[width][height];
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (matrix[i][j].content == NContent.NODE) {
                    ns[i][j] = new Node((i * density)+density/2, (j * density)+density/2);
                    nodes.add(ns[i][j]);
                }
            }
        }
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (ns[i][j] != null) {
                    for (NElement n : matrix[i][j].connected) {
                        if (ns[n.x][n.y] != null && ns[n.x][n.y] != ns[i][j]) {
                            //System.out.println("Connected: " + i + "," + j + " - " + n.x + "," + n.y);
                            Lane l = ns[i][j].connectTo(ns[n.x][n.y]);
                            if (l != null) lanes.add(l);
                        }
                    }
                }
            }
        }
        
        for (INode n : nodes){
            if (n.getNodes().size() == 1) ends.add(n);
        }
        initialized = true;
        return true;
    }
    
    @Override
    public boolean saveToFile() {
        // TODO: same as loadFromFile
        return false;
    }
    
    @Override
    public void setTimeOfDay(int time) {
        TrafficEntity.setTimeOfDay(time);
    }
    
    @Override
    public void setWeather(Weather current) {
        TrafficEntity.setWeather(current);
    }
    
    @Override
    public int getTimeOfDay() {
        return TrafficEntity.getTimeOfDay();
    }
    
    @Override
    public Weather getWeather() {
        return TrafficEntity.getWeather();
    }
    
    @Override
    public Path<INode> findRoute(INode from, INode to){
        computePaths(from);
        return getShortestPathTo(to);
    }
    
    /*
    ============= Below is Gustaf's Dijkstra ShortestPath ================
    */
    
    /**
     * Computes all the Paths in a graph from a starNode.
     *
     * @param source to begin calculation from.
     */
    private void computePaths(INode source) {
        
        for (INode n : nodes){
            n.setMinDistance(Double.POSITIVE_INFINITY);
            n.setPrevious(null);
        }
        
        source.setMinDistance(0);
        Queue<INode> nodeQueue = new PriorityQueue<>();
        nodeQueue.add(source);
        
        while (!nodeQueue.isEmpty()) {
            INode node1 = nodeQueue.poll();
            
            // Visit each edge exiting node1
            for (Lane l : node1.getOutgoingLanes()) {
                INode node2 = l.getEndNode();
                int weight = l.getWeight();
                double distanceThroughU = node1.getMinDistance() + weight;
                if (distanceThroughU < node2.getMinDistance()) {
                    nodeQueue.remove(node2);
                    node2.setMinDistance(distanceThroughU);
                    node2.setPrevious(node1);
                    nodeQueue.add(node2);
                }
            }
        }
    }
    
    /**
     * Returns the shortest path to the target in terms
     * of weights.
     *
     * @param target node to reach.
     * @return a Path to the taget node.
     */
    private Path<INode> getShortestPathTo(INode target) {
        Path<INode> path = new Path<>();
        for (INode node = target; node != null; node = node.getPrevious()){
            path.append(node, 1);
        }
        Path<INode> reverse = new Path<>();
        for (int i = path.getLength(); i > 0; i--){
            reverse.append(path.get(i), 1);
        }
        if (reverse.getLength() <= 1){
            return null;
        }
        return reverse;
    }
    
    
    
    /*
    ============= BELOW ARE HELPER FUNCTIONS FOR MAP GENERATION ================
    */
    
    /**
     *  Used in the map generation for storing data in the matrix.
     */
    private static class NElement {
        
        public final ArrayList<NElement> connected;
        public final int x, y;
        public NContent content;
        
        public NElement(int x, int y) {
            this.x = x;
            this.y = y;
            connected = new ArrayList<>();
        }
    }
    
    /**
     *   Represents the different contents of the map generation matrix
     */
    private enum NContent {
        ROAD,
        NODE,
        EMPTY
    }
    
    /**
     * Just an enum representing directions
     */
    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    
    /**
     * A class that can generate random values for a given enum
     * @param <E> An Enum
     */
    private static class RandomEnum<E extends Enum> {
        
        private static final Random RND = new Random();
        private final E[] values;
        
        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }
        
        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }
    
    /**
     * Get the multiplier in the x direction
     * @param dir
     * @return
     */
    private int getXmultiplier(Direction dir){
        switch (dir){
            case LEFT:
                return -1;
            case RIGHT:
                return 1;
            default:
                return 0;
        }
    }
    
    /**
     * Get the multiplier in the y direction
     * @param dir
     * @return
     */
    private int getYmultiplier(Direction dir){
        switch (dir){
            case UP:
                return -1;
            case DOWN:
                return 1;
            default:
                return 0;
        }
    }
    
    /**
     * Rotate the direction one step clockwise
     * @param dir the starting direction
     * @return the resulting direction
     */
    private Direction rotateDirection(Direction dir){
        switch (dir){
            case UP:
                return Direction.RIGHT;
            case DOWN:
                return Direction.LEFT;
            case LEFT:
                return Direction.UP;
            case RIGHT:
                return Direction.DOWN;
        }
        return Direction.UP;
    }
    
    
    /**
     * Check the the element one step in the direction given from x,y
     *
     * @param matrix the map matrix from the generator function
     * @param x the x position to start at
     * @param y the y position to start at
     * @param dir the direction to travel in
     * @return the content of the element one step in the direction given
     */
    private NContent peekDirection(NElement[][] matrix, int x, int y, Direction dir){
        //System.out.println("X: "+x+"  Y: "+y);
        if (checkInside(matrix, x, y, dir)){
            switch (dir){
                case UP:
                    return matrix[x][y-1].content;
                case DOWN:
                    return matrix[x][y+1].content;
                case LEFT:
                    return matrix[x-1][y].content;
                case RIGHT:
                    return matrix[x+1][y].content;
            }
        }
        return NContent.EMPTY;
    }
    
    /**
     * Check if one step in the direction given from x,y is inside the matrix
     * @param matrix given map matrix
     * @param x starting x
     * @param y starting y
     * @param dir direction to check
     * @return true if in the bounds of matrix, false if not
     */
    private boolean checkInside(NElement[][] matrix, int x, int y, Direction dir){
        switch (dir){
            case UP:
                if (y-1 < 0) return false;
            case DOWN:
                if (y+1 >= matrix[0].length)return false;
            case LEFT:
                if (x-1 < 0) return false;
            case RIGHT:
                if (x+1 >= matrix.length) return false;
        }
        return true;
    }
}
