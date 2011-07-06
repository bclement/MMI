/**
 * 
 */
package com.clementscode.mmi;

import com.clementscode.mmi.swing.Gui;

/**
 * @author bclement
 * 
 */
public class MainGui {

	// TODO: Get this out of the JAR file...
	public static String propFile = "config.properties";

	// TODO: Get this from the file open menu!
	// TODO: Change this to come from the current directory
	static String confFile = "src/test/resources/session.txt";

	public static String sndKey = "snd.exts";

	public static String imgKey = "img.exts";

	public static void main(String[] args) throws Exception {
		try {
			confFile = args.length > 1 ? args[0] : confFile;
			new MainGui().run();
		} catch (Exception bland) {
			System.out.println("bland=" + bland);
			bland.printStackTrace();
		}
	}

	public void run() throws Exception {
		new Gui();
	}
}
