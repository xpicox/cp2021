package cp;

import cp.week9.ThreadsExercise3.*;

/**
 * Main class (entry point) of the Java Application.
 */
public final class App {
	/**
	 * Entry point method of the Java application.
	 *
	 * @param args The arguments of the program.
	 */
	public static void main( String[] args ) {
		// FirstThread.main();
		// Counting.main();
		Utils.doAndMeasure(SynchronizedMap2T::main);
		Utils.doAndMeasure(SynchronizedMap2T2::main);
	}
}
