package com.clementscode.mmi.util;

import java.io.File;
import java.util.List;

public class Utils {

	public static void deleteTempDirectories(List<String> lstTempDirectories) {
		// TODO Auto-generated method stub
		for (String file : lstTempDirectories) {
			System.out.println("Need to delete dir: " + file);
			deleteDir(new File(file));
		}
	}

	// from: http://www.exampledepot.com/egs/java.io/DeleteDir.html
	// Deletes all files and subdirectories under dir.
	// Returns true if all deletions were successful.
	// If a deletion fails, the method stops attempting to delete and returns
	// false.
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		// The directory is now empty so delete it
		return dir.delete();
	}

}
