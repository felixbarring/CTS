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

package model.path;

/**
 *
 * @author Andreas Löfman <lofman.andreas@gmail.com>
 * @param <N>
 */
public class PathItem<N> {
    private PathItem next;
    private PathItem prev;
    private final N item;
    
    protected PathItem(N i){
        this.item = i;
    }
    
    protected void setNext(PathItem n){
        this.next = n;
    }
    
    protected void setPrev(PathItem p){
        this.prev = p;
    }
    
    protected PathItem getNext(){
        return this.next;
    }
    
    protected PathItem getPrev(){
        return this.prev;
    }
    
    protected N getItem(){
        return this.item;
    }
}
