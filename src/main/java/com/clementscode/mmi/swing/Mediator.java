package com.clementscode.mmi.swing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.bytecode.opencsv.CSVWriter;

import com.clementscode.mmi.data.SessionData;
import com.clementscode.mmi.data.SessionDataCollector;
import com.clementscode.mmi.data.SessionDataCollector.RespType;
import com.clementscode.mmi.res.CategoryItem;
import com.clementscode.mmi.res.Session;
import com.clementscode.mmi.sound.SoundRunner;

//TODO: This code needs to be more state machine like and refactored heavily.  Too bad there is no time!

/*

 <pre>
 1) Display image
 2) wait time1 seconds
 3) play first sound file
 4) wait time 2 seconds
 5) play second sound file
 6) wait for evaluator to click a button

 Any time an evaluator clicks a button, the eval is done and move to the next
 </pre>

 */
public class Mediator implements MediatorListener {
	// public static final int ATTENDING = 0;
	// public static final int INDEPENDENT = 1;
	// public static final int VERBAL = 2;
	// public static final int MODELING = 3;
	// public static final int NO_ANSWER = 4;
	// public static final int QUIT = 5;
	// public static final int TIMER = 6;
	// public static final int OPEN = 7;
	// public static final int CRUD = 8;
	// public static final int SAVE_AS = 9;
	// public static final int SAVE = 10;
	// public static final int OPEN_HTTP = 11;
	// public static final int SHOW_LOGGING_FRAME = 12;
	// public static final int BETWEEN_TIMER = 13;
	// public static final int TOGGLE_BUTTONS = 14;
	// public static final int WRONG_ANSWER = 15;
	// public static final int CHANGE_DELAY_TIMER = 16;
	protected Log log = LogFactory.getLog(this.getClass());
	private Gui gui;
	private boolean playPrompt = true;
	private SessionDataCollector collector;
	private CategoryItem item;

	// private Thread soundThread = null;
	private SoundRunner soundRunner = null;

	private boolean waiting = false;

	private boolean justWaited = false;
	private boolean bAttending = false;

	private int errorCount = 0;

	private boolean nextOnError = false;

	public Mediator(Gui gui) {
		this.gui = gui;

	}

