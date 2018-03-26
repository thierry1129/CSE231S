/*******************************************************************************
 * Copyright (C) 2016-2017 Dennis Cosgrove
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
package fibonacci.studio;

import static edu.wustl.cse231s.v5.V5.doWork;

import java.math.BigInteger;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.asymptoticanalysis.OrderOfGrowth;
import fibonacci.core.FibonacciCalculator;

/**
 * @author Terry Lyu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class MemoizationSequentialFibonacciCalculator implements FibonacciCalculator {
	private BigInteger fibonacciMemo(BigInteger[] memos, int n) {
		if (n==0||n==1) {
			return BigInteger.valueOf(n);
		}
		memos[0]=BigInteger.valueOf(0);
		memos[1] = BigInteger.valueOf(1);
		
		for (int k = 2;k<=n;k++) {
			memos[k]=memos[k-1].add(memos[k-2]);
		}
//		for (BigInteger a = BigInteger.valueOf(2);a.compareTo( BigInteger.valueOf(n))<=0; a = a.add(BigInteger.ONE) ) {
//			memos[a.intValue()] = BigInteger.valueOf(memos[a.intValue()-1].intValue()+memos[a.intValue()-2].intValue());
//					
//					
//					
//					//BigInteger.valueOf(memos[BigInteger.valueOf((a.intValue()-1))]+memos[a.intValue()-2].intValue());			
//		}
		return memos[n];
		//throw new NotYetImplementedException();
	}

	@Override
	public BigInteger fibonacci(int n) {
		BigInteger[] memos = new BigInteger[n + 1];
		return fibonacciMemo(memos, n);
	}

	@Override
	public OrderOfGrowth getOrderOfGrowth() {
		return OrderOfGrowth.LINEAR;
	}

	@Override
	public boolean isSusceptibleToStackOverflowError() {
		return true;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
