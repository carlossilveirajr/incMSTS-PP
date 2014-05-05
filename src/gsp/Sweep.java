package gsp;

import java.util.Vector;
import useful.FileReader;
import useful.Item;
import useful.Itemset;

/**
 * @brief This class makes the first sweep on the database.
 * 
 *        At the first sweep, this implementation makes a vector with all item
 *        and each item has its own vector of traps, this class take care of it
 *        completting thises vectors.
 * 
 *        This class has the file with database and after that process the
 *        algorithm does not need to sweep the database anymore.
 * 
 * @author Carlos Roberto Silveira Junior
 * @data 2011.08.19
 */
public class Sweep {

	/**
	 * Name of the file with the database.
	 */
	private String fileName;

	/**
	 * It represents a char which is used to break the tuple line. It says what
	 * separete an item of other.
	 */
	private final String breaker = new String(" ");

	/**
	 * After process exec, it has the number of tuples on database.
	 */
	private int numberTuples;

	/**
	 * This method is the main of this class. It read line-by-line of the
	 * database and extrate items. Than it creat an vector with this items and
	 * complet their traps.
	 */
	protected Vector<Item> exec() {

		FileReader bd = new FileReader(this.fileName);
		Vector<Item> v = new Vector<Item>();
		String line;

		/* while there is tuple in file */
		while (!(line = bd.getLine()).isEmpty()) {

			/* Breaking line in breker */
			String s[] = line.split(this.breaker);

			/* tupla identification */
			Integer when = new Integer(s[0]);

			/* for all items in each line */
			for (int i = 1; i < s.length; ++i)
				/* new item */
				if (!v.contains(new Item(s[i])))
					v.add(new Item(s[i], when));
				else
					/* old item, add just trap */
					v.elementAt(v.indexOf(new Item(s[i]))).addTrap(when);

			/* calculating number of tuples */
			++this.numberTuples;
		}

		bd.closeFile();

		return v;
	}

	/**
	 * Constructor. It needs to know name of database.
	 * 
	 * @param file
	 *            Name of database
	 */
	public Sweep(String file) {
		this.fileName = file;
		this.numberTuples = 0;
	}

	/**
	 * This method works as a interface between the vector of items founded and
	 * the returned.
	 * 
	 * It makes a new vector with all 1-itemset (itemset with just one
	 * elemente).
	 * 
	 * @return All 1-itenset on database
	 */
	public Vector<Itemset> execute() {
		Vector<Item> temp = this.exec();
		Vector<Itemset> candidates = new Vector<Itemset>(temp.size());

		for (int i = 0; i < temp.size(); ++i)
			candidates.add(new Itemset(temp.elementAt(i)));

		return candidates;
	}

	/**
	 * Return the number of tuples there are on database. This number is
	 * calculeted by exec than this method needs to run before on the other hand
	 * the number will be zero.
	 * 
	 * @return Number of tuples
	 */
	public int numberTuples() {
		return this.numberTuples;
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Unutilized
	 */
	public static void main(String[] args) {

		Sweep sweep = new Sweep("testeTiny");

		Vector<Itemset> v = sweep.execute();

		System.out.println(v.size());
		for (int i = 0; i < v.size(); ++i) {
			System.out.print(v.elementAt(i));
			System.out.println(v.elementAt(i).traps());
		}

	}

}
