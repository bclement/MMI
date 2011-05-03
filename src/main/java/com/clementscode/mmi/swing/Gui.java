package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.sound.SoundUtility;
import com.clementscode.mmi.util.Shuffler;

public class Gui {
	public static void main(String[] arg) {
		try {
			new Gui().run(null);
		} catch (Exception bland) {
			bland.printStackTrace();
		}
	}

	private ImageIcon imgIconCenter;
	private String pathA;
	private String pathB;
	private String path;
	private JButton centerButton;
	private Queue<CategoryItem> itemQueue = null;
	private Session session = null;
	private Timer timer;
	protected Log log = LogFactory.getLog(this.getClass());
	private JCheckBox attending;
	
	public void run(Session session) {

		if (null != session) {
			this.session = session;
			CategoryItem[] copy = Arrays.copyOf(session.getItems(),
					session.getItems().length);
			for (int i = 0; i < session.getShuffleCount(); ++i) {
				Shuffler.shuffle(copy);
			}
			itemQueue = new ConcurrentLinkedQueue<CategoryItem>();
			for (CategoryItem item : copy) {
				System.out.println(String.format(
						"item=%s, t1=%d, t2=%d, prompt=%s", item,
						session.getTimeDelayPrompt(),
						session.getTimeDelayAnswer(), session.getPrompt()));
				itemQueue.add(item);
			}
		}

		Mediator mediator = new Mediator(this);
		pathA = "src/test/resources/bc/animals/fooduck/fooduck.gif";
		pathB = "src/test/resources/bc/food/foobar/redbar.jpg";
		path = pathA;

		// TODO: Internationalize the strings....
		Action attendingAction = new ActionRecorder("Attending", null,
				"Was the child looking at the prompter?", new Integer(
						KeyEvent.VK_L), Mediator.ATTENDING, mediator);
		Action independentAction = new ActionRecorder("Independent", null,
				"Child answered before the prompt audio?", new Integer(
						KeyEvent.VK_L), Mediator.INDEPENDENT, mediator);
		Action verbalAction = new ActionRecorder("Verbal", null,
				"Child answered after the prompt but before the answer?",
				new Integer(KeyEvent.VK_L), Mediator.VERBAL, mediator);
		Action modelingAction = new ActionRecorder("Modeling", null,
				"Child answered anytime after the answer audio?", new Integer(
						KeyEvent.VK_L), Mediator.MODELING, mediator);
		Action noAnswerAction = new ActionRecorder("No Answer", null,
				"The child did not answer?", new Integer(KeyEvent.VK_L), Mediator.NO_ANSWER,
				mediator);

		Action quitAction = new ActionRecorder("Quit", null,
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), Mediator.QUIT,
				mediator);

		Action timerAction = new ActionRecorder("Timer (Swing)", null,
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), Mediator.TIMER,
				mediator);

		Action openAction = new ActionRecorder("Open...", null,
				"Open directory tree or ZIP file for training session",
				new Integer(KeyEvent.VK_L), Mediator.OPEN, mediator);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton b = new JButton("North");
		// panel.add(b, BorderLayout.NORTH);
		b = new JButton("South");
		JPanel southPanel = new JPanel();
		 attending = new JCheckBox(attendingAction);// ""
		southPanel.add(attending);
		JButton responseButton = new JButton(independentAction);
		southPanel.add(responseButton);
		responseButton = new JButton(verbalAction);
		southPanel.add(responseButton);
		responseButton = new JButton(modelingAction);
		southPanel.add(responseButton);
		responseButton = new JButton(noAnswerAction);
		southPanel.add(responseButton);

		// http://download.oracle.com/javase/tutorial/uiswing/misc/action.html

		// response value. This can be 1 of 4 things: independent (child
		// answered before the prompt audio), verbal (child answered after the
		// prompt but before the answer), modeling (child answered anytime after
		// the answer audio) or the child did not answer.
		panel.add(southPanel, BorderLayout.SOUTH);
		b = new JButton("EAST");
		// panel.add(b,BorderLayout.EAST);
		b = new JButton("West");
		// panel.add(b,BorderLayout.WEST);

		path = pathA;
		imgIconCenter = new ImageIcon(path);
		centerButton = new JButton(imgIconCenter);
		panel.add(centerButton, BorderLayout.CENTER);

		JFrame f = new JFrame("demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(panel);

		// http://download.oracle.com/javase/tutorial/uiswing/components/menu.html
		// http://download.oracle.com/javase/tutorial/uiswing/misc/action.html
		// Create the menu bar.
		JMenuBar menuBar = new JMenuBar();

		// Build the first menu.
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);

		// a group of JMenuItems
		JMenuItem menuItem = new JMenuItem(openAction);

		menu.add(menuItem);

		menuItem = new JMenuItem(quitAction);
		menuItem.setMnemonic(KeyEvent.VK_B);
		menu.add(menuItem);

		menuBar.add(menu);

		JMenu buttonMenu = new JMenu("Buttons");
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
		f.setJMenuBar(menuBar);

		f.pack();
		f.setVisible(true);
		
	timer = new Timer(session.getTimeDelayAnswer(), timerAction);
		timer.setInitialDelay(session.getTimeDelayPrompt());
		timer.setRepeats(true);
		timer.start();
	}

	public void playSound(File file) {
		try {
			SoundUtility.playSound(file);
					//"src/test/resources/bc/animals/fooduck/answer.wav"));
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Queue<CategoryItem> getItemQueue() { return itemQueue; }

	public void switchImage(File file) {
		
		try {
			centerButton.setIcon(new ImageIcon(file.getCanonicalPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
