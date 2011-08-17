package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import com.clementscode.mmi.MainGui;
import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.ConfigParser;
import com.clementscode.mmi.res.ConfigParser100;
import com.clementscode.mmi.res.LegacyConfigParser;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.res.SessionConfig;
import com.clementscode.mmi.util.Shuffler;
import com.clementscode.mmi.util.Utils;

public class Gui implements ActionListener, MediatorListenerCustomer {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public static final String BROWSE_SESSION_DATA_FILE = "BROWSE_SESSION_DATA_FILE";
	public static final String SESSION_DIRECTORY = "SESSION_DIRECTORY";
	public static final String ADVT_URL = "http://clementscode.com/avdt";
	public static final Object OUTPUT_CSV_FILE_LOCATION = "OUTPUT_CSV_FILE_LOCATION";
	public static final Object SESSION_CONFIG_FILENAME = "SESSION_CONFIG_FILENAME";
	public static final Object RESOURCES_DIRECTORY = "RESOURCES_DIRECTORY";
	// private ImageIcon imgIconCenter;
	private JButton centerButton;
	private Queue<CategoryItem> itemQueue = null;
	private Session session = null;
	private Timer timer;
	private Timer preBetweenTimer;
	private Timer betweenTimer;
	protected static Log log = LogFactory.getLog(Gui.class);

	private JFrame frame;
	private String frameTitle = Messages.getString("Gui.FrameTitle"); //$NON-NLS-1$
	private ActionRecorder attendingAction;
	private ActionRecorder independentAction;
	private ActionRecorder verbalAction;
	private ActionRecorder modelingAction;
	private ActionRecorder noAnswerAction;
	private ActionRecorder quitAction;
	private ActionRecorder timerAction;
	private ActionRecorder timerBetweenAction;
	private ActionRecorder openAction;
	private JPanel mainPanel;
	private Mediator mediator;

	private File tmpDir;
	private ArrayList<JComponent> lstButtons;
	private ImageIcon iiSmilingFace, iiSmilingFaceClickToBegin;
	private JTextField tfSessionName;
	private static JTextField tfSessionDataFile;
	private JButton clickToStartButton;
	private List<String> lstTempDirectories;

	private LoggingFrame loggingFrame;

	private ActionRecorder showLoggingFrameAction;

	// private URL codeBaseUrl = null;

	private int shownItemCount = 0;

	private int totalItemCount;

	private boolean bDebounce = false; // DISGUSTING! and Mysterious.

	private BufferedImage clearImage;

	public static Properties preferences;
	private ActionRecorder toggleButtonsAction;
	private boolean buttonsVisible;

	// GLOBALS EVERYWHERE!
	private CategoryItem currentItem;

	private ConfigParser parser = null;
	private ActionRecorder wrongAnswerAction;
	private ActionRecorder timerTimeDelayAutoAdvance;
	private int maxHeight;
	private int maxWidth;
	private ActionRecorder baselineModeAction;

	public Gui() {
		loggingFrame = new LoggingFrame();
		// TODO: Trim the extra! jnlpSetup();
		preferences = loadPreferences();
		String tmpDirStr = "/tmp/mmi";
		tmpDir = new File(tmpDirStr);
		tmpDir.mkdirs();
		mediator = new Mediator(this);
		setupActions(mediator);
		mainPanel = setupMainPanel();
		// TODO: Check to see if there's a logic bug here....
		frame = new JFrame(frameTitle);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		setupMenus();
		disableButtons();
		hideButtons();

		lstTempDirectories = new ArrayList<String>();
		// Register a shutdown thread
		Runtime.getRuntime().addShutdownHook(new Thread() {
			// This method is called during shutdown
			public void run() {
				// Do shutdown work ...
				savePreferences();
				Utils.deleteTempDirectories(lstTempDirectories);
			}
		});
	}

	private void disableButtons() {
		for (JComponent jc : lstButtons) {
			jc.setEnabled(false);
		}
	}

	public void toggleButtons() {
		if (buttonsVisible) {
			hideButtons();
		} else {
			showButtons();
		}
	}

