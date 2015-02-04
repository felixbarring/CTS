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

package model.path;

import org.junit.*;

/**
 *
 * @author Andreas
 */
public class PathTest {
    
    @Test
    public void testAppendGet(){
        Path<Integer> p = new Path<Integer>();
        p.append(new Integer(1),0);
        Assert.assertTrue(p.get(1) == 1);
        Assert.assertTrue(p.getWeight() == 0);
        p.append(new Integer(50),1);
        Assert.assertTrue(p.get(2) == 50);
        Assert.assertTrue(p.getWeight() == 1);
    }
    
    @Test
    public void testGetLength(){
        Path<Integer> p = new Path<Integer>();
        Assert.assertTrue(p.getLength() == 0);
        p.append(new Integer(1),0);
        Assert.assertTrue(p.getLength() == 1);
        p.append(new Integer(2),0);
    }
    
    @Test
    public void testNext(){
        Path<Integer> p = new Path<Integer>();
        p.append(new Integer(1),0);
        p.append(new Integer(2),0);
        p.append(new Integer(3),0);
        p.append(new Integer(4),0);
        
        p.setPointer(1);
        Assert.assertTrue(p.next() == 2);
        
        p.setPointer(3);
        Assert.assertTrue(p.next() == 4);
        
        Assert.assertTrue(p.next(1) == 2);
        Assert.assertTrue(p.next(2) == 3);
        Assert.assertTrue(p.next(4) == null);
        Assert.assertTrue(p.next(7) == null);
    }
    
    @Test
    public void testSetGetIncrementPointer(){
        Path<Integer> p = new Path<Integer>();
        p.append(new Integer(1),0);
        p.append(new Integer(2),0);
        p.append(new Integer(3),0);
        p.append(new Integer(4),0);
        
        p.setPointer(4);
        Assert.assertTrue(p.getPointer() == 4);
        
        p.setPointer(1);
        Assert.assertTrue(p.getPointer() == 1);
        
        p.setPointer(0);
        Assert.assertTrue(p.getPointer() == 1);
        
        p.setPointer(5);
        Assert.assertTrue(p.getPointer() == 1);
        
        p.incrementPointer();
        Assert.assertTrue(p.getPointer() == 2);
        
        p.setPointer(4);
        p.incrementPointer();
        Assert.assertTrue(p.getPointer() == 4);
    }
    
    
    @Test
    public void testClone(){
        Path<Integer> p = new Path<>();
        p.append(new Integer(1),0);
        p.append(new Integer(2),0);
        p.append(new Integer(3),0);
        p.append(new Integer(4),0);
        
        Path<Integer> p2 = p.clone();
        
        p2.setPointer(3);
        Assert.assertTrue(p.getPointer() == 1);
        Assert.assertTrue(p2.getLength() == 4);
        p2.setPointer(1);
        for (int i=1; i<=4; i++){
            Assert.assertTrue(p.get(i) == p2.get(i));
        }
        
        Assert.assertTrue(p.getLength() == p2.getLength());
    }
}
