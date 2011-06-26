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
	public static final int ATTENDING = 0;
	public static final int INDEPENDENT = 1;
	public static final int VERBAL = 2;
	public static final int MODELING = 3;
	public static final int NO_ANSWER = 4;
	public static final int QUIT = 5;
	public static final int TIMER = 6;
	public static final int OPEN = 7;
	public static final int CRUD = 8;
	public static final int SAVE_AS = 9;
	public static final int SAVE = 10;
	public static final int OPEN_HTTP = 11;
	public static final int SHOW_LOGGING_FRAME = 12;
	public static final int BETWEEN_TIMER = 13;
	public static final int TOGGLE_BUTTONS = 14;
	protected Log log = LogFactory.getLog(this.getClass());
	private Gui gui;
	private boolean playPrompt = true;
	private SessionDataCollector collector;
	private CategoryItem item;

	// private Thread soundThread = null;
	private SoundRunner soundRunner = null;

	private boolean waiting = false;

	private boolean justWaited = false;

	public Mediator(Gui gui) {
		this.gui = gui;

	}

	public void setSession(Session session) {
		// FIXME session.getSessionName is null here
		collector = new SessionDataCollector(session.getConfigName(), session
				.getSessionName());
		item = gui.getItemQueue().remove();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.clementscode.mmi.swing.MediatorListener#execute(int)
	 */
	public void execute(int action) {
		boolean hit = false;
		switch (action) {
		case ATTENDING:
			break;
		case INDEPENDENT:
			if (!waiting) {
				collector.addResponse(item, gui.getAttending().isSelected(),
						RespType.INDEPENDENT);
				hit = true;
			}
			break;
		case VERBAL:
			if (!waiting) {
				collector.addResponse(item, gui.getAttending().isSelected(),
						RespType.VERBAL);
				hit = true;
			}
			break;
		case MODELING:
			if (!waiting) {
				collector.addResponse(item, gui.getAttending().isSelected(),
						RespType.MODEL);
				hit = true;
			}
			break;
		case NO_ANSWER:
			if (!waiting) {
				collector.addResponse(item, gui.getAttending().isSelected(),
						RespType.NONE);
				hit = true;
			}
			break;
		case TOGGLE_BUTTONS:
			gui.toggleButtons();
			break;
		case QUIT:
			// TODO: Ask them if they want to save the file anyway....
			System.exit(0);
			break;
		// case CRUD:
		// new CrudFrame(this);
		// gui.setVisble(false);
		// break;
		case TIMER:
			timer();
			break;
		case BETWEEN_TIMER:
			hit = true;
			waiting = false;
			justWaited = true;
			break;
		case SHOW_LOGGING_FRAME:
			gui.showLoggingFrame();
			break;
		case OPEN:
			gui.openSession();
			break;
		case OPEN_HTTP:
			gui.openHttpSession();
			break;
		}
		if (hit) {
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
