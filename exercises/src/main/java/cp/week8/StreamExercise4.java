package cp.week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StreamExercise4
{
	/* @formatter:off
	- Create a stream of lines for the file created in StreamExercise1.
	- Use Stream::mapToInt and IntStream::sum to count how many times
	  the letter "C" occurs in the entire file.
	- to check that the result is right you can use the following bash command:
	  tr -cd 'C' < loremipsum.txt | wc -c
	*/
	public static void main(String[] args) {
		final Path path = Paths.get("loremipsum.txt");
		try (Stream<String> lines = Files.lines(path)) {
			int result = lines
				.flatMap( line -> Stream.of( line.split( "" ) ) ) // Stream<String>
				.mapToInt( letter -> letter.equals("C") ? 1 : 0 )
				.sum();
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
