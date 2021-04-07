package cp.week14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import cp.week10.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise9
{
	/*
	Modify Threads/cp/ConcurrentMap to compute a map of type Map<Character, Set<String>>.
	The map should map a character to the set of words that start with that character (case sensitive).
	*/

	public static void main()
	{
		// word -> number of times that it appears over all files
		Map<Character, Set<String>> occurrences = new ConcurrentHashMap<>();

		List<String> filenames = List.of("text1.txt", "text2.txt", "text3.txt", "text4.txt", "text5.txt", "text6.txt",
				"text7.txt", "text8.txt", "text9.txt", "text10.txt");

		CountDownLatch latch = new CountDownLatch(filenames.size());

		filenames.stream().map(filename -> new Thread(() -> {
			computeWords(filename, occurrences);
			latch.countDown();
		})).forEach(Thread::start);

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		//		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
	}

	private static void computeWords(String filename, Map<Character, Set<String>> occurrences) {
		try {
			Files.lines(Paths.get(filename)).flatMap(Words::extractWords).forEach(s -> {
				Character c = s.charAt(0);
				occurrences.merge(c, Collections.singleton(s), (s1, s2) -> {
						Set<String> result = new HashSet<>(s1);
						result.addAll(s2);
						return result;
					});
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
