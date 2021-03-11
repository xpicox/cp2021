package cp.week10;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise4
{
	/* @formatter:off
	- Write the example from Listing 4.2 in the book.
	- Add a method that returns a reference to the internal field mySet.
	- Use the new method from concurrent threads to create unsafe access to mySet.
	*/

	public static class Person {
		private final String name;

		Person( String name ) {
			this.name = name;
		}

		// @Override
		public String toString() {
			return name;
		}
	}

	static public class PersonSet {
		private final Set< Person > mySet = new HashSet<>();

		public void addPerson( Person p ) {
			synchronized( this ) {
				mySet.add( p );
			}
		}

		public synchronized boolean containsPerson( Person p ) {
			return mySet.contains( p );
		}

		public synchronized Set<Person> getMySet() {
			return mySet;
		}
	}


	static public void main() {
		PersonSet personSet = new PersonSet();

		final List<Person> people = Stream.of( "Valentino", "Fabrizio", "Marco", "Luìs" )
			.map( Person::new ).collect( Collectors.toList() );

		Set<Person> set = personSet.getMySet();

		Thread t1 = new Thread( () -> {
				synchronized ( personSet ) {
					people.forEach( personSet::addPerson );
					try {
						Thread.sleep( 3000 );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					personSet.getMySet().forEach( System.out::println );
				}
		});

		Thread t2 = new Thread( () -> {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				set.remove( people.get(0) );
				set.remove( people.get(0) );
				set.remove( people.get(0) );
				set.remove( people.get(0) );
		});

		t1.start();
		t2.start();

		try {
			t1.join();
			t2.join();
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		}
	}
}
