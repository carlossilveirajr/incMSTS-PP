package slide;

import java.util.Vector;

import useful.Item;
import useful.Itemset;
import useful.Pattern;
import gsp.SequentialPatterns;

/**
 * @brief Extends Sequential Patterns to use Slide Windows.
 * 
 *        This class extends SequentialPatterns to use Slide Windows, in this
 *        class there are methods to chance windows size value and it
 *        re-implement same methods which needs to make an upgrade on traps
 *        vector as addItem, addItemset.
 * 
 * @see SequentialPatterns
 * @author Carlos Roberto Silveira Junior
 * @data 2011.09.01
 */
public class SlideSequentialPatterns extends SequentialPatterns {

	/**
	 * Windows value, this will say how long an item should be found to be
	 * considere on the occurrence of the first item.
	 */
	private int windows;

	/**
	 * All occurence of first item that is the pattern happen.
	 */
	private Vector<Integer> firstOccorrence;

	/**
	 * Make a update on traps vector.
	 * 
	 * If there's just one item in the vector, traps will have the same traps
	 * vector of the item. But if there are more than one, traps will have the
	 * occorrences represented by the last item occorrence, when the pattern
	 * happend. This method used and upgread firstOcoorence vector, when we are
	 * locking for an occorrence of a new item, it's used the occorrence of the
	 * last item to know start point and the occorrence of the first item to
	 * know how long seach can go.
	 */
	private void updateTraps() {

		if (super.items.size() == 1) {
			super.traps = (Vector<Integer>) super.items.elementAt(0).traps()
					.clone();
			this.firstOccorrence = (Vector<Integer>) super.traps.clone();
		} else {
			int trap;
			int delta;
			boolean find;

			Vector<Integer> newTraps = new Vector<Integer>();
			Vector<Integer> trapLastElem = super.items.elementAt(
					super.items.size() - 1).traps();

			for (int i = 0, count = 0; i < super.traps.size()
					&& this.firstOccorrence.size() != 0; ++i) {
				trap = super.traps.elementAt(i);
				delta = this.windows - (trap - this.firstOccorrence.elementAt(count++)) / super.items.size();
				find = false;

				for (int j = 1; j <= delta && !find; ++j) {				
					if (trapLastElem.contains(new Integer(trap + j)) ) {
						newTraps.add(new Integer(trap + j));
						find = true;
					}
	
					if (super.traps.contains(new Integer(trap + j)))
						break;
				}

				if (!find)
					this.firstOccorrence.remove(--count);
			}

			super.traps = (Vector<Integer>) newTraps.clone();
		}
	}

	/**
	 * Construtor without parameter. Set windows as one.
	 */
	public SlideSequentialPatterns() {
		super();
		this.windows = 1;
		this.firstOccorrence = new Vector<Integer>();
	}

	/**
	 * Construtor which set just the windows value.
	 * 
	 * @param windows
	 *            Windows value.
	 */
	public SlideSequentialPatterns(int windows) {
		super();
		this.windows = windows;
		this.firstOccorrence = new Vector<Integer>();
	}

	/**
	 * Copy constructor. Creat a copy of the pattern.
	 * 
	 * @param old
	 *            Pattern to be copied.
	 */
	public SlideSequentialPatterns(SlideSequentialPatterns old) {
		super((SequentialPatterns) old);
		this.windows = old.windows;
		this.firstOccorrence = (Vector<Integer>) old.firstOccorrence.clone();
	}

	/**
	 * This constructor creat a patttern with an itemset.
	 * 
	 * @param itemset
	 *            Fist itemset to start the pattern
	 * @param windows
	 *            The size of the windows
	 */
	public SlideSequentialPatterns(Itemset itemset, int windows) {
		super();

		this.windows = windows;
		this.firstOccorrence = new Vector<Integer>();

		this.addItem(itemset);
	}

	/**
	 * Return the valeu of the windows.
	 * 
	 * @return The windows value
	 */
	public int getWindows() {
		return this.windows;
	}

	/**
	 * Set an value to window.
	 * 
	 * @param windows
	 *            The windows value to set
	 */
	public void setWindows(int windows) {
		this.windows = windows;
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

		SlideSequentialPatterns sp = new SlideSequentialPatterns();

		sp.setWindows(3);

		System.out.println("Without items");
		sp.printAll();

		sp.addItem(i1);
		System.out.println("With one item: " + sp);
		sp.printAll();
		System.out.println(sp.firstOccorrence + "\n");

		sp.addItem(i2);
		System.out.println("With two items: " + sp);
		sp.printAll();
		System.out.println(sp.firstOccorrence + "\n");

		sp.addItem(i3);
		System.out.println("With tree items: " + sp);
		sp.printAll();
		System.out.println(sp.firstOccorrence + "\n");

		sp.addItem(i1);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();
		System.out.println(sp.firstOccorrence + "\n");

		sp.addItem(i2);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();
		System.out.println(sp.firstOccorrence + "\n");

		sp.addItem(i1);
		System.out.println("Whit tree items: " + sp);
		sp.printAll();
		System.out.println(sp.firstOccorrence + "\n");
	}

}
