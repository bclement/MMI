package com.clementscode.mmi.swing.crud.old;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Demo1DragableJLabel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Demo1DragableJLabel();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	private JFrame frame;

	public Demo1DragableJLabel() {
		frame = new JFrame("Demo1DragableJLabel");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(5, 5));
		for (int i = 0; i < 25; ++i) {
			DragableJLabel lbl = new DragableJLabel();
			lbl.setText("label " + i);
			panel.add(lbl);
		}
		frame.pack();
		frame.setVisible(true);
	}

}
