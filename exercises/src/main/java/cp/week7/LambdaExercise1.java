package cp.week7;

/**
 *
 * @author Fabrizio Montesi
 */
public class LambdaExercise1
{
	/*
	Create a class Box<T> with a single final field of type T called "content".
	Its constructor must take the content as parameter and set it.

	Add a public method called "content()" that returns the content of the box.
	*/
	static public class Box<T> {
		final T content;
		Box(T content) {
			this.content = content;
		}

		public T content() {
			return content;
		}
	}
}
