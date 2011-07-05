package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.clementscode.mmi.res.ConfigParser;
import com.clementscode.mmi.res.ItemConfig;
import com.clementscode.mmi.res.LegacyConfigParser;
import com.clementscode.mmi.res.SessionConfig;
import com.clementscode.mmi.swing.ActionRecorder;
import com.clementscode.mmi.swing.LoggingFrame;
import com.clementscode.mmi.swing.MediatorListener;
import com.clementscode.mmi.swing.MediatorListenerCustomer;

public class TriPanelCrud extends JFrame implements MediatorListenerCustomer {
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass().getName());
	private LoggingFrame loggingFrame;
	private JPanel mainPanel;
	private Vector<ImageIcon> vector;
	// private ArrayList<JPanel> lstTriPanels;
	// private ArrayList<DragableJLabelWithImage> lstImageSources;
	private SessionConfig sessionConfig = null;
	private ActionRecorder openAction;
	private MediatorListener crudMediator;
	private ActionRecorder saveAction;
	private ActionRecorder saveAsAction;
	private ActionRecorder debugAction;
	private ActionRecorder quitAction;
	private List<String> lstSoundFileNames;
	private List<TriJPanel> lstTriPanel;
	private Map<Integer, String> mapPictureNumberToPictureFileName = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new TriPanelCrud();
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	public TriPanelCrud() {
		super("TriPanelCrud4 -- a new approach!");
		mapPictureNumberToPictureFileName = new TreeMap<Integer, String>();
		sessionConfig = new SessionConfig();
		lstSoundFileNames = new ArrayList<String>();
		crudMediator = new CrudMediator(this);
		vector = new Vector<ImageIcon>();
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);
		String dirName = "/Users/mgpayne/resources/";
		visitAllFiles(new File(dirName));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel = new JPanel();
		getContentPane().add(mainPanel);
		mainPanel.setLayout(new BorderLayout());


		mainPanel.add(new JScrollPane(soundFilePanel()), BorderLayout.WEST);

		JPanel imageFilePanel = new JPanel();
		JPanel tripleStimulusPanel = new JPanel();
		weaveDragAndDrop(imageFilePanel, tripleStimulusPanel);

		mainPanel
				.add(new JScrollPane(tripleStimulusPanel),
				BorderLayout.CENTER);
		mainPanel.add(new JScrollPane(imageFilePanel), BorderLayout.EAST);
		setupMenus();
		pack();
		setVisible(true);
	}

	private void setupMenus() {
		String hk = "control S"; // (String)
									// hotKeysProperties.get("Hotkey.Gui.Attending");
		openAction = new ActionRecorder("Open...", null,
				"Open a file to work with...", null,
				KeyStroke.getKeyStroke(hk), Action.OPEN, crudMediator);
		saveAction = new ActionRecorder("Save", null,
				"Save the file you're working with", null,
				KeyStroke.getKeyStroke(hk), Action.SAVE, crudMediator);
		saveAsAction = new ActionRecorder("Save As...", null,
				"Save the file you're working with using a different name...",
				null, KeyStroke.getKeyStroke(hk), Action.SAVE_AS, crudMediator);
		debugAction = new ActionRecorder("Debug", null,
				"Open the debug window.", null, KeyStroke.getKeyStroke(hk),
				Action.DEBUG, crudMediator);
		quitAction = new ActionRecorder("Quit", null, "Quit the program.",
				null, KeyStroke.getKeyStroke(hk), Action.QUIT, crudMediator);

		// http://download.oracle.com/javase/tutorial/uiswing/components/menu.html
		// http://download.oracle.com/javase/tutorial/uiswing/misc/action.html
		// Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		// Build the first menu.
		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		// a group of JMenuItems
		JMenuItem menuItem = new JMenuItem(openAction);
		menu.add(menuItem);
		menuItem = new JMenuItem(saveAction);
		menu.add(menuItem);
		menuItem = new JMenuItem(saveAsAction);
		menu.add(menuItem);
		menuItem = new JMenuItem(debugAction);
		menu.add(menuItem);
		menuItem = new JMenuItem(quitAction);
		menu.add(menuItem);

		menuBar.add(menu);
		this.setJMenuBar(menuBar);

	}

	void collectSessionConfigData() {
		List<ItemConfig> lstItemConfig = new ArrayList<ItemConfig>();
		for (TriJPanel tp : lstTriPanel) {
			String audioPrompt = tp.getPrompt();
			String audioSD = tp.getAnswer();
			int row = tp.getPictureNumber();
			if (-1 == row) {
				break;
			}
			String visualSD = mapPictureNumberToPictureFileName.get(row);
			ItemConfig itemConfig = new ItemConfig(visualSD, audioSD,
					audioPrompt);
			lstItemConfig.add(itemConfig);
		}
		ItemConfig[] array = (ItemConfig[]) lstItemConfig
				.toArray(new ItemConfig[lstItemConfig.size()]);
		sessionConfig.setItems(array);
	}

	void writeSessionConfig() throws JsonGenerationException,
			JsonMappingException, FileNotFoundException, IOException {
		
		ConfigParser parser = new ConfigParser(new LegacyConfigParser(
				new String[0]));
		parser.Serialize(new FileOutputStream(new File("xxx.txt")),
		sessionConfig);
	}

	private BufferedImage readImageDataFromClasspath(String fileName)
			throws IOException {

		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream(fileName);
		return ImageIO.read(in);
	}
	private void weaveDragAndDrop(JPanel imageFilePanel,
			JPanel tripleStimulusPanel) {
		tripleStimulusPanel.setLayout(new GridLayout(0, 1));
		imageFilePanel.setLayout(new GridLayout(0, 1));
		lstTriPanel = new ArrayList<TriJPanel>();
		int row = 0;
		for (Object obj : vector) {
			ImageIcon ii = (ImageIcon) obj;
			mapPictureNumberToPictureFileName.put(row, ii.getDescription());
			DragableJLabelWithImage lblSource = new DragableJLabelWithImage();
			lblSource.setIcon(ii);
			lblSource.setName(String.format("row=%d;%s", row,
					ii.getDescription()));
			imageFilePanel.add(lblSource);

			TriJPanel triPanel = new TriJPanel();
			triPanel.setName("PanelRow=" + row);
			TitledBorder title = BorderFactory.createTitledBorder("title");
			triPanel.setBorder(title);
			DragableJLabelWithImage lblDestination = new DragableJLabelWithImage();
			String fn = "images/question_mark.jpg";
			BufferedImage imageData = null;
			try {
				imageData = readImageDataFromClasspath(fn);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ImageIcon smallIi = smallImageIcon(imageData, 128, 128);
			lblDestination.setIcon(smallIi);
			// lblDestination.setText("" + i);
			triPanel.setImage(lblDestination);
			triPanel.createPromptAndAnswer();
			tripleStimulusPanel.add(triPanel);
			lblDestination.setParentPanel(triPanel);
			lblSource.setParentPanel(triPanel);
			lstTriPanel.add(triPanel);
			// lblDestination.setDestination(lblSource);
			// lblSource.setDestination(lblDestination);
			row++;
		}
	}

	ImageIcon smallImageIcon(BufferedImage imageData, int i, int j) {
		ImageIcon ii = new ImageIcon(imageData);
		return smallImageIcon(ii, i, j);
	}

	ImageIcon smallImageIcon(String fn, int i, int j) {
		ImageIcon ii = new ImageIcon(fn);
		return smallImageIcon(ii, i, j);
	}

	ImageIcon smallImageIcon(ImageIcon ii, int i, int j) {
		Image smallImg = getScaledImage(ii.getImage(), 128, 128);
		ImageIcon smallIi = new ImageIcon(smallImg);
		// smallIi.setDescription(fn);
		return smallIi;
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
				smallIi.setDescription(fn);
			} else if (fn.indexOf("wav") > 0) {
				lstSoundFileNames.add(fn);
			}
		}
	}



	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * 
	 * @param srcImg
	 *            - source image to scale
	 * @param w
	 *            - desired width
	 * @param h
	 *            - desired height
	 * @return - the new resized image
	 */
	public static Image getScaledImage(Image srcImg, int w, int h) {
		// from:
		// http://download.oracle.com/javase/tutorial/uiswing/examples/components/IconDemoProject/src/components/IconDemoApp.java

		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	private JPanel soundFilePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));

		for (String soundFileName : lstSoundFileNames) {
			JLabel label1 = new JLabel(soundFileName);
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
				log.info("Starting drag operation...");
				// Start the drag operation
				th.exportAsDrag(comp, evt, TransferHandler.COPY);
			}
		});

	}

	void refreshGui() {

		mainPanel.revalidate();
		this.pack();
	}
}
