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
public class Mediator {
	public static final int ATTENDING = 0;
	public static final int INDEPENDENT = 1;
	public static final int VERBAL = 2;
	public static final int MODELING = 3;
	public static final int NO_ANSWER = 4;
	public static final int QUIT = 5;
	public static final int TIMER = 6;
	public static final int OPEN = 7;
	protected Log log = LogFactory.getLog(this.getClass());
	private Gui gui;
	private boolean playPrompt = true;
	private SessionDataCollector collector;
	private CategoryItem item;

	public Mediator(Gui gui) {
		this.gui = gui;
		collector = new SessionDataCollector(gui.getSession().getName(), gui
				.getSession().getDescription());
		item = gui.getItemQueue().remove();
	}

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
			System.exit(0);
			break;
		case TIMER:
			timer();
			break;
		case OPEN:
			break;
		}
		if (hit) {
			if (gui.getItemQueue().size() == 0) {
				
				try {
					File csvFile = new File("/tmp/brian.csv");
					CSVWriter csvWriter = new CSVWriter(new FileWriter(csvFile));
					SessionData data = collector.getData();
					data.write(csvWriter);
					csvWriter.close();
					System.out.println("Wrote to " + csvFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				System.exit(0);
			} else {
				item = gui.getItemQueue().remove();
				gui.switchImage(item.getImgFile());
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
