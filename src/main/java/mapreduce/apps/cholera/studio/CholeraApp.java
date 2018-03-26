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
package mapreduce.apps.cholera.studio;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.apache.commons.lang3.mutable.MutableDouble;

import edu.wustl.cse231s.NotYetImplementedException;
import mapreduce.apps.cholera.core.CholeraDeath;
import mapreduce.apps.cholera.core.WaterPump;
import mapreduce.apps.cholera.core.Location;
import mapreduce.collector.intsum.studio.IntSumCollector;
import mapreduce.framework.core.Mapper;

/**
 * @author Terry Lyu
 * @author Dennis Cosgrove (http://www.cse.wustl.edu/~cosgroved/)
 */
public class CholeraApp {
	public static CholeraAppValueRepresentation getValueRepresentation() {
		return CholeraAppValueRepresentation.LOW_NUMBERS_SUSPECT;
	}

	public static Mapper<CholeraDeath, WaterPump, Number> createMapper() {
		Mapper<CholeraDeath, WaterPump, Number> mapper = new Mapper<CholeraDeath, WaterPump, Number>() {
			@Override
			public void map(CholeraDeath item, BiConsumer<WaterPump, Number> keyValuePairConsumer) {
				for (WaterPump a : WaterPump.values()){
					keyValuePairConsumer.accept(a,a.getLocation().getDistanceTo(item.getLocation()));
				}

			}
		};
		return mapper;

	}

	public static Collector<? extends Number, ?, ? extends Number> createCollector() {
		return new Collector<Double, MutableDouble, Double>() {
			public Set<Characteristics> characteristics() {
				return EnumSet.of(Characteristics.UNORDERED);
			}

			public Function<MutableDouble, Double> finisher() {
				return new Function<MutableDouble, Double>() {
					@Override
					public Double apply(MutableDouble mutableDouble) {
						return mutableDouble.getValue();
					}
				};


			}

			public BinaryOperator<MutableDouble> combiner() {
				return new BinaryOperator<MutableDouble>() {

					public MutableDouble apply(MutableDouble mutableDouble, MutableDouble mutableDouble2) {
						MutableDouble res = mutableDouble;
						res.add(mutableDouble2);
						return res;
					}
				};
			}

			public BiConsumer<MutableDouble, Double> accumulator() {
				return new BiConsumer<MutableDouble, Double>() {

					public void accept(MutableDouble mutableDouble, Double double1) {
						mutableDouble.add(double1);
					}
				};
			}

			public Supplier<MutableDouble> supplier() {
				return new Supplier<MutableDouble>() {
					@Override
					public MutableDouble get() {
						return new MutableDouble();
					}
				};

			}


		};

	}
}
