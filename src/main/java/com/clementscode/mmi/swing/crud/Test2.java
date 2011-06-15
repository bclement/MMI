package com.clementscode.mmi.swing.crud;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

public class Test2 implements ActionListener {

	private static final String TEXT_FIELD = "TEXT_FIELD";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Test2();
		} catch (Exception bland) {
			bland.printStackTrace();
		}

	}

	private JFrame frame;
	private JTextField tf;

	public Test2() {
		frame = new JFrame("test2");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel);
		ImageIcon ii = new ImageIcon(
				"/Users/mgpayne/resources/vehicles/boat/boat2.jpg");
		JLabel label1 = new JLabel(ii);
		// Specify that the label's text property be transferable; the value of
		// this property will be used in any drag-and-drop involving this label
		final String propertyName = "text";
		label1.setTransferHandler(new TransferHandler(propertyName));

		panel.add(label1);

		tf = new JTextField(20);
		tf.addActionListener(this);
		tf.setActionCommand(TEXT_FIELD);
		panel.add(tf);
		String[] opts = { "Big", "Red", "Wagon" };
		JList jList = new JList(opts);
		jList.setDragEnabled(true);
		jList.add(new JLabel("blah blah blah"));
		JList jList2 = new JList();
		jList2.setCellRenderer(new CustomCellRenderer()); 
		ii = new ImageIcon("/Users/mgpayne/resources/vehicles/boat/boat3.jpg");
		Vector vector = new Vector();
		vector.add(new JLabel(ii));
		jList2.setListData(vector);
		jList2.setDragEnabled(true);
		panel.add(fancyJLabel("I like eggs"));
		panel.add(jList);
		panel.add(jList2);
		JButton btn = new JButton("button");
		btn.addActionListener(this);
		panel.add(btn);

		JPanel targetPanel = new JPanel();
		JList targetJList = new JList();
		targetJList.setCellRenderer(new CustomCellRenderer());
		ii = new ImageIcon("/Users/mgpayne/resources/vehicles/boat/boat2.jpg");
		vector = new Vector();
		vector.add(new JLabel(ii));
		targetJList.setListData(vector);
		targetJList.setDragEnabled(true);
		targetJList.setDropMode(DropMode.ON);
		targetJList.setPreferredSize(new Dimension(100, 100));
		targetPanel.add(targetJList);


		TitledBorder title;
		title = BorderFactory.createTitledBorder("Category Item");
		targetPanel.setBorder(title);
		panel.add(targetPanel);

		frame.pack();
		frame.setVisible(true);
	}

	private JLabel fancyJLabel(String string) {
		// Start with
		// http://www.exampledepot.com/egs/javax.swing/label_LblCp.html

		// Create a label
		ImageIcon ii = new ImageIcon(
				"/Users/mgpayne/resources/vehicles/boat/boat4.jpg");
		JLabel label = new JLabel(ii);// "Label Text");

		// Specify that the label's text property be transferable; the value of
		// this property will be used in any drag-and-drop involving this label
		final String propertyName = "text";
		label.setTransferHandler(new TransferHandler(propertyName));

		// Listen for mouse clicks
		label.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				JComponent comp = (JComponent) evt.getSource();
				TransferHandler th = comp.getTransferHandler();

				// Start the drag operation
				th.exportAsDrag(comp, evt, TransferHandler.COPY);
			}
		});
		return label;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("tf=" + tf.getText());
		String cmd = e.getActionCommand();
		System.out.println("cmd=" + cmd);
	}
}
