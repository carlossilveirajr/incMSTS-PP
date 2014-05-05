package incremental;

import gsp.SequentialPatterns;

import java.util.Vector;

import slide.SlideSequentialPatterns;
import useful.PatternReader;
import useful.PatternWriter;

/**
 * @brief This class implements other iteration of the algorithm on the same
 *        database.
 * 
 *        This class implements the second... times this algorithm run to the
 *        same database with an incremental. All the set came from headline of
 *        the input file, support minimum, delta value, windows search size...
 *        After cat this information, the old knowledge is catch from the same
 *        file. The next step is sweet the the incremental to find all item
 *        there. The old patterns are reconstructed using the new occurrence, it
 *        updates the count of traps. After that, it makes an processes on the
 *        new items to find the new patterns. With all new pattern the old one
 *        is up dating and divided in frequency and semi-frequency.
 * 
 * @author Carlos Roberto Silveira Junior
 * 
 * @see FirstProcessing
 */
public class OtherIteration extends FirstProcessing {

	/**
	 * Old database size.
	 */
	private int databaseSize;

	/**
	 * Old patterns.
	 */
	private Vector<OldPattern> oldKnowledge;

	/**
	 * Constructor extracts the old configuration and patterns. Set all that to
	 * the algorithm work. And also, sweet the new database finding all item.
	 * 
	 * @param newBase
	 *            Path of the incremental of the database
	 * @param oldPatterns
	 *            Path of old knowledge
	 */
	public OtherIteration(String newBase, String oldPatterns) {
		super(newBase, 1);

		PatternReader reader = new PatternReader(oldPatterns);

		super.setMinSup(reader.getMinSup());
		super.setWindowSize(reader.getWindowsSize());
		super.setDelta(reader.getDelta());
		super.setOutObject(oldPatterns);
		this.databaseSize = reader.getDatabaseSize();
		this.oldKnowledge = (Vector<OldPattern>) reader.getPatterns().clone();

		reader.closeFile();
	}

