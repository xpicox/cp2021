package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import cp.week10.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise13 {
	/*
	Modify ThreadsExercise9 to use executors.
	Try different kinds of executor (cached thread pool or fixed thread pool) and different fixed pool sizes.
	Which executor runs faster?
	Can you explain why?
	*/

	public static void main() {
		// word -> number of times that it appears over all files
		Map< Character, Set< String > > occurrences = new ConcurrentHashMap<>();

		List< String > filenames =
			List.of( "text1.txt", "text2.txt", "text3.txt", "text4.txt", "text5.txt", "text6.txt",
				"text7.txt", "text8.txt", "text9.txt", "text10.txt" );
		ExecutorService executor = Executors.newCachedThreadPool();
		// ExecutorService executor =
		// 	Executors.newFixedThreadPool( Runtime.getRuntime().availableProcessors() );
		// ExecutorService executor = Executors.newFixedThreadPool( 16 );

		filenames.stream()
			.forEach( filepath -> {
				executor.submit( () -> {
					computeWords( filepath, occurrences );
				} );
			} );

		try {
			executor.shutdown();
			executor.awaitTermination( 1, TimeUnit.DAYS );
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}

		//		filesInfo.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}

	private static void computeWords( String filename, Map< Character, Set< String > > occurrences ) {
		try {
			Files.lines( Paths.get( filename ) )
				.flatMap( Words::extractWords ).forEach( s -> {
					Character c = s.charAt( 0 );
					occurrences.merge( c, Collections.singleton( s ), ( s1, s2 ) -> {
						Set< String > result = new HashSet<>( s1 );
						result.addAll( s2 );
						return result;
					} );
				} );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}
}
