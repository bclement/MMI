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
	protected Log log = LogFactory.getLog(this.getClass());
	private Gui gui;
	private boolean playPrompt = true;
	private SessionDataCollector collector;
	private CategoryItem item;

	public Mediator(Gui gui) {
		this.gui = gui;

	}

	public void setSession(Session session) {
		collector = new SessionDataCollector(session.getName(),
				session.getDescription());
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
		case OPEN:
			gui.openSession();
			break;
		}
		if (hit) {
			if (gui.getItemQueue().size() == 0) {
				// TODO: Have filename come from session
				String fileName = "/tmp/brian.csv";
				try {
					File csvFile = new File(fileName);
					CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));
					SessionData data = collector.getData();
					data.write(csvWriter);
					csvWriter.close();
					System.out.println("Wrote to " + csvFile);
				} catch (IOException e) {
					log.error(String.format(
							"Problem writting stats to file='%s'", fileName), e);
					e.printStackTrace();
				}

				System.exit(0);
			} else {
				item = gui.getItemQueue().remove();
				gui.switchImage(item.getImgFile());
				// TODO: There may be different times between sessions....
				gui.getTimer().start();
			}
		}

	}

	private void timer() {
		if (playPrompt) {
			playPrompt = false;
			gui.playSound(gui.getSession().getPrompt());
		} else {
			playPrompt = true;

			gui.playSound(item.getAudio());
			gui.getTimer().stop();
		}
	}

}
