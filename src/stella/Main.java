/**
 * @brief Stella's package
 * 
 * This is the main package of this project. This packages controls the 
 * others and it is used to run the algorithm
 */
package stella;

import postprocessing.Generalizer;
import slide.SlideGsp;
import useful.GSP;
import gsp.Gsp;
import incremental.IncrementalGsp;

/**
 * @mainpage Stella Algorithm
 * 
 * The Stella Algorithm is an algorithm to mine very large and incremental
 * databases. It tries to find sequential patterns in databases and present
 * this patterns in a way that users can understand. To find this patterns,
 * this algorithm uses generation of candidates and test this the same way
 * that GSP made.
 * 
 * The data proposed to be mined is geo-reference from watershed of Feijao.
 * This data is sorted by the time theses happened and location. For that,
 * Stella uses an sliding windows controlled by user, it is used to allow
 * gaps between itemset in sequences. This base is incremental for this 
 * Stella is incremental this strategy avoid unnecessary processing. After 
 * it has been found all patterns they pass by a post-processing to generalize 
 * they, it will summarize them.  
 * 
 * @image html stella.png
 * 
 * Nowadays, Stella has been renamed to IncMSTS-PP; Incremental Miner of 
 * Stretchy Time Sequences with Post-Processing. This renomiation was 
 * necessary to put more semantical to the name in te articles and monografic.
 * That way, IncMSTS-PP was divided in MSTS that evolves to IncMSTS that 
 * evolves to IncMSTS-PP. 
 * 
 * 
 * @section Detailed
 * 
 * 	Stella is projected to find patterns in very large databases. The domain
 * 	show important to allow at the occurrence of the patterns present gaps
 * 	between their itemsets. That is important because Stella was projected to
 * 	mine natural domain and this kind of domain can present errors, missing
 * 	data and same events can result in a later event influenced by other
 * 	variance that was not measured.
 * 
 * 	This algorithm use generation of candidates and test strategy. It means
 * 	that the algorithm firstly find the itemsets frequent on the database.
 * 	After that, it combine all itemset greasing the sequence with size equal
 * 	two (two itemsets). The next step, the 2-sequences are tested to see
 * 	which one is frequent or not. The non-frequent ones are deleted based on
 * 	anti-monotonic property. The frequent 2-sequences are, again, combined to
 * 	create the 3-sequence (sequence with size equal three itemsets). These are
 * 	tested again and this two steps continues until it is impossible to 
 * 	create a bigger sequence.
 * 
 *  At the second step described, testing a sequence to see if it frequent,
 *  the Time Search Windows propose an alteration. Stella allow at the user
 *  say how many tuples that happen after an occurrence of a pattern can be
 *  looked for find a new itemset.  
 * 
 * 	When a pattern p2 is composed by another frequent pattern p1 added at 
 * 	the and an itemset i. When Stella goes to check is p2 happens, Stella
 * 	will look for all occurrence of p1, after that it will see if i happens
 * 	after the occurrence. If it happens the number of occurrence is 
 * 	incremented. The Time Search Windows say to Stella how many tuples the
 * 	algorithm can see to try to find i. The classic implementation of GSP
 * 	looks just one tuple. Stella can see N tuples (parameter set by
 * 	the user).
 * 
 * 	There are some problems with Time Search Windows, the performance fall
 * 	down. On the other hand, the size of the window doesn't not has to much
 * 	relevance to the performance. Other problem happens because it a dynamic
 * 	search, so we cannot invade the search space of other occurrence; to not
 * 	count twice the same itemset. The occurrence of the pattern has at maximum
 * 	N space between item. And this strategy returns more sequences than
 *  classic GSP.
 * 
 * 	To improve the performance we using an incremental strategy; Semi-Frequent
 * 	Item Strategy. This strategy need to set a parameter delta which say how
 * 	closer the support of a non-frequent pattern should be of the value of
 * 	the minimum support to be considerer semi-frequent. This strategy keeps
 * 	the semi-frequent because this patterns can become frequent with
 * 	increments at the database.
 * 
 * 	To implements this strategy we need to sort the patterns in semi-frequent
 *  and frequent, the frequent fellow the same way at the regure one. The
 *  semi-frequent is no generalized, it is just armazenated. At the end of
 *  the process we have the frequent patterns (to show to users) and the
 *  semi-frequent ones (which will be saved as well the frequent). The other
 *  iteration of the algorithm with the same database and an increment the
 *  algorithm we look at just the increment because the knowledge of the 
 *  database was extracted. That old knowledge just need to be update. This
 *  strategy proved be very useful to take care about the performance issues.
 * 
 * 	The problem of return more sequences can be solved by the post-processing.
 *  Our approach use the knowledge in the ontologies to generalized sequences
 *  found. This sequences becomes more semantically and in few number. To use
 *  the generalization is important to group the sequences and mining by 
 *  semelhance. How much semelhance is a sequence better is the chance to
 *  be generalized.
 *  
 *  As conclusion, this work is an algorithm to find sequential patterns
 *  respecting the time constraint. The algorithm is incremental to get
 *  better performance and apply ontology on post-processing to generalize
 *  the sequences. At next figure is possible to see the division by module
 *  of this algorithm.
 *  
 *  @image html diagrama.png
 *  
 * @section User's Manual
 * 
 * There is two ways to use IncMSTS-PP: 
 * 
 * First time use:
 * 
 * $java Stella -s database minSup windows delta object ontology ontLocation
 * 
 * database: Path to the database. This database should be formated.
 * 
 * minSup: minimum support valeu. Between 0 and 1.
 * 
 * windows: size of the time search windows. Integer bigger than 1.
 * 
 * delta: value that say how closer the semi-frequents are of minSup.
 * 
 * object: path to a not existent object that keeps the information of the processing.
 * 
 * ontology: the name of the ontology.
 * 
 * ontLocation: the path of the ontology.
 * 
 * 
 * Or\nSecond time:
 * 
 * $java Stella -s increment old ontology ontLocation
 * 
 * increment: incremental of the database, secound iteration.");
 * 
 * old: information of the old iteration (old object).
 * 
 * 
 * To use MSTS (just the method Stretchy Time Windows):
 * 
 * $java Stella -w database minSup windows
 * 
 * 
 * To use IncMSTS (the method and the incremental model):
 * 
 * $java Stella -i database minSup windows delta object
 * 
 * If it's the very first time, else: 
 * 
 * $java Stella -i increment old
 * 
 * If you want to use the implementation of the GSP (MSTS 
 * without the method SWT)
 * 
 * $java Stella -g database minSup
 * 
 * 
 * To print the help:
 * 
 * $java Stella --help
 * 
 * 
 * 
 * @author Carlos Roberto Silveira Junior
 * @data 2012
 */

