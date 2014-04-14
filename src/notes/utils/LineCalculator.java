package notes.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Calculates how many lines of codes are there in current project.
 *
 * Author: Rui Du
 */
public class LineCalculator {

    private static int lineCount = 0;
    private static int fileCount = 0;

    /**
     * Main method to run the program.
     *
     * @param args Input arguments list.
     */
    public static void main(String[] args) {
        File file = new File("./src/");
        visitFolder(file);
        file = new File("./test/");
        visitFolder(file);
        System.out.println();
        System.out.println("Number of files: " + fileCount);
        System.out.println("Number of lines: " + lineCount);
    }

    private static void visitFolder(File file) {
        if (file.getName().startsWith(".")) {
            return;
        }
        if (file.isFile()) {
            System.out.println(file.getAbsolutePath());
            fileCount++;
            try {
                BufferedReader input = new BufferedReader(new FileReader(file));
                while (input.readLine() != null) {
                    lineCount++;
                }
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            System.out.println("Cannot find any file under " + file.getAbsolutePath());
            return;
        }
        for (File subFile : files) {
            visitFolder(subFile);
        }
    }

}
