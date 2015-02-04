/*
 * The MIT License
 *
 * Copyright 2014 Robert Wennergren.
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

import java.util.ArrayList;
import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;
/**
 *
 * @author Robert Wennergren
 */
public class NodeTest extends TestCase {
    
    private Node node;
    
    
    public NodeTest(String testName) {
        super(testName);
    }
    
    /**
     * 
     * @throws Exception 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        node = new Node(1,1);
        
    }
    
    /**
     * 
     * @throws Exception 
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        node  = null;
        
    }
    
    @Test
    public void testNewNode(){
        node = new Node(0,0);
        assertNotNull(node);
    }
    /*
    @Test
    public void testSpawn(){
        Node node2 = new Node(0,0);
        node.connectTo(node2);
        Car car = Car.newInstance();
        assertTrue(node.spawn(car, node.getStart().get(0)));
        
    }
    */
    
    /*
    @Test
    public void testMoveIntoQueue(){
        Node node2 = new Node(0,0);
        Node node3 = new Node(0,0);
        Car car = Car.newInstance();
        assertFalse(node.moveIntoQueue(null, null));
        node2.connectTo(node);
        assertFalse(node.moveIntoQueue(node.getEnd().get(0), null));
        
        try{
            assert(node.moveIntoQueue(null, node.getStart().get(0)));
        }
        catch(IndexOutOfBoundsException e){
                System.out.println(e + "Lane is missing");
                }
        node.spawn(car, node.getEnd().get(0));
        node.connectTo(node3);
        assertTrue(node.moveIntoQueue(node.getEnd().get(0), node.getStart().get(0)));
        
    }
    */
    @Test
    public void testNewLaneStart(){
        Node node2 = new Node(0,0);
        Node node3 = new Node(0,0);
        Node node4 = new Node(0,0);
        Node node5 = new Node(0,0);
        Node node6 = new Node(0,0);
        assertNotNull(node.connectTo(node2));
        assertNotNull(node.connectTo(node3));
        assertNotNull(node.connectTo(node4));
        assertNotNull(node.connectTo(node5));
        assertNull(node.connectTo(node6));
        
        
    }
    
    @Test
    public void testGetNodes(){
        assertNotNull(node.getNodes());
    }
    
    @Test
    public void testGetEnd(){
        assertNotNull(node.getIncomingLanes());
    }
    
    /*
    @Test
    public void testTrafficLights(){
        
     
        Node north = new Node(1,0);
        Node south = new Node(1,2); 
        Node west = new Node(0,1);
        Node east = new Node(2,1);
       
        node.activateTrafficLights();
        node.deactivateTrafficLights();
      
        north.connectTo(node);
        node.connectTo(north);
        
        south.connectTo(node);
        node.connectTo(south);
    
        
        west.connectTo(node);
        node.connectTo(west);
      
        east.connectTo(node);
        node.connectTo(east);
        
        node.activateTrafficLights();
        node.think();
        node.setTrafficLightDelay(10);
        node.think();
        node.deactivateTrafficLights();  
      
        
    }
    */
}
