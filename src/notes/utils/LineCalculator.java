/**
 * 
 */
package notes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Calculates how many lines of codes are there in current project.
 * 
 * @author Rui Du
 * @version 1.0
 * 
 */
public class LineCalculator {

	private static int number;

	/**
	 * Main method to run the program.
	 * 
	 * @param args
	 *            Input arguments list.
	 */
	public static void main(String[] args) {
		File file = new File("./src/");
		visitFolder(file);
		file = new File("./test/");
		visitFolder(file);
		System.out.println("Number of lines: " + number);
	}

	private static void visitFolder(File file) {
		if (file.getName().startsWith(".")) {
			return;
		}
		if (file.isFile()) {
			System.out.println(file.getAbsolutePath());
			try {
				BufferedReader input = new BufferedReader(new FileReader(file));
				while (input.readLine() != null)
					number++;
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		File[] files = file.listFiles();
		for (File subFile : files) {
			visitFolder(subFile);
		}
	}

}
