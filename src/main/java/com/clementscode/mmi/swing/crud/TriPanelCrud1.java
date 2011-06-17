package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

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

import org.apache.log4j.Logger;

import com.clementscode.mmi.swing.LabelAndField;
import com.clementscode.mmi.swing.LoggingFrame;

public class TriPanelCrud1 implements ActionListener, FocusListener {
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
		panel.setLayout(new BorderLayout());

		panel.add(new JScrollPane(soundFilePanel()), BorderLayout.WEST);
		panel.add(new JScrollPane(tripleStimulusPanel()), BorderLayout.CENTER);
		panel.add(new JScrollPane(imageFilePanel()), BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}

	private JPanel imageFilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		String dirName = "/Users/mgpayne/resources/vehicles/boat/";
		File dir = new File(dirName);
		String[] files = dir.list();

		JList targetJList = new JList();
		// targetJList.setCellRenderer(new CustomCellRenderer());

		Vector vector = new Vector();
		ImageIcon ii;


		for (String fn : files) {
			if (fn.indexOf("jpg") > 0) {
				ii = new ImageIcon(dirName + fn);
				vector.add(ii);
				// makeDragable(label1);
				// panel.add(label1);
			}
		}
		targetJList.setListData(vector);
		targetJList.setDragEnabled(true);
		targetJList.setDropMode(DropMode.ON);
		panel.add(targetJList);
		return panel;
	}

	private JPanel tripleStimulusPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < 10; ++i) {
			JPanel triPanel = new JPanel();
			JTextField tf = new JTextField(20);
			tf.addActionListener(this);
			tf.addFocusListener(this);
			triPanel.add(new LabelAndField("Image: ", tf));
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

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e);
	}

	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("focusGained: e=" + arg0);
	}

	public void focusLost(FocusEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("focusLost: e=" + arg0);
		JTextField tf = (JTextField) arg0.getSource();
		System.out.println("\ttf=" + tf.getText());
	}

}
