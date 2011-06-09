package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import junk.ExtractFileSubDirectories;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;

import com.clementscode.mmi.MainGui;
import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.res.SessionConfig;
import com.clementscode.mmi.sound.SoundUtility;
import com.clementscode.mmi.util.Shuffler;

public class Gui implements ActionListener {
	private ImageIcon imgIconCenter;
	private JButton centerButton;
	private Queue<CategoryItem> itemQueue = null;
	private Session session = null;
	private Timer timer;
	protected Log log = LogFactory.getLog(this.getClass());
	private JCheckBox attending;
	private JFrame frame;
	private String frameTitle = Messages.getString("Gui.FrameTitle"); //$NON-NLS-1$
	private ActionRecorder attendingAction;
	private ActionRecorder independentAction;
	private ActionRecorder verbalAction;
	private ActionRecorder modelingAction;
	private ActionRecorder noAnswerAction;
	private ActionRecorder quitAction;
	private ActionRecorder timerAction;
	private ActionRecorder openAction;
	private JPanel mainPanel;
	private Mediator mediator;
	private ActionRecorder openHttpAction;
	private File tmpDir;
	private ArrayList<JComponent> lstButtons;
	private ImageIcon iiSmilingFace, iiSmilingFaceClickToBegin;

	public Gui() {
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
	}

	private void disableButtons() {
		for (JComponent jc : lstButtons) {
			jc.setEnabled(false);
		}
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
		menuItem = new JMenuItem(quitAction);
		menuItem.setMnemonic(KeyEvent.VK_B);
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

		menuBar.add(buttonMenu);
		frame.setJMenuBar(menuBar);

		frame.pack();

		frame.setVisible(true);
	}

	private JPanel setupMainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel southPanel = new JPanel();
		attending = new JCheckBox(attendingAction);
		southPanel.add(attending);
		lstButtons = new ArrayList<JComponent>();
		lstButtons.add(attending);
		addButton(southPanel, independentAction);
		addButton(southPanel, verbalAction);
		addButton(southPanel, modelingAction);
		addButton(southPanel, noAnswerAction);

		// http://download.oracle.com/javase/tutorial/uiswing/misc/action.html

