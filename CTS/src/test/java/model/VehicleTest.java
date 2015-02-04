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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class VehicleTest {
    
    private final Vehicle car, car2;
    private final Method randomYear;
    
    public VehicleTest() throws NoSuchMethodException{
        Class[] paramInt = new Class[2];
        paramInt[0] = Integer.TYPE;
        paramInt[1] = Integer.TYPE;
        randomYear = Vehicle.class.getDeclaredMethod("randomYear", paramInt);
        randomYear.setAccessible(true);
        car = Car.newInstance();
        car2 = Car.newInstance();
    }
    
    /**
     * Test the lane reference.
     */
    @Test
    public void testSetGetLane(){
        assertNull(car.getCurrentLane());
        car.setCurrentLane(new Lane());
        assertNotNull(car.getCurrentLane());
    }
    
    /**
     * Test the back reference.
     */
    @Test
    public void testSetGetBehind(){
        assertNull(car.getVehicleBehind());
        car.setVehicleBehind(car);
        assertNull(car.getVehicleBehind());
        car.setVehicleBehind(car2);
        assertNotNull(car.getVehicleBehind());
         assertTrue(car2.equals(car.getVehicleBehind()));
        car.setVehicleBehind(null);
    }
    
    /**
     * Test the front reference.
     */
    @Test
    public void testSetGetInFront(){
        assertNull(car.getVehicleInFront());
        car.setVehicleInFront(car);
        assertNull(car.getVehicleInFront());
        car.setVehicleInFront(car2);
        assertNotNull(car.getVehicleInFront());
         assertTrue(car2.equals(car.getVehicleInFront()));
        car.setVehicleInFront(null);
    }
    
    /**
     * Test Strange inception like references.
     */
    @Test
    public void testInceptionCars(){
        assertNull(car.getVehicleInFront());
        assertNull(car.getVehicleBehind());
        //Make both directions are null from start.
        car.setVehicleInFront(car2);
        car.setVehicleBehind(car2);
        /*
        Then try to set the same car to be both behind and
        in front of this one. Inception cars shouldn't be possible.
        */
        assertTrue(car2.equals(car.getVehicleInFront()));
        assertNull(car.getVehicleBehind());
        car.setVehicleInFront(null);
        /*
        Since we set the front first this will have a reference to
        car2 and this will deny the possiblity to set car2 to be
        behind the current car.
        */
    }
    
    /**
     * 
     */
    @Test
    public void testSetGetTargetPosition(){
        assertEquals(0.0,car.getTargetX(),1);
        assertEquals(0.0,car.getTargetY(),1);
        car.setTargetPosition(1.0, 2.0);
        assertEquals(1.0,car.getTargetX(),1);
        assertEquals(2.0,car.getTargetY(),1);
    }
    
    /**
     * 
     */
    @Test
    public void testGetFrontDistance(){
        assertEquals(0.0,car.getFrontDistance(),1);
        car.setXpos(1.0);
        car.setYpos(1.0);
        car2.setXpos(10.0);
        car2.setYpos(10.0);
        car.setVehicleInFront(car2);
        assertEquals(12.0,car.getFrontDistance(),1);
    }
    
    @Test
    public void testSetGetBehindDistance(){
        car2.setXpos(1.0);
        car2.setYpos(1.0);
        car.setXpos(10.0);
        car.setYpos(10.0);
        car.setVehicleBehind(car2);
        assertEquals(12.0,car.getBehindDistance(),1);
    }
    
    @Test
    public void testSetGetTargetDistance(){
        car.setXpos(10.0);
        car.setYpos(10.0);
        car.setTargetPosition(10.0, 10.0);
        assertEquals(0.0,car.getTargetDistance(),1);
    }
    
    /**
     * Using Reflection to test private method.
     *
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test
    public void testRandomYear() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        Integer ret;
        for (int i = 0; i < 100; i++){
            ret = (Integer) randomYear.invoke(Car.newInstance(),new Integer(2), new Integer(4));
            assertTrue(ret.intValue() < 5 && 1 < ret.intValue());
        }
        ret = (Integer) randomYear.invoke(Car.newInstance(),new Integer(4), new Integer(2));
        assertTrue(ret.intValue() == 0);
        ret = (Integer) randomYear.invoke(Car.newInstance(),new Integer(4), new Integer(4));
        assertTrue(ret.intValue() == 0);
    }
    
    
    
    @Test
    public void testAimAtTarget(){
        car.setXpos(0.0);
        car.setYpos(0.0);
        car.setTargetPosition(10.0, 10.0);
        car.aimAtTarget();
        assertTrue(car.getDirection() == 45.0);
        
        car.setTargetPosition(-10.0, 10.0);
        car.aimAtTarget();
        assertTrue(car.getDirection() == 135.0);
        
        car.setTargetPosition(10.0, -10.0);
        car.aimAtTarget();
        assertTrue(car.getDirection() == -45.0);
        
        car.setTargetPosition(-10.0, -10.0);
        car.aimAtTarget();
        assertTrue(car.getDirection() == -135.0);
    }
}