	private void hideButtons() {
		for (JComponent jc : lstButtons) {
			jc.setVisible(false);

		}
		buttonsVisible = false;
	}

	private void showButtons() {
		for (JComponent jc : lstButtons) {
			jc.setVisible(true);
		}
		buttonsVisible = true;
	}

	private void enableButtons() {
		for (JComponent jc : lstButtons) {
			jc.setEnabled(true);
		}
	}

	private void setupMenus() {
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

		// menuItem = new JMenuItem(openHttpAction);
		// menu.add(menuItem);

		/*
		 * menuItem = new JMenuItem(crudAction); menu.add(menuItem);
		 */

		menuItem = new JMenuItem(showLoggingFrameAction);
		menu.add(menuItem);

		menuItem = new JMenuItem(quitAction);
		menu.add(menuItem);

		menuBar.add(menu);

		JMenu buttonMenu = new JMenu(Messages.getString("Gui.Buttons")); //$NON-NLS-1$
		menuItem = new JMenuItem(attendingAction);
		buttonMenu.add(menuItem);
		menuItem = new JCheckBoxMenuItem(baselineModeAction);
		buttonMenu.add(menuItem);
		menuItem = new JMenuItem(independentAction);
		buttonMenu.add(menuItem);
		menuItem = new JMenuItem(verbalAction);
		buttonMenu.add(menuItem);
		menuItem = new JMenuItem(modelingAction);
		buttonMenu.add(menuItem);
		menuItem = new JMenuItem(noAnswerAction);
		buttonMenu.add(menuItem);
		menuItem = new JMenuItem(wrongAnswerAction);
		buttonMenu.add(menuItem);

		menuItem = new JMenuItem(toggleButtonsAction);
		buttonMenu.add(menuItem);

		menuBar.add(buttonMenu);
		frame.setJMenuBar(menuBar);

		frame.pack();

		frame.setVisible(true);
	}

	private JPanel setupMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel southPanel = new JPanel();

		lstButtons = new ArrayList<JComponent>();

		addButton(southPanel, wrongAnswerAction);
		addButton(southPanel, attendingAction);
		// per issue #39 addButton(southPanel, baselineModeAction);
		addButton(southPanel, independentAction);
		addButton(southPanel, verbalAction);
		addButton(southPanel, modelingAction);
		addButton(southPanel, noAnswerAction);

		JPanel belowSouthPanel = new JPanel();
		belowSouthPanel.setLayout(new GridLayout(0, 1));
		tfSessionName = new JTextField(40);
		if (null != session) {
			tfSessionName.setText(session.getSessionName());
		} else {
			tfSessionName.setText("Session 1");
		}
		belowSouthPanel.add(southPanel);

