package incremental;

import java.util.Vector;

import slide.SlideSequentialPatterns;
import useful.Item;
import useful.Itemset;
import useful.Pattern;

/**
 * @brief Pattern read, it is resulted of other iteration.
 * 
 *        This class represents a pattern read. The information kept is all
 *        itemset (represented by a string for each one) and the number of trap
 *        on the old database.
 * 
 *        This class was tested by PatternReader class.
 * 
 * @author Carlos Roberto Silveira Junior
 * @see Pattern
 */
public class OldPattern extends SlideSequentialPatterns {

	/**
	 * Numbe the traps of this patterns
	 */
	private int numberOfOccorrence;

	/**
	 * Itemsets
	 */
	private String[] itemsets;

	/**
	 * This method break an pattern string in substring with all itemset without
	 * delimitations caracteres.
	 * 
	 * @param pattern
	 *            Pattern to be broke
	 * @return Collection of string with all itemset
	 */
	private String[] breakingStringPattern(String pattern) {

		int i = 0;

		Vector<String> v = new Vector<String>();

		String[] temp = pattern.split(" ");

		for (; i < temp.length; ++i)
			/* found an itemset */
			if (temp[i].startsWith("(")) {
				String add = temp[i].substring(1);

				for (++i; i < temp.length; ++i)
					if (temp[i].endsWith(")")) {
						add = add.concat(" " + temp[i].replace(")", ""));
						break;
					} else
						add = add.concat(" " + temp[i]);

				v.add(add);
			} else
				/* working with 1-itemset */
				v.add(temp[i]);

		/* creating string collection */
		String[] s = new String[v.size()];
		for (i = 0; i < v.size(); ++i)
			s[i] = v.elementAt(i);

		return s;
	}

	/**
	 * Contructor, recives an string with that representes the pattern and how
	 * much traps that pattern has.
	 * 
	 * @param pattern
	 *            The pattern read
	 * @param traps
	 *            Number of traps
	 */
	public OldPattern(String pattern, int traps) {

		this.numberOfOccorrence = traps > 0 ? traps : 0;

		/* passing the string removing the delimitator caracters */
		this.itemsets = this.breakingStringPattern(pattern.substring(2)
				.replaceAll(">", ""));
	}

	/**
	 * Returns the number of traps.
	 * 
	 * @return number of traps
	 */
	public int numberOfOccorrence() {
		return this.numberOfOccorrence;
	}

	/**
	 * Plus one on numberOfOccorrence.
	 */
	public void incTraps() {
		++this.numberOfOccorrence;
	}
	
	/**
	 * Add the number of occorrencie.
	 * 
	 * @param add Value to be added
	 */
	public void incTraps(int add) {
		this.numberOfOccorrence += add;
	}

	/**
	 * Return all the itemsets.
	 * 
	 * @return Array of Itemsets
	 */
	public String[] getItemset() {
		return this.itemsets;
	}

	/**
	 * Recreating the pattern with a new configuration and up dating the number
	 * of occorence.
	 * 
	 * @param itemsets
	 *            New configuration
	 */
	public void creatingNewPattern(Vector<Itemset> itemsets) {

		int index;

		for (String s : this.itemsets) {

			index = itemsets.indexOf(new Itemset(s));

			if (index == -1) {
				super.items.clear();
				super.traps.clear();
				break;
			}

			super.addItem(itemsets.elementAt(index));
		}

		/* up dating number of traps */
		this.numberOfOccorrence += super.numberOfOccorrence();
	}

	/**
	 * String with all itemset without formatation.
	 * 
	 * @return Old Pattern
	 */
	public String toString() {

		String s = "< ";

		for (String temp : this.itemsets)
			if (temp.contains(" "))
				s += ("(" + temp + ") ");
			else
				s += (temp + " ");

		return s + ">";
	}

}
