/*******************************************************************************
 * Copyright (C) 2016-2018 Dennis Cosgrove
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
package count.assignment;

import static edu.wustl.cse231s.v5.V5.async;
import static edu.wustl.cse231s.v5.V5.finish;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import edu.wustl.cse231s.NotYetImplementedException;
import edu.wustl.cse231s.bioinformatics.Nucleobase;
import midpoint.assignment.MidpointUtils;
import slice.studio.*;
import slice.core.*;


/**
 * @author Terry Lyu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class NucleobaseCounting {
	/**
	 * Should sequentially count all of the instances of a specific nucleobase that
	 * are within the given range. This method should involve no parallelism. The
	 * range of numbers to search includes all indices in the array that are equal
	 * to or greater than the min index and less than the max index.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes. Each
	 *            byte in the sequence represents one nucleobase--that is, cytosine
	 *            (C), guanine (G), adenine (A), thymine (T), or unknown (N). A byte
	 *            is used (rather than char, int, String, or enum, for example)
	 *            because a byte is a primitive data type that takes up very little
	 *            memory.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome. The byte value
	 *            representing this nucleobase occurs some number of times in the
	 *            chromosome. In other words, if you call nucleobase.toByte(), it
	 *            will be equal to of the bytes in the chromosome array.
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param maxExclusive
	 *            the highest array index in the range to search, exclusive
	 * @return the total number of times that the given nucleobase occurs in the
	 *         given chromosome
	 */
	public static int countRangeSequential(byte[] chromosome, Nucleobase targetNucleobase, int min, int maxExclusive) {
		int result = 0;
		for (int a = min; a < maxExclusive;a++) {


			if (targetNucleobase.toByte() == chromosome[a]) {
				result ++;
			}



		}


		return result;


	}

	/**
	 * Sequentially counts all of the instances of a specific nucleobase. This
	 * method is very simple in that all it does is call
	 * {@link #countRangeSequential(byte[], Nucleobase, int, int)}. You do not need
	 * to change this method at all, but you will need to implement the
	 * {@code countRangeSequential} method.
	 * 
	 * @param chromosome
	 *            the chromosome to examine, as an array of bytes
	 * @param targetNucleobase
	 *            the nucleobase to look for in the chromosome
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 */
	public static int countSequential(byte[] chromosome, Nucleobase targetNucleobase) {
		return countRangeSequential(chromosome,targetNucleobase,0,chromosome.length);

	}

	/**
	 * This method should asynchronously count all of the instances of a specific
	 * nucleobase, creating two tasks. The chromosome should be split into two
	 * halves, and the "upper" half should be counted at the same time
	 * (asynchronously) as the "lower" half. You will need async and finish for this
	 * method.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static int countParallelLowerUpperSplit(byte[] chromosome, Nucleobase targetNucleobase)
			throws InterruptedException, ExecutionException {

		final int[] arr = new int[2];
		finish(() -> {

			async(()-> {
				for (int a = 0 ; a <chromosome.length/2 ;a++) {
					if (targetNucleobase.toByte() == chromosome[a]) {
						arr[0]++;
					}
				}

			});

			async(()-> {
				for (int a = chromosome.length/2 ; a <chromosome.length ;a++) {
					if (targetNucleobase.toByte() == chromosome[a]) {
						arr[1]++;
					}
				}
			});
		});
		return arr[0]+arr[1];

	}

	/**
	 * This method should asynchronously count all of the instances of a specific
	 * nucleobase, creating the given number of tasks. In other words, you should
	 * spawn n tasks, each of which counts for 1/n of the chromosome. For example,
	 * if numTasks is 8, the chromosome should be divided into 8 pieces, and each of
	 * these pieces should be counted in a separate asynchronous task. You should
	 * enclose each of these tasks in an async block, so that each task can run in
	 * parallel. Note: if numTasks is 2, the behavior of this method will be the
	 * same as countParallelLowerUpperSplit.
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param numTasks
	 *            The number of tasks to create.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static int countParallelNWaySplit(byte[] chromosome, Nucleobase targetNucleobase, int numTasks)
			throws InterruptedException, ExecutionException {
		//new ArrayList<Slice<byte[]>>();
		final List<Slice<byte[]>> slicedChromo =  Slices.createNSlices(chromosome,numTasks);

		//for (Slice smallSlice : slicedChromo) {
		int result = 0;
		final int[] sum = new int [slicedChromo.size()];

		finish(() -> {
			for ( int b = 0 ; b< slicedChromo.size();b++) {
				Slice smallSliceCp = slicedChromo.get(b);


				async(() -> {

					for (int a = smallSliceCp.getMinInclusive() ;a < smallSliceCp.getMaxExclusive();a++) {

						byte[] slicePart = (byte[]) smallSliceCp.getOriginalUnslicedData();
						if (slicePart[a]==targetNucleobase.toByte()) {

							sum[smallSliceCp.getSliceIndexId()]++;

						}

					}

				});


			}


		});

		for (int d : sum) {
			result+=d;

		}

		return result;

	}

	/**
	 * This method should count all of the instances of a target nucleobase in a
	 * chromosome.
	 * 
	 * @see #countParallelDivideAndConquerKernel(byte[],Nucleobase,int,int,int)
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param threshold
	 *            The number of tasks to create.
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */
	public static int countParallelDivideAndConquer(byte[] chromosome, Nucleobase targetNucleobase, int threshold)
			throws InterruptedException, ExecutionException {
		return countParallelDivideAndConquerKernel(chromosome, targetNucleobase, threshold, 0, chromosome.length);
	}

	/**
	 * This method should count all of the instances of a target nucleobase in a
	 * chromosome between [min,maxExclusive).
	 * 
	 * This method should perform each lower and upper half in parallel, creating
	 * tasks until the length between min and maxExclusive falls below threshold. At
	 * that point it should invoke countRangeSequential to do the rest of the work.
	 * 
	 * @see #countRangeSequential(byte[],Nucleobase,int,int)
	 * 
	 * @param chromosome
	 *            The chromosome to examine, represented as an array of bytes.
	 * @param targetNucleobase
	 *            The nucleobase to look for in the chromosome.
	 * @param threshold
	 *            The number of tasks to create.
	 * @param min
	 *            the lowest array index in the range to search, inclusive
	 * @param maxExclusive
	 *            the highest array index in the range to search, exclusive
	 * 
	 * @return The total number of times that the given nucleobase occurs in the
	 *         given chromosome.
	 * @throws InterruptedException,
	 *             ExecutionException
	 */

	//
	//		static int countParallelDivideAndConquerKernel(byte[] chromosome, Nucleobase targetNucleobase, int threshold,
	//				int min, int maxExclusive) throws InterruptedException, ExecutionException {
	//			resultList = new ArrayList<PairInt>();
	//			int start = min;
	//			int end  = maxExclusive;
	//			
	//		
	//			organize(min,maxExclusive,threshold);
	//			final int[] sum = new int[resultList.size()];
	//			
	//			
	//			finish(() -> {
	//				for (int c = 0 ;c<resultList.size();c++) {
	//					final int cp = c;
	//					async(() -> {
	//						int minTarget = resultList.get(cp).getMin();
	//						int maxTarget = resultList.get(cp).getMax();				
	//						sum[cp]+=countRangeSequential(chromosome, targetNucleobase, minTarget, maxTarget);
	//					});
	//				}
	//			});
	//	
	//			int actualResult = 0;
	//			for(int d : sum) {
	//				actualResult +=d;
	//			}
	//	
	//			return actualResult;
	//	
	//	
	//		}



	static int countParallelDivideAndConquerKernel(byte[] chromosome, Nucleobase targetNucleobase, int threshold,
			int min, int maxExclusive) throws InterruptedException, ExecutionException {
		final int[] sum = new int[(maxExclusive-min)/threshold+1];
	
		
		if (maxExclusive-min<threshold) {	
			int res = countRangeSequential(chromosome, targetNucleobase, min, maxExclusive);
			return res;
		}

		else {
			int mid = (min+maxExclusive)/2;
			final int sum2[]  =  {0,0};
			finish(() -> {
				async(() ->{
					sum2[0]+= 	countParallelDivideAndConquerKernel(chromosome,targetNucleobase,threshold,min,mid);
				});
				sum2[1] += countParallelDivideAndConquerKernel(chromosome,targetNucleobase,threshold,mid,maxExclusive);
			});
			return  countSum(sum2);
		}	

	}


	static int countSum(int[] tst) {
		int res = 0;
		for (int a : tst ) {
			res += a;
		}
		return res;
	}

