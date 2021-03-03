package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import cp.Utils;
/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise3 {
	/* @formatter:off
	Modify the threads/cp/SynchronizedMap2T example such that:
	- There are now two maps (instead of just one) for accumulating results, one for each thread.
	- Each thread uses only its own map, without synchronizing on it.
	- After the threads terminate, create a new map where you merge the results of the two dedicated maps.

	Questions:
	- Does the resulting code work? Can you explain why?
	  The resulting code works because each map is accessed by at most one
	  thread at a time.
	- Does the resulting code perform better or worse than the original example SynchronizedMap2T?
	  The resulting code performs better.
	- Can you hypothesise why?
	  Synchronization among threads forces an interleaving of the operations on
	  the shared map, reducing the concurrency of our program.
	*/

	static public void main( String[] main ) {
		Utils.doAndMeasure( SynchronizedMap2T::main );
		Utils.doAndMeasure( SynchronizedMap2T2::main );
	}

	/* Modified Example */
	static public class SynchronizedMap2T2 {
		public static void main() {
			Map< Character, Long > occurrences1 = new HashMap<>();
			Map< Character, Long > occurrences2 = new HashMap<>();

			Thread t1 = new Thread( () ->
			    countLetters( Paths.get( "text1.txt" ), occurrences1 ) );
			Thread t2 = new Thread( () ->
			    countLetters( Paths.get( "text2.txt" ), occurrences2 ) );

			t1.start();
			t2.start();
			try {
				t1.join();
				t2.join();
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}

			Map< Character, Long > occurrences = new HashMap<>( occurrences1 );
			occurrences2.forEach( (c, value) -> {
					occurrences.merge(c, value, Long::sum);
				});

			System.out.println( "e -> " + occurrences.get( 'e' ) );
		}

		private static void countLetters( Path textPath, Map< Character, Long > occurrences ) {
			try( Stream< String > lines = Files.lines( textPath ) ) {
				lines.forEach( line -> {
						for( int i = 0; i < line.length(); i++ ) {
							final char c = line.charAt( i );
							// synchronized( occurrences ) {
								occurrences.merge( c, 1L, Long::sum );
							// }
						}
					} );
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
	}

	/* Original Example */
	static public class SynchronizedMap2T {
		public static void main() {
			Map< Character, Long > occurrences = new HashMap<>();

			Thread t1 = new Thread( () ->
			    countLetters( Paths.get( "text1.txt" ), occurrences ) );
			Thread t2 = new Thread( () ->
			    countLetters( Paths.get( "text2.txt" ), occurrences ) );

			t1.start();
			t2.start();
			try {
				t1.join();
				t2.join();
			} catch( InterruptedException e ) {
				e.printStackTrace();
			}

			System.out.println( "e -> " + occurrences.get( 'e' ) );
		}

		private static void countLetters( Path textPath, Map< Character, Long > occurrences ) {
			try( Stream< String > lines = Files.lines( textPath ) ) {
				lines.forEach( line -> {
						for( int i = 0; i < line.length(); i++ ) {
							final char c = line.charAt( i );
							synchronized( occurrences ) {
								occurrences.merge( c, 1L, Long::sum );
							}
						}
					} );
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
	}
}
