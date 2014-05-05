/**
 * @package incremental
 * 
 * @brief Implements the incremental model
 * 
 * This package implements the incremental model, it used to avoid 
 * reprocessing whole database with a processing was made.
 */
package incremental;

import useful.GSP;

/**
 * @classe IncrementalGsp
 * 
 * @brief This class implements incremental function on SlideGSP.
 * 
 *        This class is the main class of the incremetal modele. This class see
 *        if this the first time of the algorithm is run to a database; if it
 *        is, this will use FirstProcessing class else this will use
 *        OtherIteration class.
 * 
 *        Basicaly this class calls the other, it works as an interface betwean
 *        other modele and this modele.
 * 
 * @author Carlos Roberto Silveira Junior
 * 
 * @see FirstProcessing
 * @see OtherIteration
 */
public class IncrementalGsp {

	/**
	 * Interface to run the algorithm.
	 */
	private GSP incGsp;

	/**
	 * This constructor is used if it is the first time that the database is
	 * processed.
	 * 
	 * @param database
	 *            The path of the database
	 * @param minSup
	 *            Value of the minimum support
	 * @param windows
	 *            Time of the windows size
	 * @param delta
	 *            Variantion of support to considerer an patterns
	 *            semi-frequencie
	 * @param out
	 *            path and name of the output object
	 */
	public IncrementalGsp(String database, double minSup, int windows,
			double delta, String out) {

		this.incGsp = new FirstProcessing(database, minSup, windows, delta, out);
	}

	/**
	 * With it is not the first time that the algorithm run to a database, this
	 * constructor should be used. All the main information cames to oldPatterns
	 * file.
	 * 
	 * @param newDatabase
	 *            The incremental database
	 * @param oldPatterns
	 *            File created to keep the result of earlier iteration.
	 */
	public IncrementalGsp(String newDatabase, String oldPatterns) {

		this.incGsp = new OtherIteration(newDatabase, oldPatterns);
	}

	/**
	 * Execute the algorithm and return the patterns.
	 * 
	 * @return Patterns found
	 */
	public String exec() {

		incGsp.execute();

		return incGsp.toString();
	}

	/**
	 * Just for test. Not implemented.
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(String[] args) {

	}

}
