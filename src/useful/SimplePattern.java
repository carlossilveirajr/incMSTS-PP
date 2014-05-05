/**
 * @file This file keep two classes, the most important is the 
 * SimplePattern, that is composed by SimpleItemset. Both classes are 
 * presented in this file.
 */
package useful;

import java.util.Arrays;
import java.util.Vector;

/**
 * @brief Represents an itemset simplified.
 * 
 *        This class implements a simple itemset. That itemset has neither a
 *        vector to keep the traps nor other information to make data mining.
 * 
 *        It's just a way to keep the items to gather, presenting an itemset.
 * 
 * @author Carlos Roberto Silveira Junior
 */
class SimpleItemset {

	/**
	 * Items that compose the itemset.
	 */
	private String[] items;

	/**
	 * Number of the items in vector items.
	 */
	private int used;

	/**
	 * Constructor which receives just the number of item this itemset has.
	 * 
	 * @param number
	 *            Number of items.
	 */
	public SimpleItemset(int number) {

		this.items = new String[number];

		this.used = 0;
	}

	/**
	 * Copy constructor. It makes a deep copy.
	 * 
	 * @param old
	 *            Simple Itemset to be copied.
	 */
	public SimpleItemset(SimpleItemset old) {

		this.used = old.used;

		this.items = old.items.clone();
	}

	/**
	 * Returns the number of items this itemset should have; not the number of
	 * it there is in the vector.
	 * 
	 * @return Number of items the itemset completely has.
	 */
	public int numberOfItems() {

		return this.items.length;
	}

	/**
	 * Return how much items there are in the itemset.
	 * 
	 * @return Number of item on the itemset.
	 */
	public int getUsed() {

		return this.used;
	}

	/**
	 * True if the itemset are full of item, false on the other hand.
	 * 
	 * @return True if this itemset is Full.
	 */
	public boolean isFull() {

		return this.used == this.items.length;
	}

	/**
	 * Add a new item if there are space for that.
	 * 
	 * @param item
	 *            New item to be add.
	 */
	public void puttingItem(String item) {

		if (this.used < this.items.length)
			this.items[this.used++] = new String(item);
	}

	/**
	 * See if an itemset is a sub-itemset of the other.
	 * 
	 * @param superItemset
	 *            Super itemset
	 * @return true if this is a subitemset.
	 */
	public boolean isSubItemset(SimpleItemset superItemset) {

		if (this.used > superItemset.used)
			return false;

		boolean find;
		for (String sub : this.items) {

			find = false;
			for (String sup : superItemset.items)
				if (sup.contentEquals(sub)) {
					find = true;
					break;
				}

			if (!find)
				return false;
		}

		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		SimpleItemset other = (SimpleItemset) obj;
		if (this.used != other.used)
			return false;
		if (!Arrays.equals(this.items, other.items))
			return false;

		return true;
	}

	@Override
	public String toString() {
		if (this.used > 1) {
			String s = "(";
			for (String temp : this.items)
				s += temp + " ";
			return s += ")";
		}
		else
			return this.items[0];
	}
	
	

}

