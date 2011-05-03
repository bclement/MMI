package com.clementscode.mmi.swing;

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
	private Gui gui;
	private boolean playPrompt = true;

	public Mediator() {
	}

	public Mediator(Gui gui) {
		this.gui = gui;
	}

	public void execute(int action) {
		switch (action) {
		case ATTENDING:
			break;
		case INDEPENDENT:
			break;
		case VERBAL:
			break;
		case MODELING:
			break;
		case NO_ANSWER:
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

	}

	private void timer() {
		if (playPrompt) {
			playPrompt = false;
			gui.playSound(gui.getSession().getPrompt());
		} else {
			playPrompt = true;
			CategoryItem item = gui.getItemQueue().remove();
			gui.playSound(item.getAudio());
			gui.getTimer().stop();
		}
	}

	public void switchImage() {
		gui.switchImage();
	}
}
