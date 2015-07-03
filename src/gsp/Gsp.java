/*****************************
 * @brief This package implements the GSP Algorithm.
 * 
 *        This package implements GSP classic algorithm. There are no alteration
 *        to include any new funcionalty. It's intends to be a base to implement
 *        all new funcionalty on this algorithm.
 * 
 *        On this implementation, the work starts on Sweep, this class open the
 *        database and makes the first and single scan. That returns a vector
 *        with all item in the database file, all item has a name which uniquely
 *        identifies an item and a vector of integer with all occorrence of the
 *        item. This scan retorns number of tuple too.
 * 
 *        After that, all non-frequente items are cut off, it is made by minimum
 *        support value which is setted by user. All items which has support
 *        less than minimum required are discarded. A support value is given by:
 * 
 *        Support (s) = |size of occorence of s| / |Total number of tuple| ; s
 *        can be an item, itemset or sequential patterns.
 * 
 *        Pruning of s happens if Support(s) < Support Mininum Value
 * 
 *        After that the itemset are created by the items. An itemset is a non
 *        empty set of items and means that all items in that set happend at the
 *        same time or on the same tuple. In other words, if Item1, Item2, Item3
 *        are in Itemset1 than Item1, Item2 and Item3 happends on the same
 *        tuple. A set doesn't have sort.
 * 
 *        The itemset are created by combination of all item one-by-one. After
 *        that, all itemset are analyzed and all non-frequente ones are cut
 *        following same principles of Items.
 * 
 *        The sequential patterns are found by combining all itemset one-by-one,
 *        the candidates created by the combination are checked to see their
 *        support as happend before. But, if a sequence s = <i1 i2 i3> happend
 *        than it means in a lot of tuples i1 happen and a lot of that tuple are
 *        followed by others which i2 happend and that's are followed by tuple
 *        where i3 happend immediately.
 * 
 *        The support is how many time s happend divided by how many tuple the
 *        database has and this value must be more than minimum to s be
 *        accepted.
 * 
 */
package gsp;

import java.util.Vector;

import useful.GSP;
import useful.Item;
import useful.Itemset;
import useful.Pattern;

/**
 * @brief This class is the start to GSP.
 * 
 *        This class implements GSP classic algorithm proposed by (Srikant, R. &
 *        Agrawal, R.; 1996).
 * 
 *        On the first interation, the algorithm found all item on databases,
 *        after that all item with support value is less than minimum necessary
 *        are cut off. Based on this items is created itemset, set of almost one
 *        item which will compose the patterns. The vector of Itemset are
 *        checked and all non-frequent itemset is cut off.
 * 
 *        This vector is called candidate becouse has all candidates itemset to
 *        make patterns. All patterns are made combining itemset and check the
 *        support. Interation by interarion, new patterns are created and
 *        that're bigger than older.
 * 
 *        The interation overs when it cannot creat a big pattern.
 * 
 *        An support of pattern/item/itemset S is: S = number of occorrence of
 *        item/itemset/pattern per number of truples in data base
 * 
 *        If i is an item/itemset/pattern, i is cut if only if S(i) < MinSup (
 *        value defined by user)
 * 
 * @author Carlos Roberto Silveira Junior
 * @data 2011.08.11
 * @see GSP
 */
public class Gsp implements GSP {

	/**
	 * Vector with all itemset candidates composed by that ones which attended
	 * minimal support.
	 */
	private Vector<Itemset> candidates;

	/**
	 * Vector with all patterns.
	 */
	protected Vector<Pattern> patterns;

	/**
	 * Total number of tuplas in database.
	 */
	private int numberTuples;

	/**
	 * Value of minimum support.
	 */
	private double minSup;
	
	/**
	 * It's the karnel of this algoritmo.
	 * 
	 * It combine all itemset one by one and test to see if they have the
	 * support minimum necessary.
	 */
	private void generation() {

		SequentialPatterns sp;

		Vector<SequentialPatterns> temp = new Vector<SequentialPatterns>();

		/* with the first interation of this method */
		/* creat patterns with lenght one */
		if (this.patterns.isEmpty())
			for (int i = 0; i < this.candidates.size(); ++i) {
				this.patterns.add(new SequentialPatterns());
				this.patterns.elementAt(i)
						.addItem(this.candidates.elementAt(i));
			}

		/* combine all itemset and creat all pattern frequent */
		for (int i = 0; i < this.patterns.size(); ++i) {
			temp.clear();
			
			for (int j = 0; j < this.candidates.size(); ++j) {
				sp = new SequentialPatterns((SequentialPatterns)this.patterns.elementAt(i));

				/* check the minimum support */
				if ((double) sp.addItem(this.candidates.elementAt(j))
						/ (double) this.numberTuples >= this.minSup)
					temp.add(sp);
			}
			
			/*check if there are items generated*/
			if (!temp.isEmpty()) {
				/* looking and removing subPatterns */
				for (int j = 0; j < this.patterns.size(); ++j)
					for (int k = 0; k < temp.size(); ++k)
						if (temp.elementAt(k).subPattern((SequentialPatterns) this.patterns.elementAt(j))) {
							this.patterns.removeElementAt(j);
							
							if (--j <= i)
								--i;
							
							if (j < 0)
								j = 0;
							if (i < 0)
								i = 0;
						}
				
				this.patterns.addAll(temp);
			}
		}
	}

	/**
	 * An non-frequent candidate is all item which does not have the support
	 * necessare.
	 * 
	 * The support is how much tuple has the candidate divided per number of
	 * tuple on database.
	 */
	protected void removingNonfrequentCandidates() {

		for (int i = 0; i < this.candidates.size(); ++i)
			if ((double) this.candidates.elementAt(i).traps().size()
					/ (double) this.numberTuples < this.minSup)
				this.candidates.remove(i--);
	}

