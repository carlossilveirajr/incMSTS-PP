package useful;

import java.util.Vector;

/**
 * @brief Represents the itemset. A collection of unsorting items.
 */
/**
 * This class implements the itemset. An itemset is a no empty set of item which
 * happends in the same truples. This class intends to help on the mining
 * process and it's used because it's necessery to represent the situations
 * that's two or more items happend in the same truples.
 * 
 * In this class there is no contructor without parameter because the itemset
 * needs to have at list one element. Than it had better do not have this kind
 * of constructor
 * 
 * @author Carlos Roberto Silveira Junior
 * @data 2011.08.09
 */
public class Itemset {

	/**
	 * String that represents the itemset.
	 */
	private String itemset = new String();
	
	/**
	 * Vector with all occurrences.
	 */
	private Vector<Integer> traps = new Vector<Integer>();
	
	/**
	 * Number of items in the itemset.
	 */
	private int size = 0;
	
	/**
	 * Constructor with one parameter which creat an object with just one item.
	 * This method creat a vector and than insert de item.
	 * 
	 * @param item
	 *            Item that composed the vector
	 */
	public Itemset(Item item) {
		
		this.size = 1;
		this.itemset = item.getName();
		this.traps = (Vector<Integer>) item.getTraps().clone();
	}
	
	/**
	 * Constructor with one parametre which creat an object with some
	 * information. This set will recive a vector of items that composed the
	 * set.
	 * 
	 * @param itemset
	 *            Vector of items
	 */
	public Itemset(Vector<Item> itemset) {
		
		this.traps = new Vector<Integer>();
		this.size = itemset.size();
		
		/*creating vector of occurence*/
		if (!itemset.isEmpty()) {
			
			Item item = itemset.elementAt(0);
			this.itemset = item.getName() + " ";
			this.traps = (Vector<Integer>) item.getTraps().clone();
			
			for (int i = 1; i < this.size; ++i) {
				item = itemset.elementAt(i);
				this.itemset += item.getName() + " ";
				
				for (int j = 0; j < this.traps.size(); ++j)
					if (!item.getTraps().contains(this.traps.elementAt(j)))
						this.traps.remove(j--);
			}
		}
			
	}
	
	/**
	 * Copy contructor, it's cread a deep copy of the object recived as
	 * parametre.
	 * 
	 * @param old
	 *            The Itemset to be copied.
	 */
	public Itemset(Itemset old) {
		
		this.size = old.size;
		this.itemset = old.itemset.toString();
		this.traps = (Vector<Integer>) old.traps.clone();
	}
	
	/**
	 * Construtor that recives just a string with all item. It creates an
	 * itemset without occorrence.
	 * 
	 * @param itemset
	 *            String of all item
	 */
	public Itemset(String itemset) {

		this.itemset = new String(itemset);

		this.traps = new Vector<Integer>();

		String[] s = itemset.split(" ");
		this.size = s.length;
	}
	
	/**
	 * Return the size of the itemset.
	 * 
	 * @return Size of itemset
	 */
	public int size() {
		/*
		 * I had heard that the population of Earth had exceeded 7 billion, but
		 * I didn't know that that was the maximum capacity. Is it time for a
		 * culling, population control, or some human compression algorithm to
		 * reduce the planet's population or increase its size? Are we working
		 * on some sort ...
		 */
		return this.size;
	}
	
	/**
	 * If there's just one item, the traps is the traps of the item, but if
	 * there are more, the traps is the interception of all vectors.
	 * 
	 * @return A vector with the occorrence of the Itemset
	 */
	public Vector<Integer> traps() {
		return this.traps;
	}
	
	/**
	 * Change the vector of items.
	 * 
	 * @param itemset
	 *            New vector
	 */
	public void setItemset(Vector<Item> itemset) {
		
		this.size = itemset.size();
		
		Item item = itemset.elementAt(0);
		this.itemset = item.getName() ;
		this.traps = (Vector<Integer>) item.getTraps().clone();
		
		for (int i = 1; i < this.size; ++i) {
			item = itemset.elementAt(i);
			this.itemset += (" " + item.getName());
			
			/*updating traps*/
			for (int j = 0; j < this.traps.size(); ++j)
				if (!item.getTraps().contains(this.traps.elementAt(j)))
					this.traps.remove(j--);
		}
	}

