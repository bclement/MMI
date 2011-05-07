package com.clementscode.mmi.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class CrudFrame extends JFrame {

	/**
	 * Too lazy to learn Matisse http://netbeans.org/features/java/swing.html
	 */
	private static final long serialVersionUID = -5629208181141679241L;
	private JPanel mainPanel;

	public CrudFrame(Mediator mediator) {
		super("Cread Read Update Delete utility....");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		getContentPane().add(mainPanel);

		pack();
		setVisible(true);
	}

}
