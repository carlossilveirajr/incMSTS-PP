package useful;

/**
 * @brief Interface for GSP algorithm
 * 
 *        This interface representes the GSP algorithm and has all the necessary
 *        method to works.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public interface GSP {

	/**
	 * Really execulte the algorithm.
	 */
	public void execute();

	/**
	 * Return the number of the tuple is in the database.
	 * 
	 * @return Size of the database
	 */
	public int getNumberTuples();

	/**
	 * To set the minimum support.
	 * 
	 * @param minSup
	 *            Value of the minimum support
	 */
	public void setMinSup(double minSup);

	/**
	 * To return the value of the minimum support.
	 * 
	 * @return Value of the minimum support
	 */
	public double getMinSup();
}
