package com.clementscode.mmi.swing.crud;

import java.awt.Image;
import java.io.File;
import java.util.Vector;

import javax.swing.ImageIcon;

import com.clementscode.mmi.swing.GuiForCategoryItem;

public class Utils {
	// Process only files under dir
	// Started with
	// from http://www.exampledepot.com/egs/java.io/TraverseTree.html
	public static void visitAllFiles(File dir, Vector<ImageIcon> vector) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				visitAllFiles(new File(dir, children[i]), vector);
			}
		} else {
			String fn = dir.getAbsolutePath();
			if (fn.indexOf("jpg") > 0) {
				ImageIcon smallIi = smallImageIcon(fn, 128, 128);
				// TODO:
				vector.add(smallIi);
			}
		}
	}

	static ImageIcon smallImageIcon(String fn, int i, int j) {
		ImageIcon ii = new ImageIcon(fn);
		Image smallImg = GuiForCategoryItem.getScaledImage(ii.getImage(), 128,
				128);
		ImageIcon smallIi = new ImageIcon(smallImg);
		smallIi.setDescription(fn);
		return smallIi;
	}
}