	/**
	 * This method apply candidate and test strategy to find all itemsets.
	 * 
	 * It works by levels, first it creats 2-itemsets than with this is
	 * combinated creating 3-itemsets which will be combinated creating
	 * 4-itemsets ... until creat an empty k-itemset.
	 * 
	 * At the end, this.candidates will have all [1...(k-1)]-itemset sorted.
	 */
	protected void growUpItemset() {

		final int limitOne = this.candidates.size();
		int start = 0, end = this.candidates.size();
		 
		
		while(start != end) {
			Itemset cand;
			
			for (; start < end; ++start) 
				for (int j = 0; j < limitOne; ++j)
					if (start != j) {
						/*creating new itemset*/
						cand = new Itemset(this.candidates.elementAt(start));
						cand.addItem(this.candidates.elementAt(j));
						
						/*looking the new item suport*/
						if (cand.traps().size()/(double)this.numberTuples >= this.minSup)
							if (!this.candidates.contains(cand))
								this.candidates.add(cand);
					}
			end = this.candidates.size();
		}
	}

	/**
	 * This method is used to derivated class can manipulate candidates.
	 * 
	 * @return Reference to candidates
	 */
	protected Vector<Itemset> getCandidates() {
		return this.candidates;
	}
	
	/**
	 * Adding a number of tuple, it is used when we are working at the
	 * incremental model to keep the database consistence.
	 * 
	 * @param add
	 *            Number the tuple to be added (positive or negative)
	 */
	protected void addTupleNumber(int add) {
		this.numberTuples += add;
	}

	/**
	 * Construtor with one parameter.
	 * 
	 * This method recive the path of database, creat an object Sweep and extrat
	 * all items in database.
	 * 
	 * @param databaseName
	 *            Name and/or path of database
	 */
	public Gsp(String databaseName) {

		/* Find the candidates */
		Sweep sweep = new Sweep(databaseName);

		this.candidates = sweep.execute();
		this.numberTuples = sweep.numberTuples();
		this.patterns = new Vector<Pattern>();

		sweep = null;

		this.minSup = 0.01;
	}

	/**
	 * Construtor with two argument.
	 * 
	 * This method sets minumum support and calls the other contructor.
	 * 
	 * @param databaseName
	 *            Name and/or path of database
	 * @param minSup
	 *            Support minimum value
	 */
	public Gsp(String databaseName, double minSup) {

		this(databaseName);
		this.minSup = minSup;
	}

	/**
	 * Retorn value of support minimum setted.
	 * 
	 * @return Value of support minimum.
	 */
	public double getMinSup() {
		return this.minSup;
	}

	/**
	 * Set the support minimum value.
	 * 
	 * It needs to be great than 0.01 however will be 0.01.
	 * 
	 * @param minSup
	 *            Valeu of support minumum
	 */
	public void setMinSup(double minSup) {
		this.minSup = minSup > 0.01 ? minSup : 0.01;
	}

	/**
	 * Return the number of tuples on database.
	 * 
	 * @return Number of tuples
	 */
	public int getNumberTuples() {
		return this.numberTuples;
	}

	/**
	 * This method works as an interface which will make the processing.
	 * 
	 * First, it remove all non-frequent candidates, than all itemset are
	 * created, after that it creat all candidate and test.
	 */
	public void execute() {

		/* removing non-friquent candidates */
		this.removingNonfrequentCandidates();

		/* geration itemsets and test */
		this.growUpItemset();

		/* generation candidates and test */
		this.generation();
	}

	/**
	 * This return a string with all patterns found and their support. The
	 * format is: [ itemset1 ... itemsetn ] support: X.
	 * 
	 * To works correctly it needs to run execute before.
	 * 
	 * @return String with all patterns.
	 */
	@Override
	public String toString() {
		String s = " ";

		for (int i = 0; i < this.patterns.size(); ++i)
			s += this.patterns.elementAt(i) + " support: "
					+ ((Double) (this.patterns.elementAt(i).numberOfOccorrence()
					/ (double) this.numberTuples)).toString() + "\n";

		return s;
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Unutilized
	 */
	public static void main(String[] args) {
		
		// real test
		Gsp gsp = new Gsp(args[0],new Double(args[1]));
		
		gsp.execute();
		
		System.out.println(args[0] + " support " + args[1]);
		System.out.println(gsp);
		
/*
		// developping test
		Gsp gsp = new Gsp("testeSimulated");

		System.out.println("All candidates in database");
		System.out.println(gsp.candidates);

		gsp.setMinSup(0.2);
		gsp.removingNonfrequentCandidates();
		System.out
				.println("All frequente candidates in database and patterns.");
		System.out.println(gsp.candidates);
		System.out.println(gsp.patterns);

		gsp.growUpItemset();
		System.out.println("Expended itemset");
		System.out.println(gsp.candidates);
		System.out.println(gsp.patterns);

		gsp.generation();
		System.out.println("Found patterns 1");
		System.out.println(gsp.candidates);
		System.out.println(gsp.patterns);

		gsp.generation();
		System.out.println("Found patterns 2");
		System.out.println(gsp.candidates);
		System.out.println(gsp.patterns);

		System.out.println("gsp.toString");
		System.out.printf(gsp.toString());

		System.out.println("GSP2");
		Gsp gsp2 = new Gsp("testeSimulated", 0.25);
		gsp2.execute();
		System.out.printf(gsp2.toString());
*/
	}
}
