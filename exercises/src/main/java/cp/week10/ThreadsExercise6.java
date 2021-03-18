package cp.week10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise6 {
	/* @formatter:off
	This exercise generalises Threads/cp/SynchronizedMap2TWords.
	Feel free to borrow the appropriate pieces of code from that example.

	Implement a method
		public static Map< String, Integer > computeOccurrences( Stream< String > filenames )
	that returns a map of how many times each word occurs (as in SynchronizedMap2TWords) in the files named
	in the stream parameter.

	Try first to implement the method sequentially (no threads), then try
	to implement it such that each file is processed by a dedicated thread (all threads
	should run concurrently and be waited for).
	*/

	public static Map< String, Integer > computeOccurrencesS( Stream< String > filenames ) {
		Map< String, Integer > occurrences = new HashMap<>();
		/* Sequential, single thread */
		filenames.map( filename -> Paths.get(filename) )
			.flatMap( path -> {
					try {
						return Files.lines( path );
					} catch( IOException e ) {
						e.printStackTrace();
						return Stream.empty();
					}
				} )
			.flatMap( Words::extractWords )
			.map( String::toLowerCase )
			.forEach( w -> occurrences.merge( w, 1, Integer::sum ) );
		return occurrences;
	}


	public static Map< String, Integer > computeOccurrencesT( Stream< String > filenames ) {
		Map< String, Integer > occurrences = new HashMap<>();

		filenames.map( filename -> new Thread( () -> {
					try {
						Files.lines( Paths.get( filename ) )
							.flatMap( Words::extractWords )
							.map( String::toLowerCase )
							.forEach( s -> {
									synchronized( occurrences ) {
										occurrences.merge( s, 1, Integer::sum );
									}
								} );
					} catch( IOException e ) {
						e.printStackTrace();
					}}))
			.map( t -> { t.start(); return t; } )
			.forEach( t -> {
				try {
					t.join();
				} catch( InterruptedException e ) {
					e.printStackTrace();
				}
			} );;
		return occurrences;
	}

	public static void main() {

		String filename = "text";
		String extension = ".txt";

		Stream<String> filenames = IntStream.range(1, 10).mapToObj( i -> filename + i + extension );

		Map< String, Integer > occurrences = computeOccurrencesS(filenames);

		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}
}
