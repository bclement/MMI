package com.clementscode.mmi.swing;

import java.awt.BorderLayout;
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
import javax.swing.Timer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.sound.SoundUtility;
import com.clementscode.mmi.util.Shuffler;

public class Gui {
	private ImageIcon imgIconCenter;
	private JButton centerButton;
	private Queue<CategoryItem> itemQueue = null;
	private Session session = null;
	private Timer timer;
	protected Log log = LogFactory.getLog(this.getClass());
	private JCheckBox attending;
	private JFrame frame;
	private String frameTitle = Messages.getString("Gui.FrameTitle"); //$NON-NLS-1$

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
				// TODO: Is there a collections add all I could use here?
				itemQueue.add(item);
			}
		}

		MediatorListener mediator = new Mediator(this);

		// TODO: Fix the hot keys!
		Action attendingAction = new ActionRecorder(
				Messages.getString("Gui.Attending"), null, //$NON-NLS-1$
				Messages.getString("Gui.AttendingDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F1), Mediator.ATTENDING, mediator);
		Action independentAction = new ActionRecorder(
				Messages.getString("Gui.Independent"), null, //$NON-NLS-1$
				Messages.getString("Gui.IndependentDescription"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F2), Mediator.INDEPENDENT, mediator);
		Action verbalAction = new ActionRecorder(
				Messages.getString("Gui.Verbal"), null, //$NON-NLS-1$
				Messages.getString("Gui.VerbalDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_F3), Mediator.VERBAL, mediator);
		Action modelingAction = new ActionRecorder(
				Messages.getString("Gui.Modeling"), null, //$NON-NLS-1$
				Messages.getString("Gui.ModelingDescriptin"), new Integer( //$NON-NLS-1$
						KeyEvent.VK_F4), Mediator.MODELING, mediator);
		Action noAnswerAction = new ActionRecorder(
				Messages.getString("Gui.NoAnswer"), null, //$NON-NLS-1$
				Messages.getString("Gui.NoAnswerDescription"), new Integer(KeyEvent.VK_F5), //$NON-NLS-1$
				Mediator.NO_ANSWER, mediator);

		Action quitAction = new ActionRecorder(
				Messages.getString("Gui.Quit"), null, //$NON-NLS-1$
				Messages.getString("Gui.QuitDescriptino"), new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				Mediator.QUIT, mediator);

		Action timerAction = new ActionRecorder(
				Messages.getString("Gui.TimerSwing"), null, //$NON-NLS-1$
				"Quit (Exit) the program", new Integer(KeyEvent.VK_L), //$NON-NLS-1$
				Mediator.TIMER, mediator);

		Action openAction = new ActionRecorder(
				Messages.getString("Gui.Open"), null, //$NON-NLS-1$
				Messages.getString("Gui.OpenDescription"), //$NON-NLS-1$
				new Integer(KeyEvent.VK_L), Mediator.OPEN, mediator);

		// Action crudAction = new ActionRecorder(
		//				Messages.getString("Gui.Crud"), null, //$NON-NLS-1$
		//				Messages.getString("Gui.CrudDescription"), //$NON-NLS-1$
		// new Integer(KeyEvent.VK_L), Mediator.CRUD, mediator);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		JPanel southPanel = new JPanel();
		attending = new JCheckBox(attendingAction);
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

		CategoryItem first = itemQueue.remove();
		try {
			imgIconCenter = new ImageIcon(first.getImgFile().getCanonicalPath());
		} catch (IOException e) {
			log.error(
					"Odd, this error should not happen.  Can't find the first image",
					e);
			e.printStackTrace();
		}
		centerButton = new JButton(imgIconCenter);
		panel.add(centerButton, BorderLayout.CENTER);

		// TODO: Check to see if there's a logic bug here....
		frame = new JFrame(frameTitle
				+ String.format("%d of %d", session.getItems().length - 1, //$NON-NLS-1$
						session.getItems().length));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);

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

		// TODO: Size the frame to the whole screen or the size needed for the
		// biggest picture...
		frame.pack();
		frame.setVisible(true);

		timer = new Timer(session.getTimeDelayAnswer(), timerAction);
		timer.setInitialDelay(session.getTimeDelayPrompt());
		timer.setRepeats(true);
		timer.start();
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
			frame.setTitle(frameTitle
					+ String.format("%d of %d", itemQueue.size() + 1, //$NON-NLS-1$
							session.getItems().length));
			centerButton.setIcon(new ImageIcon(file.getCanonicalPath()));
		} catch (IOException e) {
			log.error(
					String.format("Problem switching image to file='%s'", file),
					e);
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

	public void setVisble(boolean b) {
		frame.setVisible(b);

	}
}