		belowSouthPanel.add(new LabelAndField("Session Name: ", tfSessionName));
		tfSessionDataFile = new JTextField(30);
		try {
			if (null != session) {
				tfSessionDataFile.setText(session.getSessionDataFile()
						.getCanonicalPath());
			} else {
				tfSessionDataFile.setText((String) preferences
						.get(OUTPUT_CSV_FILE_LOCATION));
				// tfSessionDataFile.setText(System.getProperty("user.home")
				// + "/avdt.csv");
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JPanel midBelowSouthPanel = new JPanel();
		midBelowSouthPanel.add(new LabelAndField("Session Data File: ",
				tfSessionDataFile));
		JButton browse = new JButton("Browse...");
		browse.setActionCommand(BROWSE_SESSION_DATA_FILE);
		browse.addActionListener(this);
		midBelowSouthPanel.add(browse);
		belowSouthPanel.add(midBelowSouthPanel);
		clickToStartButton = new JButton("Click to Start");
		belowSouthPanel.add(clickToStartButton);
		clickToStartButton.setEnabled(false);
		clickToStartButton.addActionListener(this);

		// http://download.oracle.com/javase/tutorial/uiswing/misc/action.html

		// response value. This can be 1 of 4 things: independent (child
		// answered before the prompt audio), verbal (child answered after the
		// prompt but before the answer), modeling (child answered anytime after
		// the answer audio) or the child did not answer.

		// JPanel southContainerPanel = new JPanel();
		// southContainerPanel.setLayout(new GridLayout(0, 1));
		//
		// southContainerPanel.add(southPanel);
		// southContainerPanel.add(midBelowSouthPanel);
		// southContainerPanel.add(belowSouthPanel);

		panel.add(belowSouthPanel, BorderLayout.PAGE_END);
		// panel.add(southContainerPanel, BorderLayout.SOUTH);
		BufferedImage imageData = null;
		BufferedImage imageDataClickToBegin = null;
		try {
			imageData = readImageDataFromClasspath("images/happy-face.jpg");
			imageDataClickToBegin = readImageDataFromClasspath("images/happy-face-click-to-begin.jpg");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.warn("Could not find image from classpath...", e);
			e.printStackTrace();
		}

		iiSmilingFace = null;

		if (null != imageData) {
			iiSmilingFace = new ImageIcon(imageData);
			iiSmilingFaceClickToBegin = new ImageIcon(imageDataClickToBegin);
		}

		if (null == imageData) {
			try {

				iiSmilingFace = new ImageIcon(new URL(
						"http://MattPayne.org/mmi/happy-face.jpg"));
				iiSmilingFaceClickToBegin = new ImageIcon(new URL(
						"http://MattPayne.org/mmi/happy-face.jpg"));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		maxHeight = iiSmilingFace.getIconHeight();
		maxWidth = iiSmilingFace.getIconWidth();
		centerButton = new JButton(iiSmilingFace);
		centerButton.addActionListener(this);
		panel.add(centerButton, BorderLayout.CENTER);

		return panel;
	}

	public void backToStartScreen() {
		frame.setTitle(frameTitle);
		centerButton.setIcon(iiSmilingFace);
		refreshGui();
		disableButtons();
	}

	private void addButton(JPanel southPanel, ActionRecorder independentAction2) {
		JButton responseButton = new JButton(independentAction2);
		southPanel.add(responseButton);
		lstButtons.add(responseButton);
	}

	private BufferedImage readImageDataFromClasspath(String fileName)
			throws IOException {

		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				fileName);

		return ImageIO.read(in);
	}

	public void setupTimer() {
		if (null != timer) {
			timer.stop(); // fix for issue #4
		}
		SessionConfig config = session.getConfig();
		// DON'T LET THE NAME CHANGES FOOL YOU!
		int answerDelay = config.getTimeDelayAudioPrompt()
				+ getSoundLen(currentItem.getAudioSD());
		timer = new Timer(answerDelay * 1000, timerAction);
		timer.setInitialDelay(config.getTimeDelayAudioSD() * 1000);
		timer.setRepeats(true);
		timer.start();
		log
				.info(String
						.format(
								"new timer for %d seconds before SD and %d seconds before prompt",
								config.getTimeDelayAudioSD(), answerDelay));
	}

	/**
	 * 
	 */
	private void setupBetweenTimer() {

		if (betweenTimer != null) {
			// just in case
			betweenTimer.stop();
		}
		SessionConfig config = session.getConfig();
		betweenTimer = new Timer(config.getTimeDelayInterTrial() * 1000,
				timerBetweenAction);
		betweenTimer.setRepeats(false);

	}

	public Timer getBetweenTimer() {
		return betweenTimer;
	}

	public void startTimerTimeDelayAutoAdvance(int timeDelayAutoAdvance) {
		preBetweenTimer = new Timer(timeDelayAutoAdvance * 1000,
				timerTimeDelayAutoAdvance);
		preBetweenTimer.setRepeats(false);
		preBetweenTimer.start();
		log.info(String.format(
				"Started timerTimeDelayAutoAdvance timer for %d seconds.",
				timeDelayAutoAdvance));
	}

	int getSoundLen(File sndFile) {
		// FIXME I'm a horrible hack
		if (sndFile == null) {
			return 0;
		}
		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(sndFile);
			AudioFormat format = audioInputStream.getFormat();
			long frames = audioInputStream.getFrameLength();
			audioInputStream.close();
			return (int) (frames / format.getFrameRate());
		} catch (Exception e) {
			log.error("Horrible hack blew up, karma", e);
			return 0;
		}
	}

	public void setupCenterButton() {
		Dimension max = session.getMaxDimensions();
		centerButton.setPreferredSize(max);
		int width = (int) max.getWidth();
		int height = (int) max.getHeight();
		clearImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics g = clearImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		g.dispose();
	}

	private void setupActions(MediatorListener mediator) {
		// TODO: Fix bug that control A does not toggle the checkbox
		// TODO: Make attending not a checkbox.
		// TODO: Make hot keys come from a properties file. Hopefully ask JNLP
		// Utils where this program was loaded from and do a http get to there
		// for the properties file.

		Properties hotKeysProperties = null;
		String fileName = "hotkeys.properties";
		try {
			hotKeysProperties = readPropertiesFromClassPath(fileName);
		} catch (Exception e) {
			hotKeysProperties = new Properties();
			hotKeysProperties.put("Hotkey.Gui.BaselineMode", "B");
			hotKeysProperties.put("Hotkey.Gui.Attending", "A");
			hotKeysProperties.put("Hotkey.Gui.Independent", "1");
			hotKeysProperties.put("Hotkey.Gui.Verbal", "2");
			hotKeysProperties.put("Hotkey.Gui.Modeling", "3");
			hotKeysProperties.put("Hotkey.Gui.NoAnswer", "4");
			hotKeysProperties.put("Hotkey.Gui.WrongAnswer", "5");
			log.warn(String.format(
					"Problem reading %s.  Defaulting hotkeysPropteries=%s",
					fileName, hotKeysProperties), e);
		}

		String hk = (String) hotKeysProperties.get("Hotkey.Gui.BaselineMode");

		baselineModeAction = new ActionRecorder(Messages
				.getString("Gui.BaselineMode"), null, //$NON-NLS-1$
				Messages.getString("Gui.BaselineModeDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.BASELINE_MODE, mediator);

		hk = (String) hotKeysProperties.get("Hotkey.Gui.Attending");

		attendingAction = new ActionRecorder(Messages
				.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke(hk),
				Action.ATTENDING, mediator);

		hk = (String) hotKeysProperties.get("Hotkey.Gui.Independent");

		independentAction = new ActionRecorder(Messages
				.getString("Gui.Independent"), null, //$NON-NLS-1$
				Messages.getString("Gui.IndependentDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F2), KeyStroke.getKeyStroke(hk),
				Action.INDEPENDENT, mediator);

		hk = (String) hotKeysProperties.get("Hotkey.Gui.Verbal");

		verbalAction = new ActionRecorder(
				Messages.getString("Gui.Verbal"), null, //$NON-NLS-1$
				Messages.getString("Gui.VerbalDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_F3), KeyStroke.getKeyStroke(hk),
				Action.VERBAL, mediator);

		hk = (String) hotKeysProperties.get("Hotkey.Gui.Modeling");

		modelingAction = new ActionRecorder(
				Messages.getString("Gui.Modeling"), null, //$NON-NLS-1$
				Messages.getString("Gui.ModelingDescriptin"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F4), KeyStroke.getKeyStroke(hk),
				Action.MODELING, mediator);

		hk = (String) hotKeysProperties.get("Hotkey.Gui.NoAnswer");

		noAnswerAction = new ActionRecorder(
				Messages.getString("Gui.NoAnswer"), null, //$NON-NLS-1$
				Messages.getString("Gui.NoAnswerDescription"), new Integer(KeyEvent.VK_F5), //$NON-NLS-1$
				KeyStroke.getKeyStroke(hk), Action.NO_ANSWER, mediator);

		hk = (String) hotKeysProperties.get("Hotkey.Gui.WrongAnswer");
		wrongAnswerAction = new ActionRecorder(
				Messages.getString("Gui.WrongAnswer"), null, //$NON-NLS-1$
				Messages.getString("Gui.WrongAnswerDescription"), new Integer(KeyEvent.VK_F5), //$NON-NLS-1$
				KeyStroke.getKeyStroke(hk), Action.WRONG_ANSWER, mediator);

		toggleButtonsAction = new ActionRecorder(
				Messages.getString("Gui.ToggleButtons"), null, //$NON-NLS-1$
				Messages.getString("Gui.ToggleButtons.Description"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("T"), Action.TOGGLE_BUTTONS, mediator);

		quitAction = new ActionRecorder(
				Messages.getString("Gui.Quit"), null, //$NON-NLS-1$
				Messages.getString("Gui.QuitDescriptino"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control Q"), Action.QUIT, mediator);

		timerAction = new ActionRecorder(
				Messages.getString("Gui.TimerSwing"), null, //$NON-NLS-1$
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control F2"), Action.TIMER, mediator);

		timerBetweenAction = new ActionRecorder(Messages
				.getString("Gui.TimerBetweenSwing"), null, //$NON-NLS-1$
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control F2"), Action.BETWEEN_TIMER,
				mediator);

		timerTimeDelayAutoAdvance = new ActionRecorder(Messages
				.getString("Gui.TimeDelayAutoAdvance"), null, //$NON-NLS-1$
				"xxxxxxxxxxxxxx", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control F2"),
				Action.CHANGE_DELAY_TIMER, mediator);

		openAction = new ActionRecorder(Messages.getString("Gui.Open"), null, //$NON-NLS-1$
				Messages.getString("Gui.OpenDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L),
				KeyStroke.getKeyStroke("control O"), Action.OPEN, mediator);

		showLoggingFrameAction = new ActionRecorder(
				Messages.getString("Gui.Open.ShowLoggingFrame"), null, //$NON-NLS-1$
				Messages.getString("Gui.ShowLoggingFrameDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L),
				KeyStroke.getKeyStroke("control D"), Action.SHOW_LOGGING_FRAME,
				mediator);

	}

	public Queue<CategoryItem> getItemQueue() {
		return itemQueue;
	}

	public void clearImage() {
		centerButton.setIcon(new ImageIcon(clearImage));
		refreshGui();
	}

	public void switchImage(ImageIcon ii) {
		setFrameTitle();
		centerButton.setIcon(ii);

	}

	/*
	 * Brian says "They use bmp images which don't display with the program
	 * right now. I looked at the code and noticed that you don't use the 'img'
	 * field of the CategoryItem. You use the image file to get a path to the
	 * image and read it in again using an icon. I suspect that it will work
	 * better if you use the image that ImageIO already read into memory."
	 */
	public void switchItem(CategoryItem item) {
		currentItem = item;
		ImageIcon ii = new ImageIcon(imageOfMaxSize(item.getImg()));
		switchImage(ii);
		setupTimer();
	}

	private BufferedImage imageOfMaxSize(BufferedImage img) {
		BufferedImage bi = img;
		int w = img.getWidth();
		int h = img.getHeight();
		if ((w > maxWidth) || (h > maxHeight)) {
			log.info(String.format("Resizing! since (%d,%d) > (%d,%d)", w, h,
					maxWidth, maxHeight));
			bi = toBufferedImage(getScaledImage(img, maxWidth, maxHeight));
			w = bi.getWidth();
			h = bi.getHeight();
			log.info(String.format("Now bi size is (%d,%d)", w, h));
		}

		return bi;
	}

	/**
	 * Resizes an image using a Graphics2D object backed by a BufferedImage.
	 * 
	 * THIS ONLY WORKS FOR REDUCING SIZE. WILL EXPLODE AND CATCH FIRE IF
	 * 
	 * <pre>
	 * h &lt; srcImg.getHeight() &amp;&amp; w &lt; srcImg.getWidth()
	 * </pre>
	 * 
	 * @param srcImg
	 *            - source image to scale
	 * @param w
	 *            - desired width
	 * @param h
	 *            - desired height
	 * @return - the new resized image
	 */
	private Image getScaledImage(BufferedImage srcImg, int w, int h) {
		int srcWidth = srcImg.getWidth();
		int srcHeight = srcImg.getHeight();
		int diffWidth = w - srcWidth;
		int diffHeight = h - srcHeight;
		if (diffWidth < diffHeight) {
			double ratio = (double) w / (double) srcWidth;
			h = (int) Math.round(srcHeight * ratio);
		} else {
			double ratio = (double) h / (double) srcHeight;
			w = (int) Math.round(srcWidth * ratio);
		}
		BufferedImage resizedImg = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();
		return resizedImg;
	}

	/**
	 * This method returns a buffered image with the contents of an image from
	 * http://www.exampledepot.com/egs/java.awt.image/image2buf.html
	 */
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}

		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();

		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = false; // hasAlpha(image);

		// Create a buffered image with a format that's compatible with the
		// screen
		BufferedImage bimage = null;
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}

			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image
					.getHeight(null), transparency);
		} catch (HeadlessException e) {
			// The system does not have a screen
		}

		if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image
					.getHeight(null), type);
		}

		// Copy image to buffered image
		Graphics g = bimage.createGraphics();

		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();

		return bimage;
	}

	private void setFrameTitle() {
		frame.setTitle(frameTitle
				+ String.format("%d of %d", shownItemCount++, totalItemCount));
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void stopTimer() {
		if (timer != null) {
			timer.stop();
		}
		if (preBetweenTimer != null) {
			preBetweenTimer.stop();
		}
	}

	public void setVisble(boolean b) {
		frame.setVisible(b);
	}

	public void openSession() {
		bDebounce = false;
		File file;

		// TODO: Get application to remember the last place we opened this...
		String sessionDir = (String) preferences.get(SESSION_DIRECTORY);
		sessionDir = null == sessionDir ? System.getProperty("user.home")
				: sessionDir;
		JFileChooser chooser = new JFileChooser(new File(sessionDir));
		int returnVal = chooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			try {
				readSessionFile(file);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(frame, String.format(
						"Problem reading %s\n%s", file, e.getMessage()));
				e.printStackTrace();
			}
			preferences.put(SESSION_DIRECTORY, getDirectory(file));

			savePreferences();
		}

		displayClickToBegin();
	}

	private Object getDirectory(File file) {
		String dirStr = file.getAbsolutePath();
		int firstSlash = dirStr.lastIndexOf(File.separator);
		dirStr = dirStr.substring(0, firstSlash);
		return dirStr;
	}

	public static void savePreferences() {
		if (null != tfSessionDataFile) {
			String sessionDataFile = tfSessionDataFile.getText();
			preferences.put(OUTPUT_CSV_FILE_LOCATION, sessionDataFile);
		}
		File preferencesFile = getPreferencesFile();
		PrintWriter out;
		try {
			out = new PrintWriter(preferencesFile);
			preferences.store(out, "Config information for " + ADVT_URL
					+ " written on " + new java.util.Date());
			out.close();
		} catch (Exception e) {
			log.error("Problem saving preferences to " + preferencesFile, e);
			e.printStackTrace();
		}

	}

	public static Properties loadPreferences() {
		File preferencesFile = getPreferencesFile();
		Properties prefs = new Properties();
		FileReader in;
		try {
			in = new FileReader(preferencesFile);
			prefs.load(in);
			in.close();
		} catch (Exception e) {
			log.error("Problem loading preferences from " + preferencesFile, e);
			e.printStackTrace();
		}
		return prefs;
	}

	private static File getPreferencesFile() {
		String home = System.getProperty("user.home");
		//
		String advtSettingsDirectory = home + "/AVDT_Settings";
		File preferencesDir = new File(advtSettingsDirectory);
		if (!preferencesDir.exists()) {
			createPreferencesDirectory(advtSettingsDirectory);
		}
		File prefsFile = new File(advtSettingsDirectory + "/advt_prefs.ini");
		return prefsFile;
	}

	private static void createPreferencesDirectory(String advtSettingsDirectory) {
		File dir = new File(advtSettingsDirectory);
		dir.mkdirs();
		try {
			PrintWriter out = new PrintWriter(advtSettingsDirectory
					+ "/README.txt");
			out
					.println("This directory contains settings for the program AVDT.");
			out.println("For more information, please visit " + ADVT_URL);
			out.println("This directory was initially created at "
					+ new java.util.Date());
			out.println("Please do not delete these files or this directory.");
			out.close();
		} catch (Exception e) {
			log.error("Problem making readme in directory: "
					+ advtSettingsDirectory, e);
			e.printStackTrace();
		}

	}

	public void useNewSession() {
		if (bDebounce) {
			return;
		}
		bDebounce = true;
		shownItemCount = 0;// fix for issue #32
		centerButton.removeActionListener(this);

		clickToStartButton.setEnabled(false);
		clickToStartButton.setForeground(Color.white);
		// centerButton.setText("");
		if (null != session) {

			CategoryItem[] copy = Arrays.copyOf(session.getItems(), session
					.getItems().length);
			SessionConfig config = session.getConfig();
			for (int i = 0; i < config.getShuffleCount(); ++i) {
				Shuffler.shuffle(copy);
			}
			itemQueue = new ConcurrentLinkedQueue<CategoryItem>();
			totalItemCount = 0;
			for (CategoryItem item : copy) {
				// TODO: Is there a collections add all I could use here?
				itemQueue.add(item);
				totalItemCount++;
			}
			// itemQueue.add(copy[copy.length - 1]); // DISGUSTING!
			// totalItemCount = itemQueue.size() - 1; // DISGUSTING!
			mediator.setSession(session);
			setupCenterButton();
			setFrameTitle();

			refreshGui();
			// setupTimer is now done on a per-item basis
			// setupTimer();
			setupBetweenTimer();
			enableButtons();
			mediator.execute(Action.BETWEEN_TIMER);
		}
	}

	private void readSessionFile(File file) throws Exception {
		readSessionFile(file, null);
	}

	private void readSessionFile(File file, String newItemBase)
			throws Exception {
		if (this.parser == null) {
			// BC: SHOULDN'T ALL OF THIS BE HAPPENING DURING STARTUP?
			// MP: No, the open menu allows reading new sessions.
			// BC: but this never happens again after the first session is
			// loaded because of the if statement. Having it here leaves the
			// parser as null until the last minute
			Properties props = readPropertiesFromClassPath(MainGui.propFile);
			String[] sndExts = props.getProperty(MainGui.sndKey).split(",");
			LegacyConfigParser legacy = new LegacyConfigParser(sndExts);
			this.parser = new ConfigParser(legacy);
			this.parser.registerVersionParser(new ConfigParser100());
		}

		SessionConfig config = this.parser.parse(file);
		if (null != newItemBase) {
			config.setItemBase(newItemBase);
			// prompts are relative to base in new configs
			// config.setPrompt(newItemBase + "/prompt.wav");
		}
		session = new Session(config);
	}

	public static Properties readPropertiesFromClassPath(String fileName)
			throws IOException {
		Properties props = new Properties();
		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = Gui.class.getClassLoader().getResourceAsStream(
				fileName);
		props.load(new InputStreamReader(in));
		return props;
	}

	void refreshGui() {

		mainPanel.revalidate();
		// disabled per Brian's 8/16/11 email //frame.pack();
	}

	private void displayClickToBegin() {
		centerButton.setEnabled(true);
		centerButton.addActionListener(this); // fix for issue #3
		centerButton.setIcon(iiSmilingFaceClickToBegin);
		// centerButton.setText("Click to Begin");
		centerButton.invalidate();
		clickToStartButton.setEnabled(true);
		clickToStartButton.setForeground(Color.red);
		refreshGui();
	}

	public void actionPerformed(ActionEvent e) {
		if ((clickToStartButton == e.getSource())
				|| (centerButton == e.getSource())) {
			useNewSession();
		} else if (BROWSE_SESSION_DATA_FILE.equals(e.getActionCommand())) {
			chooseSessionDataFile();
		}
	}

	private void chooseSessionDataFile() {
		JFileChooser chooser = new JFileChooser(new File(tfSessionDataFile
				.getText()));
		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				tfSessionDataFile.setText(file.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void populateSessionName() {
		session.setSessionName(tfSessionName.getText());

	}

	public void populateSessionDataFile() {
		String fileName = System.getProperty("user.home") + "/brian.csv";
		String str = tfSessionDataFile.getText();
		str = "".equals(str) ? fileName : str;
		session.setSessionDataFile(new File(str));
	}

	public void showLoggingFrame() {
		loggingFrame.setVisible(true);

	}

}
