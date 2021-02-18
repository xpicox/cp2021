package cp.week7;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import cp.week7.LambdaExercise2.*;

/**
 *
 * @author Fabrizio Montesi
 */
public class LambdaExercise3
{
	/*
	NOTE: When I write Class::methodName, I don't mean to use a method reference (lambda expression), I'm simply
	talking about a particular method.
	*/

	/*
	- Create a Box that contains an ArrayList<String> with some elements of your preference.
	- Now compute a sorted version of your list by invoking Box::apply, passing a lambda expression that uses List::sort.
	*/
	static public void exercise() {
		ArrayList<String> array =
			new ArrayList<>( List.of("One", "Two", "Three") );
		Box<ArrayList<String>> box = new Box<>( array );
		box.apply( a -> {
			Collections.sort(a);
			return null;
			} );
	}
}
