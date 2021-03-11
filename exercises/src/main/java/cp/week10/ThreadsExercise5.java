package cp.week10;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise5
{
	/* @formatter:off
	Apply the technique for fixing Listing 4.14 to Listing 4.15 in the book, but to the following:
	- Create a thread-safe Counter class that stores an int and supports increment and decrement.
	- Create a new thread-safe class Point, which stores two Counter objects.
	- The two counter objects should be public.
	- Implement the method boolean areEqual() in Point, which returns true if the two counters store the same value.

	Question: Is the code you obtained robust with respect to client-side locking (see book page 73)?
	          Would it help if the counters were private?
	*/


	public static class Counter {
		private int i = 0;

		Counter( int i ) {
			this.i = i;
		}

		public synchronized int increment() {
			return ++i;
		}

		public synchronized int decrement() {
			return --i;
		}

		public synchronized int getValue() {
			return i;
		}
	}

	/* @formatter:off
	   NB: Point is not thread-safe!
	 */
	public static class Point {
		public Counter x;
		public Counter y;

		Point( int x, int y ) {
			this.x = new Counter( x );
			this.y = new Counter( y );
		}

		public synchronized void set( int x, int y ) {
			this.x = new Counter(x);
			this.y = new Counter(y);
		}

		public synchronized int getX() {
			return x.getValue();
		}

		public synchronized int getY() {
			return y.getValue();
		}

		public synchronized boolean areEqual() {
			return x.getValue() == y.getValue();
		}
	}


	static public void main() {
		Point p = new Point(2, 2);

		Thread t1 = new Thread( () -> {
				synchronized ( p ) {
					if( p.areEqual() ) {
						try {
							Thread.sleep( 2000 );
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.printf( "Point coordinates are equal: (%d,%d)%n", p.getX(), p.getY() );
					}
				}
		});

		Thread t2 = new Thread( () -> {
				try {
					Thread.sleep( 1000 );
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				p.x = new Counter( 10 );
		});

		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch ( InterruptedException e) {
			e.printStackTrace();
		}
	}
}
