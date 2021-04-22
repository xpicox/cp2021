package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise15
{
	/*
	Adapt your program from ThreadsExercise14 to use an ExecutorCompletionService, as in Threads/cp/WalkCompletionService.
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
		ExecutorCompletionService< Map< Path, FileInfo > > completionService =
			new ExecutorCompletionService<>( executor );

		try {
			long pendingTasks =
				Files.walk( Paths.get( "data" ) )
					.filter( Files::isRegularFile )
					.map( filepath -> completionService.submit( () -> computeFileInfo( filepath ) ) ).count();
			while( pendingTasks > 0 ) {
				Map< Path, FileInfo > fileOccurrences = completionService.take().get();

				fileOccurrences.forEach( ( path, info ) -> filesInfo.put(path, info) );
				pendingTasks--;
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
