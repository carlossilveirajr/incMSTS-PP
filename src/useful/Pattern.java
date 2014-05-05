package useful;

/**
 * @brief Interface of Pattern, it makes easer implementation.
 * 
 *        This interface is used to show what a pattern needs to do. There are a
 *        collection of methods which all patterns must to implements.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public interface Pattern {

	/**
	 * Add an itemset in pattern based on a item.
	 * 
	 * @param i
	 *            New item
	 * @return Number of traps
	 */
	public int addItem(Item i);

	/**
	 * Add an itemset in pattern.
	 * 
	 * @param i
	 *            New item
	 * @return Number of traps
	 */
	public int addItem(Itemset i);

	/**
	 * Returns the number of traps.
	 * 
	 * @return number of traps
	 */
	public int numberOfOccorrence();
}
