package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.JComponent;
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
import javax.swing.KeyStroke;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.lemoine.MemoryMonitorBar;

import com.clementscode.mmi.MainGui;
import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.res.SessionConfig;

public class CrudFrame extends JFrame {

	private static final long serialVersionUID = -5629208181141679241L;
	public static final String DELETE_CATEGORY_ITEM = "DELETE_CATEGORY_ITEM";
	public static final String ADD_CATEGORY_ITEM = "ADD_CATEGORY_ITEM";
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

	protected ObjectMapper mapper;

	public static void main(String[] args) {
		try {
			new CrudFrame();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public CrudFrame() {
		super("Cread Read Update Delete utility....");

		mapper = new ObjectMapper();
		AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
		mapper.getDeserializationConfig().withAnnotationIntrospector(
				introspector);
		mapper.getSerializationConfig()
				.withAnnotationIntrospector(introspector);

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

		scrollPane = createDiyTableScrollPane();
		Dimension ps = scrollPane.getPreferredSize();
		ps.setSize(ps.getWidth(), ps.getHeight() * 2);
		scrollPane.setPreferredSize(ps);
		JPanel southPane = new JPanel();
		southPane.setLayout(new GridLayout(0, 1));
		southPane.add(scrollPane);
		MemoryMonitorBar mmb = new MemoryMonitorBar();
		// TODO: Figure out how to make not as tall...
		southPane.add(mmb);
		mainPanel.add(southPane, BorderLayout.SOUTH);
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
				new Integer(KeyEvent.VK_L),
				KeyStroke.getKeyStroke("control O"),

				Mediator.OPEN, mediator);

		Action saveAction = new ActionRecorder("Save", null,
				"Save the session.", new Integer(KeyEvent.VK_L), KeyStroke
						.getKeyStroke("control S"), Mediator.SAVE, mediator);

		Action saveAsAction = new ActionRecorder("Save As...", null,
				"Choose the file to Save the session.", new Integer(
						KeyEvent.VK_L), KeyStroke
						.getKeyStroke("control shift S"), Mediator.SAVE_AS,
				mediator);

		Action quitAction = new ActionRecorder(
				Messages.getString("Gui.Quit"), null, //$NON-NLS-1$
				Messages.getString("Gui.QuitDescriptino"), new Integer(
						KeyEvent.VK_L), KeyStroke.getKeyStroke("control Q"),
				//$NON-NLS-1$
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

		JScrollPane sp = new JScrollPane(diyTable,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		return sp;
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

	public void saveSessionFileAs() {
		// TODO: Remove hard coded directory.
		// TODO: Get application to remember the last place we opened this...
		JFileChooser chooser = new JFileChooser(new File(
				"/Users/mgpayne/MMI/src/test/resources"));
		int returnVal = chooser.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			SessionConfig config = new SessionConfig();
			// no more description, you can remove it from the crud
			// config.setDescription(tfDescription.getText());
			config.setName(tfName.getText());
			config.setShuffleCount(Integer.parseInt(tfShuffleCount.getText()));
			config.setTimeDelayAnswer(Integer.parseInt(tfDelayForAnswer
					.getText()));
			config.setTimeDelayPrompt(Integer.parseInt(tfDelayForPrompt
					.getText()));
			int numComponents = diyTable.getComponentCount();
			String[] items = new String[numComponents];
			for (int n = 0; n < numComponents; ++n) {
				GuiForCategoryItem comp = (GuiForCategoryItem) diyTable
						.getComponent(n);
				items[n] = comp.getImageFileName();
			}

			config.setItems(items);
			try {
				writeSessionConfig(config, file);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void readSessionFile(File file) throws Exception {
		Properties props = new Properties();
		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				MainGui.propFile);

		props.load(new InputStreamReader(in));
		String[] sndExts = props.getProperty(MainGui.sndKey).split(",");

		SessionConfig config = mapper.readValue(new FileInputStream(file),
				SessionConfig.class);
		Session session = new Session(config, sndExts);
		String sessionPath = file.getParent();
		populateGui(session, sessionPath);
	}

	protected void writeSessionConfig(SessionConfig config, File file)
			throws JsonGenerationException, JsonMappingException, IOException {
		writeSessionConfig(config, new FileOutputStream(file));
	}

	protected void writeSessionConfig(SessionConfig config, OutputStream out)
			throws JsonGenerationException, JsonMappingException, IOException {
		ObjectWriter writer = mapper
				.prettyPrintingWriter(new DefaultPrettyPrinter());
		writer.writeValue(out, config);
	}

	private void populateGui(Session session, String sessionPath) {
		tfName.setText(session.getConfigName());
		// no more description
		// tfDescription.setText(session.getDescription());
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
				final GuiForCategoryItem g4ci = new GuiForCategoryItem(this);
				g4ci.populate(categoryItem);
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						lstGuiForCategoryItems.add(g4ci);
						diyTable.add(g4ci);

					}
				});

			}

		}// foreach categoryItem
		refreshGui();
	}

	void refreshGui() {
		// diyTable.revalidate();
		// scrollPane.revalidate();
		((JComponent) scrollPane.getParent()).revalidate();
		pack();
	}

	public JPanel getDiyTable() {
		return diyTable;
	}

	public void setDiyTable(JPanel diyTable) {
		this.diyTable = diyTable;
	}

	public List<GuiForCategoryItem> getLstGuiForCategoryItems() {
		return lstGuiForCategoryItems;
	}

	public void setLstGuiForCategoryItems(
			List<GuiForCategoryItem> lstGuiForCategoryItems) {
		this.lstGuiForCategoryItems = lstGuiForCategoryItems;
	}

}
