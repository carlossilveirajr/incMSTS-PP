package useful;

import java.util.Vector;

import incremental.OldPattern;

/**
 * @brief This class reads the object file resulted by older iteration
 * 
 *        This class implements a way to read the file that representes the
 *        result of an older iteration. With this, is possible to read the
 *        headline of the file and catch the information of the firster
 *        iteration like size of database, minimum support, size of the search
 *        windows and the delta value for the incremental mining.
 * 
 *        Also, this class is used to read all the old patterns which is the old
 *        knowledge that will be used to updating the knowledge tring to don't
 *        reprecess the whole database.
 * 
 * @author Carlos Roberto Silveira Junior
 * @see FileReader
 */
public class PatternReader extends FileReader {

	/**
	 * The headline of the file, has name of the file, database size, minimum
	 * support value, size of search windows, delta value
	 */
	private String[] headline;

	/**
	 * Constructor that recives the path of the database.
	 * 
	 * @param file
	 *            Path of the database
	 */
	public PatternReader(String file) {

		super(file);

		this.headline = super.getLine().split("\\|");
	}

	/**
	 * Getting the file name.
	 * 
	 * @return file name
	 */
	public String getFileName() {

		String[] s2 = this.headline[0].split("\\(");

		return s2[0];
	}

	/**
	 * Getting the old database size.
	 * 
	 * @return Database size
	 */
	public int getDatabaseSize() {

		String[] s2 = this.headline[0].split("\\(");

		return (new Integer(s2[1]));
	}

	/**
	 * Getting the minimum support.
	 * 
	 * @return Minimum support
	 */
	public double getMinSup() {
		return new Double(this.headline[1]);
	}

	/**
	 * Getting the size of the windows.
	 * 
	 * @return Windows size
	 */
	public int getWindowsSize() {
		return new Integer(this.headline[2]);
	}

	/**
	 * Getting the delta value.
	 * 
	 * @return Delta
	 */
	public double getDelta() {

		String[] s = this.headline[3].split("\\)");

		return new Double(s[0]);
	}

	/**
	 * This method process the whole file and extress all old pattern and their
	 * number of occorrences.
	 * 
	 * @return Vector with all old patterns
	 */
	public Vector<OldPattern> getPatterns() {

		String s;
		String[] s2;
		Vector<OldPattern> vector = new Vector<OldPattern>();

		s = super.getLine();
		while (!s.isEmpty()) {
			s2 = s.split("@");
			vector.add(new OldPattern(s2[0], new Integer(s2[1])));
			s = super.getLine();
		}

		return vector;
	}

	/**
	 * Just for test.
	 * 
	 * @param args
	 *            Not used
	 */
	public static void main(String[] args) {

		PatternReader pr = new PatternReader("outputTesteSimulated");

		for (String s : pr.headline)
			System.out.println(s);

		System.out.println(pr.getFileName() + "(" + pr.getDatabaseSize() + "|"
				+ pr.getMinSup() + "|" + pr.getWindowsSize() + "|"
				+ pr.getDelta() + ")");
				
		Vector<OldPattern> v = pr.getPatterns();
		
		for (int i = 0; i < v.size(); ++i)
			System.out.println(v.elementAt(i)+ " " + v.elementAt(i).numberOfOccorrence());
	}

}
