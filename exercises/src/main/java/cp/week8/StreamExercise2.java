package cp.week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class StreamExercise2
{
	/*@formatter:off
	- Create a stream of lines for the file created in StreamExercise1.
	- Use Stream::filter and Stream::collector (the one with three parameters)
	  to create an ArrayList of all lines that start with a "C".
	- Suggestion: look at https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#collect-java.util.function.Supplier-java.util.function.BiConsumer-java.util.function.BiConsumer-
	*/

	public static void main(String[] args) {
		final Path path = Paths.get("loremipsum.txt");
		try (Stream<String> lines = Files.lines(path)) {
			ArrayList<String> result =
				lines
				.filter(s -> s.startsWith("C"))
				.collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
			result.forEach( System.out::println );
			System.out.println( result.size() );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
