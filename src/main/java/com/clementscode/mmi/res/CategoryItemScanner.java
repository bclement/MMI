/**
 * 
 */
package com.clementscode.mmi.res;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * deprecated use SessionConfig instead
 * 
 * @author bclement
 */
public class CategoryItemScanner {

	protected String[] sndExtentions;

	protected String[] imgExtentions;

	protected Log log = LogFactory.getLog(this.getClass());

	public File[] getImgFiles(File dir) {
		return getFileType(dir, imgExtentions);
	}

	public File[] getAudioFiles(File dir) {
		return getFileType(dir, sndExtentions);
	}

	public static File[] getFileType(File dir, final String[] validExtentions) {
		if (isDir(dir)) {
			return dir.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return isOneOf(name, validExtentions);
				}
			});
		} else {
			return new File[0];
		}
	}

	public static boolean isOneOf(String name, String[] validExtentions) {
		String lower = name.toLowerCase();
		boolean rval = false;
		for (String ext : validExtentions) {
			if (lower.endsWith(ext.toLowerCase())) {
				rval = true;
				break;
			}
		}
		return rval;
	}

	public static boolean isDir(File dir) {
		return dir != null && dir.isDirectory();
	}
}
