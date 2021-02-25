package cp.week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StreamExercise3
{
	/* @formatter:off
	- Create a stream of lines for the file created in StreamExercise1.
	- Use Stream::filter and Stream::count to count how many lines
	  contain the letter "L".
	*/

	public static void main(String[] args) {
		final Path path = Paths.get("loremipsum.txt");
		try (Stream<String> lines = Files.lines(path)) {
			long result = lines.filter(s -> s.contains("L")).count();
			System.out.println( result );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
