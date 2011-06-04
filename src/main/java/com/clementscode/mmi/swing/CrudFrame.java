package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.clementscode.mmi.MainGui;
import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.res.SessionConfig;

public class CrudFrame extends JFrame implements ActionListener {

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
	private GuiForCategoryItem firstCategoryItem;
	private List<GuiForCategoryItem> lstGuiForCategoryItems;

	public static void main(String[] args) {
		try {
			new CrudFrame();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public CrudFrame() {
		super("Cread Read Update Delete utility....");
		lstGuiForCategoryItems = new ArrayList<GuiForCategoryItem>();
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

		Action saveAction = new ActionRecorder("Save", null,
				"Save the session.", new Integer(KeyEvent.VK_L), Mediator.SAVE,
				mediator);

		Action saveAsAction = new ActionRecorder("Save As...", null,
				"Choose the file to Save the session.", new Integer(
						KeyEvent.VK_L), Mediator.SAVE_AS, mediator);

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

		menuItem = new JMenuItem(saveAction);
		menu.add(menuItem);

		menuItem = new JMenuItem(saveAsAction);
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
			firstCategoryItem = new GuiForCategoryItem(this);
			diyTable.add(firstCategoryItem);
			lstGuiForCategoryItems.add(firstCategoryItem);
		}
		// scrollPane = new JScrollPane(diyTable,
		// JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane = new JScrollPane(diyTable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		return scrollPane;
	}

	public void actionPerformed(ActionEvent e) {

		GuiForCategoryItem g4ci = new GuiForCategoryItem(this);
		lstGuiForCategoryItems.add(g4ci);
		diyTable.add(g4ci);

		// diyTable.invalidate();
		// scrollPane.invalidate();
		// scrollPane.repaint();
		scrollPane.revalidate();
		// this.pack();
	}

	public void openSessionFile() {
		File file;
		// TODO: Remove hard coded directory.
		// TODO: Get application to remember the last place we opened this...
		JFileChooser chooser = new JFileChooser(new File(
				"/Users/mgpayne/MMI/src/test/resources"));
		int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			try {
				readSessionFile(file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, String.format(
						"Problem reading %s exception was %s", file, e));
				e.printStackTrace();
			}
		}
	}

	private void readSessionFile(File file) throws Exception {
		Properties props = new Properties();
		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream(MainGui.propFile);

		props.load(new InputStreamReader(in));
		String[] sndExts = props.getProperty(MainGui.sndKey).split(",");

		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().withAnnotationIntrospector(
				introspector);
		mapper.getSerializationConfig()
				.withAnnotationIntrospector(introspector);
		SessionConfig config = mapper.readValue(new FileInputStream(file),
				SessionConfig.class);
		Session session = new Session(config, sndExts);
		String sessionPath = file.getParent();
		populateGui(session, sessionPath);
	}

	private void populateGui(Session session, String sessionPath) {
		tfName.setText(session.getName());
		tfDescription.setText(session.getDescription());
		tfDelayForAnswer.setText("" + session.getTimeDelayAnswer());
		tfDelayForPrompt.setText("" + session.getTimeDelayPrompt());
		CategoryItem[] items = session.getItems();
		for (CategoryItem categoryItem : items) {
			// System.out.println("sessionPath=" + sessionPath);
			// System.out.println("categoryItem=" + categoryItem);
			if (!firstCategoryItem.isSet()) {
				firstCategoryItem.setSet(true);
				firstCategoryItem.populate(categoryItem);
			} else {
				GuiForCategoryItem g4ci = new GuiForCategoryItem(this);
				lstGuiForCategoryItems.add(g4ci);
				diyTable.add(g4ci);
				g4ci.populate(categoryItem);
				diyTable.revalidate();
				scrollPane.revalidate();
			}

		}
	}

}
