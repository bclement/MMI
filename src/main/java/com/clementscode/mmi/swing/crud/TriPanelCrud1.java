package com.clementscode.mmi.swing.crud;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.LoggingFrame;

public class TriPanelCrud1 {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame;
	private JFrame frame;

	public static void main(String[] args) {
		try {
			new TriPanelCrud1().run();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	private void run() {
		// TODO: Make a menu to show the logging frame...
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);
		frame = new JFrame("Round1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(3, 1));
		panel.add(soundFilePanel());
		panel.add(tripleStimulusPanel());
		panel.add(imageFilePanel());
		frame.pack();
		frame.setVisible(true);
	}

	private JPanel imageFilePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	private JPanel tripleStimulusPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	private JPanel soundFilePanel() {
		JPanel panel = new JPanel();
		return panel;
	}

}
