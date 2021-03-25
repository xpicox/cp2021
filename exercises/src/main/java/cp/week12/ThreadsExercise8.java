package cp.week12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import cp.week10.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise8 {
	/* @formatter:off
	- As ThreadExercise7, but now use a global counter among all threads instead.
	- Reason about the pros and cons of the two concurrency strategies
	  (write them down).
	*/


	public static void main()
	{
		// word -> number of times that it appears over all files
		Map< String, Integer > occurrences = new HashMap<>();
		// Global counter
		AtomicInteger counter = new AtomicInteger(0);


		List< String > filenames = List.of(
			"text1.txt",
			"text2.txt",
			"text3.txt",
			"text4.txt",
			"text5.txt",
			"text6.txt",
			"text7.txt",
			"text8.txt",
			"text9.txt",
			"text10.txt"
		);

		CountDownLatch latch = new CountDownLatch( filenames.size() );

		filenames.stream()
			.map( filename -> new Thread( () -> {
				computeOccurrences( filename, occurrences );
				countLWords( filename, counter );
				latch.countDown();
			} ) )
			.forEach( Thread::start );

		try {
			latch.await();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}

		System.out.println( "Total number of L-words: " + counter );
		// occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}

	private static void computeOccurrences( String filename, Map< String, Integer > occurrences )
	{
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
		}
	}

	private static void countLWords( String filename, AtomicInteger counter  )
	{
		try {
			Files.lines( Paths.get( filename ) )
				.flatMap( Words::extractWords )
				.filter( s -> s.startsWith( "L" ) )
				// Increment the global counter
				.forEach( s -> counter.incrementAndGet() );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
