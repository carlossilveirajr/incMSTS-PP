package useful;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * @brief Create a file with all important information about a processed time.
 * 
 *        This class implements a way to salve all important information about a
 *        processed time of Stella Algorithm. The file will be created (even if
 *        there is another with the same name) with this structure: first line
 *        there is a headline
 *        ("file name(size of database|minSup|windows size|delta value)". The
 *        others lines there are all frequent patterns followed by semi-frequent
 *        patterns. They has the same aspect
 *        "<itemset1 itemset2 ... itemsetn> @ number of traps". There is just
 *        one patterns for line.
 * 
 *        Pay attention: there is a file with the same name at the same path
 *        this file will be deleted and recreated.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public class PatternWriter {

	/**
	 * Object to handle with files
	 */
	private File file;

	/**
	 * Object to write in the file
	 */
	private FileWriter fstream;

	/**
	 * Object to buffering the writing
	 */
	private BufferedWriter out;

	/**
	 * This consctrutor receives the name of the output file and creates that
	 * file (if the file already exists it will have been deleted and recreated)
	 * after that is create pipe to conection to the new file and the headline is
	 * written.
	 * 
	 * The head line has this aspect:
	 * "fileName(size of the Database|minSup value|windows size|delta value)"
	 * and occupies the first line.
	 * 
	 * @param fileName
	 *            Name and path of the output file
	 * @param sizeDataBase
	 *            Number of tuples in the database
	 * @param minSup
	 *            Value of the minimum support
	 * @param windowsSize
	 *            Size of the search windows
	 * @param delta
	 *            incremental variance
	 */
	public PatternWriter(String fileName, int sizeDataBase, double minSup,
			int windowsSize, double delta) {
		this.file = new File(fileName);

		if (this.file.exists())
			this.file.delete();

		try {
			this.file.createNewFile();
			this.fstream = new FileWriter(fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		this.out = new BufferedWriter(this.fstream);

		/* writting headline */
		try {
			this.out.write(fileName + "(" + sizeDataBase + "|" + minSup + "|"
					+ windowsSize + "|" + delta + ")");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * This method will flush the buffer and close the conection with the file.
	 */
	public void close() {
		try {
			this.out.close();
			this.fstream.close();
		} catch (IOException e) {
	e.printStackTrace();
		}
	}

	/**
	 * Write the patterns in the file with this aspect:
	 * "<itemset1 ... itemsetn> @ number of traps of this patterns". There is
	 * just one patterns for line.
	 * 
	 * @param toWrite
	 *            Vector of patterns to by written
	 */
	public void write(Vector<Pattern> toWrite) {

		Pattern element;

		for (int i = 0; i < toWrite.size(); ++i) {
			element = toWrite.elementAt(i);
			try {
				out.write("\n" + element.toString() + " @"
						+ element.numberOfOccorrence());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			this.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 // Just for test
	public void write(Vector<String> toWrite) {

		String element;

		for (int i = 0; i < toWrite.size(); ++i) {
			element = toWrite.elementAt(i);
			try {
				out.write("\n" + element.toString() + " @[" + element.length() + "]");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		PatternWriter pw = new PatternWriter("written", 10, 0.5, 2);

		Vector<String> v = new Vector<String>(6);
		v.add("40 isn't old. If you're a tree -- fortune");
		v.add("A person who is more than casually interested in computers should be well schooled in machine languege, since it is a fundamental part of a computer. -- Donald Knuth");
		v.add("I had the rare misfortune of being one of the first people to try and implement a PL/1 compiler. -- T. Cheatham");
		v.add("We can predict everything, except the future. -- fortune");
		v.add("Cats are intended to teach us that not everything in nature has a function. -- Garrison Keillor");
		
		pw.write(v);
		v.clear();
		
		v.add("The future is a myth created by insurance salesmen and high school counselors. -- fortune");
		v.add("Marriage is like twirling a baton, terning handsprings, or eating with chopsticks. It looks easy until you try it. -- fortune");
		v.add("Two is company, three is an orgy. --fortune");
		v.add("Assembly language experience is [important] for the maturity and understanding of how computers work that it provides. --D. Gries");
		v.add("What will you do if all your problems aren't solved by the time you die? --fortune");
		
		pw.write(v);
		
		v.add("40 isn't old. If you're a tree -- fortune");
		v.add("A person who is more than casually interested in computers should be well schooled in machine languege, since it is a fundamental part of a computer. -- Donald Knuth");
		v.add("I had the rare misfortune of being one of the first people to try and implement a PL/1 compiler. -- T. Cheatham");
		v.add("We can predict everything, except the future. -- fortune");
		v.add("Cats are intended to teach us that not everything in nature has a function. -- Garrison Keillor");
		
		pw.write(v);

		pw.close();
	}
	*/
}
