package incremental;

import gsp.SequentialPatterns;

import java.util.Vector;

import slide.SlideGsp;
import slide.SlideSequentialPatterns;
import useful.Itemset;
import useful.Pattern;
import useful.PatternWriter;

/**
 * @classe FirstProcessing
 * 
 * @brief This class implements incremental function on SlideGSP to first
 *        processing of the database.
 * 
 *        This class processes the database in the first time. It will run as
 *        the SlideGsp, the only diferences are (i) it sorts the itemsets in two
 *        classes, frequencies and semi-frequencies; (ii) the output is composed
 *        by the patterns and a file.
 * 
 *        The file has a headline and all frequent and semi-frequent patterns.
 *        It will be used to the other reprocesses.
 * 
 * @author Carlos Roberto Silveira Junior
 * @see SlideGsp, PatternWriter
 */
public class FirstProcessing extends SlideGsp {

	/**
	 * Value of delta to select the semi-frequencies
	 */
	private double delta;

	/**
	 * Vector with semi-frequencies patterns
	 */
	private Vector<Pattern> semiFrequencies;

	/**
	 * Name of the object to be created with the processing information
	 */
	private String outObject;

	/**
	 * Generation with creat the patterns. It starts creating 1-sequencies after
	 * the algorithm combines the 1-sequencies making the 2-sequencies this will
	 * be checked sorting the sequencies in frequencies and semi-frequencies.
	 * The frequencies will combine with the 1-sequencies and check agian til it
	 * is impossible to make bigger sequencies.
	 * 
	 * Pay attention: the frequencies will be processed again, the
	 * semi-frequencies stay on semi-frequencies pattern and the non-frequencies
	 * are lost.
	 */
	protected void generation() {

		SlideSequentialPatterns sp;
		Vector<SlideSequentialPatterns> temp = new Vector<SlideSequentialPatterns>();

		/* with the first interation of this method */
		/* creat patterns with lenght one */
		if (super.patterns.isEmpty())
			for (int i = 0; i < super.getCandidates().size(); ++i) {
				super.patterns.add(new SlideSequentialPatterns(super
						.getWindowSize()));
				super.patterns.elementAt(i).addItem(
						super.getCandidates().elementAt(i));
			}

		/* combine all itemset and creat all pattern frequent */
		for (int i = 0; i < super.patterns.size(); ++i) {
			temp.clear();

			for (int j = 0; j < super.getCandidates().size(); ++j) {
				sp = new SlideSequentialPatterns(
						(SlideSequentialPatterns) super.patterns.elementAt(i));

				/* check the minimum support */
				double support = (double) sp.addItem(super.getCandidates()
						.elementAt(j)) / (double) super.getNumberTuples();
				if (support > super.getMinSup() * this.delta)
					if (support >= super.getMinSup())
						if (!super.patterns.contains(sp))
							temp.add(sp);
						else
							;
					else if (!this.semiFrequencies.contains(sp))
						this.semiFrequencies.add(sp);
			}

			if (!temp.isEmpty()) {
				/* looking and removing subPatterns */
				for (int j = 0; j < super.patterns.size(); ++j)
					for (int k = 0; k < temp.size(); ++k)
						if (temp.elementAt(k).subPattern(
								(SequentialPatterns) super.patterns
										.elementAt(j))) {
							this.semiFrequencies.add(super.patterns.remove(j--));

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
	 * An non-frequent candidate is all item which does not have the support
	 * necessare.
	 * 
	 * The support is how much tuple has the candidate divided per number of
	 * tuple on database.
	 */
	protected void removingNonfrequentCandidates() {

		double sup, minSup = super.getMinSup();
		for (int i = 0; i < super.getCandidates().size(); ++i) {
			
			sup = (double) super.getCandidates().elementAt(i).traps().size()
					/ (double) super.getNumberTuples();

			if (sup < minSup * this.delta)
				super.getCandidates().remove(i--);
			else if (sup < minSup)
				this.semiFrequencies.add(new SlideSequentialPatterns(super
						.getCandidates().remove(i--), super.getWindowSize()));
		}
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

		final int limitOne = super.getCandidates().size();
		int start = 0, end = limitOne;
		 
		
		while(start != end) {
			Itemset cand;
			
			for (; start < end; ++start) 
				for (int j = 0; j < limitOne; ++j)
					if (start != j) {
						/*creating new itemset*/
						cand = new Itemset(super.getCandidates().elementAt(start));
						cand.addItem(super.getCandidates().elementAt(j));
						
						/*looking the new item suport*/
						double support = cand.traps().size() / (double)super.getNumberTuples();
						if (support >= super.getMinSup() * this.delta && support != 0)
							if (support >= super.getMinSup())
								if (!super.getCandidates().contains(cand))
									super.getCandidates().add(cand);
								else;
							else
								this.semiFrequencies.add(new SlideSequentialPatterns(cand, super.getWindowSize()));
					}
			end = super.getCandidates().size();
		}
	}

/*
                         ___====-_  _-====___
                  _--~~~#####// '  ` \\#####~~~--_
                -~##########// (    ) \\##########~-_
               -############//  |\^^/|  \\############-
             _~############//   (O||O)   \\############~_ 
            ~#############((     \\//     ))#############~  
           -###############\\    (oo)    //###############-
          -#################\\  / `' \  //#################- 
         -###################\\/  ()  \//###################-
        _#/|##########/\######(  (())  )######/\##########|\#_
        |/ |#/\#/\#/\/  \#/\##|  \()/  |##/\#/  \/\#/\#/\#| \|
        `  |/  V  V  `   V  )||  |()|  ||(  V   '  V /\  \|  '
           `   `  `      `  / |  |()|  | \  '      '<||>  '
                           (  |  |()|  |  )\        /|/
                          __\ |__|()|__| /__\______/|/
                         (vvv(vvvv)(vvvv)vvv)______|/
*/
	
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
	public FirstProcessing(String bd, int size) {

		super(bd, size);

		this.semiFrequencies = new Vector<Pattern>();
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
	public FirstProcessing(String databaseName, double minSup, int windows) {

		super(databaseName, minSup, windows);

		this.semiFrequencies = new Vector<Pattern>();
	}

	/**
	 * Constructor with five argumentes.
	 * 
	 * This method sets minumum support and calls the other contructor.
	 * 
	 * @param databaseName
	 *            Name and/or path of database
	 * @param minSup
	 *            Support minimum value
	 * @param windows
	 *            The size of the window (great than 1)
	 * @param delta
	 *            Variance to consider semi-frequencies patterns
	 * @param outObject
	 *            File which will keep the information about our patterns
	 */
	public FirstProcessing(String databaseName, double minSup, int windows,
			double delta, String outObject) {

		super(databaseName, minSup, windows);

		this.delta = (delta > 0 && delta < 1) ? delta : 0;
		this.semiFrequencies = new Vector<Pattern>();
		this.outObject = outObject;
	}

	/**
	 * This method is the core of the class. It is started removing the non
	 * frequent itens and sorting the semi-frequencies. After that, it makes the
	 * itemsets based on frequencies items. With that the generation are made
	 * creating the patterns. These steps are almost the same of Slide GSP. Now,
	 * it makes the output file with the information of this process.
	 */
	public void execute() {

		/* removing non-friquent candidates */
		this.removingNonfrequentCandidates();

		/* geration itemsets and test */
		this.growUpItemset();

		/* generation candidates and test */
		this.generation();

		/* writing the output object */
		PatternWriter pw = new PatternWriter(this.outObject,
				super.getNumberTuples(), super.getMinSup(),
				super.getWindowSize(), this.delta);

		pw.write(super.getPatterns());
		pw.write(this.semiFrequencies);

		pw.close();
	}

	/**
	 * Return the delta value.
	 * 
	 * @return The delta
	 */
	public double getDelta() {
		return delta;
	}

	/**
	 * Change the delta value.
	 * 
	 * @param delta
	 *            the delta to set
	 */
	protected void setDelta(double delta) {
		this.delta = delta;
	}

	/**
	 * Return the semi frequency set
	 * 
	 * @return the semiFrequencies
	 */
	public Vector<Pattern> getSemiFrequencies() {
		return semiFrequencies;
	}

	/**
	 * Return the name of the object output.
	 * 
	 * @return the outObject
	 */
	public String getOutObject() {
		return outObject;
	}

	/**
	 * Change the name of the object output.
	 * 
	 * @param outObject
	 *            the outObject to set
	 */
	public void setOutObject(String outObject) {
		this.outObject = outObject;
	}

	/*
	 * Failure is not an option- it comes bundled with your Microsoft product.
	 */
	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(String[] args) {
		FirstProcessing gsp = new FirstProcessing("databaseSimulate01", .2, 2, .1,
				"outputTesteSimulated");

		System.out.println("All candidates in database");
		System.out.println(gsp.getCandidates());

		gsp.setMinSup(0.2);
		gsp.removingNonfrequentCandidates();
		System.out
				.println("All frequente candidates in database and patterns.");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.getSemiFrequencies());
		System.out.println(gsp.patterns);

		gsp.growUpItemset();
		System.out.println("Expended itemset");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.getSemiFrequencies());
		System.out.println(gsp.patterns);

		gsp.generation();
		System.out.println("Found patterns");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.getSemiFrequencies());
		System.out.println(gsp.patterns);

		gsp.generation();
		System.out.println("Found patterns");
		System.out.println(gsp.getCandidates());
		System.out.println(gsp.getSemiFrequencies());
		System.out.println(gsp.patterns);

		System.out.printf(gsp.toString());

		gsp = new FirstProcessing("databaseSimulate01", .2, 2, .5,
				"outputTesteSimulated");
		gsp.execute();
		System.out.println("GSP2");
		System.out.println(gsp.getSemiFrequencies());
		System.out.print(gsp);

	}

}
