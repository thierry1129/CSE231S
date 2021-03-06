/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove, Ben Choi
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package util.lab.map;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.iterators.LazyIteratorChain;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.util.KeyMutableValuePair;
import net.jcip.annotations.NotThreadSafe;
import util.lab.collection.LinkedNodesCollection;

/**
 * @author __STUDENT_NAME__
 * @author Ben Choi (benjaminchoi@wustl.edu)
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
@NotThreadSafe
public class BucketsHashMap<K, V> extends AbstractMap<K, V> {

	private final Collection<Entry<K, V>>[] buckets;

	/**
	 * A simple HashMap which extends the simple map you implemented earlier. This
	 * implementation will make use of buckets based on an object's hash in order to
	 * cut down on the runtime of finding entries. See the two given methods in
	 * order to find out which bucket an entry should go in depending on the
	 * hashcode of the entry's key. Note: this class is not thread-safe.
	 */
	@SuppressWarnings("unchecked")
	// initliazed the buckets using parameter capacity, 
	// in fact each bucket is an instance of linkednodescollection
	public BucketsHashMap(int capacity) {
		buckets = new Collection[capacity];
		for (int i = 0; i < buckets.length; ++i) {
			buckets[i] = new LinkedNodesCollection<>();
		}
	}
// when the size is not set, the default size is 1024
	public BucketsHashMap() {
		this(1024);

	}

	/**
	 * Uses the key's hashcode and the length of the number of buckets to determine
	 * which bucket the entry should go into
	 * 
	 * @param key
	 * @return the index of the bucket the entry should go into
	 */
	
	private int hash(Object key) {
		int hashCode = key.hashCode();
		return Math.floorMod(hashCode, buckets.length);
	}

	/**
	 * Uses the key's hash to create a reference to the bucket the key is in
	 * 
	 * @param key
	 * @return the bucket the key is in
	 */
	private Collection<Entry<K, V>> getBucketFor(Object key) {
		int hashCode = hash(key);
		return buckets[hashCode];
	}

	/**
	 * {@inheritDoc}
	 * return the size of 
	 */
	@Override
	public int size() {
		int res = 0 ;
		for (Collection<Entry<K, V>> test : buckets) {
			res += test.size();			
		}
		return res;
	}



	/**
	 * put a new value to a certain key, if key is null, do nothing, if key has occurred before, return the old value and put the new value
	 * if key is new, put it in as a new key value pair.
	 */
	@Override
	public V put(K key, V value) {
		if (key == null) {
			return null;
		}
		boolean test = false;
		for (Entry<K, V> a: this.entrySet()) {
			if (key.equals(a.getKey())) {
				V oldval = a.getValue();
				a.setValue(value);
				test = true;
				return oldval;
			}
		}
		if (!test) {
			Entry<K,V> x = new  KeyMutableValuePair<K,V>(key,value);
			Collection<Entry<K, V>> bucket = this.getBucketFor(key);			
			bucket.add(x);
		}
		return null;
	}

	/**
	 * Use iterator to remove from the buckets, if value is null, do nothing and return null
	 * if value is not null, return the value and remove it from the buckets. 
	 */
	@Override
	public V remove(Object key) {
		V value = this.get(key);
		if (value != null) {	
			@SuppressWarnings("unchecked")
			Entry<K,V> x = new  KeyMutableValuePair<K,V>((K) key,value);
			 Collection<Entry<K, V>> buc = this.getBucketFor(key);
			 Iterator<Entry<K, V>> iterator = buc.iterator();
			 while (iterator.hasNext()) {
				 if (iterator.next().equals(x)) {
					 iterator.remove();
					 break;
				 }
			 }			
			return value;		
		}
		return null;
	}

	/**
	 * get the value of a certain key from the map, if key not in map return null
	 */
	@Override
	public V get(Object key) {	
		for (Entry<K, V> a: this.entrySet()) {
			if (key.equals(a.getKey())) {
				return a.getValue();
			}		
		}
		return null;
	}

	/**
	 * predefined method to get the entryset
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return new AbstractSet<Entry<K, V>>() {
			@Override
			public Iterator<Entry<K, V>> iterator() {
				return new LazyIteratorChain<Map.Entry<K, V>>() {
					@Override
					protected Iterator<? extends java.util.Map.Entry<K, V>> nextIterator(int count) {
						int index = count - 1;
						return index < BucketsHashMap.this.buckets.length
								? BucketsHashMap.this.buckets[index].iterator()
										: null;
					}
				};
			}

			@Override
			public int size() {
				return BucketsHashMap.this.size();
			}
		};
	}
}