	/**
	 * This method combines all candidate itemset with the old pattern trying to
	 * creating a new one bigger.
	 * 
	 * @param old
	 *            Element to be generalized
	 */
	private void generation(OldPattern old) {

		SlideSequentialPatterns sp;
		Vector<SlideSequentialPatterns> temp = new Vector<SlideSequentialPatterns>();

		/* combine all itemset and create all pattern frequent */
		for (int i = super.patterns.size() - 1; i < super.patterns.size(); ++i) {
			temp.clear();

			for (int j = 0; j < super.getCandidates().size(); ++j) {
				sp = new SlideSequentialPatterns(old);

				double support = (double) sp.addItem(super.getCandidates()
						.elementAt(j)) / (double) super.getNumberTuples();
				if (support > super.getMinSup() * super.getDelta())
					if (support >= super.getMinSup())
						if (!super.patterns.contains(sp))
							temp.add(sp);
						else
							;
					else if (!super.getSemiFrequencies().contains(sp))
						super.getSemiFrequencies().add(sp);
			}

			/* check if there are items generated */
			if (!temp.isEmpty()) {
				/* looking and removing subPatterns */
				for (int j = 0; j < super.patterns.size(); ++j)
					for (int k = 0; k < temp.size() && !super.patterns.isEmpty(); ++k)
						if (temp.elementAt(k).subPattern(
								(SequentialPatterns) super.patterns
										.elementAt(j))) {
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
	
/*
_/I\_____________o______________o___/I\     l  * /    /_/ *   __  '     .* l
I"""_____________l______________l___"""I\   l     * //      _l__l_   . *.  l
 [__][__][(******)__][__](******)[__][] \l  l-\ ---//---*----(oo)----------l
 [][__][__(******)][__][_(******)_][__] l   l  \\ // ____ >-(    )-<    /  l
 [__][__][_l    l[__][__][l    l][__][] l   l \\)) ._****_.(......) .@@@:::l
 [][__][__]l   .l_][__][__]   .l__][__] l   l   ll  _(o_o)_        (@*_*@  l
 [__][__][/   <_)[__][__]/   <_)][__][] l   l   ll (  / \  )     /   / / ) l
 [][__][ /..,/][__][__][/..,/_][__][__] l   l  / \\  _\  \_   /     _\_\   l
 [__][__(__/][__][__][_(__/_][__][__][] l   l______________________________l
 [__][__]] l     ,  , .      [__][__][] l
 [][__][_] l   . i. '/ ,     [][__][__] l        /\**_/\       season's
 [__][__]] l  O .\ / /, O    [__][__][] l       ( o_o   )_)       greetings
_[][__][_] l__l======='=l____[][__][__] l_______,(u  u  ,),__________________
 [__][__]]/  /l\-------/l\   [__][__][]/       {}{}{}{}{}{}<R>

In Ellen's house it is warm and toasty while fuzzies play in the snow outside.
 */

	/**
	 * This method will up date the old knowledge considering the new one. It's
	 * starting look if an old pattern is frequency or semi-frequency to the new
	 * database, if it is it is removed off that condition to be re-analized and
	 * also, the information about the number of occurrence soffer an up date in
	 * this moment. After that all old pattern fellow the same way, they are
	 * sorting in semi-frequency and frequency. If it is frequency, the pattern
	 * can generation bigger pattern, so it is projected.
	 * 
	 * @see generation
	 */
	private void upDatingKnowledge() {

		Vector<Integer> frequence = new Vector<Integer>();
		Vector<Integer> semiFrequence = new Vector<Integer>();
		
		for (int i = 0; i < this.oldKnowledge.size(); ++i) {
			OldPattern o = oldKnowledge.elementAt(i);

			if (super.getPatterns().contains(o)) {
				int index = super.getPatterns().indexOf(o);

				o.incTraps(super.getPatterns().elementAt(index)
						.numberOfOccorrence());
				
				frequence.add(index);

			} else if (super.getSemiFrequencies().contains(o)) {
				int index = super.getSemiFrequencies().indexOf(o);

				o.incTraps(super.getSemiFrequencies().elementAt(index)
						.numberOfOccorrence());
				
				semiFrequence.add(index);
			}		
		}

		for (int i = frequence.size() - 1; i >= 0; --i)
			super.getPatterns().removeElementAt(frequence.elementAt(i));
		
		for (int i = semiFrequence.size() - 1; i >= 0; --i)
			super.getSemiFrequencies().removeElementAt(semiFrequence.elementAt(i));
		
		/* sorting between frequency and semi-frequency */
		for (int i = 0; i < this.oldKnowledge.size(); ++i) {
			OldPattern o = oldKnowledge.elementAt(i);		
			
			if (o.numberOfOccorrence() / (double) super.getNumberTuples() >= super
					.getMinSup() * super.getDelta())
				if (o.numberOfOccorrence() / (double) super.getNumberTuples() >= super
						.getMinSup()) {

					super.getPatterns().add(o);

					this.generation(o);
				} else
					super.getSemiFrequencies().add(o);
		}
	}

	/**
	 * It kind of hard to describe, first of all the old candidate are mounted
	 * using the new information, the new database.
	 * 
	 * After that the algorithm processes at the same way, if all frequent
	 * candidates, creating the new itemset and production the new patterns.
	 * 
	 * After this moment, the old knowledge are up dating. The patterns are
	 * recreated with the new database, so it is possible to process this
	 * combining with the new frequents itemset.
	 * 
	 * After that it will see with the candidates created are frequent or
	 * semi-frequent like it was made in the past.
	 */
	public void execute() {

		super.growUpItemset();
		
		/* 0- mounting old patterns with new sets */
		for (int i = 0; i < this.oldKnowledge.size(); ++i)
			this.oldKnowledge.elementAt(i).creatingNewPattern(
					super.getCandidates());

		/* 1- incremental gsp on new item database */
		super.removingNonfrequentCandidates();
		super.generation();

		/* 2- updating old knowledge */
		super.addTupleNumber(this.databaseSize); // keeping consistency
		this.upDatingKnowledge();

		/* writing the result */ 
		PatternWriter pw = new PatternWriter(super.getOutObject(),
				super.getNumberTuples(), super.getMinSup(),
				super.getWindowSize(), super.getDelta());

		pw.write(super.getPatterns());
		pw.write(super.getSemiFrequencies());

		pw.close();
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Not used.
	 */
	public static void main(String[] args) {

		OtherIteration oi = new OtherIteration("databaseSimulate02",
				"outputTesteSimulated");

		oi.execute();
		System.out.println(oi.oldKnowledge);
		System.out.println(oi.getCandidates());
		System.out.println(oi.getPatterns());
		
		
		System.out.print(oi);
		
		System.out.println(oi.oldKnowledge);
		System.out.println(oi.getCandidates());
		System.out.println(oi.getPatterns());
		System.out.println(oi.getSemiFrequencies());
	}

}
