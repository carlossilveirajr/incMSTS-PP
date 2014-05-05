/**
 * @brief Colection of classes which is used in all implementation.
 *
 * This package implementes classes which are used all the time in
 * the algorithm implementation.  
 */
package useful;

import java.util.Vector;

/**
 * @brief This classe representes one item.
 * 
 *        This classe represente the item and it's used by all model of this
 *        implementation. This classe is composed by the name of the item and a
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
		this.name = new String();
		this.traps = new Vector<Integer>();
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
		this.traps = new Vector<Integer>();
	}

	/**
	 * Copy constructor, that creat a item as the old one.
	 * 
	 * @param old
	 *            The item to be copied
	 */
	public Item(Item old) {
		this.name = old.getName().toString();
		this.traps = (Vector<Integer>) (old.getTraps().clone());
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
		this.traps = new Vector<Integer>();
		this.traps.add(new Integer(trap));
	}

	/**
	 * Return the name of the item.
	 * 
	 * @return Name of the item
	 */
	public String getName() {
		return this.name;
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
		return this.traps;
	}

	/**
	 * Define or replace the occorences´ vector.
	 * 
	 * @param traps
	 *            The new vector of occorences
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
		if (!this.traps.contains(trap))
			this.traps.add(trap);
	}
	
	/**
	 * Give the number of occorences this item has.
	 * 
	 * @return Number of traps
	 */
	public int trapsNumber() {
		return this.traps.size();
	}

	/**
	 * Represente the item like a string.
	 * 
	 * @return Item as string
	 */
	@Override
	public String toString() {
		return this.name + " ";
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

	/**
	 * Just for test
	 * 
	 * @param args
	 *            Unutilized
	 */
	public static void main(String[] args) {

		Vector<Integer> v = new Vector<Integer>();
		v.add(new Integer(1));
		v.add(new Integer(2));
		v.add(new Integer(3));

		Item i1 = new Item();
		Item i2 = new Item("Item2");
		Item i3 = new Item("Item3",v);
		
		Item i1c = new Item(i1);
		Item i2c = new Item(i2);
		Item i3c = new Item(i3);
		
		/* Outputs :
		 * Testing i1
			i1.name = 
			i1.name = Item1
			i1.traps = []
			i1.traps = [1, 2, 3]
			Testing i2
			i2.name = Item2
			i2.name = Item2b
			i2.traps = []
			i2.traps = [1, 2, 3]
			Testing i3
			i3.name = Item3
			i3.name = Item3b
			i3.traps = [1, 2, 3]
			i3.traps = [1, 2, 3, 8]
			Testing i1c
			i1c.name = 
			i1c.name = Item1c
			i1c.traps = []
			i1c.traps = [1, 2, 3, 8]
			Testing i2c
			i2c.name = Item2
			i2c.name = Item2cb
			i2c.traps = []
			i2c.traps = [1, 2, 3, 8]
			Testing i3c
			i3c.name = Item3
			i3c.name = Item3cb
			i3c.traps = [1, 2, 3]
			i3c.traps = [1, 2, 3, 8]
			Item1 
		 */
		System.out.println("Testing i1");
		System.out.println("i1.name = " + i1.getName());
		i1.setName("Item1");
		System.out.println("i1.name = " + i1.getName());
		System.out.println("i1.traps = " + i1.getTraps().toString());
		i1.setTraps(v);
		System.out.println("i1.traps = " + i1.getTraps().toString());
		
		System.out.println("Testing i2");
		System.out.println("i2.name = " + i2.getName());
		i2.setName("Item2b");
		System.out.println("i2.name = " + i2.getName());
		System.out.println("i2.traps = " + i2.getTraps().toString());
		i2.setTraps(v);
		System.out.println("i2.traps = " + i2.getTraps().toString());
		
		System.out.println("Testing i3");
		System.out.println("i3.name = " + i3.getName());
		i3.setName("Item3b");
		System.out.println("i3.name = " + i3.getName());
		System.out.println("i3.traps = " + i3.getTraps().toString());
		v.add(new Integer(8));
		i3.setTraps(v);
		v.add(new Integer(9));
		System.out.println("i3.traps = " + i3.getTraps().toString());

		System.out.println("Testing i1c");
		System.out.println("i1c.name = " + i1c.getName());
		i1c.setName("Item1c");
		System.out.println("i1c.name = " + i1c.getName());
		System.out.println("i1c.traps = " + i1c.getTraps().toString());
		i1c.setTraps(v);
		System.out.println("i1c.traps = " + i1c.getTraps().toString());
		
		System.out.println("Testing i2c");
		System.out.println("i2c.name = " + i2c.getName());
		i2c.setName("Item2cb");
		System.out.println("i2c.name = " + i2c.getName());
		System.out.println("i2c.traps = " + i2c.getTraps().toString());
		i2c.setTraps(v);
		System.out.println("i2c.traps = " + i2c.getTraps().toString());
		
		System.out.println("Testing i3c");
		System.out.println("i3c.name = " + i3c.getName());
		i3c.setName("Item3cb");
		System.out.println("i3c.name = " + i3c.getName());
		System.out.println("i3c.traps = " + i3c.getTraps().toString());
		i3c.setTraps(v);
		System.out.println("i3c.traps = " + i3c.getTraps().toString());
		
		System.out.println(i1);
	}

}