/**
 * @brief It is the most important class. It makes the interface between
 *        Incremental Module e the Post-Processing Module.
 * 
 *        This class implements the Stella Algorithm. The whole processing is at
 *        the subclass. This class makes an interface between Incremental Module
 *        (Responsible to find patterns at the incremental way) and the
 *        Post-Processing Module (resposible to generalize the found patterns
 *        using the knowledge in the ontology). Actually, the algorithm doesn't
 *        use an ontology, it can be a taxonomy because the important thing is
 *        the relation between an instance and their father. Another important
 *        thing is the Time Windows Search implemented at Slide Module, it
 *        allows at finding patterns with gaps between their occurrences. It is
 *        useful to our domain because with that we can find bigger and stronger
 *        patterns than classic GSP. The classic GSP is implemented at Gsp
 *        Module. All modules which find patterns doesn't shows the subpatterns
 *        excepted the case of Incremental one, because if a subpatterns became
 *        strong it is important to show.
 * 
 *        This class has two constructor to run the Stella algorithm the other
 *        is to run tests. Test with just GSP, just Sliding GSP or just
 *        Incremental GSP. The Stella is the incremental and the post-processing.
 *        The method main present a console interface to use the Stella.
 * 
 * @author Carlos Roberto Silveira Junior
 * @see IncrematalGsp
 * @see Generalizer
 */
public class Main {

	/**
	 * Object to use the Incremental Module. Module to find the patterns.
	 */
	private IncrementalGsp incr;

	/**
	 * Object use the post-processing with ontology.
	 */
	private Generalizer generalizer;

	/**
	 * Keep the result of processing.
	 */
	private String result;
	
	/**
	 * Show time to the user if it's true;
	 */
	private final boolean debug = true;

