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
	protected Log log = LogFactory.getLog(this.getClass());
	private Gui gui;
	private boolean playPrompt = true;
	private SessionDataCollector collector;
	private CategoryItem item;

	// private Thread soundThread = null;
	private SoundRunner soundRunner = null;

	public Mediator(Gui gui) {
		this.gui = gui;

	}

	public void setSession(Session session) {
		collector = new SessionDataCollector(session.getName(), session
				.getDescription());
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
			collector.addResponse(item, gui.getAttending().isSelected(),
					RespType.INDEPENDANT);
			hit = true;
			break;
		case VERBAL:
			collector.addResponse(item, gui.getAttending().isSelected(),
					RespType.VERBAL);
			hit = true;
			break;
		case MODELING:
			collector.addResponse(item, gui.getAttending().isSelected(),
					RespType.MODEL);
			hit = true;
			break;
		case NO_ANSWER:
			collector.addResponse(item, gui.getAttending().isSelected(),
					RespType.NONE);
			hit = true;
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
			gui.getTimer().stop(); // hope this is a fix to issue #4
			stopSound();
			playPrompt = true;
			if (gui.getItemQueue().size() == 0) {

				gui.populateSessionName();
				gui.populateSessionDataFile();

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

					File session = new File(csvFile.getParentFile(), data
							.getOverall().getName()
							+ ".csv");
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
				gui.getTimer().stop();
			} else {
				item = gui.getItemQueue().remove();
				// gui.switchImage(item.getImgFile());
				gui.switchImage(item.getImg());
				// TODO: There may be different times between sessions....
				gui.getTimer().start();
			}
		}

	}

	private void timer() {
		if (playPrompt) {
			playPrompt = false;
			startSound(gui.getSession().getPrompt());
		} else {
			playPrompt = true;

			startSound(item.getAudio());
			gui.getTimer().stop();
		}
	}

	private void startSound(File f) {
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
