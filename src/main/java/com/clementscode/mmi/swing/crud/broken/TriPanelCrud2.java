package com.clementscode.mmi.swing.crud.broken;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.LoggingFrame;

public class TriPanelCrud2 {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame;
	private JFrame frame;
	private Vector vector;
	private JPanel mainPanel;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new TriPanelCrud2().run();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	public TriPanelCrud2() {
		vector = new Vector();
		// TODO: Make a menu to show the logging frame...
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);
		frame = new JFrame("Tri Panel CRUD for AVDT");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(new JScrollPane(new SoundFilePanel()), BorderLayout.WEST);
		mainPanel.add(new JScrollPane(new TripleStimulusPanel()),
				BorderLayout.CENTER);
		mainPanel.add(new JScrollPane(new ImageFilePanel()), BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	private void run() {
		// TODO Auto-generated method stub

	}

}
