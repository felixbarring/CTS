/*
 * The MIT License
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
 * @invariant 0 <= stress <= 2000
 *
 * @author Gustaf Ringius <Gustaf@linux.com>
 */
public class Normal implements Stress{

    @Override
    public int calculateStress(int bathroom, int hunger, int timeOfDay) {
        int stress = 0;
        if (bathroom <= 500){
            stress += bathroom;
        } else {
            stress += bathroom / 2;
            stress += (int) ((Math.random() * bathroom)/2);
        }
        if (hunger <= 500){
            stress += hunger;
        } else {
            stress += hunger / 2;
            stress += (int) ((Math.random() * hunger)/2);
        }
        if (timeOfDay < 16 && 9 < timeOfDay){
            stress *= 0.75;
        }
        return stress;
    }

    @Override
    public String getName() {
       return "Normal";
    }
}
