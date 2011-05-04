package com.clementscode.mmi.swing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBarFrame extends JFrame {

	private Mediator mediator;
	private JProgressBar progressBar;
	private JPanel panel;

	public ProgressBarFrame(Mediator mediator) {
		super("Seconds left");
		this.mediator=mediator;
		panel = new JPanel();
		getContentPane().add(panel);
		
		//Where the GUI is constructed:
		progressBar = new JProgressBar(0, 30);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		
		pack();
		setVisible(true);
	}

}
