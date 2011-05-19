package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CrudFrame extends JFrame implements ActionListener {

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
	private JPanel diyTable;
	private JScrollPane scrollPane;

	public static void main(String[] args) {
		try {
			CrudFrame crudFrame = new CrudFrame();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public CrudFrame() {
		super("Cread Read Update Delete utility....");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		topPanel = new JPanel();

		getContentPane().add(mainPanel);

		mainPanel.add(topPanel, BorderLayout.NORTH);

		topPanel.setLayout(new BorderLayout());
		tfName = new JTextField(60);
		topPanel.add(new LabelAndField("Name: ", tfName), BorderLayout.NORTH);
		tfDescription = new JTextArea(5, 60);
		topPanel.add(new LabelAndField("Description:", tfDescription),
				BorderLayout.CENTER);
		tfShuffleCount = new JTextField("1");
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(0, 1));
		gridPanel.add(new LabelAndField("Shuffle Count: ", tfShuffleCount));
		tfDelayForPrompt = new JTextField(5);
		tfDelayForAnswer = new JTextField(5);
		gridPanel
				.add(new LabelAndField("Delay for prompt: ", tfDelayForPrompt));
		gridPanel.add(new LabelAndField("Delay for Answer:", tfDelayForAnswer));
		topPanel.add(gridPanel, BorderLayout.SOUTH);

		JScrollPane scrollPane = createDiyTableScrollPane();
		mainPanel.add(scrollPane, BorderLayout.SOUTH);
		setupMenus();
		pack();
		setVisible(true);
	}

	private void setupMenus() {

		// TODO: Different portable strings
		// TODO: fill in the mediator stuff!

		MediatorListener mediator = new MediatorForCrud(this);

		Action openAction = new ActionRecorder(
				Messages.getString("Gui.Open"), null, //$NON-NLS-1$
				Messages.getString("Gui.OpenDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L), Mediator.OPEN, mediator);

		Action quitAction = new ActionRecorder(
				Messages.getString("Gui.Quit"), null, //$NON-NLS-1$
				Messages.getString("Gui.QuitDescriptino"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				Mediator.QUIT, mediator);

		// http://download.oracle.com/javase/tutorial/uiswing/components/menu.html
		// http://download.oracle.com/javase/tutorial/uiswing/misc/action.html
		// Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		// Build the first menu.
		JMenu menu = new JMenu(Messages.getString("Gui.File")); //$NON-NLS-1$
		menu.setMnemonic(KeyEvent.VK_A);
		menuBar.add(menu);
		// a group of JMenuItems
		JMenuItem menuItem = new JMenuItem(openAction);

		menu.add(menuItem);
		menuItem = new JMenuItem(quitAction);
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuBar.add(menu);
		this.setJMenuBar(menuBar);

	}

	private JScrollPane createDiyTableScrollPane() {
		diyTable = new JPanel();
		diyTable.setLayout(new GridLayout(0, 1)); // one column
		for (int i = 0; i < 1; ++i) {
			// JButton b = new JButton("Test " + i);
			// b.addActionListener(this);
			// diyTable.add(b);
			diyTable.add(new GuiForCategoryItem(this));
		}
		scrollPane = new JScrollPane(diyTable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		return scrollPane;
	}

	public void actionPerformed(ActionEvent e) {

		diyTable.add(new GuiForCategoryItem(this));

		// diyTable.invalidate();
		// scrollPane.invalidate();
		// scrollPane.repaint();
		scrollPane.revalidate();
		// this.pack();
	}

}