/**
 * @brief This class represents a pattern in a simple way.
 * 
 *        This class implements a way to represent a pattern in a simple way.
 *        This simple pattern doesn't keep the information about where the
 *        pattern happens or how much time it happens.
 * 
 *        The only information here is the itemsets that composes this pattern.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public class SimplePattern {

	/**
	 * Keep the itemsets that composes the pattern.
	 */
	private Vector<SimpleItemset> itemsets;

	/**
	 * Keep the support value.
	 */
	private double support;

	/**
	 * Used at generalization, if this pattern could be generalized than it is
	 * redudante, so can be deleted.
	 */
	private boolean canDelete = false;

	/**
	 * Constructor that need to receives the set of itemset.
	 * 
	 * @param itemsets
	 *            Vector of itemsets.
	 */
	public SimplePattern(Vector<Itemset> itemsets, double support) {

		this.support = support;

		this.itemsets = new Vector<SimpleItemset>(itemsets.size());

		for (int i = 0; i < this.itemsets.size(); ++i) {
			Itemset old = itemsets.elementAt(i);

			SimpleItemset si = new SimpleItemset(old.size());

			/* removing parentheses and breaking the itemset */
			String[] s = old.toString().replace("(", "").replace(")", "")
					.split(" ");

			for (String s1 : s)
				si.puttingItem(s1);

			this.itemsets.add(si);
		}
	}

	/**
	 * Transform a string in a simple pattern.
	 * 
	 * @param pattern
	 *            String that represents the pattern.
	 */
	public SimplePattern(String pattern, double support) {

		this.support = support;

		// pattern without '<' and '>'
		String p = pattern.replace("< ", "").replace(" >", "");

		// breaking in items
		String[] items = p.split(" ");

		// variance of internal control
		boolean findParentese = false;
		SimpleItemset si;
		Vector<String> temp = new Vector<String>();

		// starting the vector
		this.itemsets = new Vector<SimpleItemset>();

		// creating the itemset
		for (String i : items)
			if (!i.contentEquals(" ") && !i.contentEquals("") && !i.isEmpty())
				// itemset with one element
				if (!findParentese && !i.startsWith("(")) {
					si = new SimpleItemset(1);
					si.puttingItem(i);
					this.itemsets.add(si);
				} else { // itemset with more than one
					findParentese = true;

					temp.add(i.replace("(", "").replace(")", ""));

					// end of itemset, creating it really
					if (i.endsWith(")")) {
						findParentese = false;

						si = new SimpleItemset(temp.size());

						for (int j = 0; j < temp.size(); ++j)
							si.puttingItem(temp.elementAt(j));

						this.itemsets.add(si);
					}
				}
	}

	/**
	 * Copy constructor creat a copy of the old object. The only thing that
	 * doesn't change is canDelete. It still false also if old is true.
	 * 
	 * @param old
	 *            Object to copy.
	 */
	public SimplePattern(SimplePattern old) {

		this.itemsets = new Vector<SimpleItemset>(old.getItemsets().size());

		for (int i = 0; i < old.getItemsets().size(); ++i)
			this.itemsets
					.add(new SimpleItemset(old.getItemsets().elementAt(i)));

		this.support = old.getSupport();
		
		this.canDelete = old.isCanDelete();
	}

	/**
	 * Find the distance between the pattern and another one. This distance is
	 * an value between zero and one ([0;1]) and represents how closer two
	 * patterns are. The distance is calculated by the formula: |number of
	 * itemset these sequences has at the same position| / (size of the bigger
	 * sequence * (2(difference of size this sequences has) + 1). This "+1" is
	 * necessary to doesn't create zero division.
	 * 
	 * @param other
	 *            Other pattern to find the distance
	 * @return Distance
	 */
	public double distance(SimplePattern other) {

		int count = 0, sizeThis = this.itemsets.size(), sizeThat = other.itemsets
				.size();

		for (int i = 0; i < sizeThis && i < sizeThat; ++i)
			if (this.itemsets.elementAt(i) == other.itemsets.elementAt(i))
				++count;

		return count
				/ (double) ((sizeThis > sizeThat ? sizeThis : sizeThat) * (2 * Math
						.abs(sizeThat - sizeThis) + 1));
	}

	/**
	 * This method find the diferent element between this pattern and the other
	 * one. If the size if diferent it returns null and also is there is no
	 * diferences it returns null.
	 * 
	 * @param Other
	 *            the other pattern to capare
	 * @return Array with all index diferent.
	 */
	public Integer[] variante(SimplePattern other) {

		Vector<Integer> v = new Vector<Integer>();

		Integer[] r = new Integer[v.size()];

		if (other.getItemsets().size() != this.itemsets.size())
			return null;

		for (int i = 0; i < this.itemsets.size(); ++i)
			if (!this.itemsets.elementAt(i).equals(other.getItemsets().elementAt(i)))
				v.add(new Integer(i));

		if (v.size() < 1)
			return null;

		for (Integer i : r)
			i = v.remove(0);

		return r;
	}

	/**
	 * Change an itemset for a new one.
	 * 
	 * @param position
	 *            Available position
	 * @param newItemset
	 *            New itemset for the position
	 */
	public void change(int position, String newItemset) {

		String[] s = newItemset.split(" ");

		this.itemsets.add(position, new SimpleItemset(s.length));
		this.itemsets.remove(position + 1);

		for (String s2 : s)
			this.itemsets.elementAt(position).puttingItem(s2);
	}

	/**
	 * To access the data.
	 * 
	 * @return The itemsets vector
	 */
	public Vector<SimpleItemset> getItemsets() {
		return itemsets;
	}

	/**
	 * Return the itemset at the position posix.
	 * 
	 * @param posix
	 *            Position of the itemset
	 * @return itemset at posix
	 */
	public String getItemset(int posix) {
		return posix >= 0 && posix < this.itemsets.size() ? this.itemsets
				.elementAt(posix).toString() : null;
	}

	/**
	 * Return the support value.
	 * 
	 * @return the support
	 */
	public double getSupport() {
		return support;
	}

	/**
	 * Getting the support value.
	 * 
	 * @param support
	 *            the support to set
	 */
	public void setSupport(double support) {
		this.support = support;
	}

	/**
	 * True if this pattern was generalized.
	 * 
	 * @return the canDelete
	 */
	public boolean isCanDelete() {
		return canDelete;
	}

	/**
	 * Set canDelete to true.
	 */
	public void setCanDelete() {
		this.canDelete = true;
	}

	/**
	 * String that represents the pattern.
	 * < i1 i2 ... > support=x
	 * 
	 * @return String that represents the pattern.
	 */
	@Override
	public String toString() {
		String s = new String();
		
		s = "< " ; 
		for (int i = 0; i < this.itemsets.size(); ++i)
			s += this.itemsets.elementAt(i) + " "; 
		s += "> support=" + this.support;
		
		return s;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((itemsets == null) ? 0 : itemsets.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePattern other = (SimplePattern) obj;
		if (itemsets == null) {
			if (other.itemsets != null)
				return false;
		} else if (!itemsets.equals(other.itemsets))
			return false;
		return true;
	}

	
}
