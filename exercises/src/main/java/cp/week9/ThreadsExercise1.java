package cp.week9;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise1
{
	/* @formatter:off
	- Create a Counter class storing an integer (a field called i), with an increment and decrement method.
	- Make Counter thread-safe (see Chapter 2 in the book)
	- Does it make a different to declare i private or public?
	*/

	public static class Counter0 {
		Integer i = 0;

		Integer increment() {
			return ++i;
		}

		Integer decrement() {
			return --i;
		}
	}

	/* @formatter:off
	 Does it make any difference declare i public or private?
	 - No. From the book:
	 >> When a single element of state is added to a stateless class, the
	 >> resulting class will be thread-safe if the state is entirely managed
	 >> by a thread-safe object.
	 The state of Counter1 is entirely managed by a thread-safe class, thus
	 Counter1 is thread-safe.
	 Public access to the thread-safe field i does not violate the thread-safety
	 of Counter1.
	*/
	public static class Counter1 {
		AtomicInteger i = new AtomicInteger(0);

		Integer increment() {
			return i.incrementAndGet();
		}

		Integer decrement() {
			return i.decrementAndGet();
		}
	}


	/* @formatter:off
	 Does it make any difference declare i public or private?
	 - Yes. Declaring i public introduces a way to access i that is not guarded
	   by any lock.
	*/
	public static class Counter2 {
		Integer i = 0;

		synchronized Integer increment() {
			return ++i;
		}

		synchronized Integer decrement() {
			return --i;
		}
	}
}
