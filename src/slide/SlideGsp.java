/**
 * @brief Adding a windows search on GSP algorithm.
 * 
 * 		  This class implements an extention to GSP including a Slide Windows.
 *        It's makes found more patterns because the classic implentation, when
 *        check a support value, looks for patterns that happend one before the
 *        other.
 * 
 *        With this chance we can look for patterns which happend same truples
 *        after. The namber of tuples after is showed by the windows size.
 *        Although, we still respecting sort of eventes.
 * 
 *        Look the exemplo below:
 *         
 *        1 - i1 i2
 *         
 *        2 - i3
 *         
 *        3 - i4 i5
 * 
 *        GSP classic: { < i1 i3 > < i2 i3 > < (i1 i2) i3 > < i3 i4 > < i3 i5 > < i3 (i4
 *        i5) > ... }
 * 
 *        GSP new (w=2): { < i1 i3 > < i1 i4 > < i1 i5 > < i1 (i4 i5) > < i2 i3 > < i2 i4 >
 *        < i2 i5 > < i2 (i4 i5) > < (i1 i2) i3 > < (i1 i2) i4 > < (i1 i2) i5 > < (i1 i2)
 *        (i4 i5) > < i3 i4 > < i3 i5 > < i3 (i4 i5) > ... }
 * 
 *        It can look redundant however it makes patterns with bigger support
 *        and found same patterns very interested applicantion. Imagine an base
 *        which i1 x y i2 i3 i4 is a patterns on database and x, y are two
 *        random elements this implementation try to solve this problem.
 */
package slide;

import java.util.Vector;

import useful.Item;
import useful.Pattern;

import gsp.Gsp;
import gsp.SequentialPatterns;

/**
 * @brief Extended GSP to use windows search.
 * 
 *        To add this function we extended Gsp class and overwrite the attribute
 *        patterns putting an new vector of SlideSequentialPatterns. Basic it
 *        was the big change because SlideSequentialPatterns makes the hard work
 *        when update traps vector.
 * 
 *        This class sets windows slide, get path of database file and support
 *        minimum.
 * 
 *        There're same method which needed to be overwritten to use the new
 *        vector of patterns like generation and execute. Whatever the biggest
 *        part of the work is made by Gsp class.
 * 
 * @see Gsp, SlideSequentialPatterns
 * @author Carlos Roberto Silveira Junior
 * @date 2011.09.02
 */
public class SlideGsp extends Gsp {

	/**
	 * Value of windows search.
	 */
	private int windowSize;

	/**
	 * It's the karnel of this algoritmo.
	 * 
	 * It combine all itemset one by one and test to see if they have the
	 * support minimum necessary.
	 * 
	 * It is the same method of GSP, although is necessary to use new patterns.
	 */
	private void generation() {

        SlideSequentialPatterns sp;
        Vector<SlideSequentialPatterns> temp = new Vector<SlideSequentialPatterns>();
        
        /* with the first interation of this method */
        /* creat patterns with lenght one */
        if (super.patterns.isEmpty())
			for (int i = 0; i < super.getCandidates().size(); ++i) {
				super.patterns.add(new SlideSequentialPatterns(this.windowSize));
				super.patterns.elementAt(i)
						.addItem(super.getCandidates().elementAt(i));
			}

		/* combine all itemset and creat all pattern frequent */
		for (int i = 0; i < super.patterns.size(); ++i) {
			temp.clear();
			
			for (int j = 0; j < super.getCandidates().size(); ++j) {
				sp = new SlideSequentialPatterns((SlideSequentialPatterns) super.patterns.elementAt(i));

				/* check the minimum support */
				if ((double) sp.addItem(super.getCandidates().elementAt(j))
						/ (double) super.getNumberTuples() >= super.getMinSup())
					temp.add(sp);
			}
			
			/*check if there are items generated*/
			if (!temp.isEmpty()) {
				/* looking and removing subPatterns */
				for (int j = 0; j < super.patterns.size(); ++j)
					for (int k = 0; k < temp.size() && !super.patterns.isEmpty(); ++k)
						if (temp.elementAt(k).subPattern((SequentialPatterns) super.patterns.elementAt(j))) {
							super.patterns.removeElementAt(j--);
							
							if (j < i)
								--i;
							
							if (j < 0)
								j = 0;
							if (i < 0)
								i = 0;
						}
				
				super.patterns.addAll(temp);
			}
		} 
	}

	/**
	 * It's used to manipulate patterns vector.
	 * 
	 * @return Patterns vector
	 */
	protected Vector<Pattern> getPatterns() {
		return super.patterns;
	}
	
	/**
	 * Constructor with two argumentes.
	 * 
	 * It sets the size of windows and it needs to be great than 1 else the
	 * value will be 1.
	 * 
	 * @param bd
	 *            Path or name of database
	 * @param size
	 *            Size of windows search.
	 */
	public SlideGsp(String bd, int size) {
		super(bd);
		this.windowSize = size > 0 ? size : 1;
	}

	/**
	 * Constructor with tree argumentes.
	 * 
	 * This method sets minumum support and calls the other contructor.
	 * 
	 * @param databaseName
	 *            Name and/or path of database
	 * @param minSup
	 *            Support minimum value
	 * @param windows
	 *            The size of the window (great than 1)
	 */
	public SlideGsp(String databaseName, double minSup, int windows) {

		this(databaseName, windows);
		super.setMinSup(minSup);
	}

	/**
	 * This method works as an interface which will make the processing.
	 * 
	 * First, it remove all non-frequent candidates, than all itemset are
	 * created, after that it creat all candidate and test.
	 * 
	 * It is the same method of GSP, but it is necessary because we need to
	 * indicate to use the new generation.
	 */
	public void execute() {

		/* removing non-friquent candidates */
		super.removingNonfrequentCandidates();

		/* geration itemsets and test */
		super.growUpItemset();

		/* generation candidates and test */
		this.generation();
	}
	
	/**
	 * Change the value of the windows cheching if value is great or equal one
	 * 
	 * @param windows
	 *            New windows size value
	 */
	protected void setWindowSize(int windows) {
		this.windowSize = windows >= 1 ? windows : 1;
	}

	/**
	 * Return the size of the search windows.
	 * 
	 * @return The size of the windows
	 */
	public int getWindowSize() {
		return this.windowSize;
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
					+ this.patterns.elementAt(i).numberOfOccorrence()
					/ (double) super.getNumberTuples() + "\n";

		return s;
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Ununseful
	 */
	public static void main(String[] args) {
		
		// real test 
		SlideGsp gsp = new SlideGsp(args[0],new Double(args[1]), new Integer(args[2]));
		
		gsp.execute();
		
		System.out.println(args[0] + " support " + args[1] + " with size of windows in " + args[2]);
		System.out.println(gsp);

/*		// developping test
		SlideGsp gsp = new SlideGsp("testeSimulated", 3);

		System.out.println("All candidates in database");
		System.out.println(gsp.getCandidates());

		gsp.setMinSup(0.2);
		gsp.removingNonfrequentCandidates();
		System.out
				.println("All frequente candidates in database and patterns.");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.patterns);

		gsp.growUpItemset();
		System.out.println("Expended itemset");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.patterns);

		
		gsp.generation();
		System.out.println("Found patterns");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.patterns);
		
		gsp.generation();
		System.out.println("Found patterns");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.patterns);

		System.out.printf(gsp.toString());

		System.out.println("GSP2");
		SlideGsp gsp2 = new SlideGsp("testeSimulated", .2, 3);
		gsp2.execute();
		System.out.printf(gsp2.toString());
*/  
	}

}
