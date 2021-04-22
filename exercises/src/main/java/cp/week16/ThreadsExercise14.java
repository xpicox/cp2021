package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise14 {
	/*
	Modify Threads/cp/WalkExecutorFuture such that, instead of word occurrences,
	it computes a map of type Map< Path, FileInfo >, which maps the Path of each file found in "data"
	to an object of type FileInfo that contains:
		- the size of the file;
		- the number of lines contained in the file;
		- the number of lines starting with the uppercase letter "L".
	*/

	private static class FileInfo {
		private final long size;
		private long lines = 0;
		private long linesStartingWithL = 0;

		FileInfo( long size ) {
			this.size = size;
		}

		void countLine( String line ) {
			if( line.startsWith( "L" ) ) {
				linesStartingWithL++;
			}
			lines++;
		}

		@Override
		public String toString() {
			return "FileInfo [lines=" + lines + ", L-lines=" + linesStartingWithL + ", size=" + size + "]";
		}


	}

	public static void main() {
		// path -> file info
		Map< Path, FileInfo > filesInfo = new HashMap<>();
		ExecutorService executor = Executors.newWorkStealingPool();

		try {
			List< Future< Map< Path, FileInfo > > > futures =
				Files.walk( Paths.get( "data" ) )
					.filter( Files::isRegularFile )
					.map( filepath -> executor.submit( () -> computeFileInfo( filepath ) ) )
					.collect( Collectors.toList() );

			for( Future< Map< Path, FileInfo > > future : futures ) {
				Map< Path, FileInfo > fileInfo = future.get();
				filesInfo.putAll( fileInfo );
			}
		} catch( InterruptedException | ExecutionException | IOException e ) {
			e.printStackTrace();
		}

		try {
			executor.shutdown();
			executor.awaitTermination( 1, TimeUnit.DAYS );
		} catch( InterruptedException e ) {
			e.printStackTrace();
		}

		//		filesInfo.forEach( (path, info) -> System.out.println( path + ": " + info ) );
	}

	private static Map< Path, FileInfo > computeFileInfo( Path regularFile ) {
		Map< Path, FileInfo > fileInfo = new HashMap<>();
		try {
			FileInfo info = new FileInfo( Files.size( regularFile ) );
			Files.lines( regularFile ).forEach( info::countLine );
			fileInfo.put( regularFile, info );
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return fileInfo;
	}

}
