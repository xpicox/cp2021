package cp.week7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cp.week7.LambdaExercise2.*;
/**
 *
 * @author Fabrizio Montesi
 */
public class LambdaExercise4
{
	/*
	- Create a list of type ArrayList<String> with some elements of your preference.
	- Create a Box that contains the list.
	- Now compute the sum of the lengths of all strings in the list inside of the box,
	  by invoking Box::apply with a lambda expression.
	*/
	static public void exercise() {
		ArrayList<String> array = new ArrayList<>(List.of("One", "Two", "Three"));
		Box<ArrayList<String>> box = new Box<>(array);
		box.apply(a -> {
			int sum = 0;
			for( String str : a) {
				sum += str.length();
			}
			return sum;
		});
	}
}
