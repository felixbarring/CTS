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

package model.risk;

import util.Weather;

/**
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class Average implements Risk{
    
    private final static int MAX_STRESS = 2000;
    private final static int RISK_VALUE = 1100;

    @Override
    public boolean willTakeRisk(int stress, int yearModel, Weather weather, int timeOfDay) {
        if (stress >= MAX_STRESS - 100){
            return true;
        } else {
            return calculate(stress,weather, timeOfDay);
        }
    }
    
    /**
     * @invariant 0 <= risk <= 2000
     * 
     * @param stress
     * @param weather
     * @return 
     */
    private boolean calculate(int stress, Weather weather, int timeOfDay){
        int risk = 0;
        switch (weather){
            case RAIN:
                risk = 100;
                break;
            case SUNSHINE:
                risk = 400;
                break;
            case CLOUDY:
                risk = 200;
                break;
            case SNOW:
                risk = 150;
                break;
            case STORM:
                risk = 50;
                break;
            default:
                break;
        }
        if (timeOfDay < 16 && 9 < timeOfDay){
            stress *= 0.75;
        }
        if (stress <= 100){
            risk += 50;
        } else {
            risk += stress / 2;
            risk += (int) ((Math.random()*stress)/2);
        }
        return risk > RISK_VALUE;
    }

    @Override
    public String getDescription() {
        return "Average driver";
    }
    
}
