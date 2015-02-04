/*
* The MIT License
*
* Copyright 2014 Andreas Löfman <lofman.andreas@gmail.com>.
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import model.path.Path;
import org.junit.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Gustaf Ringius <Gustaf@linux.com> 
 * @revision Andreas Löfman <lofman.andreas@gmail.com>
 *                      made testGetPathSimple & testGetPathNotConnected
 */
public class WorldMapTest {
    
    private static final List<Lane> lanes = new ArrayList<>();
    private static final List<INode> nodes = new ArrayList<>();
    
    private static final List<INode> ends = new ArrayList<>();
    
    private static final Node n1 = new Node(200, 000);
    private static final Node n2 = new Node(200, 100);
    private static final Node n3 = new Node(300, 100);
    private static final Node n4 = new Node(400, 100);
    private static final Node n5 = new Node(200, 200);
    private static final Node n6 = new Node(300, 000);
    private static final Node n7 = new Node(100, 100);
    private static final Node n8 = new Node(100, 200);
    
    private static IWorldMap world;
    
    private static Field field1;
    private static Field field2;
    private static Field field3;
    
    public WorldMapTest(){}
    
    @BeforeClass
    public static void setUpClass() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        world = WorldMap.getInstance();
        world.generate(800, 600, 50, 5, 100);
        ends.add(n1);
        ends.add(n8);
        ends.add(n5);
        ends.add(n4);
        ends.add(n6);
        
        lanes.add(n8.connectTo(n5));
        lanes.add(n5.connectTo(n8));
        
        lanes.add(n2.connectTo(n1));
        lanes.add(n5.connectTo(n2));
        lanes.add(n3.connectTo(n2));
        lanes.add(n4.connectTo(n3));
        lanes.add(n6.connectTo(n3));
        
        lanes.add(n7.connectTo(n2));
        lanes.add(n2.connectTo(n7));
        lanes.add(n8.connectTo(n7));
        lanes.add(n7.connectTo(n8));
        
        lanes.add(n1.connectTo(n2));
        lanes.add(n2.connectTo(n5));
        lanes.add(n2.connectTo(n3));
        lanes.add(n3.connectTo(n4));
        lanes.add(n3.connectTo(n6));
        
        nodes.add(n1);
        nodes.add(n2);
        nodes.add(n3);
        nodes.add(n4);
        nodes.add(n5);
        nodes.add(n6);
        nodes.add(n7);
        nodes.add(n8);
        
        Class<?> clazz = WorldMap.class;
        field1 = clazz.getDeclaredField("lanes");
        field2 = clazz.getDeclaredField("nodes");
        field3 = clazz.getDeclaredField("ends");
        field1.setAccessible(true);
        field2.setAccessible(true);
        field3.setAccessible(true);
        field1.set(world, lanes);
        field2.set(world, nodes);
        field3.set(world, ends);
    }
    
    @Test
    public void testGetPathSimple(){
        System.out.println("Test: testGetPath with 2 nodes");
        
        Node n1 = new Node(0,0);
        Node n2 = new Node(1,1);
        
        Lane l1 = n1.connectTo(n2);
        Lane l2 = n2.connectTo(n1);
        
        Path<INode> p = world.findRoute(n1, n2);
        
        Assert.assertTrue(p.getLength() == 2);
        Assert.assertTrue(p.get(1) == n1);
        Assert.assertTrue(p.get(2) == n2);
    }
    
    @Test
    public void testGetPathSimpleThree(){
        System.out.println("Test: testGetPath with 3 nodes");
        
        Node ln1 = new Node(0,0);
        Node ln2 = new Node(1,1);
        Node ln3 = new Node(2,2);
        
        Lane ll1 = ln1.connectTo(ln2);
        Lane ll2 = ln2.connectTo(ln1);
        Lane ll3 = ln2.connectTo(ln3);
        Lane ll4 = ln3.connectTo(ln2);
        
        Path<INode> p = world.findRoute(ln1, ln3);
        
        Assert.assertTrue(p.getLength() == 3);
        System.out.println("first path: " + p);
        Assert.assertTrue(p.get(1) == ln1);
        Assert.assertTrue(p.get(2) == ln2);
        Assert.assertTrue(p.get(3) == ln3);
        
        /*
            Reset nodes since these are out of the control
            for the algorithm!
        */
        ln1.setMinDistance(Double.POSITIVE_INFINITY);
        ln1.setPrevious(null);
        ln2.setMinDistance(Double.POSITIVE_INFINITY);
        ln2.setPrevious(null);
        ln3.setMinDistance(Double.POSITIVE_INFINITY);
        ln3.setPrevious(null);
        
        p =  world.findRoute(ln2, ln3);
        System.out.println("second path : " + p);
        Assert.assertTrue(p.getLength() == 2);
        Assert.assertTrue(p.get(1) == ln2);
        Assert.assertTrue(p.get(2) == ln3);
    }
    
    @Test
    public void testGetPathNotConnected(){
        System.out.println("Test: testGetPath with 2 separated nodes");
        
        Node ln1 = new Node(0,0);
        Node ln2 = new Node(1,1);
        
        Path<INode> p =  world.findRoute(ln1, ln2);
        System.out.println("notconnected " + p);
        Assert.assertTrue(p == null);
    }
    @Test
    public void testConstructorDestructor() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Class<?> clazz = WorldMap.class;
        Field localField = clazz.getDeclaredField("INSTANCE");
        localField.setAccessible(true);
        IWorldMap localWorld = WorldMap.getInstance();
        assertNotNull(localField.get(localWorld));
        WorldMap.destroyMap();
        assertNull(localField.get(localWorld));
    }
    //@Test
    public void testClearMap() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        
        field1.set(world, lanes);
        field2.set(world, nodes);
        field3.set(world, ends);
        for (Lane l : lanes){
            l.offerVehicle(Car.newInstance());
        }
        field1.set(world, lanes);
        world.clearMap();
        Type type = field1.getGenericType();  
        List<Lane> lanes2 = (List<Lane>) field1.get(lanes);
        System.out.println("lanes: " + lanes2);
//        assertTrue(world.getEntities().isEmpty());
//        for (Lane l : field1.get(lanes)){
//            assertEquals(0, l.getNumberOfVehicles());
//        }
    }
    @Test
    public void testDijkstra() throws IllegalArgumentException, IllegalAccessException { //10
        System.out.println("computePaths");
        INode source = n1;
        INode target = n8;
        Path<INode> path = world.findRoute(source,target);
        /*
        * n8 -> n7 -> n2 -> n1
        */
        assertNotNull(path);
        assertEquals(4, path.getLength());
        assertEquals(n8, path.get(4));
        assertEquals(n7, path.get(3));
        assertEquals(n2, path.get(2));
        assertEquals(n1, path.get(1));
        /*
        * Increase weight on edge between n7, n2 and n8
        */
        lanes.get(8).offerVehicle(Car.newInstance());
        lanes.get(7).offerVehicle(Car.newInstance());
        lanes.get(9).offerVehicle(Car.newInstance());
        lanes.get(10).offerVehicle(Car.newInstance());
        field1.set(world, lanes);
        /*
        * If successful it will make the algorithm take another
        * quicker way with less weight.
        */
        System.out.println("Second pass: ");
        path = world.findRoute(source,target);
        /*
        * n8 -> n5 -> n2 -> n1
        */
        assertEquals(4, path.getLength());
        assertEquals(n8, path.get(4));
        assertEquals(n5, path.get(3));
        assertEquals(n2, path.get(2));
        assertEquals(n1, path.get(1));
    }
}
