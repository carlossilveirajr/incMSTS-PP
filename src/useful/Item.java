/**
 * @brief Collection of classes which is used in all implementation.
 *
 * This package implements classes which are used all the time in
 * the algorithm implementation.  
 */
package useful;

import java.util.Vector;

/**
 * @brief This class represents one item.
 * 
 *        This class represents the item and it's used by all model of this
 *        implementation. This class is composed by the name of the item and a
 *        vector with all occurrence numbers.
 * 
 * @author Carlos Roberto Silveira Junior
 * @data 2011.08.09
 */
public class Item {

	/**
	 * Name of the item which defines the item.
	 */
	private String name;

	/**
	 * A vector with the numbers of tuples where the item occurred.
	 */
	private Vector<Integer> traps;

	/**
	 * Constructor without parameter. This had better do not use.
	 */
	public Item() {
		name = new String();
		traps = new Vector<Integer>();
	}

	/**
	 * Constructor with only one parameter. This method gives just a name to de
	 * item.
	 * 
	 * @param name
	 *            Name of the item
	 */
	public Item(String name) {
		this.name = name;
		traps = new Vector<Integer>();
	}

	/**
	 * Copy constructor, that creat a item as the old one.
	 * 
	 * @param old
	 *            The item to be copied
	 */
	public Item(Item old) {
		name = old.getName();
		traps = (Vector<Integer>) (old.getTraps().clone());
	}

	/**
	 * Constructor with all parameter. This method needs the name of the item
	 * and the traps vector.
	 * 
	 * @param name
	 *            Name of the item
	 * @param traps
	 *            Vector with the traps.
	 */
	public Item(String name, Vector<Integer> traps) {
		this.name = name;
		this.traps = (Vector<Integer>) traps.clone();
	}
	
	/**
	 * Constructor with two parameter. This method needs the name of the item
	 * and the first trap start trap vector.
	 * 
	 * @param name
	 *            Name of the item
	 * @param trap
	 *            First trap
	 */
	public Item(String name, int trap) {
		this.name = name;
		traps = new Vector<Integer>();
		traps.add(new Integer(trap));
	}

	/**
	 * Return the name of the item.
	 * 
	 * @return Name of the item
	 */
	public String getName() {
		return name;
	}

	/**
	 * Give a name to the item.
	 * 
	 * @param name
	 *            The name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return all the item occorences.
	 * 
	 * @return A vector with all occorences
	 */
	public Vector<Integer> getTraps() {
		return traps;
	}

	/**
	 * Define or replace the occurrence vector.
	 * 
	 * @param traps
	 *            The new vector of occurrences
	 */
	public void setTraps(Vector<Integer> traps) {
		this.traps = (Vector<Integer>) traps.clone();
	}
	
	/**
	 * Add a new trap on traps vector.
	 * 
	 * @param trap Number of trap
	 */
	public void addTrap(Integer trap) {
		if (!traps.contains(trap))
			traps.add(trap);
	}
	
	/**
	 * Give the number of occurrences this item has.
	 * 
	 * @return Number of traps
	 */
	public int getNumberOfTraps() {
		return traps.size();
	}

	/**
	 * Represent the item like a string.
	 * 
	 * @return Item as string
	 */
	@Override
	public String toString() {
		return name + " ";
	}

	/**
	 * Check if an item is equal to another.
	 * 
	 * @return true, if it's equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
