package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.GuiForCategoryItem;
import com.clementscode.mmi.swing.LabelAndField;
import com.clementscode.mmi.swing.LoggingFrame;

public class TriPanelCrud1 {
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame;
	private JFrame frame;
	private Vector vector;
	private JPanel mainPanel;

	public static void main(String[] args) {
		try {
			new TriPanelCrud1().run();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	public TriPanelCrud1() {
		vector = new Vector();
	}

	private void run() {
		// TODO: Make a menu to show the logging frame...
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);
		frame = new JFrame("Round1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		frame.getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		mainPanel.add(new JScrollPane(soundFilePanel()), BorderLayout.WEST);
		mainPanel.add(new JScrollPane(tripleStimulusPanel()),
				BorderLayout.CENTER);
		mainPanel.add(new JScrollPane(imageFilePanel()), BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	private JPanel imageFilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		String dirName = "/Users/mgpayne/resources/";
		File dir = new File(dirName);
		String[] files = dir.list();

		JList targetJList = new JList();
		// targetJList.setCellRenderer(new CustomCellRenderer());

		// Vector vector = new Vector();
		// ImageIcon ii;
		//
		//
		// for (String fn : files) {
		// if (fn.indexOf("jpg") > 0) {
		// ii = new ImageIcon(dirName + fn);
		// vector.add(ii);
		// // makeDragable(label1);
		// // panel.add(label1);
		// }
		// }
		visitAllFiles(new File(dirName));
		targetJList.setListData(vector);
		targetJList.setDragEnabled(true);
		targetJList.setDropMode(DropMode.ON);
		panel.add(targetJList);
		return panel;
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
				ImageIcon smallIi = smallImageIcon(fn, 128, 128);
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

	private JPanel tripleStimulusPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < 10; ++i) {
			JPanel triPanel = new JPanel();
			TitledBorder title = BorderFactory.createTitledBorder("title");
			triPanel.setBorder(title);
			triPanel.add(new LabelAndFieldWithIcon("Image: ",
					new JTextField(20), this));
			triPanel.add(new LabelAndField("Prompt: ", new JTextField(20)));
			triPanel.add(new LabelAndField("Answer: ", new JTextField(20)));
			panel.add(triPanel);
		}

		return panel;
	}

	private JPanel soundFilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < 10; ++i) {
			// TODO: Make this a function or a class....
			JLabel label1 = new JLabel("sound file " + i);
			makeDragable(label1);

			panel.add(label1);
		}
		return panel;
	}

	private void makeDragable(JLabel label1) {
		// Specify that the label's text property be transferable; the value
		// of
		// this property will be used in any drag-and-drop involving this
		// label
		final String propertyName = "text";
		label1.setTransferHandler(new TransferHandler(propertyName));

		// Listen for mouse clicks
		label1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				JComponent comp = (JComponent) evt.getSource();
				TransferHandler th = comp.getTransferHandler();

				// Start the drag operation
				th.exportAsDrag(comp, evt, TransferHandler.COPY);
			}
		});

	}

	void refreshGui() {

		mainPanel.revalidate();
		frame.pack();
	}

}
