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

import junit.framework.TestCase;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class CarTest extends TestCase {
    
    private Vehicle car;
    private final int LENGTH = 2;
    private final int MAX_SPEED = 160;
    
    public CarTest(String testName) {
        super(testName);
    }
    
    /**
     * 
     * @throws Exception 
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        car = Car.newInstance();
    }
    
    /**
     * 
     * @throws Exception 
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        car = null;
    }
    
    /**
     * Tests the factory method in the Car class.
     */
    @Test
    public void testNewInstance(){
        car = Car.newInstance();
        assertNotNull(car);
        assertNotNull(car.getID());
    }
    
    @Test
    public void testGetLength(){
        assertEquals(LENGTH,car.getLength());
    }
    
    @Test 
    public void testMaxSpeed(){
        assertEquals(MAX_SPEED, car.maxSpeed());
    }
    
    @Test
    public void testGetYearModel(){
        assertTrue(car.getYearModel() > 1940);
        assertTrue(car.getYearModel() <= 2014);
    }
    
    //@Test
    public void testSetGetCrashed(){
        assertFalse(car.getCrashed());
        car.setCrashed(true);
        car.think();
        assertTrue(car.getCrashed());
    }
    
}
