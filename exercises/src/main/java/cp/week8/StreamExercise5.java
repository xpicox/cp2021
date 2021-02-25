package cp.week8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class StreamExercise5 {
	/*@formatter:off
	! (Exercises marked with ! are more difficult.)

	- Create a stream of lines for the file created in StreamExercise1.
	- Use Stream::map to map each line to a HashMap<String, Integer> that
	  stores how many times each character appears in the line.
	  For example, for the line "abbc" you would produce a map with entries:
	    a -> 1
	    b -> 2
	    c -> 1
	- Use Stream::reduce(T identity, BinaryOperator<T> accumulator)
	  to produce a single HashMap<String, Integer> that stores
	  the results for the entire file.
	*/

	public static void main(String[] args) {
		final Path path = Paths.get( "loremipsum.txt" );
		try ( Stream<String> lines = Files.lines( path ) ) {
			Map<String,Integer> result = lines
				.map( line -> {
					Map<String,Integer> m = new HashMap<>();
					Stream.of( line.split( "" ) )
						.forEach( letter -> m.merge( letter, 1, Integer::sum ) );
					return m;
				} )
				.reduce( new HashMap< String, Integer >(), (m1, m2) -> {
				    Map<String, Integer> res = new HashMap<>( m1 );
				    m2.forEach( (key, value) -> res.merge(key, value, Integer::sum ));
					return res;
				});
			result.forEach( (letter, count) -> System.out.println( letter + " -> " + count ) );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
}
