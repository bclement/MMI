package com.clementscode.mmi.swing.crud;

import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Demo2DragableJLabel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Demo2DragableJLabel();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	private JFrame frame;
	private List<ImageIcon> lstFileNames;

	public Demo2DragableJLabel() {
		frame = new JFrame("Demo2DragableJLabel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(5, 5));
		lstFileNames = new ArrayList<ImageIcon>();
		String dirName = "/Users/mgpayne/resources/";
		visitAllFiles(new File(dirName));
		for (int i = 0; i < 25; ++i) {
			DragableJLabelWithImage lbl = new DragableJLabelWithImage();
			lbl.setIcon(lstFileNames.get(i));
			panel.add(lbl);
		}
		frame.pack();
		frame.setVisible(true);
	}

	// Process only files under dir
	// Started with
	// from http://www.exampledepot.com/egs/java.io/TraverseTree.html
	public void visitAllFiles(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				visitAllFiles(new File(dir, children[i]));
			}
		} else {
			String fn = dir.getAbsolutePath();
			if (fn.indexOf("jpg") > 0) {
				ImageIcon smallIi = TriPanelCrud1.smallImageIcon(fn, 128, 128);
				lstFileNames.add(smallIi);
			}
		}
	}
}
