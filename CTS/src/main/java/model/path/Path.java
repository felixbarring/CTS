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
 * A list of nodes representing a path.
 * @author Andreas Löfman <lofman.andreas@gmail.com>
 * @author Gustaf Ringius <Gustaf@linux.com>
 * @param <N>
 */
public class Path<N> {
	private PathItem<N> first;
	private PathItem<N> last;
	private int length;
	private int pointer;
        
        private int weight;

	public Path(){
		this.length = 0;
		this.pointer = 1;
                this.weight = 0;
	}

	/**
	 * Append a node to the end of the path.
	 * @param i 
         * @param w weight of the path to this node
	 */
	public void append(N i, int w){
		PathItem<N> n = new PathItem<>(i);
		if (last == null){
			last = n;
			first = n;
		}
		else {
			n.setPrev(last);
			last.setNext(n);
			last = n;
		}
                weight+=w;
		length+=1;
	}
        
        
        /**
         * get the weight of this path
         * @return 
         */
        public int getWeight(){
            return weight;
        }
        
	/**
	 * Does this path contain the specified node?
	 * returns true if it does.
	 * false if not.
	 * @param i
	 * @return 
	 */
	public boolean contains(N i){
		PathItem<N> n = first;
		while (n != null){
			if (n.getItem().equals(i)){
				return true;
			}
			n = n.getNext();
		}
		return false;
	}

	/**
	 * Get the node at the current pointer position.
	 * @return 
	 */
	public N get(){
		return this.get(this.pointer);
	}

	/**
	 * Get the node at position n.
	 * First element is 1, not 0
	 * @param n
	 * @return 
	 */
	public N get(int n){
		PathItem<N> cur = first;
		for (int i=1; i<n; i++){
			if (cur != null){
				cur = cur.getNext();
			}
		}
		if (cur != null){
			return cur.getItem();
		}
		return null;
	}

	/**
	 * Get the node comming after supplied node i.
	 * @param i supplied node
	 * @return the node immediately after i
	 */
	public N next(N i){
		PathItem<N> cur = first;
		while (cur != null){
			if (cur.getItem().equals(i)){
				if (cur.getNext() != null){
					cur = cur.getNext();
					return cur.getItem();
				}
				else {
					return null;
				}
			}
			cur = cur.getNext();
		}
		return null;
	}


	/**
	 * 
	 * @return the node after the current pointer position 
	 */
	public N next(){
		return this.get(this.pointer+1);
	}

	/**
	 * 
	 * @return the length of the path (number of nodes)
	 */
	public int getLength(){
		return this.length;
	}

	/**
	 * Set the pointer position, if not between 1 and length, automatically set to 1.
	 * @param p pointer position
	 */
	public void setPointer(int p){
		if (p > 0 && p <= this.length){
			this.pointer = p;
		}
		else {
			this.pointer = 1;
		}
	}

	/**
	 * Increments the pointer position by 1, if at the end its unmodified.
	 */
	public void incrementPointer(){
		if (pointer < this.length){
			pointer++;
		}
	}

	/**
	 * 
	 * @return the current pointer position
	 */
	public int getPointer(){
		return this.pointer;
	}

	/**
	 * 
	 * @return a shallow clone  
	 */
	@Override
	public Path<N> clone(){
		Path<N> p = new Path<>();
		PathItem<N> cur = first;
		p.setPointer(this.pointer);
		while (cur != null){
			p.append(cur.getItem(),0);
			cur = cur.getNext();
		}
                p.setWeight(this.weight);
		return p;
	}
        
        protected void setWeight(int w){
            weight = w;
        }
        
        @Override
        public String toString(){
            StringBuilder builder = new StringBuilder();
            for(int i = getLength(); i > 0; i--){
                builder.append(get(i) + " ");
            }
            return builder.toString();
        }
}
