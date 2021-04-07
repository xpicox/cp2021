package cp.week14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cp.week10.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise10 {
	/*
	 * Modify ThreadsExercise9 to use Files.walk over the data directory in the
	 * Threads project, such that you recursively visit all files in that directory
	 * instead of using a fixed list of filenames.
	 */

	public static void main() {
		// word -> number of times that it appears over all files
		Map<Character, Set<String>> occurrences = new ConcurrentHashMap<>();
		List<Thread> threads = new ArrayList<>();

		try {
			Files.walk(Paths.get(".")).filter(Files::isRegularFile)
					.map(path -> new Thread(() -> computeWords(path, occurrences)))
					.forEach(thread -> threads.add(thread));
		} catch (IOException e) {
			e.printStackTrace();
		}

		threads.forEach(Thread::start);
		threads.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	private static void computeWords(Path path, Map<Character, Set<String>> occurrences) {
		try {
			Files.lines(path).flatMap(Words::extractWords).forEach(s -> {
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
