package com.clementscode.mmi.swing.crud.old;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Test1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Test1().run();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	private JFrame frame;

	public Test1() {
		frame = new JFrame("test1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		ImageIcon ii = new ImageIcon(
				"/Users/mgpayne/resources/vehicles/bus/School bus2.jpg");
		panel.add(new JLabel(ii));
		ii = new ImageIcon("/Users/mgpayne/resources/vehicles/boat/boat2.jpg");
		panel.add(new JLabel(ii));
		frame.pack();
		frame.setVisible(true);
	}

	private void run() {
		System.out.println("Running...");

	}

}