	public void setSession(Session session) {
		// FIXME session.getSessionName is null here
		collector = new SessionDataCollector(session.getConfigName(), session
				.getSessionName());
		// Commenting out this bug is the fix to
		// https://github.com/payne/MMI/issues/35
		// Since in hit & justWaited near line 236 there's a item =
		// gui.getItemQueue().remove();
		// item = gui.getItemQueue().remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clementscode.mmi.swing.MediatorListener#execute(int)
	 */
	public void execute(Action action) {
		boolean hit = false;
		switch (action) {
		case BASELINE_MODE:
			/*
			 * https://github.com/payne/MMI/issues/38 This option would make the
			 * 'nextOnError' boolean in Moderator.java equal to true. This
			 * should be a toggle so you can switch back to normal mode.
			 */
			nextOnError = !nextOnError;
			log
					.info("Per https://github.com/payne/MMI/issues/38 nextOnError is now "
							+ nextOnError);
			break;
		case ATTENDING:
			bAttending = !bAttending;
			break;
		case INDEPENDENT:
			if (!waiting) {
				collector.addResponse(item, bAttending, RespType.INDEPENDENT,
						errorCount);
				hit = true;
				bAttending = false;
			}
			break;
		case VERBAL:
			if (!waiting) {
				collector.addResponse(item, bAttending, RespType.VERBAL,
						errorCount);
				hit = true;
				bAttending = false;
			}
			break;
		case MODELING:
			if (!waiting) {
				collector.addResponse(item, bAttending, RespType.MODEL,
						errorCount);
				hit = true;
				bAttending = false;
			}
			break;
		case NO_ANSWER:
			if (!waiting) {
				collector.addResponse(item, bAttending, RespType.NONE,
						errorCount);
				hit = true;
				bAttending = false;
			}
			break;
		case WRONG_ANSWER:
			if (!waiting) {
				++errorCount;
				if (nextOnError) {
					collector.addResponse(item, bAttending,
							RespType.NO_RESPONSE, errorCount);
					hit = true;
					bAttending = false;
				}
			}
			break;
		case TOGGLE_BUTTONS:
			gui.toggleButtons();
			break;
		case QUIT:
			// TODO: Ask them if they want to save the file anyway....
			System.exit(0);
			break;
		case TIMER:
			timer();
			break;
		case BETWEEN_TIMER:
			hit = true;
			waiting = false;
			justWaited = true;
			break;

		case CHANGE_DELAY_TIMER:
			/*
			 * new stuff from issue 25: A new entry is in the session config
			 * called timeDelayAutoAdvance. This timer should start after the
			 * audioPrompt (formerly called the answer) is played. When the
			 * timer goes off, advancement to the next item should be triggered
			 * with a no response entry in the data collector. If the time delay
			 * has a value of less than zero, there should be no timer and
			 * advancement to the next item should only happen on response
			 * entry.
			 */
			collector.addResponse(item, bAttending, RespType.NO_RESPONSE,
					errorCount);
			hit = true;
			log.info("Saw CHANGE_DELAY_TIMER");
			break;

		case SHOW_LOGGING_FRAME:
			gui.showLoggingFrame();
			break;
		case OPEN:
			gui.openSession();
			break;

		}
		if (hit) {
			errorCount = 0;
			gui.stopTimer(); // hope this is a fix to issue #4
			stopSound();
			playPrompt = true;
			if (gui.getItemQueue().size() == 0) {

				gui.populateSessionName();
				gui.populateSessionDataFile();

				// FIXME this should be poplulated when collector is created
				collector.setSessionName(gui.getSession().getSessionName());

				File csvFile = null;
				try {
					csvFile = gui.getSession().getSessionDataFile();

					boolean writeHeader = !csvFile.exists();
					CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile,
							true)); // true -- I want to append.
					SessionData data = collector.getData();
					if (writeHeader) {
						data.writeSummaryHeader(csvWriter);
					}
					data.writeSummary(csvWriter);
					csvWriter.close();
					String sessionCsvName = data.getOverall().getName() + " "
							+ data.getSessionName() + ".csv";
					File session = new File(csvFile.getParentFile(),
							sessionCsvName);
					CSVWriter writer = new CSVWriter(new FileWriter(session));
					data.writeSessionFile(writer);
					writer.close();
					System.out.println("Wrote to " + csvFile);
				} catch (IOException e) {
					log.error(String.format(
							"Problem writting stats to file='%s'", csvFile
									.getAbsolutePath()), e);
					e.printStackTrace();
				}

				// System.exit(0);
				gui.backToStartScreen();
				gui.stopTimer();
			} else {

				if (justWaited) {
					item = gui.getItemQueue().remove();
					log.info(String.format("About to switch image to %s (#%d)",
							item.getImgFile(), item.getItemNumber()));
					gui.switchItem(item);
					justWaited = false;
				} else {
					gui.clearImage();
					gui.getBetweenTimer().start();
					waiting = true;
				}
			}
		}

	}

	/*
	 * new stuff from issue 25: A new entry is in the session config called
	 * timeDelayAutoAdvance. This timer should start after the audioPrompt
	 * (formerly called the answer) is played. When the timer goes off,
	 * advancement to the next item should be triggered with a no response entry
	 * in the data collector. If the time delay has a value of less than zero,
	 * there should be no timer and advancement to the next item should only
	 * happen on response entry.
	 */
	private void timer() {
		if (playPrompt) {
			playPrompt = false;
			// NAME CHANGE!
			startSound(item.getAudioSD(), -1);
		} else {
			playPrompt = true;
			// it's all so confusing when we don't update all the names at once
			startSound(item.getAudioPrompt(), item.getItemNumber());
			gui.stopTimer();
			int timeDelayAutoAdvance = gui.getSession().getConfig()
					.getTimeDelayAutoAdvance();
			if (timeDelayAutoAdvance > 0) {
				int audioPromptLen = gui.getSoundLen(item.getAudioPrompt());
				timeDelayAutoAdvance += audioPromptLen;
				if (timeDelayAutoAdvance > 0) {
					gui.startTimerTimeDelayAutoAdvance(timeDelayAutoAdvance);
				} else {
					log
							.error("Arrgh!  This is auful!  audioPromptLen is larger than timeDelayAutoAdvance.");
				}
			}
		}
	}

	private void startSound(File f, int n) {
		log.info(String.format("About to play sound: %s from itemNumber %d", f,
				n));
		soundRunner = new SoundRunner(f);
		new Thread(soundRunner).start();

	}

	private void stopSound() {
		if (soundRunner != null) {
			soundRunner.stop();
			soundRunner = null;
		}
	}

}
