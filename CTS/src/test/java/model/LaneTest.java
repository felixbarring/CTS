/*
 * The MIT License
 *
 * Copyright 2014 Gustaf Ringius <Gustaf@linux.com>.
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


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @revision Andreas Löfman <lofman.andreas@gmail.com> 2014-04-08
 *                      Added thinking steps and lane length to testGetNumberOfVehicles()
 */
public class LaneTest {
    
    public LaneTest() {
    }

    /**
     * Test of setStartX method, of class Lane.
     */
    @Test
    public void testSetGetStartX() {
        System.out.println("setStartX");
        double n = 1.0;
        Lane instance = new Lane();
        instance.setStartX(n);
        assertEquals(n,instance.getStartX(),1);
    }

    /**
     * Test of setStartY method, of class Lane.
     */
    @Test
    public void testSetGetStartY() {
        System.out.println("setStartY");
        double n = 1.0;
        Lane instance = new Lane();
        instance.setStartY(n);
        assertEquals(n,instance.getStartY(),1);
    }

    /**
     * Test of setEndX method, of class Lane.
     */
    @Test
    public void testSetGetEndX() {
        System.out.println("setEndX");
        double n = 1.0;
        Lane instance = new Lane();
        instance.setEndX(n);
        assertEquals(n,instance.getEndX(),1);
    }

    /**
     * Test of setEndY method, of class Lane.
     */
    @Test
    public void testSetGetEndY() {
        System.out.println("setEndY");
        double n = 1.0;
        Lane instance = new Lane();
        instance.setEndY(n);
        assertEquals(n,instance.getEndY(),1);
    }

    /**
     * Test of getNumberOfVehicles method, of class Lane.
     * 
     * 
     */
    @Test
    public void testGetNumberOfVehicles() {
        System.out.println("getNumberOfVehicles");
        Lane instance = new Lane();
        instance.setEndX(1000);
        instance.setEndY(0);
        instance.setStartX(0);
        instance.setStartY(0);
        int expResult = 3;
        
        Vehicle c1 = Car.newInstance();
        Vehicle c2 = Car.newInstance();
        Vehicle c3 = Car.newInstance();
        
        System.out.println(instance.offerVehicle(c1));
        for (int i=0; i<20; i++){
            c1.think();
        }
        System.out.println(instance.offerVehicle(c2));
        for (int i=0; i<20; i++){
            c1.think();
            c2.think();
        }
        System.out.println(instance.offerVehicle(c3));
        int result = instance.getNumberOfVehicles();
        assertEquals(expResult, result);
    }

    /**
     * Test of peekVehicle method, of class Lane.
     */
    @Test
    public void testPeekVehicle() {
        System.out.println("peekVehicle");
        Lane instance = new Lane();
        Vehicle expResult = null;
        Vehicle result = instance.peekVehicle();
        assertEquals(expResult, result);
        expResult = Car.newInstance();
        instance.offerVehicle(expResult);
        assertTrue(instance.peekVehicle().equals(expResult));
    }

    /**
     * Test of pollVehicle method, of class Lane.
     */
    @Test
    public void testPollVehicle() {
        System.out.println("pollVehicle");
        Lane instance = new Lane();
        assertNull(instance.pollVehicle());
        Vehicle expResult = Car.newInstance();
        instance.offerVehicle(expResult);
        assertTrue(instance.pollVehicle().equals(expResult));
    }

    /**
     * Test of hasRoomForVehicle method, of class Lane.
     */
    @Test
    public void testHasRoomForVehicle() {
        System.out.println("hasRoomForVehicle");
        Vehicle v = null;
        Lane instance = new Lane();
        boolean expResult = true;
        boolean result = instance.hasRoomForVehicle(v);
        assertEquals(expResult, result);
    }

    /**
     * Test of offerVehicle method, of class Lane.
     */
    //@Test
    public void testOfferVehicle() {
        System.out.println("offerVehicle");
        Vehicle v = Car.newInstance();
        Lane instance = new Lane();
        boolean expResult = true;
        boolean result = instance.offerVehicle(v);
        assertEquals(expResult, result);
    }
    
}
