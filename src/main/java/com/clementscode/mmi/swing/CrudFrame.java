package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CrudFrame extends JFrame {

	/**
	 * Too lazy to learn Matisse http://netbeans.org/features/java/swing.html
	 */
	private static final long serialVersionUID = -5629208181141679241L;
	private JPanel topPanel;
	private JTextField tfName;
	private JTextArea tfDescription;
	private JTextField tfShuffleCount;
	private JTextField tfDelayForPrompt;
	private JTextField tfDelayForAnswer;
	private JPanel mainPanel;

	public CrudFrame(Mediator mediator) {
		super("Cread Read Update Delete utility....");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		topPanel = new JPanel();

		getContentPane().add(mainPanel);

		mainPanel.add(topPanel, BorderLayout.NORTH);

		topPanel.setLayout(new BorderLayout());
		tfName = new JTextField();
		topPanel.add(new LabelAndField("Name: ", tfName), BorderLayout.NORTH);
		tfDescription = new JTextArea(5, 60);
		topPanel.add(new LabelAndField("Description:", tfDescription),
				BorderLayout.CENTER);
		tfShuffleCount = new JTextField();
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(0, 1));
		gridPanel.add(new LabelAndField("Shuffle Count: ", tfShuffleCount));
		tfDelayForPrompt = new JTextField();
		tfDelayForAnswer = new JTextField();
		gridPanel
				.add(new LabelAndField("Delay for prompt: ", tfDelayForPrompt));
		gridPanel.add(new LabelAndField("Delay for Answer:", tfDelayForAnswer));
		topPanel.add(gridPanel, BorderLayout.SOUTH);

		String[] columnNames = { "First Name", "Last Name", "Sport",
				"# of Years", "Vegetarian" };

		Object[][] data = {
				{ "Kathy", "Smith", "Snowboarding", new Integer(5),
						new Boolean(false) },
				{ "John", "Doe", "Rowing", new Integer(3), new Boolean(true) },
				{ "Sue", "Black", "Knitting", new Integer(2),
						new Boolean(false) },
				{ "Jane", "White", "Speed reading", new Integer(20),
						new Boolean(true) },
				{ "Joe", "Brown", "Pool", new Integer(10), new Boolean(false) } };

		final JTable table = new JTable(data, columnNames);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		mainPanel.add(scrollPane, BorderLayout.SOUTH);
		pack();
		setVisible(true);
	}

}
