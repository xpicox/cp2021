package cp.week12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import cp.week10.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise7
{
	/* @formatter:off
	- Modify Threads/cp/threads/SynchronizedMap such that:
		* Each threads also counts the total number of times that any word
		  starting with the letter "L" appears.
		* Each thread should have its own total (no shared global counter).
		* The sum of all totals is printed at the end.
	*/
	public static void main()
	{
		// word -> number of times that it appears over all files
		Map< String, Integer > occurrences = new HashMap<>();
		// filename -> number of times an L-word appears in it
		Map< String, Integer > lWordsCount = new HashMap<>();

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
				countLWords( filename, lWordsCount );
				latch.countDown();
			} ) )
			.forEach( Thread::start );

		try {
			latch.await();
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}

		Integer totalLWords = lWordsCount.entrySet().stream()
			.map( Entry::getValue )
			.reduce( 0, Integer::sum );

		System.out.println( "Total number of L-words: " + totalLWords );
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

	private static void countLWords( String filename, Map< String, Integer > occurrences )
	{
		try {
			Files.lines( Paths.get( filename ) )
				.flatMap( Words::extractWords )
				.filter( s -> s.startsWith( "L" ) )
				/* Increment the counter for the given filename */
				.forEach( s -> occurrences.merge( filename, 1, Integer::sum ) );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