//	public static List<PairInt> organize(int min, int max, int threshold) {
//
//		if (max-min<threshold) {			
//			PairInt a = new PairInt(min,max);			
//			resultList.add(a);
//			return resultList;
//		}
//
//		int mid = (min+max)/2;
//		organize(min,mid,threshold);
//		organize(mid,max,threshold);
//		return resultList;
//	}

//	public static void main(String[] args) throws InterruptedException, ExecutionException {
//
//
//		byte[] chrmo = "AAAAAAAAAA".getBytes();
//		Nucleobase targetNucleobase =  Nucleobase.ADENINE;
//
//		int threshold = 8;
//		System.out.println(countParallelDivideAndConquer(chrmo, targetNucleobase,threshold));
//
//
//	}
}

//
//class PairInt
//{
//	private int vmin;
//	private int vmax;
//
//	public PairInt(int min, int max)
//	{
//		vmin=min;
//		vmax=max;
//	}
//	public int getMin() {
//		return vmin;
//	}
//
//	public int getMax() {
//		return vmax;
//	}
//	public void setMin(int min) {
//		this.vmin = min;
//	}
//	public void setMax(int max) {
//		this.vmax = max;
//	}
//	public String toString() {
//		String result = "( "+vmin+" , "+vmax+" )";
//		return result;
//
//	}
//
//}




