/**
 * 
 */
package com.clementscode.mmi.res;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

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

	/**
	 * @param sndExtentions
	 * @param imgExtentions
	 */
	public CategoryItemScanner(String[] sndExtentions, String[] imgExtentions) {
		super();
		this.sndExtentions = sndExtentions;
		this.imgExtentions = imgExtentions;
	}

	public CategoryItem[] scanDirectory(String category, File dir)
			throws Exception {
		File[] snds = getAudioFiles(dir);
		File[] imgs = getImgFiles(dir);
		if (snds.length < 1 || imgs.length < 1) {
			throw new Exception("Unable to find images and sound in directory "
					+ dir);
		}
		return processDirectory(dir.getName(), imgs, snds[0]);
	}

	protected CategoryItem[] processDirectory(String dir, File[] images,
			File audio) throws IOException {
		ArrayList<CategoryItem> rval = new ArrayList<CategoryItem>(
				images.length);
		for (int i = 0; i < images.length; ++i) {
			File f = images[i];
			try {
				rval.add(new CategoryItem(f, audio));
			} catch (IOException e) {
				log.error("Unable to read image: " + f.getName(), e);
			}
		}
		if (rval.isEmpty()) {
			String line1 = "Unable to read any images in directory: " + dir;
			String line2 = "\nCheck previous log entries for details";
			throw new IOException(line1 + line2);
		}
		return rval.toArray(new CategoryItem[rval.size()]);
	}

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
