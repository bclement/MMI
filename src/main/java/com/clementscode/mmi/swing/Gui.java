package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;
import javax.jnlp.BasicService;
import javax.jnlp.UnavailableServiceException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import junk.ExtractFileSubDirectories;

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

public class Gui implements ActionListener {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final String BROWSE_SESSION_DATA_FILE = "BROWSE_SESSION_DATA_FILE";
	private static final String SESSION_DIRECTORY = "SESSION_DIRECTORY";
	private static final String ADVT_URL = "http://clementscode.com/avdt";
	private static final Object OUTPUT_CSV_FILE_LOCATION = "OUTPUT_CSV_FILE_LOCATION";
	// private ImageIcon imgIconCenter;
	private JButton centerButton;
	private Queue<CategoryItem> itemQueue = null;
	private Session session = null;
	private Timer timer;
	private Timer betweenTimer;
	protected Log log = LogFactory.getLog(this.getClass());

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
	private ActionRecorder openHttpAction;
	private File tmpDir;
	private ArrayList<JComponent> lstButtons;
	private ImageIcon iiSmilingFace, iiSmilingFaceClickToBegin;
	private JTextField tfSessionName;
	private JTextField tfSessionDataFile;
	private JButton clickToStartButton;
	private List<String> lstTempDirectories;

	private LoggingFrame loggingFrame;

	private ActionRecorder showLoggingFrameAction;

	private URL codeBaseUrl = null;

	private int shownItemCount = 0;

	private int totalItemCount;

	private boolean bDebounce = false; // DISGUSTING! and Mysterious.

	private BufferedImage clearImage;

	private Properties preferences;
	private ActionRecorder toggleButtonsAction;
	private boolean buttonsVisible;

	// GLOBALS EVERYWHERE!
	private CategoryItem currentItem;

	private ConfigParser parser = null;
	private ActionRecorder wrongAnswerAction;

