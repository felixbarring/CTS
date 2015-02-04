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

import java.awt.Color;
import java.lang.reflect.Field;
import static junit.framework.TestCase.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import util.IDrawable;
import util.Weather;

/**
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class TrafficEntityTest {
    
    private final TrafficEntity car;
    
    public TrafficEntityTest() throws NoSuchMethodException{
        car = Car.newInstance();
    }
    
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void testConstructor(){
        class Apa extends Vehicle{
            
            public Apa(){
                super(Integer.MAX_VALUE,Integer.MAX_VALUE);
            }
            @Override
            protected IDrawable getGraphics() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
            @Override
            protected boolean think() {
                throw new UnsupportedOperationException("Not supported yet."); 
            }
        }
        Apa a = new Apa();
        a = null;
    }
    
    /**
     * The calculations are completely random and bugged.
     * We haven't discussed how these should be implemented and
     * the implementation right now uses Math.Random in a bad
     * way since Math.Random can return 0.0
     */
    @Test
    public void testSetGetStress(){
        TrafficEntity car2  = Car.newInstance();
        int stress = car2.getStress();
        assertTrue(stress >= 0 && stress < 1000);
        String expected = "Normal";
        assertEquals(expected,car2.getStressStatus());
        car2.setStress(null);
        assertEquals(expected,car2.getStressStatus());
    }
    
    //@Test
    public void testRiskEvaluation(){
        TrafficEntity car2  = Car.newInstance();
        car2.setBathroom(0);
        car2.setHunger(0);
        boolean takesRisk = car2.takesRisk();
        assertTrue(!takesRisk);
        car2.setBathroom(1000);
        car2.setHunger(1000);
        takesRisk = car2.takesRisk();
        assertTrue(takesRisk);
    }
    
    @Test
    public void testRiskDecorator(){
        TrafficEntity car2  = Car.newInstance();
        String expected = "Average driver";
        assertEquals(expected,car2.getRiskDescription());
        assertTrue(!car2.isDrunk());
        car2.setDrunk();
        assertTrue(car2.isDrunk());
        expected = "Average driver" + ", Drunk";
        assertEquals(expected,car2.getRiskDescription());
    }
    
    //@Test
    public void testGiveSnickers(){
        TrafficEntity car2  = Car.newInstance();
         car2.setBathroom(990);
         car2.setHunger(1000);
         int stress = car2.getStress();
         assertTrue(995 <= stress && stress < 1990);
         int hunger = car2.getHunger();
         int bathroom = car2.getBathroom();
         car2.giveSnickers();
         assertEquals(bathroom + 10 ,car2.getBathroom());
         assertEquals(hunger / 2, car2.getHunger());
    }
    
    @Test
    public void testSetColor(){
        Color before = car.getColor();
        car.setColor(null);
        assertEquals(before.getRGB(), car.getColor().getRGB());
        car.setColor(Color.CYAN);
        assertNotEquals(before.getRGB(), car.getColor().getRGB());
        assertEquals(Color.CYAN.getRGB(),car.getColor().getRGB());
    }
    
    @Test
    public void testSetGetBathroom(){
        int pee = car.getBathroom();
        assertTrue( 0 <=  pee && pee <= 1000);
        car.setBathroom(-1);
        assertEquals(pee, car.getBathroom());
        car.setBathroom(1000);
        assertEquals(1000, car.getBathroom());
        car.setBathroom(1001);
        assertEquals(1000, car.getBathroom());
    }
    
    @Test
    public void testSetGetHunger(){
        int hunger = car.getHunger();
        assertTrue( 0 <=  hunger && hunger <= 1000);
        car.setHunger(-1);
        assertEquals(hunger, car.getHunger());
        car.setHunger(1000);
        assertEquals(1000, car.getHunger());
        car.setHunger(1001);
        assertEquals(1000, car.getHunger());
    }
    
    @Test
    public void testSetGetDirection(){
        assertTrue(0.0 <= car.getDirection() && car.getDirection() < 360);
        car.setDirection(1080.0);
        assertEquals(0.0, car.getDirection());
    }
    
    @Test
    public void testSetGetXpos(){
        car.setXpos(10.0);
        assertEquals(10.0,car.getXpos(),1);
    }
    
    @Test
    public void testSetGetYpos(){
        car.setYpos(10.0);
        assertEquals(10.0,car.getYpos(),1);
    }
    
    @Test
    public void testSetGetTimeOfDay(){
        assertEquals(8,Car.getTimeOfDay());
        Car.setTimeOfDay(24);
        assertEquals(8,Car.getTimeOfDay());
        Car.setTimeOfDay(-1);
        assertEquals(8,Car.getTimeOfDay());
        Car.setTimeOfDay(11);
        assertEquals(11,Car.getTimeOfDay());
    }
    
    @Test
    public void testSetGetWeather(){
        assertEquals(Weather.SUNSHINE, Car.getWeather());
        Car.setWeather(null);
        assertEquals(Weather.SUNSHINE, Car.getWeather());
        Car.setWeather(Weather.RAIN);
        assertEquals(Weather.RAIN, Car.getWeather());
        Car.setWeather(Weather.SUNSHINE);
    }
    
    @Test
    public void testEquals(){
        Vehicle car2 = Car.newInstance();
        assertTrue(!car2.equals(car));
        assertTrue(!car.equals(car2));
        car2 = (Car) car;
        assertTrue(car2.equals(car));
        assertTrue(car.equals(car2));
        //Null check
        assertTrue(!car.equals(null));
    }
    
    /**
     * Makes sure equals and hash code are reflexive, symmetric,
     * and transitive from Joshua Bloch's recommendation by
     * checking on two traffic entities with the same ID.
     *
     * TrafficEntities should never be able to have the same ID so
     * reflection is used to set the private field ID to be equal to
     * another TrafficEntity's ID.
     *
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Test
    public void testHashCode() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException{
        Vehicle c1 = Car.newInstance();
        Vehicle c2 = Car.newInstance();
        
        assertFalse(c1.equals(c2) && c2.equals(c1));
        assertFalse(c1.hashCode() == c2.hashCode());
        
        Class<?> clazz = TrafficEntity.class;
        Field field = clazz.getDeclaredField("ID");
        field.setAccessible(true);
        field.set(c1, c2.getID());
        
        assertTrue(c1.equals(c2) && c2.equals(c1));
        assertTrue(c1.hashCode() == c2.hashCode());
    }
    
    @Test
    public void testToString(){
        assertEquals("TrafficID: " + car.getID() ,car.toString());
    }
}