	/**
	 * Constructor used to the first time that Stella run at a database. To use
	 * this constructor is necessary to set a hack o parameter to configure the
	 * Stella Algorithm at all level. After that, the result can be seen at
	 * results.
	 * 
	 * @param dataPath
	 *            Path of the database
	 * @param minSup
	 *            Value of minimum support
	 * @param windowsSize
	 *            Size of time search window
	 * @param deltaValue
	 *            Value of delta parameter
	 * @param outPath
	 *            Path to the object with the processing information
	 * @param ontologyName
	 *            Name of the ontology
	 * @param ontologyLocation
	 *            Path of the ontology xml
	 */
	public Main(String dataPath, double minSup, int windowsSize,
			double deltaValue, String outPath, String ontologyName,
			String ontologyLocation) {

		long startTime = 0;
		
		if (this.debug)
			startTime = System.currentTimeMillis();
		
		this.incr = new IncrementalGsp(dataPath, minSup, windowsSize,
				deltaValue, outPath);

		this.generalizer = new Generalizer(ontologyName, ontologyLocation);

		String temp = this.incr.exec();
		
		this.generalizer.setPatterns(temp);
		
		if (this.debug) {
			System.out.println("Result without generalization:");
			System.out.println(this.generalizer.toString());
			System.out.println("Time taken to find patterns: ");
			System.out.print(System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();
			System.out.println("\n\n");
		}

		this.generalizer.exec();

		this.result = new String(this.generalizer.toString());

		this.incr = null;
		this.generalizer = null;
		
		if (this.debug) {
			System.out.println("Time taken to generalized the patterns: ");
			System.out.print(System.currentTimeMillis() - startTime);
			System.out.println();
		}
	}

	/**
	 * This constructor is used to execute Stella to a database that has an
	 * increment. because of that, the implementation has saved a file with
	 * some information whose can be used to not represses the whole database.
	 * This file will be changed to another one with the information of this
	 * processing. The information about the ontology can be alternated, by that
	 * this algorithm needs the whole information again. The parameter of the
	 * data mining
	 * 
	 * @param dataPath
	 *            Path of the incremental of the database
	 * @param outPath
	 *            Path of the old processing information
	 * @param ontologyName
	 *            Name of the ontology
	 * @param ontologyLocation
	 *            Path of the ontology xml
	 */
	public Main(String dataPath, String outPath, String ontologyName,
			String ontologyLocation) {

		long startTime = 0;
		
		if (this.debug)
			startTime = System.currentTimeMillis();
		
		this.incr = new IncrementalGsp(dataPath, outPath);

		this.generalizer = new Generalizer(ontologyName, ontologyLocation);

		this.generalizer.setPatterns(this.incr.exec());

		if (this.debug) {
			System.out.println("Result without generalization:");
			System.out.println(this.generalizer.toString());
			System.out.println("Time taken to find patterns: ");
			System.out.print(System.currentTimeMillis() - startTime);
			startTime = System.currentTimeMillis();
			System.out.println("\n\n");
		}
		
		this.generalizer.exec();

		this.result = new String(this.generalizer.toString());

		this.incr = null;
		this.generalizer = null;
		
		if (this.debug) {
			System.out.println("Time taken to generalized the patterns: ");
			System.out.print(System.currentTimeMillis() - startTime);
			System.out.println();
		}
	}

	/**
	 * Constructor to use just the Gsp Module. It works as the GSP algorithm
	 * without modification.
	 * 
	 * @param dataPath
	 *            Path of the database
	 * @param minSup
	 *            Value of minimum support
	 */
	public Main(String dataPath, double minSup) {
		
		long startTime = 0;
		if (this.debug)
			startTime = System.currentTimeMillis();
		
		GSP alg = new Gsp(dataPath, minSup);

		alg.execute();

		this.result = new String(alg.toString());

		if (this.debug) {
			System.out.println("Time taken the GSP patterns: ");
			System.out.print(System.currentTimeMillis() - startTime);
			System.out.println();
		}
	}

	/**
	 * Constructor to use just the Sliding Module. This module implements Time
	 * Search Window which is used to find patterns with gaps.
	 * 
	 * @param dataPath
	 *            Path of the database
	 * @param minSup
	 *            Value of minimum support
	 * @param windows
	 *            Size of time search window
	 */
	public Main(String dataPath, double minSup, int windows) {

		long startTime = 0;
		if (this.debug)
			startTime = System.currentTimeMillis();		

		GSP alg = new SlideGsp(dataPath, minSup, windows);

		alg.execute();

		this.result = new String(alg.toString());
		

		if (this.debug) {
			System.out.println("Time taken the MSTS's pattern: ");
			System.out.print(System.currentTimeMillis() - startTime);
			System.out.println();
		}
	}

	/**
	 * Constructor to use just the Incremental Module. This module has two ways
	 * to processing it's dictated by the input. With minSup and windows have
	 * zero as value, the method will use the second way to process. That is
	 * used when you ya made a execution and have the information about that
	 * execution (incrementing of the database). With one of this value, the
	 * algorithm will understand that is the first time this database is
	 * processed.
	 * 
	 * @param dataPath
	 *            Path to the database or to the increment
	 * @param minSup
	 *            Value of minimum support or zero if it is an increment
	 * @param windows
	 *            Value of the size of time search windows or zero if it is
	 *            increment
	 * @param delta
	 *            Value of delta parameter
	 * @param out
	 *            Path to the object with the processing information
	 */
	public Main(String dataPath, double minSup, int windows, double delta,
			String out) {
		
		long startTime = 0;
		if (this.debug)
			startTime = System.currentTimeMillis();

		if (minSup == 0.0 && windows == 0)
			this.incr = new IncrementalGsp(dataPath, out);
		else
			this.incr = new IncrementalGsp(dataPath, minSup, windows, delta,
					out);

		this.result = new String(this.incr.exec());
		
		if (this.debug) {
			System.out.println("Time taken the IncMSTS's pattern: ");
			System.out.print(System.currentTimeMillis() - startTime);
			System.out.println();
		}
	}

	/**
	 * String with the result.
	 * 
	 * @return The result of the processing
	 */
	public String getResult() {
		return this.result;
	}

	/**
	 * It has the interface to execute Stella Algorithm. This method provides a
	 * console interface to select how execute Stella (or any module of it). To
	 * help menu we show just the option -s, that is to use the whole algorithm
	 * like as proposed. But to test, it is important to use the other modules.
	 * because of that, there are -g to use just GSP, -w to use GSP with window,
	 * -i to incremental GSP with window (that has to ways to execute). The
	 * difference between -i and -s is the post-processing with ontology. The
	 * options needs diferents configurations to work.
	 * 
	 * @param args
	 *            String of configuration
	 */
	public static void main(String[] args) {

		Main stella = null;

		boolean error = false;

		// error by does not have parameter.
		if (args.length < 1)
			System.out
					.println("Use: \"java Stella --help\" to print the help.");
		else {

			// allowing at using any module interface.
			switch (args[0].charAt(1)) {

			case 's':
				if (args.length == 5)
					stella = new Main(args[1], args[2], args[3], args[4]);
				else if (args.length == 8)
					stella = new Main(args[1],
							(new Double(args[2])).doubleValue(), (new Integer(
									args[3])).intValue(),
							(new Double(args[4])).doubleValue(), args[5],
							args[6], args[7]);
				else
					error = true;

				break;

			case 'g':
				if (args.length == 3)
					stella = new Main(args[1],
							(new Double(args[2])).doubleValue());
				else
					error = true;

				break;

			case 'w':
				if (args.length == 4)
					stella = new Main(args[1],
							(new Double(args[2])).doubleValue(), (new Integer(
									args[3])).intValue());
				else
					error = true;
				break;

			case 'i':
				if (args.length == 3)
					stella = new Main(args[1], 0.0, 0, 0.0, args[2]);
				else if (args.length == 6)
					stella = new Main(args[1],
							(new Double(args[2])).doubleValue(), (new Integer(
									args[3])).intValue(),
							(new Double(args[4])).doubleValue(), args[5]);
				else
					error = true;

				break;

			default:
				error = true;
			}

			// printing help menu
			if (args[0].equalsIgnoreCase("-h")
					|| args[0].equalsIgnoreCase("--help") || error) {
				System.out
						.println("Thanks to use Stella. You must use this implementation in two ways:\n");

				System.out.println("First time use:");
				System.out
						.println("\t $java Stella -s database minSup windows delta object ontology ontLocation\n");
				System.out
						.println("\t \t database: Path to the database. This database should be formated.");
				System.out
						.println("\t \t minSup: minimum support valeu. Between 0 and 1.");
				System.out
						.println("\t \t windows: size of the time search windows. Integer bigger than 1.");
				System.out
						.println("\t \t delta: value that say how closer the semi-frequents are of minSup.");
				System.out
						.println("\t \t object: path to a not existent object that keeps the information of the processing.");
				System.out.println("\t \t ontology: the name of the ontology.");
				System.out
						.println("\t \t ontLocation: the path of the ontology.\n");

				System.out.println("Or\nSecond time:");
				System.out
						.println("\t $java Stella -s increment old ontology ontLocation\n ");
				System.out
						.println("\t \t increment: incremental of the database, secound iteration.");
				System.out
						.println("\t \t old: information of the old iteration (old object).");
				System.out.println("\t \t ontology: the name of the ontology.");
				System.out
						.println("\t \t ontLocation: the path of the ontology.\n");

				System.out
						.println("And\n\t$java Stella --help prints the help.");
			}

			// printing result if it doesn't have error
			if (!error)
				System.out.print(stella.getResult());
		}

	}
}
