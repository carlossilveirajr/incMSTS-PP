package useful;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @brief It's used to read a text file.
 * 
 *        This class is used to read texts files, the database or the new
 *        databases may be in a text file where each line is a tuple of the
 *        database.
 * 
 *        In this implementation this class helps other classes opening files
 *        and giving than lines of the file.
 * 
 *        At the end of the useful the file should be closed.
 * 
 * @author Carlos Roberto Silveira Junior
 */
public class FileReader {

	/**
	 * Object to scanner files
	 */
	private Scanner database;

	/**
	 * A constructor that recives just a parameter and this is the path and de
	 * name of the file. The file needs to exist, if the file doesn't exist 
	 * a messege "Error opening file ..." show on.
	 */
	public FileReader(String fileName) {

		try {
			database = new Scanner(new File(fileName));
		} catch (FileNotFoundException fnfe) {
			fileNotFoundHandler(fileName);
		}

	}

	protected void fileNotFoundHandler(String fileName) {
		System.out.println("Error opening file " + fileName + ".");
		System.exit(0);
	}

	/**
	 * Get a line of the data file which means a tuple of database. If the
	 * database overs a blank line will the returned.
	 * 
	 * @return A tuple of the database
	 */
	public String getLine() {
		if (database.hasNext())
			return database.nextLine();
		else
			return "";
	}

	/**
	 * This method close the file opened by the constructor.
	 */
	public void closeFile() {
		if (database != null)
			database.close();
	}
}