		// response value. This can be 1 of 4 things: independent (child
		// answered before the prompt audio), verbal (child answered after the
		// prompt but before the answer), modeling (child answered anytime after
		// the answer audio) or the child did not answer.
		panel.add(southPanel, BorderLayout.SOUTH);
		byte[] imageData = null;
		byte[] imageDataClickToBegin = null;
		try {
			imageData = readImageDataFromClasspath("images/a-happy-face.jpg",
					17833);
			imageDataClickToBegin = readImageDataFromClasspath(
					"images/a-happy-face-click-to-begin.jpg", 30055);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Could not find image from classpath...");
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

	private byte[] readImageDataFromClasspath(String fileName, int lazy)
			throws IOException {

		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader()
				.getResourceAsStream(fileName);

		return readImageDataFromInputStream(in, lazy);

	}

	private byte[] readImageDataFromInputStream(InputStream in, int lazy)
			throws IOException {

		byte[] imageData = new byte[lazy];

		int numBytesRead = 0, totalBytesRead = 0;
		// Yes, I feel dirty for not finding the size of the file by hand
		// here.
		// I'm in a hurry.
		// Yes, I know I'll burn in hell. Unless Jesus saves me. Which he
		// has. Thanks!
		while (totalBytesRead < lazy) {
			numBytesRead = in.read(imageData, totalBytesRead, lazy);
			totalBytesRead += numBytesRead;
		}
		in.close();

		return imageData;
	}

	public void setupTimer() {
		if (null != timer) {
			timer.stop(); // fix for issue #4
		}
		timer = new Timer(session.getTimeDelayAnswer() * 1000, timerAction);
		timer.setInitialDelay(session.getTimeDelayPrompt() * 1000);
		timer.setRepeats(true);
		timer.start();
	}

	public void setupCenterButton() {
		// TODO: Call this when we get a new session file read in....
		CategoryItem first = itemQueue.remove();
		try {
			imgIconCenter = new ImageIcon(first.getImgFile().getCanonicalPath());
		} catch (IOException e) {
			log
					.error(
							"Odd, this error should not happen.  Can't find the first image",
							e);
			e.printStackTrace();
		}
		// centerButton = new JButton(imgIconCenter);
		centerButton.setIcon(imgIconCenter);
		centerButton.setPreferredSize(session.getMaxDimensions());
	}

	private void setupActions(MediatorListener mediator) {
		// TODO: Fix bug that control A does not toggle the checkbox
		attendingAction = new ActionRecorder(Messages
				.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), KeyStroke.getKeyStroke("control A"),
				Mediator.ATTENDING, mediator);
		independentAction = new ActionRecorder(Messages
				.getString("Gui.Independent"), null, //$NON-NLS-1$
				Messages.getString("Gui.IndependentDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F2), KeyStroke.getKeyStroke("control I"),
				Mediator.INDEPENDENT, mediator);
		verbalAction = new ActionRecorder(
				Messages.getString("Gui.Verbal"), null, //$NON-NLS-1$
				Messages.getString("Gui.VerbalDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_F3), KeyStroke
						.getKeyStroke("control V"), Mediator.VERBAL, mediator);
		modelingAction = new ActionRecorder(
				Messages.getString("Gui.Modeling"), null, //$NON-NLS-1$
				Messages.getString("Gui.ModelingDescriptin"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F4), KeyStroke.getKeyStroke("control M"),
				Mediator.MODELING, mediator);
		noAnswerAction = new ActionRecorder(
				Messages.getString("Gui.NoAnswer"), null, //$NON-NLS-1$
				Messages.getString("Gui.NoAnswerDescription"), new Integer(KeyEvent.VK_F5), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control N"), Mediator.NO_ANSWER,
				mediator);

		quitAction = new ActionRecorder(
				Messages.getString("Gui.Quit"), null, //$NON-NLS-1$
				Messages.getString("Gui.QuitDescriptino"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control Q"), Mediator.QUIT, mediator);

		timerAction = new ActionRecorder(
				Messages.getString("Gui.TimerSwing"), null, //$NON-NLS-1$
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				KeyStroke.getKeyStroke("control F2"), Mediator.TIMER, mediator);

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

	}

	public void run(Session session) {

	}

	public void playSound(File file) {
		try {
			SoundUtility.playSound(file);
			// "src/test/resources/bc/animals/fooduck/answer.wav"));
		} catch (UnsupportedAudioFileException e) {
			log.error("Problem with playSound: " + file, e);
			e.printStackTrace();
		} catch (IOException e) {
			log.error("Problem with playSound: " + file, e);
			e.printStackTrace();
		}
	}

	public Queue<CategoryItem> getItemQueue() {
		return itemQueue;
	}

	public void switchImage(File file) {

		try {
			setFrameTitle();
			centerButton.setIcon(new ImageIcon(file.getCanonicalPath()));
		} catch (IOException e) {
			log.error(String.format("Problem switching image to file='%s'",
					file), e);
			e.printStackTrace();
		}
	}

	private void setFrameTitle() {
		frame.setTitle(frameTitle
				+ String.format("%d of %d", itemQueue.size() + 1, //$NON-NLS-1$
						session.getItems().length));
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public JCheckBox getAttending() {
		return attending;
	}

	public void setAttending(JCheckBox attending) {
		this.attending = attending;
	}

	public void setVisble(boolean b) {
		frame.setVisible(b);

	}

	public void openSession() {
		File file;
		// TODO: Remove hard coded directory.
		// TODO: Get application to remember the last place we opened this...
		JFileChooser chooser = new JFileChooser(new File(
				"/Users/mgpayne/MMI/src/test/resources"));
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
		}
		displayClickToBegin();
	}

	public void useNewSession() {
		centerButton.removeActionListener(this);
		centerButton.setText("");
		if (null != session) {

			CategoryItem[] copy = Arrays.copyOf(session.getItems(), session
					.getItems().length);
			for (int i = 0; i < session.getShuffleCount(); ++i) {
				Shuffler.shuffle(copy);
			}
			itemQueue = new ConcurrentLinkedQueue<CategoryItem>();
			for (CategoryItem item : copy) {
				// TODO: Is there a collections add all I could use here?
				itemQueue.add(item);
			}
			mediator.setSession(session);
			setupCenterButton();
			setFrameTitle();

			refreshGui();
			setupTimer();
			enableButtons();
		}
	}

	private void readSessionFile(File file) throws Exception {
		readSessionFile(file, null);
	}

	private void readSessionFile(File file, String newItemBase)
			throws Exception {
		Properties props = new Properties();
		// http://stackoverflow.com/questions/1464291/how-to-really-read-text-file-from-classpath-in-java
		// Do it this way and no relative path huha is needed.
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(
				MainGui.propFile);
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
		if (null != newItemBase) {
			config.setItemBase(newItemBase);
			config.setPrompt(newItemBase + "/prompt.wav");
		}
		session = new Session(config, sndExts);
	}

	void refreshGui() {

		mainPanel.revalidate();
		frame.pack();
	}

	public void openHttpSession() {
		// Started with clues from
		// http://download.oracle.com/javase/tutorial/uiswing/components/dialog.html
		Object[] possibilities = { "http://MattPayne.org/mmi/mp.zip",
				"http://MattPayne.org/mmi/bc.zip", "more to come later..." };
		String s = (String) JOptionPane.showInputDialog(frame,
				"Complete the sentence:\n" + "\"Green eggs and...\"",
				"Customized Dialog", JOptionPane.PLAIN_MESSAGE, null,
				possibilities, "ham");
		System.out.println("s=" + s);
		unpackToTempDirectory(s);

	}

	private void unpackToTempDirectory(String strUrl) {

		try {
			File tempZipFile = fetchViaHttp(strUrl);
			String zipPath = File.createTempFile("mmi", "", tmpDir)
					.getAbsolutePath()
					+ ".dir";
			ExtractFileSubDirectories.unzip(zipPath, tempZipFile
					.getAbsolutePath());
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
		centerButton.setText("Click to Begin");
		centerButton.invalidate();
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
		if (centerButton == e.getSource()) {
			useNewSession();
		}

	}

}
