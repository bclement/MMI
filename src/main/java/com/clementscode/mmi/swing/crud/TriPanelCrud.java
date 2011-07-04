package com.clementscode.mmi.swing.crud;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

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
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.border.TitledBorder;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.clementscode.mmi.res.ConfigParser;
import com.clementscode.mmi.res.LegacyConfigParser;
import com.clementscode.mmi.res.SessionConfig;
import com.clementscode.mmi.swing.ActionRecorder;
import com.clementscode.mmi.swing.LabelAndField;
import com.clementscode.mmi.swing.LoggingFrame;
import com.clementscode.mmi.swing.MediatorListener;
import com.clementscode.mmi.swing.MediatorListenerCustomer;
import com.clementscode.mmi.swing.Messages;

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
		crudMediator = new CrudMediator(this);
		vector = new Vector<ImageIcon>();
		loggingFrame = new LoggingFrame();
		loggingFrame.setVisible(true);

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
		openAction = new ActionRecorder(
				Messages.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.OPEN, crudMediator);
		saveAction = new ActionRecorder(
				Messages.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.SAVE, crudMediator);
		saveAsAction = new ActionRecorder(
				Messages.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.SAVE_AS, crudMediator);
		debugAction = new ActionRecorder(
				Messages.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.DEBUG, crudMediator);
		quitAction = new ActionRecorder(
				Messages.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.QUIT, crudMediator);

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

	void writeSessionConfig() throws JsonGenerationException,
			JsonMappingException, FileNotFoundException, IOException {
		
		ConfigParser parser = new ConfigParser(new LegacyConfigParser(
				new String[0]));
		parser.Serialize(new FileOutputStream(new File("xxx")),
		sessionConfig);
	}

	private void weaveDragAndDrop(JPanel imageFilePanel,
			JPanel tripleStimulusPanel) {
		tripleStimulusPanel.setLayout(new GridLayout(0, 1));
		imageFilePanel.setLayout(new GridLayout(0, 1));
		String dirName = "/Users/mgpayne/resources/";
		visitAllFiles(new File(dirName));
		int i = 0, row = 0;
		for (Object obj : vector) {
			ImageIcon ii = (ImageIcon) obj;
			DragableJLabelWithImage lblSource = new DragableJLabelWithImage();
			lblSource.setIcon(ii);
			lblSource.setName(String.format("row=%d;%s", row,
					ii.getDescription()));
			imageFilePanel.add(lblSource);

			JPanel triPanel = new JPanel();
			triPanel.setName("PanelRow=" + row);
			TitledBorder title = BorderFactory.createTitledBorder("title");
			triPanel.setBorder(title);
			DragableJLabelWithImage lblDestination = new DragableJLabelWithImage();
			String fn = "/Users/mgpayne/resources/people/plumber/Plumber3.jpg";
			ImageIcon smallIi = smallImageIcon(fn, 128, 128);
			lblDestination.setIcon(smallIi);
			lblDestination.setText("" + i);
			triPanel.add(lblDestination);
			triPanel.add(new LabelAndField("Prompt: ", new JTextField(20)));
			triPanel.add(new LabelAndField("Answer: ", new JTextField(20)));
			tripleStimulusPanel.add(triPanel);
			lblDestination.setParentPanel(triPanel);
			lblSource.setParentPanel(triPanel);
			// lblDestination.setDestination(lblSource);
			// lblSource.setDestination(lblDestination);
			row++;
		}
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
			}
		}
	}

	static ImageIcon smallImageIcon(String fn, int i, int j) {
		ImageIcon ii = new ImageIcon(fn);
		Image smallImg = getScaledImage(ii.getImage(), 128, 128);
		ImageIcon smallIi = new ImageIcon(smallImg);
		smallIi.setDescription(fn);
		return smallIi;
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
