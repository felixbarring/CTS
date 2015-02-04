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

package model.stress;

/**
 * Behavior interface for the strategy pattern.
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public interface Stress {
    
    /**
     * Algorithm for calculating the stress of a driver.
     *
     * @invariant 0 <= stress <= 2000
     *
     * @param bathroom need to piss represented as an integer.
     * @param hunger need to feed represented as an integer.
     * @return an integer value representing the stress level.
     */
    public int calculateStress(int bathroom, int hunger, int timeOfDay);
    public String getName();
    
}
