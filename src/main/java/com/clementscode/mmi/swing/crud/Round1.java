package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.LoggingFrame;

import demo.DTPicture;
import demo.PictureTransferHandler;

public class Round1 {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame;
	private JFrame frame;
	private PictureTransferHandler picHandler;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Round1();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public Round1() {
		// TODO: Make a menu to show the logging frame...
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);
		picHandler = new PictureTransferHandler();
		frame = new JFrame("Round1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout());
		panel.add(triplesPanel());
		panel.add(imagesPanel());
		frame.pack();
		frame.setVisible(true);
	}

	private JPanel imagesPanel() {
		JPanel imgPanel = new JPanel();
		imgPanel.setLayout(new GridLayout(4, 3));
		File dir = new File("images");
		String[] imgs = dir.list();
		for (String img : imgs) {
			if (img.indexOf("jpg") > 0) {
				log.info("Processing img=" + img);
				DTPicture pic1 = new DTPicture(new ImageIcon("images/" + img,
						img).getImage());
				pic1.setTransferHandler(picHandler);
				imgPanel.add(pic1);
			}
		}
		return imgPanel;
	}

	private JPanel triplesPanel() {
		JPanel mugshots = new JPanel();
		mugshots.setLayout(new GridLayout(1, 0));
		mugshots.add(blandSpot());
		mugshots.add(blandSpot());
		mugshots.add(blandSpot());
		return mugshots;
	}

	private Component blandSpot() {
		DTPicture pic7 = new DTPicture(null);
		pic7.setTransferHandler(picHandler);
		return pic7;
	}
}