	/**
	 * Add a NEW item.
	 * 
	 * @param i
	 *            Item to be add
	 * @return true if the item was new
	 */
	public boolean addItem(Item i) {
		
		if (this.itemset.contains(i.getName()))
			return false;
		else {
			this.itemset += " ";
			this.itemset += i.getName();
			++this.size;
	
			/*up dating traps*/
			for (int j = 0; j < this.traps.size(); ++j)
				if (!i.getTraps().contains(this.traps.elementAt(j)))
					this.traps.remove(j--);
			
			return true;
		}
	}
	
	/**
	 * Add a NEW items of itemset.
	 * 
	 * @param i
	 *            Itemset to be add
	 */
	public void addItem(Itemset i) {
		
		String s[] = i.itemset.split(" ");
		
		for (String temp : s)
			if (!this.itemset.contains(temp)) {
				this.itemset += " " + temp;
				++this.size;
			}
		
		/*up dating traps*/
		for (int j = 0; j < this.traps.size(); ++j)
			if (!i.traps().contains(this.traps.elementAt(j)))
				this.traps.remove(j--);
	}

	/**
	 * Check if test is a subitemset of this itemset
	 * 
	 * @param test
	 *            The subitemset
	 * @return true is test is a subitemset
	 */
	public boolean subItemset(Itemset test) {

		boolean found = false;
		
		String []s1 = this.itemset.split(" ");
		
		for (String s: s1)
			if (test.itemset.contains(s)) {
				found = true;
				break;
			}
		
		s1 = test.itemset.split(" ");
		
		for (String s: s1)
			if (!this.itemset.contains(s)) {
				found = false;
				break;
			}
		
		return found;
				
	}
	
	/**
	 * Represente the itemset like a string.
	 * 
	 * @return Itemset as string
	 */
	@Override
	public String toString() {
		return this.size <= 1 ? this.itemset : "(" + this.itemset + ")";
	}
	
	/**
	 * Set with an itemset is equal another.
	 * 
	 * @return true if is equal, false if not
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		Itemset other = (Itemset) obj;

		if (this.size != other.size())
			return false;
		else {
			String [] s1 = this.itemset.split(" ");
			
			/* items can be no sorted */
			for (String temp1 : s1)
				if (!other.itemset.contains(temp1))
					return false;
			
			return true;
		}
	}
	
	/**
	 * Just for test
	 * 
	 * @param args
	 *            Unutilized
	 */
	public static void main(String[] args) {

		Vector<Item> v = new Vector<Item>();

		v.add(new Item("Item1"));
		v.add(new Item("Item2"));

		Itemset i1 = new Itemset(v);
		Itemset i2 = new Itemset(v);

		v.add(new Item("Item3"));
		i1.setItemset(v);

		v.add(new Item("Item4"));

		Itemset i3 = new Itemset(i1);

		v.add(new Item("Item5"));

		System.out.println("Itemset 1 = " + i1);
		System.out.println("Itemset 2 = " + i2);
		System.out.println("Itemset 3 = " + i3);

		i3.setItemset(v);
		System.out.println("Itemset 3.2 = " + i3);

		i3.addItem(new Item("Item6"));
		System.out.println("Itemset 3.3 = " + i3);

		System.out.println("Itemset 2.traps = " + i2.traps());

		i2.traps().add(new Integer(1));
		i2.traps().add(new Integer(2));

		System.out.println("Itemset 2.2.traps = " + i2.traps());

		System.out.println("Itemset 1.size = " + i1.size());

		if (i1.equals(i2))
			System.out.println("error");
		else
			System.out.println("right");

		if (!i1.equals(i1))
			System.out.println("error");
		else
			System.out.println("right");

		Itemset i4 = new Itemset(i1);
		if (!i1.equals(i4))
			System.out.println("error");
		else
			System.out.println("right");
		
		/* Result expeted:
		 * Itemset 1 = ( Item1 Item2 Item3 )
		 * Itemset 2 = ( Item1 Item2 )
		 * Itemset 3 = ( Item1 Item2 Item3 )
		 * Itemset 3.2 = ( Item1 Item2 Item3 Item4 Item5 )
		 * Itemset 3.3 = ( Item1 Item2 Item3 Item4 Item5 Item6 )
		 * Itemset 2.traps = []
		 * Itemset 2.2.traps = [1, 2]
		 * Itemset 1.size = 3
		 * right
		 * right
		 * right */
	}
}
