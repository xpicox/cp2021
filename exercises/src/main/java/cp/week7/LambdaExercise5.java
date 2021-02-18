package cp.week7;

import java.util.ArrayList;
import java.util.List;

import cp.week7.LambdaExercise2.BoxFunction;

/**
 *
 * @author Fabrizio Montesi
 */
public class LambdaExercise5
{
	/*
	- Write a static method Box::applyToAll that, given
	  a list of Box(es) with the same type and a BoxFunction with compatible type,
	  applies the BoxFunction to all the boxes and returns a list
	  that contains the result of each BoxFunction invocation.
	*/
    static public class Box<T> extends LambdaExercise2.Box<T> {
		Box( T content ) {
			super(content);
		}

		static public<T,O> List<? super O> applyToAll( List<Box<T>> lst, BoxFunction<T,O> f ) {
			List<O> result = new ArrayList<>();
			for ( Box<T> b : lst ) {
				result.add(b.apply(f));
			}
			return result;
		}
	}
}
