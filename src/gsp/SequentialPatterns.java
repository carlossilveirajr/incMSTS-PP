package gsp;

import java.util.Vector;

import useful.Item;
import useful.Itemset;
import useful.Pattern;

/**
 * @brief This class represents the sequential patterns
 */
/**
 * This class implements the sequential patterns. Try to make the work easy this
 * class has a set with all the tuples which start the pattern. A sequential
 * pattern is composed by a sorting vector of itemset.
 * 
 * @author Carlos Roberto Silveira Junior
 * @data 2011.08.11
 */
public class SequentialPatterns implements Pattern {

	/**
	 * Composition of the pattern.
	 */
	protected Vector<Itemset> items;

	/**
	 * Composed by all occorrence of the pattern.
	 */
	protected Vector<Integer> traps;

	/**
	 * Make a update on traps vector.
	 * 
	 * If there's just one item in the vector, traps will have the same traps
	 * vector of the item. But if there are more than one, traps will have the
	 * occorrence with happend in all items.
	 */
	private void updateTraps() {
		
		if (this.items.size() == 1)
			this.traps = (Vector<Integer>) this.items.elementAt(0).traps()
					.clone();
		else {
			int trap;

			Vector<Integer> newTraps = new Vector<Integer>();
			Vector<Integer> trapLastElem = this.items.elementAt(
					this.items.size() - 1).traps();

			for (int i = 0; i < this.traps.size(); ++i) {
				trap = this.traps.elementAt(i);
				if (trapLastElem.contains(new Integer(trap + 1)))
					newTraps.add(trap + 1);
			}

			this.traps = (Vector<Integer>) newTraps.clone();
		}
	}

	/**
	 * Constructor without parameter, that start the pattern empty. It had
	 * better do not use.
	 */
	public SequentialPatterns() {
		this.items = new Vector<Itemset>();
		this.traps = new Vector<Integer>();
	}

	/**
	 * Copy conctrutor. Creat a new SequentialPatterns as the old one.
	 * 
	 * @param old
	 *            Sequential Patterns which will be copied
	 */
	public SequentialPatterns(SequentialPatterns old) {
		this.items = (Vector<Itemset>) old.items.clone();
		this.traps = (Vector<Integer>) old.traps.clone();
	}

	/**
	 * Add a new item on the end of the pattern and make the updates
	 * necessaries.
	 * 
	 * @return Number of occorrence of the sequential pattern
	 */
	public int addItem(Item i) {
		this.items.add(new Itemset(new Item(i)));
		this.updateTraps();

		return this.traps.size();
	}

	/**
	 * Add a new item on the end of the pattern and make the updates
	 * necessaries.
	 * 
	 * @return Number of occorrence of the sequential pattern
	 */
	public int addItem(Itemset i) {
		this.items.add(new Itemset(i));
		this.updateTraps();

		return this.traps.size();
	}

	/**
	 * Get the number of occorrence the pattern has.
	 * 
	 * @return Number of occorrence
	 */
	public int numberOfOccorrence() {
		
		return this.traps.size();
	}
	
	/**
	 * Check if test is subsequential of this.
	 * 
	 * @param test
	 *            subsequential
	 * @return true if test is a subsequencial
	 */
	public boolean subPattern(SequentialPatterns test) {

		boolean found;

		if (test.items.size() > this.items.size())
			return false;

		for (int i = 0, j = 0; i < test.items.size(); ++i) {

			found = false;

			for (; j < this.items.size() && !found; ++j)
				if (this.items.elementAt(j).subItemset(test.items.elementAt(i)))
					found = true;

			if (!found)
				return false;
		}

		return true;
	}

	/**
	 * Print on Stdout the data of the pattern. Just for test.
	 */
	public void printAll() {
		String s = "{ Items = ";
		s += this.toString();
		s += " Traps = ";
		s += this.traps;

		System.out.println(s + "}");
	}

	/**
	 * This method return a string with the pattern in this forma
	 * "< item1 ... itemN >";
	 * 
	 * @return The sequential pattern in string
	 */
	@Override
	public String toString() {
		String s = "< ";

		for (int i = 0; i < this.items.size(); ++i)
			s += ((Itemset) this.items.elementAt(i)) + " ";

		return s + ">";
	}

	

	/**
	 * Two patterns are equal if they have the same itemsets on
	 * the same possitions.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SequentialPatterns other = (SequentialPatterns) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Ununseful
	 */
	public static void main(String[] args) {

		Vector<Integer> v = new Vector<Integer>();

		v.add(1);
		v.add(4);
		v.add(7);

		Item i1 = new Item("i1", v);

		v.clear();
		v.add(2);
		v.add(5);

		Item i2 = new Item("i2", v);

		v.clear();
		v.add(3);
		v.add(9);

		Item i3 = new Item("i3", v);

		SequentialPatterns sp = new SequentialPatterns();

		System.out.println("Without items");
		sp.printAll();

		sp.addItem(i1);
		System.out.println("With one item: " + sp);
		sp.printAll();

		sp.addItem(i2);
		System.out.println("With two items: " + sp);
		sp.printAll();

		sp.addItem(i3);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();

		sp.addItem(i1);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();

		sp.addItem(i2);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();

		sp.addItem(i1);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();
		
		/* Results with window equal 1: 
		 * Without items
		 * { Items = [ ] Traps = []}
		 * With one item: [ i1 ]
		 * { Items = [ i1 ] Traps = [1, 4, 7]}
		 * With two items: [ i1 i2 ]
		 * { Items = [ i1 i2 ] Traps = [2, 5]}
		 * Whit tree items: [ i1 i2 i3 ]
		 * { Items = [ i1 i2 i3 ] Traps = [3]}
		 * Whit tree items: [ i1 i2 i3 i1 ]
		 * { Items = [ i1 i2 i3 i1 ] Traps = [4]}
		 * Whit tree items: [ i1 i2 i3 i1 i2 ]
		 * { Items = [ i1 i2 i3 i1 i2 ] Traps = [5]}
		 * Whit tree items: [ i1 i2 i3 i1 i2 i1 ]
		 * { Items = [ i1 i2 i3 i1 i2 i1 ] Traps = []} */
	}

}
