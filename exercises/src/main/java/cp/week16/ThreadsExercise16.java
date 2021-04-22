package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise16
{
	/*
	Adapt your program from ThreadsExercise15 to use CompletableFuture, as in Threads/cp/WalkCompletableFuture.
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
		Map< Path, FileInfo > filesInfo = new ConcurrentHashMap<>();

		try {
			CompletableFuture< Void >[] futures =
				Files.walk( Paths.get( "data" ) )
					.filter( Files::isRegularFile )
					.map( filepath ->
						  CompletableFuture.supplyAsync( () -> computeFileInfo( filepath ) )
						  .thenAccept( fileInfo -> filesInfo.putAll( fileInfo ) ) )
					.collect( Collectors.toList() ).toArray( new CompletableFuture[ 0 ] );
			CompletableFuture
				.allOf( futures )
				.join();
		} catch( IOException e ) {
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