	public Gui() {
		loggingFrame = new LoggingFrame();
		jnlpSetup();
		loadPreferences();
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

	private void jnlpSetup() {
		try {
			String[] sn = javax.jnlp.ServiceManager.getServiceNames();
			for (String string : sn) {
				logger.info("A service name is: " + string);
			}

			Object obj = javax.jnlp.ServiceManager
					.lookup("javax.jnlp.BasicService");
			BasicService bs = (BasicService) obj;
			codeBaseUrl = bs.getCodeBase();
		} catch (UnavailableServiceException e) {
			logger.error("Could not look up BasicService.", e);
			e.printStackTrace();
		} catch (Exception bland) {
			logger
					.error("Some odd JNLP related problem: bland=" + bland,
							bland);
		}
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

		menuItem = new JMenuItem(openHttpAction);
		menu.add(menuItem);

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

		addButton(southPanel, attendingAction);
		addButton(southPanel, independentAction);
		addButton(southPanel, verbalAction);
		addButton(southPanel, modelingAction);
		addButton(southPanel, noAnswerAction);
		addButton(southPanel, wrongAnswerAction);

		JPanel belowSouthPanel = new JPanel();
		belowSouthPanel.setLayout(new GridLayout(0, 1));
		tfSessionName = new JTextField(40);
		if (null != session) {
			tfSessionName.setText(session.getSessionName());
		} else {
			tfSessionName.setText("Session 1");
		}
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

		JPanel southContainerPanel = new JPanel();
		southContainerPanel.setLayout(new GridLayout(0, 1));
		southContainerPanel.add(southPanel);
		southContainerPanel.add(belowSouthPanel);

		panel.add(southContainerPanel, BorderLayout.SOUTH);
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
				+ getPromptLen(currentItem.getAudioPrompt());
		timer = new Timer(answerDelay * 1000, timerAction);
		timer.setInitialDelay(config.getTimeDelayAudioSD() * 1000);
		timer.setRepeats(true);
		timer.start();
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

	private int getPromptLen(File sndFile) {
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
		// // TODO: Call this when we get a new session file read in....
		// CategoryItem first = itemQueue.remove();
		// log.info(String.format("About to display image: %s from item=%d",
		// first.getImgFile(), first.getItemNumber()));
		// imgIconCenter = new ImageIcon(first.getImg());
		//
		// // centerButton = new JButton(imgIconCenter);
		// centerButton.setIcon(imgIconCenter);

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
		attendingAction = new ActionRecorder(Messages
				.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke("A"),
				Mediator.ATTENDING, mediator);
		independentAction = new ActionRecorder(Messages
				.getString("Gui.Independent"), null, //$NON-NLS-1$
				Messages.getString("Gui.IndependentDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F2), KeyStroke.getKeyStroke("1"),
				Mediator.INDEPENDENT, mediator);
		verbalAction = new ActionRecorder(
				Messages.getString("Gui.Verbal"), null, //$NON-NLS-1$
				Messages.getString("Gui.VerbalDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_F3), KeyStroke.getKeyStroke("2"),
				Mediator.VERBAL, mediator);
		modelingAction = new ActionRecorder(
				Messages.getString("Gui.Modeling"), null, //$NON-NLS-1$
				Messages.getString("Gui.ModelingDescriptin"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F4), KeyStroke.getKeyStroke("3"),
				Mediator.MODELING, mediator);
		noAnswerAction = new ActionRecorder(
				Messages.getString("Gui.NoAnswer"), null, //$NON-NLS-1$
				Messages.getString("Gui.NoAnswerDescription"), new Integer(KeyEvent.VK_F5), //$NON-NLS-1$
				KeyStroke.getKeyStroke("4"), Mediator.NO_ANSWER, mediator);

		wrongAnswerAction = new ActionRecorder(
				Messages.getString("Gui.WrongAnswer"), null, //$NON-NLS-1$
				Messages.getString("Gui.WrongAnswerDescription"), new Integer(KeyEvent.VK_F5), //$NON-NLS-1$
				KeyStroke.getKeyStroke("5"), Mediator.WRONG_ANSWER, mediator);

		toggleButtonsAction = new ActionRecorder(
				Messages.getString("Gui.ToggleButtons"), null, //$NON-NLS-1$
				Messages.getString("Gui.ToggleButtons.Description"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("B"), Mediator.TOGGLE_BUTTONS, mediator);

		quitAction = new ActionRecorder(
				Messages.getString("Gui.Quit"), null, //$NON-NLS-1$
				Messages.getString("Gui.QuitDescriptino"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control Q"), Mediator.QUIT, mediator);

		timerAction = new ActionRecorder(
				Messages.getString("Gui.TimerSwing"), null, //$NON-NLS-1$
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control F2"), Mediator.TIMER, mediator);

		timerBetweenAction = new ActionRecorder(Messages
				.getString("Gui.TimerBetweenSwing"), null, //$NON-NLS-1$
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control F2"), Mediator.BETWEEN_TIMER,
				mediator);

		openAction = new ActionRecorder(Messages.getString("Gui.Open"), null, //$NON-NLS-1$
				Messages.getString("Gui.OpenDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L),
				KeyStroke.getKeyStroke("control O"), Mediator.OPEN, mediator);

		openHttpAction = new ActionRecorder(
				Messages.getString("Gui.Open.Http"), null, //$NON-NLS-1$
				Messages.getString("Gui.OpenHttpDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L),
				KeyStroke.getKeyStroke("control H"), Mediator.OPEN_HTTP,
				mediator);

		showLoggingFrameAction = new ActionRecorder(
				Messages.getString("Gui.Open.ShowLoggingFrame"), null, //$NON-NLS-1$
				Messages.getString("Gui.ShowLoggingFrameDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L),
				KeyStroke.getKeyStroke("control D"),
				Mediator.SHOW_LOGGING_FRAME, mediator);

	}

	public void run(Session session) {

	}

	//
	// public void playSound(File file) {
	// try {
	// SoundUtility.playSound(file);
	// // "src/test/resources/bc/animals/fooduck/answer.wav"));
	// } catch (UnsupportedAudioFileException e) {
	// log.error("Problem with playSound: " + file, e);
	// e.printStackTrace();
	// } catch (IOException e) {
	// log.error("Problem with playSound: " + file, e);
	// e.printStackTrace();
	// }
	// }

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
	 * They use bmp images which don't display with the program right now. I
	 * looked at the code and noticed that you don't use the 'img' field of the
	 * CategoryItem. You use the image file to get a path to the image and read
	 * it in again using an icon. I suspect that it will work better if you use
	 * the image that ImageIO already read into memory.
	 */
	public void switchItem(CategoryItem item) {
		currentItem = item;
		ImageIcon ii = new ImageIcon(item.getImg());
		switchImage(ii);
		setupTimer();
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
	}

	public void setVisble(boolean b) {
		frame.setVisible(b);
	}

	public void openSession() {
		bDebounce = false;
		File file;
		// TODO: Remove hard coded directory.
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
						"Problem reading %s exception was %s", file, e));
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

	private void savePreferences() {
		preferences.put(OUTPUT_CSV_FILE_LOCATION, tfSessionDataFile.getText());
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

	private void loadPreferences() {
		File preferencesFile = getPreferencesFile();
		preferences = new Properties();
		FileReader in;
		try {
			in = new FileReader(preferencesFile);
			preferences.load(in);
			in.close();
		} catch (Exception e) {
			log.error("Problem loading preferences from " + preferencesFile, e);
			e.printStackTrace();
		}

	}

	private File getPreferencesFile() {
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

	private void createPreferencesDirectory(String advtSettingsDirectory) {
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
			for (CategoryItem item : copy) {
				// TODO: Is there a collections add all I could use here?
				itemQueue.add(item);
			}
			itemQueue.add(copy[copy.length - 1]); // DISGUSTING!
			totalItemCount = itemQueue.size() - 1; // DISGUSTING!
			mediator.setSession(session);
			setupCenterButton();
			setFrameTitle();

			refreshGui();
			// setupTimer is now done on a per-item basis
			// setupTimer();
			setupBetweenTimer();
			enableButtons();
			mediator.execute(Mediator.BETWEEN_TIMER);
		}
	}

	private void readSessionFile(File file) throws Exception {
		readSessionFile(file, null);
	}

	private void readSessionFile(File file, String newItemBase)
			throws Exception {
		if (this.parser == null) {
			// SHOULDN'T ALL OF THIS BE HAPPENING DURING STARTUP? No, the open
			// menu allows reading new sessions.
			Properties props = new Properties();
			// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
			// Do it this way and no relative path huha is needed.
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream(MainGui.propFile);
			props.load(new InputStreamReader(in));
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

	void refreshGui() {

		mainPanel.revalidate();
		frame.pack();
	}

	public void openHttpSession() {
		bDebounce = false;
		// Started with clues from
		// http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html

		// Messages.getString("Gui.Verbal")
		String[] possibilities = { "http://MattPayne.org/mmi/demo1.zip",
				"http://MattPayne.org/mmi/mp.zip",
				"http://MattPayne.org/mmi/bc.zip", "more to come later..." };
		if (null != codeBaseUrl) {
			possibilities = readPossiblitiesFromUrl(codeBaseUrl, "sessions.txt");
		}
		String s = (String) JOptionPane.showInputDialog(frame,
				"Complete the sentence:\n" + "\"Green eggs and...\"",
				"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,
				possibilities, "ham");
		System.out.println("s=" + s);
		unpackToTempDirectory(s);

	}

	private String[] readPossiblitiesFromUrl(URL codeBaseUrl2, String fileName) {
		String[] possiblities = null;
		String line;
		List<String> lst = new ArrayList<String>();
		try {
			URL url = new URL(codeBaseUrl2.toString() + fileName);
			InputStream is = url.openStream();
			LineNumberReader in = new LineNumberReader(
					new InputStreamReader(is));
			while (null != (line = in.readLine())) {
				lst.add(line);
			}
			in.close();
			possiblities = (String[]) lst.toArray(new String[lst.size()]);
		} catch (Exception e) {
			logger.error(String.format("Problem reading %s/%s", codeBaseUrl2,
					fileName), e);
			e.printStackTrace();
		}
		return possiblities;
	}

	private void unpackToTempDirectory(String strUrl) {

		try {
			File tempZipFile = fetchViaHttp(strUrl);
			File tmp = File.createTempFile("mmi", "", tmpDir);
			String zipPath = tmp.getAbsolutePath() + ".dir";
			tmp.delete();
			lstTempDirectories.add(zipPath);
			ExtractFileSubDirectories.unzip(zipPath, tempZipFile
					.getAbsolutePath());
			tempZipFile.delete();
			readSessionFile(new File(zipPath + "/session.txt"), zipPath);

			displayClickToBegin();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private File fetchViaHttp(String strUrl) throws IOException {

		// Since Mac's OS X make f-ed up temp directories like this:
		// Extracting
		// /var/folders/IL/ILL0adgsGq89FHBGBZvCF++++TI/-Tmp-/mmi7319452195262685629.dir/food/foobar/redbar.jpg
		// We're going to specify the temp directory

		File tempZipFile = File.createTempFile("mmiSession", "zip", tmpDir);

		URL url = new URL(strUrl);
		InputStream in = url.openStream();
		byte[] chunk = new byte[8 * 1024];
		OutputStream out = new FileOutputStream(tempZipFile);
		int numBytesRead = 0;
		while (-1 != (numBytesRead = in.read(chunk))) {
			out.write(chunk, 0, numBytesRead);
		}
		out.close();
		in.close();
		return tempZipFile;
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
