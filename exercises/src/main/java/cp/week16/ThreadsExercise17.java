package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise17 {
	/*
	Adapt your program from ThreadsExercise15 to stop as soon as a task that has processed a file with more than 10 lines is completed.
	*/

	private static class FileInfo {
		private final long size;
		private long lines = 0;
		private long linesStartingWithL = 0;

		FileInfo( long size ) {
			this.size = size;
		}

		public void countLine( String line ) {
			if( line.startsWith( "L" ) ) {
				linesStartingWithL++;
			}
			lines++;
		}

		public long lines() {
			return lines;
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
		ExecutorCompletionService< Map< Path, FileInfo > > completionService =
			new ExecutorCompletionService<>( executor );

		try {
			List< Future< Map< Path, FileInfo > > > futures =
				Files.walk( Paths.get( "data" ) )
					.filter( Files::isRegularFile )
					.map( filepath -> completionService.submit( () -> computeFileInfo( filepath ) ) )
					.collect( Collectors.toList() );
			long pendingTasks = futures.size();
			boolean done = false;
			while( pendingTasks > 0 && !done ) {
				Map< Path, FileInfo > fileInfo = completionService.take().get();
				done = fileInfo.values().stream().anyMatch( info -> info.lines() > 10 );
				filesInfo.putAll(fileInfo);
				pendingTasks--;
			}

			if( done ) {
				futures.forEach( future -> future.cancel( true ) );
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
		// filesInfo.forEach( (path, info) -> System.out.println( path + ": " + info ) );
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
