package org.peoplecount.getresults;

import java.io.File;
import java.util.ArrayList;

/**
 * 
 * @author Benjy Strauss
 *
 */

public class GetResults {
	protected static PCXMLParser pcParser;

	static void err(String msg) {
		System.err.println("  ERROR: "+msg);
		sayHelp();
	}
	
	static void sayHelp() {
		System.err.println("  USAGE: GetResults [ -d | -nd ] fileOrFolder");
		System.err.println("  -d       - print debug messages");
		System.err.println("  -nd      - suppress debug messages");
		System.err.println("  filename - an xml file of the html results,");
		System.err.println("             or a folder with many such files.");
		System.err.println();
		System.exit(1);
	}

	/**
	 * @param args  Pass in the file name to parse
	 */
	public static void main(String[] args) {

		boolean debug = true;
		File f = null;

		// Process arguments
		for (int ix = 0; ix < args.length; ix++) {
			String arg = args[ix];
	
			if (arg.equals("-d"))
				debug = true;
			else if (arg.equals("-nd"))
				debug = false;
			else if (arg.equals("-h") || arg.equals("--help"))
				sayHelp();
			else {
				if (f != null)
					err("Already have filename='"+f.getPath()+"', extra arg: "+arg);

				f = new File(arg);
				if (!f.exists())
					err("File doesn't exist: "+arg);
				if (!f.canRead())
					err("File isn't readable: "+arg);
			}
		}

		if (f == null)
			err("No file or folder was given as an argument");
		else if (f.isDirectory())
			getDataFromFiles(debug, f);
		else if (f.exists())
			getDataFromAFile(debug, f);
		else {
			System.err.println("arg[0] is not a file or folder name: "+args[0]);
			System.exit(1);
		}
	}

	static void getDataFromFiles(boolean debug, File folder) {
		System.err.println("getDataFromFiles(folder) is not yet implemented");
	}

	static void getDataFromAFile(boolean debug, File f) {
		if (debug)
			System.out.println("Data:");

		// Parse the file and extract the data
		pcParser = new PCXMLParser(f.getPath());

		ProfileResults profile = new ProfileResults();
		ArrayList<Question> data = profile.fill(pcParser.getMainElement());

		//NodeList nodes = pcParser.getElements();
		//ArrayList<Question> data = new GetProfileData().get(nodes);

		if (debug) {
			printData(data);
			System.out.println("Completed.");
		}
	}

	private static void printData(ArrayList<Question> data) {
		for (int index = 0; index < data.size(); index++)
			data.get(index).print();
	}
	
	
}
